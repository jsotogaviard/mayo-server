package com.mayo.rest.services;

import static com.mayo.IMayoService.BIRTH_DATE;
import static com.mayo.IMayoService.CONNECTION_ID;
import static com.mayo.IMayoService.EMAILS;
import static com.mayo.IMayoService.FIRST_NAME;
import static com.mayo.IMayoService.FROM_USER;
import static com.mayo.IMayoService.ID;
import static com.mayo.IMayoService.INTERESTS_CLASS;
import static com.mayo.IMayoService.LAST_NAME;
import static com.mayo.IMayoService.LINKS_CLASS;
import static com.mayo.IMayoService.MAIN_EMAIL;
import static com.mayo.IMayoService.MAYO_AUTH_TOKEN;
import static com.mayo.IMayoService.NAME;
import static com.mayo.IMayoService.PASSWORD;
import static com.mayo.IMayoService.PHONES;
import static com.mayo.IMayoService.SEX;
import static com.mayo.IMayoService.TO_USER;
import static com.mayo.IMayoService.USERS_CLASS;
import static com.mayo.IMayoService.USER_ID;
import static com.mayo.database.hibernate.HibernateUtil.getOne;
import static com.mayo.database.hibernate.HibernateUtil.list;
import static com.mayo.database.hibernate.HibernateUtil.save;
import static com.mayo.database.hibernate.HibernateUtil.search;
import static com.mayo.database.hibernate.HibernateUtil.searchOne;
import static com.mayo.database.hibernate.HibernateUtil.searchOneOrNone;
import static com.mayo.database.hibernate.HibernateUtil.update;

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
import com.mayo.database.hibernate.Interests;
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
	public void login(
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
		
		// 1. Search for user
		Long userId = matcher.searchUser(new String[]{mainEmail}, new String[]{});
		if (userId == null) {

			// 2. User does not exist. Create it
			// Save its email
			Users user = new Users(mainEmail, password, firstName, lastName, sex, birthDate);
			userId = (Long) save(user);
			save(new EmailsUsers(userId, mainEmail));

			// 3. Search a connection
			// that matches the email
			Long foundConnection = matcher.searchConnection(new String[]{mainEmail}, new String[]{});
			if (foundConnection != null) {
				Links link = new Links(userId,foundConnection);
				save(link);
			
				matcher.updateInterests(link);
			}
			// 4. Add email to queue
			emailQueue.offer(new VerificationMail(mainEmail, userId));
			
			// Return the id of the user 
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

		// 1. Find the current user logged
		long currentUserId = matcher.findUser(httpRequest.getCookies(), tokenStore);
		
		// 2. Search among phones and emails the connection id
		Long connectionId = matcher.searchConnection(emails, phones);
		if (connectionId == null) {

			// 2.1 This connection does not exist add it
			connectionId = (Long) save(new Connections(name));
			for (String email : emails) 
				save(new EmailsConnections(connectionId, email));
			for (String phone : phones) 
				save(new PhonesConnections(connectionId, phone));
		
			// 2.2 The user has not already been linked to a connection
			Long connectionIdUserCoordinates = matcher.searchUser(emails, phones);
			if (connectionIdUserCoordinates != null) {
				Links link = new Links(connectionIdUserCoordinates, connectionId);
				save(link);
				
				matcher.updateInterests(link);
			}
		}

		// 3. Search if the connection is linked to a user
		Links link = searchOneOrNone(LINKS_CLASS, Collections.<String,Object>singletonMap(IMayoService.CONNECTION_ID, connectionId));
		if (link == null || link.getConnectionId() == null) {
			
			// 3.1.1 Save the connection with the connection id
			save(new Interests(currentUserId, null, connectionId));
			
		} else {
			
			// 3.2.1 Save the one direction interest
			Map<String, Object> slicers = new HashMap<String,Object>();
			slicers.put(FROM_USER, currentUserId);
			slicers.put(TO_USER, link.getUserId());
			Interests oneDirectionInterest = searchOneOrNone(INTERESTS_CLASS, slicers);
			if (oneDirectionInterest == null) {
				oneDirectionInterest = new Interests(currentUserId, link.getUserId(), null);
				save(oneDirectionInterest);	
			}
			
			// 3.2.2 Try to find the match the interests
			List<Interests> interests = matcher.findMatch(oneDirectionInterest);

			// 3.2.3 Send the connections emails
			sendConnectionEmail(interests);

		}
		
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

		// 1. Find the current user logged in
		long currentUserId = matcher.findUser(httpRequest.getCookies(), tokenStore);

		// 2. Add all the necessary
		// CAREFUL we suppose that these are new information
		for (String email : emails) 
			save(new EmailsUsers(currentUserId, email));

		for (String phone : phones) 
			save(new PhonesUsers(currentUserId, phone));
		
		// 3. Search among the connections
		Long connectionId = matcher.searchConnection(emails, phones);
		if (connectionId != null) {
			
			// 3.1 Search if the connection is linked to a user
			Links link = searchOneOrNone(LINKS_CLASS, Collections.<String,Object>singletonMap(CONNECTION_ID, connectionId));
			if (link == null) {
				
				// Link does not exist
				// Update with the information
				link = new Links(currentUserId, connectionId);
				save(link);
				
				matcher.updateInterests(link);
			} else if (link.getUserId() == null) {
				
				// Update with the information
				link.setUserId(currentUserId);
				update(link);
				
				matcher.updateInterests(link);
			}

			// Check data consistency
			if (link.getUserId() != currentUserId) 
				throw new MayoException("Users do not match");

			// Search for the users interested in the connection 
			List<Interests> interests = search(INTERESTS_CLASS, Collections.<String,Object>singletonMap(TO_USER, link.getUserId()));

			// Upgrade the interests to user ids 
			for (Interests interest : interests) {

				// Try to find the match the interests
				List<Interests> otherDirectionInterest = matcher.findMatch(interest);

				// Send the connections emails
				sendConnectionEmail(otherDirectionInterest);
			}
				
				
		}
	}


	@Path("/coucou")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String coucou(){
		LOGGER.info("coucou");
		return printAll();
	}
	
	public void sendConnectionEmail(List<Interests> interests){
		for (Interests interest : interests) {
			if (!interest.isEmailSent()) {
				
				// Find the users
				Users fromUser = searchOne(USERS_CLASS, Collections.<String,Object>singletonMap(ID, interest.getFromUser()));
				
				Users toUser = searchOne(USERS_CLASS, Collections.<String,Object>singletonMap(ID, interest.getToUser()));
				
				// Send the email
				emailQueue.add(new ConnectionEmail(fromUser, toUser));	
				
				// Update the database
				interest.setEmailSet(true);
				update(interest);
			}
		}
		
	}
	
	public static String printAll() {
		StringBuilder sb = new StringBuilder();
		sb.append("Coucou <br>\n");
		for (Object database : IMayoService.DATABASES) {
			sb.append(printDatabase(database));
			sb.append("<br> \n");
		}
		return sb.toString();
	}
	
	public static void print(){
		System.out.println(printAll());
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