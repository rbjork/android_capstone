package com.androidcapstone.symptommanagement.repository;

import java.util.Date;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

//@Entity
public class Medication {
	
	//@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	
	private String medicationName;
	private String dosage;
	
	public String getMedicationName() {
		return medicationName;
	}

	public String getDosage() {
		return dosage;
	}
	
	public Medication(){
		
	}
	
	public Medication(String medicationName, String dosage){
		
		this.medicationName = medicationName;
		this.dosage = dosage;
	};
	
	
}
