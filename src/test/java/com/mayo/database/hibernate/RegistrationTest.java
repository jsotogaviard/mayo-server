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
		long userId = registerUser(email, password);
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(userId, email ,password, false)));
		validateDatabase(EMAILS_USERS_CLASS, Arrays.asList(new EmailsUsers(userId, email)));
		
		// Wait for the mails to arrive
		waitSomeTime(1000);
		
		validateSentEmail(Collections.<AMail>singletonList(new VerificationMail(email)));
	
		validateUser(userId, email);
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(userId, email ,password, true)));
		
		// Update the email with another email
		String cookie = login(email, password);
		updateUserInformation(new String[]{}, new String[]{email1}, EMPTY_ARRAY, cookie);
		
		// Adding user already registered
		try {
			registerUser(email, password);
			Assert.fail();
		} catch (Exception ex) {}

		// Adding user already registered
		// With another email
		try {
			registerUser(email1, password);
			Assert.fail();
		} catch (Exception ex) {}
				
		
		// Already verified
		// Must not throw
		validateUser(userId, email);
		
	}
	
}