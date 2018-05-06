package org.nexus.entity;

import java.io.Serializable;

/**
 * 关系类
 * @author ZuoYu
 *
 */
public class Nexus implements Serializable {

	private Integer id;
	private String firstName;
	private String lastName;
	private String resultName;
	public Nexus() {
		super();
	}
	public Nexus(Integer id, String firstName, String lastName, String resultName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.resultName = resultName;
	}
	public Nexus(String firstName, String lastName, String resultName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.resultName = resultName;
	}
	public Nexus(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	@Override
	public String toString() {
		return "Nexus{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", resultName='" + resultName + '\'' +
				'}';
	}
}
