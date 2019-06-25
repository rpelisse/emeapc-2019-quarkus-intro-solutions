package org.redhat.emeapc;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Entity
public class Country extends PanacheEntity {

	@Column(unique = true)
	private String name;
	private String alpha2Code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlpha2Code() {
		return alpha2Code;
	}

	public void setAlpha2Code(String alpha2Code) {
		this.alpha2Code = alpha2Code;
	}
}
