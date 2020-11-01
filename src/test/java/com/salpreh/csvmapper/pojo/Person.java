package com.salpreh.csvmapper.pojo;

import com.salpreh.csvmapper.annotation.CSVField;
import com.salpreh.csvmapper.annotation.CSVSerializable;

@CSVSerializable
public class Person {
	
	@CSVField(key = "first_name")
	private String name;
	@CSVField(key = "second_name")
	private String surname;
	@CSVField
	private int age;
	@CSVField
	private String email;


	public Person(String name, String surname, int age, String email) {
		super();
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
