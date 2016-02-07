package com.androidcapstone.symptommanagement.server.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class MedicationTaken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		id = id;
	}
	
	
	@ManyToOne
	@JoinColumn(name="CHECKIN_ID")
	@JsonIgnore
	private Checkin checkin;
	
	public Checkin getCheckin() {
		return checkin;
	}
	
	public void setCheckin(Checkin checkin) {
		this.checkin = checkin;
	}
	
	
	private boolean taken;
	private long timeTaken;
	
	
	public boolean isTaken() {
		return taken;
	}
	public void setTaken(boolean taken) {
		this.taken = taken;
	}
	
	
	public long getTimeTaken() {
		return timeTaken;
	}
	public void setTimeTaken(long timeTaken) {
		this.timeTaken = timeTaken;
	}

	public MedicationTaken(){}
	
	// Above works
	// New below
	// Two choices copy or reference to MedicationPrescribed data
	
	// Choice one
	private String medicationName;
	public String getMedicationName() {
		return medicationName;
	}
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}
	

	// Choice two
	@ManyToOne
	//@JoinColumn(name="MEDICATIONPRESCRIBED_ID")
	private MedicationPrescribed medicationPrescribed;
	
	
	public MedicationPrescribed getMedicationPrescribed() {
		return medicationPrescribed;
	}
	public void setMedicationPrescribed(MedicationPrescribed medicationPrescribed) {
		this.medicationPrescribed = medicationPrescribed;
	}
	// Client creation
	public MedicationTaken(boolean taken, MedicationPrescribed med, long time){
		this.taken = taken;
		this.medicationPrescribed = med;
		this.timeTaken = time;
	}
}
