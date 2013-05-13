package com.mayo.database.hibernate;

import java.util.Iterator;

import org.junit.Test;

public class IntegrationTests extends AServiceTests {

	@Test
	public void testBasicScenario() {
		
		// Jonathan adds rita
		addUser("jso@qfs.com", "jonathan", "soto", new String[]{"050505050"}, new String[]{"jso1@qfs.com"});
		HibernateUtil.update(new Users(1L, "jonathan", "jso@qfs.com" ,"soto", true));
		String tokenSoto = login("jso@qfs.com", "soto");
		addUserConnection(1L, "rita", new String[]{"060606060"}, new String[]{}, tokenSoto);

		// Rita adds Jonathan
		addUser("rita@qfs.com", "rita", "maga", new String[]{"060606060"}, new String[]{});
		HibernateUtil.update(new Users(2L, "rita", "rita@qfs.com" ,"maga", true));
		String tokenRita = login("rita@qfs.com", "maga");
		addUserConnection(2L, "jon", new String[]{"050505050"}, new String[]{}, tokenRita);
		
		// Verify emails send
		mailServer.stop();
		System.out.println(mailServer.getReceivedEmailSize());
		@SuppressWarnings("unchecked")
		Iterator<Object> it = mailServer.getReceivedEmail();
		while(it.hasNext()){
			Object mail = it.next();
			System.out.println(mail);
		}
	}
	
	@Test
	public void testBasicScenario2() {
		
		// Jonathan adds rita
		addUser("jso@qfs.com", "jonathan", "soto", new String[]{}, new String[]{});
		HibernateUtil.update(new Users(1L, "jonathan", "jso@qfs.com" ,"soto", true));
		String tokenSoto = login("jso@qfs.com", "soto");
		addUserConnection(1L, "rita", new String[]{}, new String[]{"rita@qfs.com"}, tokenSoto);

		printAll();
		
		// Rita adds Jonathan
		addUser("rita@qfs.com", "rita", "maga", new String[]{}, new String[]{});
		HibernateUtil.update(new Users(2L, "rita", "rita@qfs.com" ,"maga", true));
		String tokenRita = login("rita@qfs.com", "maga");
		addUserConnection(2L, "jon", new String[]{}, new String[]{"jso@qfs.com"}, tokenRita);
		
		// Verify emails send
		mailServer.stop();
		System.out.println(mailServer.getReceivedEmailSize());
		@SuppressWarnings("unchecked")
		Iterator<Object> it = mailServer.getReceivedEmail();
		while(it.hasNext()){
			Object mail = it.next();
			System.out.println(mail);
		}
	}

}