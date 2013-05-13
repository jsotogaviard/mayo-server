package com.mayo.mail;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.MediaType;

import com.mayo.MayoException;

public class EmailDispatcherThread extends Thread {
	
	/** The mail session*/
	protected Session session;
	
	/** The email Queue*/
	protected ConcurrentLinkedQueue<AMail> queue;

	public EmailDispatcherThread(Session session, ConcurrentLinkedQueue<AMail> queue) {
		this.session = session;
		this.queue = queue;
	}
	
	@Override
	public synchronized void start() {
		super.start();
		
		// Dispatcher thread
		while(true){
			AMail email = queue.poll();
			if(email != null)
				send(email, session);
		}
	}
	
	public static void send(AMail email, Session session){
		Message simpleMessage = new MimeMessage(session);
		
		InternetAddress toAddress = null;
		try {
			toAddress = new InternetAddress(email.to);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			simpleMessage.setSubject(email.subject);
			simpleMessage.setContent(email.text, MediaType.TEXT_HTML);
			Transport.send(simpleMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new MayoException(e);
		}
	}

}
