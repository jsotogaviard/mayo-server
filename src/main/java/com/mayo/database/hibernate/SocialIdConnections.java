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
@Table(name=IMayoService.SOCIAL_ID_CONNECTIONS)
public class SocialIdConnections {

	@Column(name="id")
	protected Long id;

	@Id
	@Column(name="socialId")
	protected String socialId;
	
	public SocialIdConnections(Long id, String socialId) {
		this.id = id;
		this.socialId = socialId;
	}
	
	public SocialIdConnections() {}

	/**
	 * @return The id
	 */
	public Long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SocialIdConnections [id=" + this.id + ", socialId=" + this.socialId
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ ((this.socialId == null) ? 0 : this.socialId.hashCode());
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
		SocialIdConnections other = (SocialIdConnections) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.socialId == null) {
			if (other.socialId != null)
				return false;
		} else if (!this.socialId.equals(other.socialId))
			return false;
		return true;
	}


}
