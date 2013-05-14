package com.mayo.database.hibernate;

import static com.mayo.IMayoService.EMAILS_USERS_CLASS;
import static com.mayo.IMayoService.USERS_CLASS;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import com.mayo.mail.AMail;
import com.mayo.mail.VerificationMail;

public class RegistrationTest extends AServiceTests {
	
	@Test
	public void uniqueTest() {
		addUser(email, password);
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L, email ,password, false)));
		validateDatabase(EMAILS_USERS_CLASS, Arrays.asList(new EmailsUsers(1L, email)));
		
		// Wait for the mails to arrive
		waitSomeTime(3000);
		
		validateSentEmail(Collections.<AMail>singletonList(new VerificationMail(email)));
	
		validateUser(1L, email);
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L, email ,password, true)));
		
		// Adding user already registered
		try {
			addUser(email, password);
			Assert.fail();
		} catch (Exception ex) {}
		
		// Already verified
		// Must not throw
		validateUser(1L, email);
		
	}
	
}