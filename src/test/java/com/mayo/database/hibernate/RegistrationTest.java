package com.mayo.database.hibernate;

import static com.mayo.IMayoService.USERS_CLASS;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import com.mayo.mail.AMail;
import com.mayo.mail.VerificationMail;

public class RegistrationTest extends AServiceTests {

	@Test
	public void testUserRegistration() {
		addUser("jso@qfs.com", "secret");
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L, "jso@qfs.com" ,"secret", false)));
		//validateDatabase(EMAILS_USERS_CLASS, Arrays.asList(new EmailsUsers(1L, "jso@qfs.com"), new EmailsUsers(1L, "jso1@qfs.com")));
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		validateSentEmail(Collections.singletonList(new AMail("jso@qfs.com", VerificationMail.SUBJECT)));
	
		validateUser(1L, "jso@qfs.com");
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L, "jso@qfs.com" ,"secret", true)));
	}
	
	@Test
	public void testUserAlreadyRegistered() {
		printAll();
		addUser("jso@qfs.com", "secret");
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L, "jso@qfs.com" ,"secret", false)));
		validateSentEmail(Collections.singletonList(new AMail("jso@qfs.com", VerificationMail.SUBJECT)));
	
		validateUser(1L, "jso@qfs.com");
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L, "jso@qfs.com" ,"soto", true)));
		
		try {
			addUser("jso@qfs.com", "secret");
			Assert.fail();
		} catch (Exception ex) {}
		
	}
	
	@Test
	public void testAlreadyVerified() {
		addUser("jso@qfs.com", "secret");
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L,"jso@qfs.com" ,"secret", false)));
		validateSentEmail(Collections.singletonList(new AMail("jso@qfs.com", VerificationMail.SUBJECT)));
	
		validateUser(1L, "jso@qfs.com");
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L, "jso@qfs.com" ,"secret", true)));
		
		// Already verified
		validateUser(1L, "jso@qfs.com");
	}
	
	

}