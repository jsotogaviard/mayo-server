/*
 * (C) Quartet FS 2007-2013
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.mayo.database.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mayo.IMayoService;

/**
 *
 * @author Quartet FS
 */
@Entity
@Table(name=IMayoService.CONNECTIONS)
public class Connections {
	
	@Override
	public String toString() {
		return "Connections [id=" + this.id + ", name=" + this.name + "]";
	}

	@Id
	@GeneratedValue
	protected Long id;
	
	@Column(name="name")
	protected String name;
	
	/** Constructor
	 * @param id
	 * @param name
	 * @param password
	 * @param connectionsId
	 */
	public Connections(String name) {
		this.name = name;
	}
	
	/** Constructor
	 * 
	 */
	public Connections() {}

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
	 * @return The name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
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
		Connections other = (Connections) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}

	

}
