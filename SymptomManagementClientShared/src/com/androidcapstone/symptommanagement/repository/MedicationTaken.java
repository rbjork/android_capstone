package com.androidcapstone.symptommanagement.repository;

import java.util.Date;

public class MedicationTaken {

	private long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		id = id;
	}
	
	
//	private Checkin checkin;
//	
//	public Checkin getCheckin() {
//		return checkin;
//	}
//	
//	public void setCheckin(Checkin checkin) {
//		this.checkin = checkin;
//	}
	
	
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
