package com.mayo.database.hibernate;

import static com.mayo.IMayoService.EMAILS;
import static com.mayo.IMayoService.MAIN_EMAIL;
import static com.mayo.IMayoService.NAME;
import static com.mayo.IMayoService.PASSWORD;
import static com.mayo.IMayoService.PHONES;
import static com.mayo.IMayoService.SOCIAL_ID;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.mayo.IMayoService;
import com.mayo.MayoException;
import com.mayo.jettyserver.MayoServer;
import com.mayo.mail.AMail;
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
	
	public static String email = "jso@qfs.com";
	public static String password = "secret";
	public static String phone = "050505050";
	public static String fbId = "456e-fb";
	public static String lnId = "456e-ln";

	public static String email1 = "jso1@qfs.com";
	public static String password1 = "secret1";
	public static String phone1 = "05060606";
	public static String fbId1 = "556e-fb";
	public static String lnId1 = "556e-ln";
	
	public static String[] EMPTY_ARRAY = new String[]{};
	
	/** The mayo server  */
	public static Server server;

	/** The client */
	public static Client client;
	
	public ObjectMapper mapper = new ObjectMapper();
	
	public static SimpleSmtpServer mailServer ;

	@BeforeClass
	public static void setUpServer() throws Exception {
		System.setProperty(IMayoService.TEST_ENV, "true");
		
		// Start the server
		server = MayoServer.createServer(PORT);
		server.start();		

		// Start the client
		client = Client.create();
		
		// The mail server
		mailServer = SimpleSmtpServer.start(Integer.parseInt(IMayoService.FAKE_MAIL_PORT));
	}

	@AfterClass
    public static void afterServer() throws Exception {
    	server.stop();
    	server.join();
    	deleteAll();
    	mailServer.stop();
    	server=null;
    	client =null;
 
    }
	
	public void validateSentEmail(List<AMail> sentEmails) {
		mailServer.stop();
		
		Assert.assertEquals(sentEmails.size(), mailServer.getReceivedEmailSize());
		Iterator<?> emailIter = mailServer.getReceivedEmail();
		for (int i = 0; i < sentEmails.size(); i++) {
			SmtpMessage email = (SmtpMessage)emailIter.next();
			AMail sentEmail = new AMail(email.getHeaderValue("To"),email.getHeaderValue("Subject"));
			Assert.assertEquals(sentEmail,sentEmails.get(i));
		}
		

	}
	
	public long registerUser(String mainEmail,String pwd){
		Form f = new Form();
		f.add(MAIN_EMAIL, mainEmail);
		f.add(PASSWORD, pwd);
	
		WebResource webResource = client.resource("http://localhost:9090/rest/mayo/registerUser");
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);

		if (response.getStatus() != 200) 
			throw new MayoException("Failed : HTTP error code : " + response.getStatus());
		String textResponse = (response.getEntity(String.class));
		return Long.parseLong(textResponse);
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
		if (response.getStatus() != 204) 
			throw new MayoException("Failed : HTTP error code : "+ response.getStatus());
		
		if (token == null) 
			throw new MayoException("Token cannot be null");

		return token;
	}
	
	protected void validateUser(Long userId, String mainEmail) {
		WebResource webResource = client.resource("http://localhost:9090/rest/mayo/verifyEmail/" + userId +	"?mainEmail=" + mainEmail);
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);

		if (response.getStatus() != 200) 
			throw new MayoException("Failed : HTTP error code : " + response.getStatus());
		
	}
	
	public <T> void validateDatabase(T clazz, List<T> expected){
		List<T> result = HibernateUtil.list(clazz);
		Assert.assertEquals(expected.size(), result.size());
		for (int i = 0; i < result.size(); i++) {
			Assert.assertEquals(expected.get(i), result.get(i));
		}
	}
	
	protected void waitSomeTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public long addUserConnection(String name, String[] phonesUser, String[] emailsUser, String[] socialIds , String token){
		Form f = new Form();
		f.add(NAME, name);
		JSONArray phones = new JSONArray();
		phones.addAll(Arrays.asList(phonesUser));
		f.add(PHONES, phones);
		JSONArray emails = new JSONArray();
		emails.addAll(Arrays.asList(emailsUser));
		f.add(EMAILS, emails);
		JSONArray socials = new JSONArray();
		socials.addAll(Arrays.asList(socialIds));
		f.add(SOCIAL_ID, socials);
		WebResource webResource = client.resource("http://localhost:9090/rest/mayo/userConnection");
		Builder builder = webResource.getRequestBuilder();
		if (token != null) 
			builder = builder.cookie(new NewCookie(IMayoService.MAYO_AUTH_TOKEN, token));
		ClientResponse response = builder.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);

		if (response.getStatus() != 200) 
			throw new MayoException("Failed : HTTP error code : " + response.getStatus());
		String textResponse = (response.getEntity(String.class));
		return Long.parseLong(textResponse);
		
	}
	
	@SuppressWarnings("unchecked")
	public void updateUserInformation(String[] phonesUser, String[] emailsUser, String[] socialIds, String token){
		Form f = new Form();
		JSONArray phones = new JSONArray();
		phones.addAll(Arrays.asList(phonesUser));
		f.add(PHONES, phones);
		JSONArray emails = new JSONArray();
		emails.addAll(Arrays.asList(emailsUser));
		f.add(EMAILS, emails);
		JSONArray socials = new JSONArray();
		socials.addAll(Arrays.asList(socialIds));
		f.add(SOCIAL_ID, socials);
		WebResource webResource = client.resource("http://localhost:9090/rest/mayo/updateUserInformation");
		Builder builder = webResource.getRequestBuilder();
		if (token != null) 
			builder = builder.cookie(new NewCookie(IMayoService.MAYO_AUTH_TOKEN, token));
		ClientResponse response = builder.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);

		if (response.getStatus() != 204) 
			throw new MayoException("Failed : HTTP error code : " + response.getStatus());
		
	}
	
	public static void deleteAll() {
		for (Object database : IMayoService.DATABASES) {
			delete(database);
		}
	}
	
	public static void printAll() {
		for (Object database : IMayoService.DATABASES) {
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