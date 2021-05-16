package com.tbiswas.covid19.cowin.service;

import java.util.Map;

import com.tbiswas.covid19.cowin.model.MailRequest;
import com.tbiswas.covid19.cowin.model.MailResponse;

public interface IEmailService {
	MailResponse sendEmail(MailRequest request, Map<String, Object> model, String data, Map<String, String> typeMap);
	void subscribeEmail(String email, String pin, String type);
	void checkAndSendMail();
	void unsubscribeEmail(String email);

}
