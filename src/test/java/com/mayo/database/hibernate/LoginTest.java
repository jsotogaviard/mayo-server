package com.mayo.database.hibernate;

import org.junit.Assert;
import org.junit.Test;

public class LoginTest extends AServiceTests {
	
	@Test
	public void uniqueTest() {
		// Add the verified user to the database
		long userId = addUser(email, password);
		HibernateUtil.update(new Users(userId, email, password, true));
		String token = login("jso@qfs.com", password);
		Assert.assertNotNull(token);
		
		// Add user to the database
		// But do not verify it
		addUser(email1, password1);
		try {
			login(email1, password1);
			Assert.fail();
		} catch (Exception e) {}

	}

}