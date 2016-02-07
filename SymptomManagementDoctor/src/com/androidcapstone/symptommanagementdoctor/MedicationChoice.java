package com.androidcapstone.symptommanagementdoctor;

import com.androidcapstone.symptommanagement.repository.Medication;

public class MedicationChoice {
	private Medication medication;
	public MedicationChoice(Medication med){
		this.medication = med;
	}
	public Medication getMedication() {
		return medication;
	}
	public void setMedication(Medication medication) {
		this.medication = medication;
	}
	public boolean isPrescribed() {
		return isPrescribed;
	}
	public void setPrescribed(boolean isPrescribed) {
		this.isPrescribed = isPrescribed;
	}
	private boolean isPrescribed;
}
