/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.database.hibernate;

import static com.mayo.IMayoService.FROM_USER;
import static com.mayo.IMayoService.INTERESTS;
import static com.mayo.IMayoService.TO_CONNECTION;
import static com.mayo.IMayoService.TO_USER;
import static com.mayo.IMayoService.VERIFICATION_EMAIL_SENT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	@Column(name=FROM_USER)
	protected Long fromUser;
	
	@Column(name=TO_USER)
	protected Long toUser;
	
	@Column(name=TO_CONNECTION)
	protected Long toConnection;
	
	@Column(name=VERIFICATION_EMAIL_SENT)
	protected boolean emailSet;
	
	public Interests(Long fromUser, Long toUser, Long toConnection) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.toConnection = toConnection;
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
	 * Sets the toConnection
	 * @param toConnection The toConnection to set
	 */
	public void setToConnection(Long toConnection) {
		this.toConnection = toConnection;
	}
	
	/**
	 * Sets the toUser
	 * @param toUser The toUser to set
	 */
	public void setToUser(Long toUser) {
		this.toUser = toUser;
	}
	
	/**
	 * @return The emailSet
	 */
	public boolean isEmailSent() {
		return this.emailSet;
	}

	@Override
	public String toString() {
		return "Interests [id=" + this.id + ", fromUser=" + this.fromUser
				+ ", toUser=" + this.toUser + ", toConnection="
				+ this.toConnection + ", emailSet=" + this.emailSet + "]";
	}
	
}
