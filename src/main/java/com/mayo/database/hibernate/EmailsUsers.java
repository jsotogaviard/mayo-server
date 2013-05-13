/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.database.hibernate;

import static com.mayo.IMayoService.EMAIL;
import static com.mayo.IMayoService.EMAILS_USERS;
import static com.mayo.IMayoService.ID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 *
 * @author Quartet FS
 */
@Entity
@Table(name=EMAILS_USERS)
public class EmailsUsers {
	
	@Column(name=ID)
	protected Long id;
	
	@Id
	@Column(name=EMAIL)
	protected String email;

	protected String mainEmail;

	/** Constructor
	 * @param id
	 * @param email
	 */
	public EmailsUsers(Long id, String email) {
		super();
		this.id = id;
		this.email = email;
	}

	public EmailsUsers(String mainEmail, String email) {
		super();
		this.mainEmail = mainEmail;
		this.email = email;
	}
	
	public EmailsUsers() {}

	/**
	 * @return The mainEmail
	 */
	public String getMainEmail() {
		return this.mainEmail;
	}

	/**
	 * Sets the mainEmail
	 * @param mainEmail The mainEmail to set
	 */
	public void setMainEmail(String mainEmail) {
		this.mainEmail = mainEmail;
	}
	
	/**
	 * @return The id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the id
	 * @param id The id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the email
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "EmailsUsers [id=" + this.id + ", email=" + this.email + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.email == null) ? 0 : this.email.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailsUsers other = (EmailsUsers) obj;
		if (this.email == null) {
			if (other.email != null)
				return false;
		} else if (!this.email.equals(other.email))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}
	
	
}
