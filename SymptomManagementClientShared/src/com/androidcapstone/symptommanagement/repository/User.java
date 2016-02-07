package com.androidcapstone.symptommanagement.repository;

public class User {
	
	
	public long id;
	
	
	private String username; // Primary Key
	private String password;
	private String userType;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
