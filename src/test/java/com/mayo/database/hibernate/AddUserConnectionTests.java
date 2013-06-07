package com.mayo.database.hibernate;

import static com.mayo.IMayoService.CONNECTIONS_CLASS;
import static com.mayo.IMayoService.EMAILS_CONNECTIONS_CLASS;
import static com.mayo.IMayoService.PHONES_CONNECTIONS_CLASS;
import static com.mayo.IMayoService.USERS_CLASS;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class AddUserConnectionTests extends AServiceTests {
	
	@Test
	public void uniqueTest() {
		
		// Add verified user
		long userId = registerUser(email, password);
		HibernateUtil.update(new Users(userId, email ,password, true));
		String token = login(email, password);
		long userConnectionId = addUserConnection("rita", new String[]{"050505050"}, new String[]{"rita1@qfs.com"}, token);
		
		// database
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(userId, email ,password, true)));
		validateDatabase(CONNECTIONS_CLASS, Arrays.asList(new Connections(userConnectionId, "rita")));
		validateDatabase(PHONES_CONNECTIONS_CLASS, Arrays.asList(new PhonesConnections(userConnectionId, "050505050")));
		validateDatabase(EMAILS_CONNECTIONS_CLASS, Arrays.asList(new EmailsConnections(userConnectionId, "rita1@qfs.com")));
		
		// Add a connection without having a connection
		// It fails
		try {
			addUserConnection("rita", new String[]{"050505050"}, new String[]{"rita1@qfs.com"}, null);
			Assert.fail();
		} catch (Exception ex) {}
	}

}