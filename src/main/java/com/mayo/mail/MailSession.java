package com.mayo.mail;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.mayo.IMayoService;

public class MailSession {
	
	public static Session createFakeSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.port", IMayoService.FAKE_MAIL_PORT);
		return Session.getDefaultInstance(props);

	}

	public static Session createGmailSession() {
		Properties props = new Properties();
		final String from = "jsotogaviard2@gmail.com";
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		return Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, "mayo2013");
			}
		});

	}


}
