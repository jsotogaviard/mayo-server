/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.rest.services;

import static com.mayo.IMayoService.EMAIL;
import static com.mayo.IMayoService.EMAILS_CONNECTIONS_CLASS;
import static com.mayo.IMayoService.EMAILS_USERS_CLASS;
import static com.mayo.IMayoService.INTERESTS_CLASS;
import static com.mayo.IMayoService.MAYO_AUTH_TOKEN;
import static com.mayo.IMayoService.NO_VALUE;
import static com.mayo.IMayoService.PHONE;
import static com.mayo.IMayoService.PHONES_CONNECTIONS_CLASS;
import static com.mayo.IMayoService.PHONES_USERS_CLASS;
import static com.mayo.IMayoService.TO_CONNECTION;
import static com.mayo.database.hibernate.HibernateUtil.getOneOrNone;
import static com.mayo.database.hibernate.HibernateUtil.search;
import static com.mayo.database.hibernate.HibernateUtil.searchOneOrNone;
import static com.mayo.database.hibernate.HibernateUtil.update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.mayo.IMayoService;
import com.mayo.IUserMatcher;
import com.mayo.MayoException;
import com.mayo.database.hibernate.EmailsConnections;
import com.mayo.database.hibernate.EmailsUsers;
import com.mayo.database.hibernate.Interests;
import com.mayo.database.hibernate.Links;
import com.mayo.database.hibernate.PhonesConnections;
import com.mayo.database.hibernate.PhonesUsers;

/**
 *
 * @author Quartet FS
 */
public class UserMatcher implements IUserMatcher {

	@Override
	public Long searchConnection(String[] emails, String[] phones) {
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
	public Long searchUser(String[] emails, String[] phones) {
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
	public long findUser(Cookie[] cookies, TokenStore tokenStore) {
		long currentUserId = NO_VALUE;
		for (int i = 0; i < cookies.length; i++) {
			if(cookies[i].getName().equals(MAYO_AUTH_TOKEN)){
				String token = cookies[i].getValue();
				currentUserId = tokenStore.validateToken(token);
				if (currentUserId == NO_VALUE) {
					throw new MayoException("Token of user is not valid");	
				} 
			} else {
				throw new MayoException("Token of user is not avalaible");
			}
		}

		if (currentUserId == NO_VALUE) 
			throw new MayoException(" The user has not been logged in");

		return currentUserId;
	}

	@Override
	public List<Interests> findMatch(Interests interest) {
		Map<String, Object> slicers = new HashMap<String,Object>();
		slicers.put(IMayoService.FROM_USER, interest.getToUser());
		slicers.put(IMayoService.TO_USER, interest.getFromUser());
		MayoService.printAll();
		Interests otherdirectionInterest = searchOneOrNone(IMayoService.INTERESTS_CLASS, slicers);
		if (otherdirectionInterest != null) {
			return Arrays.asList(interest, otherdirectionInterest);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void updateInterests(Links link) {
		List<Interests> interests = search(INTERESTS_CLASS, Collections.<String,Object>singletonMap(TO_CONNECTION, link.getConnectionId()));
		for (Interests interest : interests) {
			interest.setToConnection(null);
			interest.setToUser(link.getUserId());
			update(interest);
		}
		
	}

}
