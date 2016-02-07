package com.androidcapstone.symptommanagement.server.repository;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class MedicationPrescribed {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	
	private boolean current = true;
	
	
	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	
	//@ManyToOne
	//private Medication medication;
	
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

//	public Medication getMedication() {
//		return medication;
//	}
//
//	public void setMedication(Medication medication) {
//		this.medication = medication;
//	}

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
	@OneToMany(mappedBy="medicationPrescribed")
	//@JoinColumn(name="medicationPrescribedID")
	@JsonIgnore
	private Collection<MedicationTaken> medicationTaken;

	public Collection<MedicationTaken> getMedicationTaken() {
		return medicationTaken;
	}

	public void setMedicationTaken(Collection<MedicationTaken> medicationTaken) {
		this.medicationTaken = medicationTaken;
	}

	
}
