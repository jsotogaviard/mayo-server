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
		long userId = registerUser(email, password);
		HibernateUtil.update(new Users(userId, email, password, true));
		String tokenSoto = login(email, password);
		addUserConnection("rita", new String[]{}, new String[]{email1}, EMPTY_ARRAY,  tokenSoto);

		// Rita adds Jonathan
		long userId1 = registerUser(email1, password1);
		HibernateUtil.update(new Users(userId1, email1, password1, true));
		String tokenRita = login(email1, password1);
		addUserConnection("jon", new String[]{}, new String[]{email},EMPTY_ARRAY,  tokenRita);

		waitSomeTime(1000);
		
		// Verify emails send
		validateSentEmail(Arrays.<AMail>asList(new VerificationMail(email),
				new VerificationMail(email1),
				new ConnectionEmail(email1),
				new ConnectionEmail(email)
				));
	}
	
}