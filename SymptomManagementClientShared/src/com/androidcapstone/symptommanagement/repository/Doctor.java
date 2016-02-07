package com.androidcapstone.symptommanagement.repository;

import java.util.ArrayList;

//import javax.persistence.ElementCollection;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

//import com.fasterxml.jackson.annotation.JsonIgnore;


public class Doctor {
	

	private long id;
	
	private String firstName;
	private String lastName;
	private String department;
	private String username;
	
	//@JsonIgnore
	//@ElementCollection
	//private ArrayList<Patient> patients;
	//private ArrayList<Integer> patientIDs; // OR
	
	public Doctor(){}
	
	public Doctor(String firstName, String lastName, String department, String username){
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
		this.username = username;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
}
