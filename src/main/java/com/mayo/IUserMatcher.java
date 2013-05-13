/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo;

import java.util.Set;

/**
 *
 * @author Mayo
 */
public interface IUserMatcher {

	/**
	 * 
	 * @param emails
	 * @param phones
	 * @return
	 */
	Long matchConnection(String[] emails, String[] phones);
	
	/**
	 * 
	 * @param emails
	 * @param phones
	 * @return
	 */
	Long matchUser(String[] emails, String[] phones);
	
	/**
	 * 
	 * @param user_id
	 * @return
	 */
	Set<Long> usersLinks(Long user_id);
}
