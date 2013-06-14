/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo;

import java.util.List;

import javax.servlet.http.Cookie;

import com.mayo.database.hibernate.Interests;
import com.mayo.database.hibernate.Links;
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
	 * @param socialId 
	 * @return null or the found user id
	 */
	Long searchConnection(String[] emails, String[] phones, String[] socialId);
	
	/**
	 * Null corresponds to a no match. Otherwise
	 * return the user id value
	 * 
	 * @param emails the user emails
	 * @param phones the user phones
	 * @return null or the found user id
	 */
	Long searchUser(String[] emails, String[] phones, String[] socialIds);
	
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
	 * @param oneDirectionInterest
	 * @return
	 */
	List<Interests> findMatch(Interests oneDirectionInterest);

	/**
	 * @param link
	 */
	void updateInterests(Links link);
}
