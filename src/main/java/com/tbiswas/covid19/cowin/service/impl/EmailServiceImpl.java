package com.tbiswas.covid19.cowin.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
	public MailResponse sendEmail(MailRequest request, Map<String, Object> model, String data, Map<String, String> typeMap) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			// add attachment
			Template t = config.getTemplate("email.ftl");
			if (request.getName().equalsIgnoreCase("N")) {
				t = config.getTemplate("email2.ftl");
			}else if(typeMap.get(data).equalsIgnoreCase("Y")) {
				t = config.getTemplate("email3.ftl");
			}

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
	public void subscribeEmail(String email, String pin, String type) {
		jdbcTemplate.update("INSERT INTO 5AP58VsI5W.T_SUBSCRIBE_EMAIL(EMAIL_ID,PIN_DIST_CODE,TYPE) VALUES(?,?,?)",
				email.toUpperCase(), pin, type);

	}

	@Override
	@Transactional
	public void checkAndSendMail() {
		List<SubscribeDto> fullSubscribeList = null;
		fullSubscribeList = jdbcTemplate.query(
				"SELECT d.EMAIL_ID AS email, d.PIN_DIST_CODE AS pin, d.TYPE as type FROM 5AP58VsI5W.T_SUBSCRIBE_EMAIL d ",
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
			if (typeMap.get(data).equalsIgnoreCase("Y")) {
				toList = distEmailMap.get(data);
			} else if (typeMap.get(data).equalsIgnoreCase("N")){
				toList = pinEmailMap.get(data);
			}

			String[] arr = new String[toList.size()];
			request.setTo(toList.toArray(arr));
			// MailResponse response = new MailResponse();
			Map<String, Object> model = new HashMap<>();
			model.put("Pincode", data);

			if (availableList != null && availableList.size() > 0) {
				request.setSubject("Vaccine Available");
				request.setName("Y");
				model.put("State", availableList.get(0).getState());
				model.put("Dist", availableList.get(0).getDistrict());
				
				sendEmail(request, model,data, typeMap);

				System.out.println("+++++++++++++++++++++++++++++++++++++++Email Send with Data");

			} else {
				request.setSubject("Vaccine Not Available");
				request.setName("N");
				sendEmail(request, model,data, typeMap);
				System.out.println("=============================Email Send with No data");
			}

		});

	}

	@Override
	@Transactional
	public void unsubscribeEmail(String email) {
		 String SQL = "delete from 5AP58VsI5W.T_SUBSCRIBE_EMAIL where EMAIL_ID = ?";
		 jdbcTemplate.update(SQL, email);
		
	}

}
