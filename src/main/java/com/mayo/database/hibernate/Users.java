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
	
	/** Used for tests */
	public Users(long id, String mainEmail, String password, boolean verified){
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
	
	public Long getId() {
		return this.id;
	}
	
	public String getMainEmail() {
		return this.mainEmail;
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
				+ ((this.birthDate == null) ? 0 : this.birthDate.hashCode());
		result = prime * result
				+ ((this.firstName == null) ? 0 : this.firstName.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ ((this.lastName == null) ? 0 : this.lastName.hashCode());
		result = prime * result
				+ ((this.mainEmail == null) ? 0 : this.mainEmail.hashCode());
		result = prime * result
				+ ((this.password == null) ? 0 : this.password.hashCode());
		result = prime * result
				+ ((this.sex == null) ? 0 : this.sex.hashCode());
		result = prime * result + (this.verified ? 1231 : 1237);
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
		if (this.birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!this.birthDate.equals(other.birthDate))
			return false;
		if (this.firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!this.firstName.equals(other.firstName))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!this.lastName.equals(other.lastName))
			return false;
		if (this.mainEmail == null) {
			if (other.mainEmail != null)
				return false;
		} else if (!this.mainEmail.equals(other.mainEmail))
			return false;
		if (this.password == null) {
			if (other.password != null)
				return false;
		} else if (!this.password.equals(other.password))
			return false;
		if (this.sex == null) {
			if (other.sex != null)
				return false;
		} else if (!this.sex.equals(other.sex))
			return false;
		if (this.verified != other.verified)
			return false;
		return true;
	}

	
}
