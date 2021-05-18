package com.tbiswas.covid19.cowin.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tbiswas.covid19.cowin.service.IEmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SchedulerJob {
	
	@Autowired
	IEmailService emailSending;
	
	//@Scheduled(fixedRateString = "10000")
	public void checkAndSendEmail() {
		emailSending.checkAndSendMail();
		
	}

}
