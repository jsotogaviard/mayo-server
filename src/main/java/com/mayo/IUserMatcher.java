/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo;

import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;

import com.mayo.database.hibernate.Interests;
import com.mayo.rest.services.TokenStore;

/**
 *
 * @author Mayo
 */
public interface IUserMatcher {

	/**
	 * Null corresponds to a no match
	 * Otherwise returns the connection id value
	 * 
	 * @param emails the user emails
	 * @param phones the user phones
	 * @return null or the found user id
	 */
	Long searchConnection(String[] emails, String[] phones);
	
	/**
	 * Null corresponds to a no match. Otherwise
	 * return the user id value
	 * 
	 * @param emails the user emails
	 * @param phones the user phones
	 * @return null or the found user id
	 */
	Long searchUser(String[] emails, String[] phones);
	
	/**
	 * 
	 * @param user_id
	 * @return
	 */
	Set<Long> usersLinks(Long user_id);

	/**
	 * Search among the given cookies
	 * of the http request the mayo cookies
	 * that identify the user
	 * 
	 * @param cookies
	 * @param tokenStore
	 * @return the authenticated user
	 */
	long findUser(Cookie[] cookies, TokenStore tokenStore);

	/**
	 * @param currentUserId
	 * @return 
	 */
	List<long[]> findLinkedUsers(long currentUserId);

	/**
	 * @param currentUserId
	 * @param connectionId
	 * @return 
	 */
	List<Interests> findMatch(long currentUserId, Long connectionId);
}
