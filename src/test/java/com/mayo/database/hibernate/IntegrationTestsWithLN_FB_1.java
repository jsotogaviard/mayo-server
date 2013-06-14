package com.mayo.database.hibernate;

import java.util.Arrays;

import org.junit.Test;

import com.mayo.mail.AMail;
import com.mayo.mail.VerificationMail;

public class IntegrationTestsWithLN_FB_1 extends AServiceTests {
	
	@Test
	public void uniqueTest() {
		// Jonathan adds rita
		long userId = registerUser(email, password);
		HibernateUtil.update(new Users(userId, email, password, true));
		String tokenSoto = login(email, password);
		updateUserInformation(new String[]{}, new String[]{}, new String[]{lnId}, tokenSoto);
		addUserConnection("rita", new String[]{}, new String[]{}, new String[]{lnId1}, tokenSoto);

		// Rita adds Jonathan
		long userId1 = registerUser(email1, password1);
		HibernateUtil.update(new Users(userId1, email1, password1, true));
		String tokenRita = login(email1, password1);
		updateUserInformation(new String[]{}, new String[]{}, new String[]{fbId1}, tokenRita);
		addUserConnection("jon", new String[]{}, new String[]{},new String[]{fbId},  tokenRita);

		waitSomeTime(1000);
		
		// Verify emails send
		validateSentEmail(Arrays.<AMail>asList(new VerificationMail(email),
				new VerificationMail(email1)
				));
	}
	
}