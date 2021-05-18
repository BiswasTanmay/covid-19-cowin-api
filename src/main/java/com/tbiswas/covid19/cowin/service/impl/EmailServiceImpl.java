package com.tbiswas.covid19.cowin.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.tbiswas.covid19.cowin.model.AvailableDto;
import com.tbiswas.covid19.cowin.model.MailRequest;
import com.tbiswas.covid19.cowin.model.MailResponse;
import com.tbiswas.covid19.cowin.model.SubscribeDto;
import com.tbiswas.covid19.cowin.service.ICovidService;
import com.tbiswas.covid19.cowin.service.IEmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class EmailServiceImpl implements IEmailService {

	@Autowired
	private JavaMailSender sender;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private Configuration config;

	@Autowired
	ICovidService covidServiceApi;

	@Override
	public MailResponse sendEmail(MailRequest request, Map<String, Object> model, Template t) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

			helper.setBcc(request.getTo());
			// helper.setTo(request.getTo());
			helper.setText(html, true);
			helper.setSubject(request.getSubject());
			helper.setFrom(request.getFrom());
			sender.send(message);

			response.setMessage("mail send to : " + request.getTo());
			response.setStatus(Boolean.TRUE);

		} catch (MessagingException | IOException | TemplateException e) {
			response.setMessage("Mail Sending failure : " + e.getMessage());
			response.setStatus(Boolean.FALSE);
		}

		return response;
	}

	@Override
	@Transactional
	public void subscribeEmail(String email, String pin, String type, String dose) {
		jdbcTemplate.update("INSERT INTO T_SUBSCRIBE_EMAIL(EMAIL_ID,PIN_DIST_CODE,TYPE,DOSE_TYPE) VALUES(?,?,?,?)",
				email.toUpperCase(), pin, type, dose);

	}

	@Override
	@Transactional
	public void checkAndSendMail() {
		List<SubscribeDto> fullSubscribeList = null;
		fullSubscribeList = jdbcTemplate.query(
				"SELECT d.EMAIL_ID AS email, d.PIN_DIST_CODE AS pin, d.TYPE as type FROM T_SUBSCRIBE_EMAIL d ",
				new BeanPropertyRowMapper<SubscribeDto>(SubscribeDto.class));

		List<String> pinCodes = fullSubscribeList.stream().map(SubscribeDto::getPin).distinct()
				.collect(Collectors.toList());

		Map<String, List<String>> pinEmailMap = new HashMap<String, List<String>>();
		Map<String, List<String>> distEmailMap = new HashMap<String, List<String>>();
		Map<String, String> typeMap = new HashMap<String, String>();

		fullSubscribeList.stream().forEach(subscribe -> {
			typeMap.put(subscribe.getPin(), subscribe.getType());
			if (subscribe.getType().equalsIgnoreCase("N")) {
				List<String> mailList = new ArrayList<String>();
				mailList = pinEmailMap.get(subscribe.getPin());
				if (mailList != null && mailList.size() > 0) {
					if (!mailList.contains(subscribe.getEmail())) {
						mailList.add(subscribe.getEmail());
					}
					pinEmailMap.put(subscribe.getPin(), mailList);
				} else {
					mailList = new ArrayList<String>();
					mailList.add(subscribe.getEmail());
					pinEmailMap.put(subscribe.getPin(), mailList);
				}

			} else {

				List<String> mailList = new ArrayList<String>();
				mailList = distEmailMap.get(subscribe.getPin());
				if (mailList != null && mailList.size() > 0) {
					if (!mailList.contains(subscribe.getEmail())) {
						mailList.add(subscribe.getEmail());
					}
					distEmailMap.put(subscribe.getPin(), mailList);
				} else {
					mailList = new ArrayList<String>();
					mailList.add(subscribe.getEmail());
					distEmailMap.put(subscribe.getPin(), mailList);
				}

			}

		});

		pinCodes.stream().forEach(data -> {

			List<AvailableDto> availableList = covidServiceApi.getDetailsFromCowin(data, typeMap);
			MailRequest request = new MailRequest();
			request.setFrom("learn.latest.tech.it@gmail.com");
			List<String> toList = new ArrayList<String>();
			String unAvMailBod = null;
			
			if (typeMap.get(data).equalsIgnoreCase("Y")) {
				
				toList = distEmailMap.get(data);
			} else if (typeMap.get(data).equalsIgnoreCase("N")) {
				
				unAvMailBod = "at Pincode: "+data;
				toList = pinEmailMap.get(data);
			}

			String[] arr = new String[toList.size()];
			request.setTo(toList.toArray(arr));
			// MailResponse response = new MailResponse();
			Map<String, Object> model = new HashMap<>();
			model.put("Pincode", data);
			model.put("availableList", availableList);
			if (availableList != null && availableList.size() > 0) {
				request.setSubject("Vaccine Available");
				request.setName("Y");
				model.put("State", availableList.get(0).getState());
				model.put("Dist", availableList.get(0).getDistrict());

				Template t = selectTemplate(request, data, typeMap);
				sendEmail(request, model, t);

				System.out.println("+++++++++++++++++++++++++++++++++++++++Email Send with Data");

			} else {
				LocalTime now = LocalTime.now();
				int currentHour = now.getHour();
				request.setSubject("Vaccine Not Available");
				request.setName("N");
				model.put("unavBody", unAvMailBod);
				Template t = selectTemplate(request, data, typeMap);
				if (String.valueOf(currentHour).equalsIgnoreCase("1")
						|| String.valueOf(currentHour).equalsIgnoreCase("7")
						|| String.valueOf(currentHour).equalsIgnoreCase("13")
						|| String.valueOf(currentHour).equalsIgnoreCase("20")) {
					sendEmail(request, model, t);
				}

				System.out.println("=============================Email Send with No data");
			}

		});

	}

	private Template selectTemplate(MailRequest request, String data, Map<String, String> typeMap) {
		Template t = null;
		try {
			t = config.getTemplate("email.ftl");
			if (request.getName().equalsIgnoreCase("N")) {
				t = config.getTemplate("email2.ftl");
			} else if (typeMap.get(data).equalsIgnoreCase("Y")) {
				t = config.getTemplate("email3.ftl");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return t;

	}

	@Override
	@Transactional
	public void unsubscribeEmail(String email, Boolean checkDose1, Boolean checkDose2) {

		if ((checkDose1 != null && checkDose2 != null) || (checkDose1 == null && checkDose2 == null)) {
			String SQL = "delete from T_SUBSCRIBE_EMAIL where EMAIL_ID = ?";
			jdbcTemplate.update(SQL, email);

		} else if (checkDose1 != null || checkDose2 != null) {
			String keep = checkDose1 == null ? "D1" : "D2";
			String del = checkDose1 == null ? "D2" : "D1";

			List<SubscribeDto> fullSubscribeList = null;
			fullSubscribeList = jdbcTemplate.query(
					"SELECT d.EMAIL_ID AS email, d.PIN_DIST_CODE AS pin, d.TYPE as type, d.DOSE_TYPE as doseType FROM T_SUBSCRIBE_EMAIL d WHERE d.EMAIL_ID = ?  and d.DOSE_TYPE = ?",
					new Object[] { email.toUpperCase(), "B" },
					new BeanPropertyRowMapper<SubscribeDto>(SubscribeDto.class));

			if (fullSubscribeList == null || fullSubscribeList.isEmpty()) {

				String SQL = "delete from T_SUBSCRIBE_EMAIL where EMAIL_ID = ? and DOSE_TYPE = ?";
				jdbcTemplate.update(SQL, email, del);

			} else {

				String sqlUpdate = "update T_SUBSCRIBE_EMAIL set DOSE_TYPE = ? where EMAIL_ID = ? AND DOSE_TYPE = ?";
				jdbcTemplate.update(sqlUpdate, keep, email.toUpperCase(), "B");

				String sqlDelete = "delete from T_SUBSCRIBE_EMAIL where EMAIL_ID = ? and DOSE_TYPE = ?";
				jdbcTemplate.update(sqlDelete, email.toUpperCase(), del);

			}

		}

	}

	@Override
	public void confirmationMail(String emailBody, String email, String dose) {
		MailRequest request = new MailRequest();
		Map<String, Object> model = new HashMap<>();
		Template t = null;
		try {

			List<String> toList = new ArrayList<String>();
			toList.add(email);
			String[] toArr = new String[toList.size()];
			toArr = toList.toArray(toArr);
			request.setFrom("learn.latest.tech.it@gmail.com");
			request.setTo(toArr);
			request.setSubject("Thank you for subscription!");
			request.setName("Y");
			t = config.getTemplate("confEmail.ftl");
			model.put("bodyText", emailBody);
			String doseType = null;
			if (dose.equalsIgnoreCase("B")) {
				doseType = "Both Dose";
			} else if (dose.equalsIgnoreCase("D1")) {
				doseType = "Dose 1";
			} else if (dose.equalsIgnoreCase("D2")) {
				doseType = "Dose 2";
			}
			model.put("dose", doseType);
			sendEmail(request, model, t);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void unsubConf(String email, String dose) {
		MailRequest request = new MailRequest();
		Map<String, Object> model = new HashMap<>();
		Template t = null;
		try {
			String emailBody = "Your unsubscription is done for " + dose
					+ ". You will not receive any email notification further for this case.";
			List<String> toList = new ArrayList<String>();
			toList.add(email);
			String[] toArr = new String[toList.size()];
			toArr = toList.toArray(toArr);
			request.setFrom("learn.latest.tech.it@gmail.com");
			request.setTo(toArr);
			request.setSubject("Unsubscription Completed!");
			request.setName("Y");
			t = config.getTemplate("unsub.ftl");
			model.put("bodyText", emailBody);
			sendEmail(request, model, t);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
