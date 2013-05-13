/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.rest.services;

import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;

import java.util.UUID;

import com.mayo.IMayoService;

/**
 *
 * @author Quartet FS
 */
public class TokenStore {
	
	/** Default duration of a token */
	public static final long TOKEN_DURATION = 10 * 60 * 1000 ; // 10 min

	/** The token creation */
	TObjectLongMap<String> tokenCreation;

	/** The users by token*/
	TObjectLongMap<String> tokenUsers ;

	/** Constructor
	 * 
	 */
	public TokenStore() {
		tokenUsers = new TObjectLongHashMap<String>();
		tokenCreation = new TObjectLongHashMap<String>();
	}
	
	
	/**
	 * Creates the token and stores it 
	 * 
	 * @param userId the user id
	 * @return the created token 
	 */
	public String createToken(Long userId) {
		String token = UUID.randomUUID().toString();
		tokenUsers.put(token, userId);
		tokenCreation.put(token, System.currentTimeMillis());
		return token;
	}
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public long validateToken(String token) {
		
		if (tokenCreation.containsKey(token)) {
			
			if (System.currentTimeMillis() - tokenCreation.get(token) < TOKEN_DURATION ) {
				
				// The token is valid
				return tokenUsers.get(token);
			} else {
				return IMayoService.NO_VALUE;
			}
			
		} else {
			return IMayoService.NO_VALUE;
		}

	}

}
