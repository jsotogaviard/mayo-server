/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.database.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mayo.IMayoService;

/**
 *
 * @author Quartet FS
 */
@Entity
@Table(name=IMayoService.PHONES_USERS)
public class PhonesUsers {

	@Column(name="id")
	protected Long id;

	@Id
	@Column(name="phone")
	protected String phone;

	protected String mainEmail;
	

	/** Constructor
	 * @param id
	 * @param phone
	 */
	public PhonesUsers(Long id, String phone) {
		super();
		this.id = id;
		this.phone = phone;
	}
	
	/** Constructor
	 * @param id
	 * @param phone
	 */
	public PhonesUsers(String mainEmail, String phone) {
		super();
		this.mainEmail = mainEmail;
		this.phone = phone;
	}
	
	/** Constructor
	 * 
	 */
	public PhonesUsers() {
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
	 * @return The phone
	 */
	public String getPhone() {
		return this.phone;
	}

	/**
	 * Sets the phone
	 * @param phone The phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "PhonesUsers [id=" + this.id + ", phone=" + this.phone + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ ((this.phone == null) ? 0 : this.phone.hashCode());
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
		PhonesUsers other = (PhonesUsers) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.phone == null) {
			if (other.phone != null)
				return false;
		} else if (!this.phone.equals(other.phone))
			return false;
		return true;
	}

	

}
