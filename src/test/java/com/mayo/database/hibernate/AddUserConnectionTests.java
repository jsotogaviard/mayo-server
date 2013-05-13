package com.mayo.database.hibernate;

import junit.framework.Assert;

import org.junit.Test;

public class AddUserConnectionTests extends AServiceTests {

	@Test
	public void testAddUserConnection() {
		addUser("jso@qfs.com", "secret");
		HibernateUtil.update(new Users(1L, "jso@qfs.com" ,"secret", true));
		String token = login("jso@qfs.com", "secret");
		addUserConnection(1L, "rita", new String[]{"050505050"}, new String[]{"rita1@qfs.com"}, token);
	}
	
	@Test
	public void testWrongAddUserConnection() {
		addUser("jso@qfs.com", "secret");
		HibernateUtil.update(new Users(1L,"jso@qfs.com" ,"secret", true));
		
		// Does not send the token to be connected
		try {
			addUserConnection(1L, "rita", new String[]{"050505050"}, new String[]{"rita1@qfs.com"}, null);
			Assert.fail();
		} catch (Exception ex) {}
		
	}

}