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
@Table(name=IMayoService.PHONES_CONNECTIONS)
public class PhonesConnections {

	@Column(name="id")
	protected Long id;

	@Id
	@Column(name="phone")
	protected String phone;
	
	public PhonesConnections(Long id, String phone) {
		this.id = id;
		this.phone = phone;
	}
	
	public PhonesConnections() {}

	/**
	 * @return The id
	 */
	public Long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PhonesConnections [id=" + this.id + ", phone=" + this.phone
				+ "]";
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
		PhonesConnections other = (PhonesConnections) obj;
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
