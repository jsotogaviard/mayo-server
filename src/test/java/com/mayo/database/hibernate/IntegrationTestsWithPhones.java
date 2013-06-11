package com.mayo.database.hibernate;

import java.util.Arrays;

import org.junit.Test;

import com.mayo.mail.AMail;
import com.mayo.mail.ConnectionEmail;
import com.mayo.mail.VerificationMail;

public class IntegrationTestsWithPhones extends AServiceTests {
	
	@Test
	public void uniqueTest() {
		// Jonathan adds rita
		long userId = registerUser(email, password);
		HibernateUtil.update(new Users(userId, email, password, true));
		String tokenSoto = login(email, password);
		updateUserInformation(new String[]{phone}, new String[]{}, tokenSoto);
		addUserConnection("rita", new String[]{phone1}, new String[]{}, tokenSoto);

		// Rita adds Jonathan
		long userId1 = registerUser(email1, password1);
		HibernateUtil.update(new Users(userId1, email1, password1, true));
		String tokenRita = login(email1, password1);
		updateUserInformation(new String[]{phone1}, new String[]{}, tokenRita);
		addUserConnection("jon", new String[]{phone}, new String[]{}, tokenRita);

		waitSomeTime(1000);
		
		// Verify emails send
		validateSentEmail(Arrays.<AMail>asList(new VerificationMail(email),
				new VerificationMail(email1),
				new ConnectionEmail(email1),
				new ConnectionEmail(email)
				));
	}
	
}