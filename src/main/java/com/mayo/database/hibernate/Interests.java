/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.database.hibernate;

import static com.mayo.IMayoService.FROM_USER;
import static com.mayo.IMayoService.INTERESTS;
import static com.mayo.IMayoService.TO_USER;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Quartet FS
 */
@Entity
@Table(name=INTERESTS)
public class Interests {
	
	@Id
	@Column(name=FROM_USER)
	protected Long fromUser;
	
	@Id
	@Column(name=TO_USER)
	protected Long toUser;
	
	@Column(name=TO_USER)
	protected boolean emailSet;
	
	public Interests(Long fromUser, Long toUser) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.emailSet = false;
	}

	public Interests() {}

	/**
	 * @return The fromUser
	 */
	public Long getFromUser() {
		return this.fromUser;
	}

	/**
	 * @return The toUser
	 */
	public Long getToUser() {
		return this.toUser;
	}

	/**
	 * Sets the emailSet
	 * @param emailSet The emailSet to set
	 */
	public void setEmailSet(boolean emailSet) {
		this.emailSet = emailSet;
	}
	
	/**
	 * @return The emailSet
	 */
	public boolean isEmailSent() {
		return this.emailSet;
	}
	
	@Override
	public String toString() {
		return "Interests [fromUser=" + this.fromUser + ", toUser="
				+ this.toUser + ", emailSet=" + this.emailSet + "]";
	}

	
	
	

	
}
