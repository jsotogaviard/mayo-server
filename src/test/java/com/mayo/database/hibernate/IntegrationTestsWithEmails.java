package com.mayo.database.hibernate;

import java.util.Arrays;

import org.junit.Test;

import com.mayo.mail.AMail;
import com.mayo.mail.ConnectionEmail;
import com.mayo.mail.VerificationMail;

public class IntegrationTestsWithEmails extends AServiceTests {
	
	@Test
	public void uniqueTest() {
		// Jonathan adds rita
		long userId = addUser(email, password);
		HibernateUtil.update(new Users(userId, email, password, true));
		String tokenSoto = login(email, password);
		addUserConnection("rita", new String[]{}, new String[]{email1}, tokenSoto);

		// Rita adds Jonathan
		long userId1 = addUser(email1, password1);
		HibernateUtil.update(new Users(userId1, email1, password1, true));
		String tokenRita = login(email1, password1);
		addUserConnection("jon", new String[]{}, new String[]{email}, tokenRita);

		waitSomeTime(3000);
		
		// Verify emails send
		validateSentEmail(Arrays.<AMail>asList(new VerificationMail(email),
				new VerificationMail(email1),
				new ConnectionEmail(email),
				new ConnectionEmail(email1)
				));
	}
	
//	@Test
//	public void testBasicScenario2() {
//		
//		// Jonathan adds rita
//		addUser("jso@qfs.com", "jonathan", "soto", new String[]{}, new String[]{});
//		HibernateUtil.update(new Users(1L, "jonathan", "jso@qfs.com" ,"soto", true));
//		String tokenSoto = login("jso@qfs.com", "soto");
//		addUserConnection(1L, "rita", new String[]{}, new String[]{"rita@qfs.com"}, tokenSoto);
//
//		printAll();
//		
//		// Rita adds Jonathan
//		addUser("rita@qfs.com", "rita", "maga", new String[]{}, new String[]{});
//		HibernateUtil.update(new Users(2L, "rita", "rita@qfs.com" ,"maga", true));
//		String tokenRita = login("rita@qfs.com", "maga");
//		addUserConnection(2L, "jon", new String[]{}, new String[]{"jso@qfs.com"}, tokenRita);
//		
//		// Verify emails send
//		mailServer.stop();
//		System.out.println(mailServer.getReceivedEmailSize());
//		@SuppressWarnings("unchecked")
//		Iterator<Object> it = mailServer.getReceivedEmail();
//		while(it.hasNext()){
//			Object mail = it.next();
//			System.out.println(mail);
//		}
//	}

}