package com.tbiswas.covid19.cowin.service;

import java.util.Map;

import com.tbiswas.covid19.cowin.model.MailRequest;
import com.tbiswas.covid19.cowin.model.MailResponse;

import freemarker.template.Template;

public interface IEmailService {
	MailResponse sendEmail(MailRequest request, Map<String, Object> model, Template t);
	void subscribeEmail(String email, String pin, String type);
	void checkAndSendMail();
	void unsubscribeEmail(String email);
	void confirmationMail(String emailBody, String email);

}
