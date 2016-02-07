package com.androidcapstone.symptommanagement.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import javax.persistence.*;
import android.os.Bundle;



public class Checkin {
	
	public static Checkin factory(Bundle chkinBundle){
		Checkin chkin = new Checkin();
		chkin.canEatDrink = chkinBundle.getBoolean("canEatDrink");
		chkin.painLevel = chkinBundle.getString("painLevel");
		chkin.checkinTime = chkinBundle.getLong("checkinTime");
		chkin.patientID = chkinBundle.getLong("patientID");
		int numberMedsTaken = chkinBundle.getInt("numberMedsTaken");
		MedicationTaken med;
		MedicationPrescribed medP;
		ArrayList<MedicationTaken> meds = new ArrayList<MedicationTaken>();
		for(int i=0; i < numberMedsTaken; i++){
			medP = new MedicationPrescribed();
			medP.setDoctorsName(chkinBundle.getString("doctorsName"+String.valueOf(i)));
			medP.setMedicationName(chkinBundle.getString("medName"+String.valueOf(i)));
			medP.setDosage(chkinBundle.getString("dosage"+String.valueOf(i)));
			medP.setRxnumber(chkinBundle.getString("rxnumber"+String.valueOf(i)));
			medP.setId(chkinBundle.getLong("mID"+String.valueOf(i)));
			boolean taken = chkinBundle.getBoolean("medTaken"+String.valueOf(i));
			long time =  chkinBundle.getLong("timeTaken"+String.valueOf(i));
			med = new MedicationTaken(taken, medP, time);
			meds.add(med);
		}
		chkin.setMedTaken(meds);
		return chkin;
	}
	
	public static Bundle createBundle(long patientID, String painLevel, boolean canEatDrink, ArrayList<MedicationTaken> meds){
		Bundle chkin = new Bundle();
		
		chkin.putLong("patientID", patientID);
		chkin.putString("painLevel", painLevel);
		chkin.putBoolean("canEatDrink", canEatDrink);
		chkin.putLong("checkinTime",Calendar.getInstance().getTimeInMillis());
		
		int numberMedsTaken = meds.size();
		chkin.putInt("numberMedsTaken", numberMedsTaken);
		//int m = 1;
		MedicationTaken med;
		MedicationPrescribed medP;
		for(int i=0; i < numberMedsTaken; i++){
			med = meds.get(i);
			medP = med.getMedicationPrescribed();
			chkin.putBoolean("medTaken"+String.valueOf(i), med.isTaken());
			chkin.putString("medName"+String.valueOf(i),medP.getMedicationName());
			chkin.putString("dosage"+String.valueOf(i), medP.getDosage());
			chkin.putString("doctorsName"+String.valueOf(i), medP.getDoctorsName());
			chkin.putString("rxnumber"+String.valueOf(i), medP.getRxnumber());
			chkin.putLong("timeTaken"+String.valueOf(i), med.getTimeTaken());
			chkin.putLong("mID" + String.valueOf(i), medP.getId());
			//m++;
		}
		return chkin;
	}


	public long id;
	
	private long patientID; // foreign key
	private long checkinTime;
	private String painLevel; // Needs to be chosen from enum
	private boolean canEatDrink;
	

	private List<MedicationTaken> medTaken;
	
	public List<MedicationTaken> getMedTaken() {
		return medTaken;
	}
	public void setMedTaken(List<MedicationTaken> medTaken) {
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
