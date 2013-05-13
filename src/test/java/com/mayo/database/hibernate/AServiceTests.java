package com.mayo.database.hibernate;

import static com.mayo.IMayoService.CONNECTIONS_CLASS;
import static com.mayo.IMayoService.EMAILS;
import static com.mayo.IMayoService.EMAILS_CONNECTIONS_CLASS;
import static com.mayo.IMayoService.EMAILS_USERS_CLASS;
import static com.mayo.IMayoService.LINKS_CLASS;
import static com.mayo.IMayoService.MAIN_EMAIL;
import static com.mayo.IMayoService.NAME;
import static com.mayo.IMayoService.PASSWORD;
import static com.mayo.IMayoService.PHONES;
import static com.mayo.IMayoService.PHONES_CONNECTIONS_CLASS;
import static com.mayo.IMayoService.PHONES_USERS_CLASS;
import static com.mayo.IMayoService.USERS_CLASS;
import static com.mayo.IMayoService.USER_ID;
import static com.mayo.database.hibernate.HibernateUtil.delete;
import static com.mayo.database.hibernate.HibernateUtil.list;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.json.simple.JSONArray;
import org.junit.After;
import org.junit.Before;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.mayo.IMayoService;
import com.mayo.jettyserver.MayoServer;
import com.mayo.mail.MailSession;
import com.mayo.mail.AMail;
import com.mayo.rest.services.MayoService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.representation.Form;


 /** 
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
 * curl -d name1=val1 -d name2=val2 http://example.com/base
 *
 * @author Quartet FS
 */
public class AServiceTests{

	/** The port we are connected to  */
	public static final int PORT = 9090;
	
	public static final Object[] DATABASES = new Object[]{
		USERS_CLASS,
		EMAILS_USERS_CLASS,
		PHONES_USERS_CLASS,
		
		CONNECTIONS_CLASS,
		EMAILS_CONNECTIONS_CLASS,
		PHONES_CONNECTIONS_CLASS,
		
		LINKS_CLASS,
	};
	
	/** The mayo server  */
	public Server server;

	/** The client */
	public Client client;
	
	public ObjectMapper mapper = new ObjectMapper();
	
	public SimpleSmtpServer mailServer ;

	@Before
	public void setUpServer() throws Exception {
		// Start the server
		server = MayoServer.createServer(PORT);
		server.start();		

		// Start the client
		client = Client.create();
		
		// The mail server
		mailServer = SimpleSmtpServer.start(Integer.parseInt(MailSession.MAIL_PORT));
		
		System.setProperty(MayoService.TEST_ENV, "true");
	}

	@After
    public void afterServer() throws Exception {
    	server.stop();
    	server.join();
    	deleteAll();
    	mailServer.stop();
    	server=null;
    	client =null;
 
    }
	
	public void validateSentEmail(List<AMail> sentEmails) {
		mailServer.stop();
		
		Assert.assertEquals(mailServer.getReceivedEmailSize() , sentEmails.size());
		Iterator<?> emailIter = mailServer.getReceivedEmail();
		for (int i = 0; i < sentEmails.size(); i++) {
			
			SmtpMessage email = (SmtpMessage)emailIter.next();
			AMail sentEmail = new AMail(email.getHeaderValue("To"),email.getHeaderValue("Subject"));
			Assert.assertEquals(sentEmail,sentEmails.get(i));
		}
		

	}
	
	public void addUser(String mainEmail,String pwd){
		Form f = new Form();
		f.add(MAIN_EMAIL, mainEmail);
		f.add(PASSWORD, pwd);
	
		WebResource webResource = client.resource("http://localhost:9090/rest/mayo/registerUser");
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
	}
	
	public String login(String mainEmail, String pwd){
		Form f = new Form();
		f.add(MAIN_EMAIL, mainEmail);
		f.add(PASSWORD, pwd);

		WebResource webResource = client.resource("http://localhost:9090/rest/mayo/login");
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);
		List<NewCookie> cookies = response.getCookies();
		String token = null;
		for (int i = 0; i < cookies.size(); i++) {
			NewCookie cookie = cookies.get(i);
			if (cookie.getName().equals(IMayoService.MAYO_AUTH_TOKEN)) {
				token = cookie.getValue();
			}
		}
		if (response.getStatus() != 200 && token != null) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		return token;
	}
	
	protected void validateUser(Long userId, String mainEmail) {
		WebResource webResource = client.resource("http://localhost:9090/rest/mayo/verifyEmail/" + userId + 
				"?mainEmail=" + mainEmail);
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

	}
	
	public <T> void validateDatabase(T clazz, List<T> expected){
		List<T> result = HibernateUtil.list(clazz);
		Assert.assertEquals(expected.size(), result.size());
		for (int i = 0; i < result.size(); i++) {
			Assert.assertEquals(expected.get(i), result.get(i));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addUserConnection(long userId, String name, String[] phonesUser, String[] emailsUser, String token){
		Form f = new Form();
		f.add(USER_ID, userId);
		f.add(NAME, name);
		JSONArray phones = new JSONArray();
		phones.addAll(Arrays.asList(phonesUser));
		f.add(PHONES, phones);
		JSONArray emails = new JSONArray();
		emails.addAll(Arrays.asList(emailsUser));
		f.add(EMAILS, emails);
		WebResource webResource = client.resource("http://localhost:9090/rest/mayo/userConnection");
		Builder builder = webResource.getRequestBuilder();
		if (token != null) 
			builder = builder.cookie(new NewCookie(IMayoService.MAYO_AUTH_TOKEN, token));
		ClientResponse response = builder.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
	}
	
	/**
	 * 
	 */
	public void deleteAll() {
		for (Object database : DATABASES) {
			delete(database);
		}
	}
	
	public static void printAll() {
		for (Object database : DATABASES) {
			printDatabase(database);
		}
	}

	protected static <T> void  printDatabase(T clazz) {
		final String name = clazz.getClass().getName();
		System.out.println(name);
		List<T> users = list(clazz);
		for (T object : users) {
			System.out.println(object);
		}
		
	}
	
	public static void main(String[] args) {
		printAll();
	}
}