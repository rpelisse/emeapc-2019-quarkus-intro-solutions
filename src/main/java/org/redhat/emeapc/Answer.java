package org.redhat.emeapc;

import java.util.Objects;

import org.hibernate.validator.constraints.Length;

public class Answer {

	@Length(min = 5, message = "The name must be at least 5 characters long")
	private String name;

	private String description;

	public Answer() {};

	public Answer(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object other) {
		return !(other instanceof Answer) ? false :Objects.equals(((Answer) other).name, this.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
