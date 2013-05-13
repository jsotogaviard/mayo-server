/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.database.hibernate;

import java.util.Arrays;

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
@Table(name=IMayoService.LINKS)
public class Links {
	
	@Id
	@Column(name="user_id")
	protected Long userId;
	
	@Column(name="connection_id")
	protected Long connectionId;
	
	@Column(name="connections")
	protected long[] connections;

	/** Constructor
	 * @param userId
	 * @param connectionId
	 */
	public Links(Long userId, Long connectionId, long[] connections) {
		this.userId = userId;
		this.connectionId = connectionId;
		this.connections = connections;
	}
	
	/** Constructor
	 * 
	 */
	public Links() {
	}

	/**
	 * @return The userId
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Sets the userId
	 * @param userId The userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return The connectionId
	 */
	public Long getConnectionId() {
		return this.connectionId;
	}

	/**
	 * Sets the connectionId
	 * @param connectionId The connectionId to set
	 */
	public void setConnectionId(Long connectionId) {
		this.connectionId = connectionId;
	}

	/**
	 * @return The connections
	 */
	public long[] getConnections() {
		return this.connections;
	}
	
	/**
	 * Sets the connections
	 * @param connections The connections to set
	 */
	public void setConnections(long[] connections) {
		this.connections = connections;
	}

	@Override
	public String toString() {
		return "Links [userId=" + this.userId + ", connectionId="
				+ this.connectionId + ", connections="
				+ Arrays.toString(this.connections) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.connectionId == null) ? 0 : this.connectionId
						.hashCode());
		result = prime * result + Arrays.hashCode(this.connections);
		result = prime * result
				+ ((this.userId == null) ? 0 : this.userId.hashCode());
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
		Links other = (Links) obj;
		if (this.connectionId == null) {
			if (other.connectionId != null)
				return false;
		} else if (!this.connectionId.equals(other.connectionId))
			return false;
		if (!Arrays.equals(this.connections, other.connections))
			return false;
		if (this.userId == null) {
			if (other.userId != null)
				return false;
		} else if (!this.userId.equals(other.userId))
			return false;
		return true;
	}

	
	
	
	
	
}
