/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.database.hibernate;

import static com.mayo.IMayoService.BIRTH_DATE;
import static com.mayo.IMayoService.FIRST_NAME;
import static com.mayo.IMayoService.LAST_NAME;
import static com.mayo.IMayoService.MAIN_EMAIL;
import static com.mayo.IMayoService.PASSWORD;
import static com.mayo.IMayoService.SEX;
import static com.mayo.IMayoService.USERS;
import static com.mayo.IMayoService.VERIFIED;

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
@Table(name=USERS)
public class Users {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	@Column(name=MAIN_EMAIL)
	protected String mainEmail;
	
	@Column(name=PASSWORD)
	protected String password;
	
	@Column(name=FIRST_NAME)
	protected String firstName;
	
	@Column(name=LAST_NAME)
	protected String lastName;
	
	@Column(name=SEX)
	protected String sex;
	
	@Column(name=BIRTH_DATE)
	protected String birthDate;

	@Column(name=VERIFIED)
	protected boolean verified;
	
	public Users() {}
	
	public Users(Long id, String mainEmail, String password, boolean verified){
		this.id = id;
		this.mainEmail = mainEmail;
		this.password = password;
		this.verified = verified;
	}
	
	public Users(String mainEmail, String password, String firstName, String lastName, String sex, String birthDate) {
		this.mainEmail = mainEmail;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sex = sex;
		this.birthDate = birthDate;
		
		// When creating the user
		// The verification is false
		this.verified = false ;
	}
	
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
	 * @return The password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((mainEmail == null) ? 0 : mainEmail.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + (verified ? 1231 : 1237);
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
		Users other = (Users) obj;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (mainEmail == null) {
			if (other.mainEmail != null)
				return false;
		} else if (!mainEmail.equals(other.mainEmail))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (verified != other.verified)
			return false;
		return true;
	}

}
