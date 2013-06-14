/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo;

import com.mayo.database.hibernate.Connections;
import com.mayo.database.hibernate.EmailsConnections;
import com.mayo.database.hibernate.EmailsUsers;
import com.mayo.database.hibernate.Interests;
import com.mayo.database.hibernate.Links;
import com.mayo.database.hibernate.PhonesConnections;
import com.mayo.database.hibernate.PhonesUsers;
import com.mayo.database.hibernate.SocialIdConnections;
import com.mayo.database.hibernate.SocialIdUsers;
import com.mayo.database.hibernate.Users;

/**
 *
 * @author Mayo
 */
public interface IMayoService {
	
	public static final String _ID = "_id";
	
	/** The user's tables */
	public static final Users USERS_CLASS = new Users();
	public static final String USERS = "Users";
	public static final String USERS_ID = USERS.toLowerCase() + _ID;
	
	public static final PhonesUsers PHONES_USERS_CLASS = new PhonesUsers();
	public static final String PHONES_USERS = "PhonesUsers";
	public static final String PHONES_USERS_ID = PHONES_USERS.toLowerCase() + _ID;
	
	public static final EmailsUsers EMAILS_USERS_CLASS = new EmailsUsers();
	public static final String EMAILS_USERS = "EmailsUsers";
	public static final String EMAILS_USERS_ID = EMAILS_USERS.toLowerCase() + _ID;
	
	public static final SocialIdUsers SOCIAL_ID_USERS_CLASS = new SocialIdUsers();
	public static final String SOCIAL_ID_USERS = "SocialIdUsers";
	public static final String SOCIAL_ID_USERS_ID = SOCIAL_ID_USERS.toLowerCase() + _ID;
	
	/** Connection's tables */
	public static final Connections CONNECTIONS_CLASS = new Connections();
	public static final String CONNECTIONS = "Connections";
	
	public static final PhonesConnections PHONES_CONNECTIONS_CLASS = new PhonesConnections();
	public static final String PHONES_CONNECTIONS = "PhonesConnections";
	public static final String PHONES_CONNECTIONS_ID = PHONES_CONNECTIONS.toLowerCase() + _ID;
	
	public static final EmailsConnections EMAILS_CONNECTIONS_CLASS = new EmailsConnections();
	public static final String EMAILS_CONNECTIONS = "EmailsConnections";
	public static final String EMAILS_CONNECTIONS_ID = EMAILS_CONNECTIONS.toLowerCase() + _ID;
	
	public static final SocialIdConnections SOCIAL_ID_CONNECTIONS_CLASS = new SocialIdConnections();
	public static final String SOCIAL_ID_CONNECTIONS = "SocialIdConnections";
	public static final String SOCIAL_ID_CONNECTIONS_ID = SOCIAL_ID_CONNECTIONS.toLowerCase() + _ID;
	
	/** Link's tables */
	public static final Links LINKS_CLASS = new Links();
	public static final String LINKS = "Links";
	public static final String LINKS_ID = LINKS.toLowerCase() + _ID;
	
	/** Interests's tables */
	public static final Interests INTERESTS_CLASS = new Interests();
	public static final String INTERESTS = "Interests";
	public static final String FROM_USER = "from_user";
	public static final String TO_USER = "to_user";
	public static final String TO_CONNECTION = "to_connection";
	public static final String VERIFICATION_EMAIL_SENT = "verification_email_sent";
	public static final String INTERESTS_ID = INTERESTS.toLowerCase() + _ID;
	
	public static final String ID = "id";
	
	public static final String EMAIL = "email";
	public static final String EMAILS = "emails";
	public static final String SOCIAL_ID = "socialId";
	public static final String MAIN_EMAIL = "mainEmail";
	public static final String NAME = "name";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PHONE = "phone";
	public static final String PHONES = "phones";
	public static final String PASSWORD = "password";
	public static final Long NO_VALUE = -1L;
	public static final String USER_ID = "user_id";
	public static final String BIRTH_DATE = "birth_date";
	public static final String SEX = "sex";
	public static final String CONNECTION_ID = "connection_id";
	public static final String VERIFIED = "verified";
	public static final String CONNECTION_EMAIL_SENT = "connection_email_sent";

	public static final String MAYO_AUTH_TOKEN = "MAYO_AUTH_TOKEN";
	
	public static final String FAKE_MAIL_PORT = "2567";

	/** TODO better method for test environment */
	public static final String TEST_ENV = "TestEnvironment";
	
	public static final Object[] DATABASES = new Object[]{
		USERS_CLASS,
		EMAILS_USERS_CLASS,
		PHONES_USERS_CLASS,
		SOCIAL_ID_USERS_CLASS,
		
		CONNECTIONS_CLASS,
		EMAILS_CONNECTIONS_CLASS,
		PHONES_CONNECTIONS_CLASS,
		SOCIAL_ID_CONNECTIONS_CLASS,
		
		LINKS_CLASS,
		INTERESTS_CLASS
		
	};
	
	public static final String AWS_SERVER = "http://ec2-54-214-124-166.us-west-2.compute.amazonaws.com:9090/mayo/rest/mayo/";
	
}
