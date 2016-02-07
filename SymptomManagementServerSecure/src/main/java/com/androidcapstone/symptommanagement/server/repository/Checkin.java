package com.androidcapstone.symptommanagement.server.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Date;

import javax.persistence.*;


@Entity
public class Checkin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	
	private long patientID; // foreign key
	private long checkinTime;
	private String painLevel; // Needs to be chosen from enum
	private boolean canEatDrink;
	
	@OneToMany
	@JoinColumn(name="checkinID")
	private Collection<MedicationTaken> medTaken;
	
	public Collection<MedicationTaken> getMedTaken() {
		return medTaken;
	}
	public void setMedTaken(Collection<MedicationTaken> medTaken) {
		this.medTaken = medTaken;
	}
	
	public long getPatientID() {
		return patientID;
	}
	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}
	public long getCheckinTime() {
		return checkinTime;
	}
	public void setCheckinTime(long checkinTime) {
		this.checkinTime = checkinTime;
	}
	public String getPainLevel() {
		return painLevel;
	}
	public void setPainLevel(String painLevel) {
		this.painLevel = painLevel;
	}
	public boolean isCanEatDrink() {
		return canEatDrink;
	}
	public void setCanEatDrink(boolean canEatDrink) {
		this.canEatDrink = canEatDrink;
	}
	
	public Checkin(){};
	
	public static Checkin factory(long patientID, String painLevel, boolean canEatDrink){
		Checkin chkin = new Checkin();
		chkin.canEatDrink = canEatDrink;
		chkin.painLevel = painLevel;
		chkin.checkinTime = Calendar.getInstance().getTimeInMillis();
		chkin.patientID = patientID;
		return chkin;
	}
}
