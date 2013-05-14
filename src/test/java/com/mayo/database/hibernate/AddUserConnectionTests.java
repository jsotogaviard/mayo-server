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
		addUser(email, password);
		HibernateUtil.update(new Users(1L, email ,password, true));
		String token = login(email, password);
		addUserConnection(1L, "rita", new String[]{"050505050"}, new String[]{"rita1@qfs.com"}, token);
		
		// database
		validateDatabase(USERS_CLASS, Arrays.asList(new Users(1L, email ,password, true)));
		validateDatabase(CONNECTIONS_CLASS, Arrays.asList(new Connections(1L, "rita")));
		validateDatabase(PHONES_CONNECTIONS_CLASS, Arrays.asList(new PhonesConnections(1L, "050505050")));
		validateDatabase(EMAILS_CONNECTIONS_CLASS, Arrays.asList(new EmailsConnections(1L, "rita1@qfs.com")));
		
		// Add a connection without having a connection
		// It fails
		try {
			addUserConnection(1L, "rita", new String[]{"050505050"}, new String[]{"rita1@qfs.com"}, null);
			Assert.fail();
		} catch (Exception ex) {}
	}

}