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
	
	@Id
	@GeneratedValue
	protected Long id;
	
	@Column(name="name")
	protected String name;
	
	public Connections(String name) {
		this.name = name;
	}
	
	public Connections(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Connections() {}

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

	@Override
	public String toString() {
		return "Connections [id=" + this.id + ", name=" + this.name + "]";
	}

}
