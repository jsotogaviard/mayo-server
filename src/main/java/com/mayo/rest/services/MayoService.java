package com.mayo.rest.services;

import static com.mayo.IMayoService.BIRTH_DATE;
import static com.mayo.IMayoService.EMAILS;
import static com.mayo.IMayoService.EMTPY_ARRAY;
import static com.mayo.IMayoService.FIRST_NAME;
import static com.mayo.IMayoService.ID;
import static com.mayo.IMayoService.LAST_NAME;
import static com.mayo.IMayoService.LINKS_CLASS;
import static com.mayo.IMayoService.MAIN_EMAIL;
import static com.mayo.IMayoService.MAYO_AUTH_TOKEN;
import static com.mayo.IMayoService.NAME;
import static com.mayo.IMayoService.NO_VALUE;
import static com.mayo.IMayoService.PASSWORD;
import static com.mayo.IMayoService.PHONES;
import static com.mayo.IMayoService.SEX;
import static com.mayo.IMayoService.USERS_CLASS;
import static com.mayo.IMayoService.USER_ID;
import static com.mayo.database.hibernate.HibernateUtil.getOne;
import static com.mayo.database.hibernate.HibernateUtil.getOneOrNone;
import static com.mayo.database.hibernate.HibernateUtil.list;
import static com.mayo.database.hibernate.HibernateUtil.save;
import static com.mayo.database.hibernate.HibernateUtil.search;
import static com.mayo.database.hibernate.HibernateUtil.update;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mayo.IMayoService;
import com.mayo.IUserMatcher;
import com.mayo.MayoException;
import com.mayo.database.hibernate.Connections;
import com.mayo.database.hibernate.EmailsConnections;
import com.mayo.database.hibernate.EmailsUsers;
import com.mayo.database.hibernate.Links;
import com.mayo.database.hibernate.PhonesConnections;
import com.mayo.database.hibernate.PhonesUsers;
import com.mayo.database.hibernate.Users;
import com.mayo.mail.AMail;
import com.mayo.mail.ConnectionEmail;
import com.mayo.mail.VerificationMail;

@Component
@Path("mayo")
public class MayoService{

	/** Global logger */
	public static final Logger LOGGER = Logger.getLogger(MayoService.class.getName());

	/** To handler json types*/
	public static final ObjectMapper mapper = new ObjectMapper();
	
	/** The queue with the emails */
	@Autowired(required=true)
	protected ConcurrentLinkedQueue<AMail> emailQueue;
	
	@Context 
	protected HttpServletRequest httpRequest;
	
	@Autowired(required=true)
	protected IUserMatcher matcher;

	@Autowired(required=true)
	protected TokenStore tokenStore;

	/** Empty constructor */
	public MayoService() {}

	@Path("verifyEmail/{" + USER_ID + "}")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String verifyEmail(@PathParam(USER_ID) String userId, @QueryParam(MAIN_EMAIL) String mainEmail){
		LOGGER.info("Verifying " + mainEmail);
		Map<String, Object> slicers = new HashMap<String,Object>();
		slicers.put(MAIN_EMAIL, mainEmail);
		slicers.put(ID, userId);
		List<Users> existingUsers = search(USERS_CLASS, slicers);
		Users user = getOne(existingUsers);
		if(user.isVerified()){
			return "User already verified " + mainEmail;
		} else {
			user.setVerified(true);
			update(user);
			return "User verified " + mainEmail;
		}
	}

	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String login(
			@FormParam(MAIN_EMAIL) String mainEmail,
			@FormParam(PASSWORD) String password,
			@Context HttpServletResponse servletResponse) throws ParseException, JsonParseException, JsonMappingException, IOException{
		LOGGER.info("login " + mainEmail);
		Map<String, Object> slicers = new HashMap<String,Object>();
		slicers.put(MAIN_EMAIL, mainEmail);
		slicers.put(PASSWORD, password);
		List<Users> existingUsers = search(USERS_CLASS, slicers);
		Users user = getOne(existingUsers);
		if(user.isVerified()){
			
			// Return the created token
			String token = tokenStore.createToken(user.getId());
			servletResponse.addCookie(new Cookie(MAYO_AUTH_TOKEN, token));
			
			return "  ";
		} else {
			throw new MayoException("User is not verified");
		}
	}
	
	
	@Path("/registerUser")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String registerUser(
			@FormParam(MAIN_EMAIL) String mainEmail,
			@FormParam(FIRST_NAME) String firstName,
			@FormParam(LAST_NAME) String lastName,
			@FormParam(PASSWORD) String password,
			@FormParam(BIRTH_DATE) String birthDate,
			@FormParam(SEX) String sex,
			@Context HttpServletResponse servletResponse) throws ParseException, JsonParseException, JsonMappingException, IOException{

		LOGGER.info("register user  " + mainEmail);
		List<Users> existingUsers = search(USERS_CLASS, Collections.<String,Object>singletonMap(MAIN_EMAIL, mainEmail));
		Users user = getOneOrNone(existingUsers);
		if (user == null) {

			// User does not exist
			// Create it
			user = new Users(mainEmail, password, firstName, lastName, sex, birthDate);
			Long userId = (Long) save(user);
			
			save(new EmailsUsers(userId, mainEmail));

			// Update the links table
			Long foundConnection = matcher.matchConnection(new String[]{mainEmail}, new String[]{});
			if (foundConnection == null) {
				foundConnection = NO_VALUE;
			}
			save(new Links(userId,foundConnection, EMTPY_ARRAY));

			// Add email to queue
			VerificationMail email = new VerificationMail(mainEmail, userId);
			emailQueue.offer(email);
			return Long.toString(userId);
		} else {

			// The user exists
			// Return something telling that it failed
			throw new MayoException("User alredy exists"); 
		}

	}

	@Path("/userConnection")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String addUserConnection(
			@FormParam(NAME) String name,
			@FormParam(EMAILS) String jsonEmails,
			@FormParam(PHONES) String jsonPhones,
			@Context HttpServletResponse servletResponse) throws ParseException, JsonParseException, JsonMappingException, IOException{
		LOGGER.info("add user  " + name);
		
		String[] phones = mapper.readValue(jsonPhones, String[].class);
		String[] emails = mapper.readValue(jsonEmails, String[].class);

		long currentUserId = matcher.findUser(httpRequest.getCookies(), tokenStore);
		
		// Search among phones
		// and emails
		Long connectionId = matcher.matchConnection(emails, phones);
		if (connectionId == null) {

			// This connection does not exist add it
			connectionId = (Long) save(new Connections(name));
			for (String email : emails) 
				save(new EmailsConnections(connectionId, email));

			for (String phone : phones) 
				save(new PhonesConnections(connectionId, phone));

			// The user has not already been 
			// linked to a connection
			Long userId = matcher.matchUser(emails, phones);
			if (userId != null) {
				List<Links> links = search(LINKS_CLASS, Collections.<String,Object>singletonMap(USER_ID, userId));
				Links link = getOne(links);
				link.setConnectionId(connectionId);
				update(link);
			}
		}

		// Add the current user id connections's
		List<Links> links = search(LINKS_CLASS, Collections.<String,Object>singletonMap(USER_ID, currentUserId));
		Links link = getOne(links);
		TLongList list  = new TLongArrayList(link.getConnections());
		list.add(connectionId);
		link.setConnections(list.toArray());
		update(link);

		List<long[]> result = matcher.findLinkedUsers(currentUserId);
		
		// Send the email
		sendConnectionEmail(result);
		
		return Long.toString(connectionId);		
	}
	
	@Path("/updateUserInformation")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updateUserInformation(
			@FormParam(EMAILS) String jsonEmails,
			@FormParam(PHONES) String jsonPhones,
			@Context HttpServletResponse servletResponse) throws ParseException, JsonParseException, JsonMappingException, IOException{
		String[] phones = mapper.readValue(jsonPhones, String[].class);
		String[] emails = mapper.readValue(jsonEmails, String[].class);

		long currentUserId = matcher.findUser(httpRequest.getCookies(), tokenStore);

		// TODO Search for the mail before inserting
		// If there is an equivalent mail
		// check that it is the same
		for (String email : emails) 
			save(new EmailsUsers(currentUserId, email));

		for (String phone : phones) 
			save(new PhonesUsers(currentUserId, phone));
		
		// Search among the connections
		Long connectionId = matcher.matchConnection(emails, phones);
		if (connectionId == null) {

			// No connection was found
			// Nothing to do 
		} else {
			
			// There is a connection
			// Save it
			List<Links> links = search(LINKS_CLASS, Collections.<String,Object>singletonMap(USER_ID, currentUserId));
			Links link = getOne(links);
			link.setConnectionId(connectionId);
			update(link);
			
			List<long[]> result = matcher.findLinkedUsers(currentUserId);
			
			// Send the email
			sendConnectionEmail(result);
		}
	}


	@Path("/coucou")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String coucou(){
		LOGGER.info("coucou");
		return printAll();
	}
	
	public void sendConnectionEmail(List<long[]> result){
		// Send the email
		for (long[] linked : result) {
			List<Users> users1 = search(USERS_CLASS, Collections.<String,Object>singletonMap(ID, linked[0]));
			Users user1 = getOne(users1);
			
			// Do not send many times the connection
			// email
			if (user1.isConnectionEmailSent()){
				return;
			} else {
				
				// Set the email as sent
				user1.setConnectionEmailSent(true);
				update(user1);
			}
			List<Users> users2 = search(USERS_CLASS, Collections.<String,Object>singletonMap(ID, linked[1]));
			Users user2 = getOne(users2);

			// Do not send many times the connection
			// email
			if (user2.isConnectionEmailSent()){ 
				return;
			} else {
				
				// Set the email as sent
				user2.setConnectionEmailSent(true);
				update(user2);
			}
			
			// Send an email to both
			emailQueue.add(new ConnectionEmail(user1, user2));			
			emailQueue.add(new ConnectionEmail(user2, user1));
		}
	}
	
	public static String printAll() {
		StringBuilder sb = new StringBuilder();
		sb.append("Coucou <br>");
		for (Object database : IMayoService.DATABASES) {
			sb.append(printDatabase(database));
			sb.append("<br>");
		}
		return sb.toString();
	}
	
	protected static <T> String printDatabase(T clazz) {
		StringBuilder sb = new StringBuilder();
		final String name = clazz.getClass().getName();
		sb.append(name);
		sb.append("<br>");
		List<T> users = list(clazz);
		for (T object : users) {
			sb.append(object);
			sb.append("<br>");
		}
		return sb.toString();
		
	}
}