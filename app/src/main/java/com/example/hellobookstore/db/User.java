package com.example.hellobookstore.db;

import android.util.Log;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class User extends DataSupport {

	private static final String TAG = "User---";

	private Long id;
	private String username;
	private String name;
	private String password;
	private String email;

	private String rentedBookString;


	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.rentedBookString = "";
	}

	public User() {
		this.rentedBookString = "";
	}

	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.rentedBookString = "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void addBookId(Long id) {
		StringBuilder sb = new StringBuilder(rentedBookString);
		sb.append(id + " ");
		//StringBuilder转换为String
		rentedBookString = ""+ sb;
	}
	public void removeBookId(Long id) {
		StringBuilder sb = new StringBuilder(rentedBookString);
		String[] ids = sb.toString().split(" ");
		sb.delete(0, sb.length());
		for (String s : ids) {
			if (!s.equals(String.valueOf(id))) {
				Log.e(TAG, s + "!=" + String.valueOf(id));
				sb.append(s + " ");
			}
		}
		//StringBuilder转换为String
		rentedBookString = sb.toString();
		Log.e(TAG, "rentedBookString=" + rentedBookString);

	}
	public boolean isLastBook() {
		if (rentedBookString.length() == 2) {
			return true;
		}
		return false;
	}

	public String getRentedBookString() {
		return rentedBookString;
	}

	public void setRentedBookString(String rentedBookString) {
		this.rentedBookString = rentedBookString;
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
