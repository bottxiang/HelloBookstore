package com.example.hellobookstore.db;

import org.litepal.crud.DataSupport;

public class User extends DataSupport {

	private String username;
	private String name;
	private String password;
	private String email;


	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User() {

	}

	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
