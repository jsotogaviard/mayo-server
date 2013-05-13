/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.rest.services;

import static com.mayo.IMayoService.CONNECTION_ID;
import static com.mayo.IMayoService.EMAIL;
import static com.mayo.IMayoService.EMAILS_CONNECTIONS_CLASS;
import static com.mayo.IMayoService.EMAILS_USERS_CLASS;
import static com.mayo.IMayoService.LINKS_CLASS;
import static com.mayo.IMayoService.PHONE;
import static com.mayo.IMayoService.PHONES_CONNECTIONS_CLASS;
import static com.mayo.IMayoService.PHONES_USERS_CLASS;
import static com.mayo.IMayoService.USER_ID;
import static com.mayo.database.hibernate.HibernateUtil.getOne;
import static com.mayo.database.hibernate.HibernateUtil.getOneOrNone;
import static com.mayo.database.hibernate.HibernateUtil.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mayo.IUserMatcher;
import com.mayo.database.hibernate.EmailsConnections;
import com.mayo.database.hibernate.EmailsUsers;
import com.mayo.database.hibernate.Links;
import com.mayo.database.hibernate.PhonesConnections;
import com.mayo.database.hibernate.PhonesUsers;

/**
 *
 * @author Quartet FS
 */
public class UserMatcher implements IUserMatcher {

	@Override
	public Long matchConnection(String[] emails, String[] phones) {
		List<Long> foundConnections = new ArrayList<Long>();
		for (String email : emails) {
			List<EmailsConnections> emailConnections = search(EMAILS_CONNECTIONS_CLASS, Collections.<String,Object>singletonMap(EMAIL, email));
			for (EmailsConnections emailsConnection : emailConnections) {
				foundConnections.add(emailsConnection.getId());
			}
		}
		for (String phone : phones) {
			List<PhonesConnections> phoneConnections = search(PHONES_CONNECTIONS_CLASS, Collections.<String,Object>singletonMap(PHONE, phone));
			for (PhonesConnections phoneConnection : phoneConnections) {
				foundConnections.add(phoneConnection.getId());
			}
		}
		return getOneOrNone(foundConnections);
	}

	@Override
	public Long matchUser(String[] emails, String[] phones) {
		List<Long> foundConnections = new ArrayList<Long>();
		for (String email : emails) {
			List<EmailsUsers> emailConnections = search(EMAILS_USERS_CLASS, Collections.<String,Object>singletonMap(EMAIL, email));
			for (EmailsUsers emailsConnection : emailConnections) {
				foundConnections.add(emailsConnection.getId());
			}
		}
		for (String phone : phones) {
			List<PhonesUsers> phoneConnections = search(PHONES_USERS_CLASS, Collections.<String,Object>singletonMap(PHONE, phone));
			for (PhonesUsers phoneConnection : phoneConnections) {
				foundConnections.add(phoneConnection.getId());
			}
		}
		return getOneOrNone(foundConnections);
	}

	@Override
	public Set<Long> usersLinks(Long userId) {
		Set<Long> realUsers = new HashSet<Long>();
		List<Links> links = search(LINKS_CLASS, Collections.<String,Object>singletonMap(USER_ID, userId));
		Links link = getOne(links);
		for (int i = 0; i < link.getConnections().length; i++) {
			long connectionId = link.getConnections()[i];
			List<Links> foundUsers = search(LINKS_CLASS, Collections.<String,Object>singletonMap(CONNECTION_ID, connectionId));
			for (Links foundUser : foundUsers) {
				realUsers.add(foundUser.getUserId());
			}
		}
		
		// Real users that it is connected to 
		return realUsers;
	}

}
