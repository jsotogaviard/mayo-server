package com.mayo.database.hibernate;

import org.junit.Assert;
import org.junit.Test;

/**
 * WebResource webResource = client.resource("http://example.com/base");
 * 
 * GET
 * String s = webResource.get(String.class);
 * curl http://example.com/base
 * 
 * GET WITH PARAMS
 * MultivaluedMap queryParams = new MultivaluedMapImpl();
 * queryParams.add("param1", "val1");
 * queryParams.add("param2", "val2");
 * String s = webResource.queryParams(queryParams).get(String.class);
 * curl http://example.com/base?param1=val1&param2=val2
 * 
 * POST
 * MultivaluedMap formData = new MultivaluedMapImpl();
 * formData.add("name1", "val1");
 * formData.add("name2", "val2");
 * ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
 * curl -d mainEmail=jsotogaviard@gmail.com -d name=jonathan -d password=secret -d emails=[] -d phones=[] http://localhost:9090/rest/mayo/registerUser
 *
 * @author Quartet FS
 */
public class LoginTest extends AServiceTests {

	@Test
	public void testLogin() {
		
		// Add user to the database
		HibernateUtil.save(new Users(1L, "jso@qfs.com" ,"secret", true));
		String token = login("jso@qfs.com", "secret");
		Assert.assertNotNull(token);
	}
	
	@Test
	public void testLoginNotVerified() {
		
		// Add user to the database
		HibernateUtil.save(new Users(1L,"jso@qfs.com" ,"secret", false));
		try {
			login("jso@qfs.com", "secret");
			Assert.fail();
		} catch (Exception e) {}
		
	}
	
}