package com.androidcapstone.symptommanagement.repository;

import java.util.Collection;


public class MedicationPrescribed {
	

	public static MedicationPrescribed factory(long patientID, String medicationName, String rxnumber,String dosage, String instructions, String doctorsName){
		MedicationPrescribed mp = new MedicationPrescribed();
		mp.instructions = instructions;
		mp.patientID = patientID;
		mp.medicationName = medicationName;
		mp.rxnumber = rxnumber;
		mp.doctorsName = doctorsName;
		mp.dosage = dosage;
		return mp;
	}
	

	////
	
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private String instructions;
	private String doctorsName;
	private long patientID;
	private String rxnumber;
	private String dosage;
	
	
	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	

	
	private String medicationName;
	
	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public String getRxnumber() {
		return rxnumber;
	}

	public void setRxnumber(String rxnumber) {
		this.rxnumber = rxnumber;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getDoctorsName() {
		return doctorsName;
	}

	public void setDoctorsName(String doctorsName) {
		this.doctorsName = doctorsName;
	}

	public long getPatientID() {
		return patientID;
	}

	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}
	
	// Above works
	// below is new to support choice two of MedicationTaken class

//	private Collection<MedicationTaken> medicationTaken;
//
//	public Collection<MedicationTaken> getMedicationTaken() {
//		return medicationTaken;
//	}
//
//	public void setMedicationTaken(Collection<MedicationTaken> medicationTaken) {
//		this.medicationTaken = medicationTaken;
//	}
	
}
