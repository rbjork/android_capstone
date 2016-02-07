package com.androidcapstone.symptommanagementdoctor.data;

import java.util.ArrayList;

import com.androidcapstone.symptommanagement.repository.Checkin;

import android.os.Bundle;

public class PatientAlert {
	public static final String NUMBER_OF_ALERTS = "numberofalerts";
	public static final String IS_ALERT = "isalert";
	private long patientID;
	public long getPatientID() {
		return patientID;
	}
	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}
	public String getPainlevel() {
		return painlevel;
	}
	public void setPainlevel(String painlevel) {
		this.painlevel = painlevel;
	}
	public boolean isCanEatDrink() {
		return canEatDrink;
	}
	public void setCanEatDrink(boolean canEatDrink) {
		this.canEatDrink = canEatDrink;
	}
	private String painlevel;
	private boolean canEatDrink;
	
	public static void addToBundle(Bundle b, Checkin c, int index){
		long p = c.getPatientID();
		b.putLong("id" + String.valueOf(index), p);
		b.putString("painlevel" + String.valueOf(index), c.getPainLevel());
		b.putBoolean("canEatDrink"+ String.valueOf(index), c.isCanEatDrink());
	}
	
	public static ArrayList<PatientAlert> getAlertsFromBundle(Bundle b){
		int num = b.getInt(PatientAlert.NUMBER_OF_ALERTS);
		ArrayList<PatientAlert> alerts = new ArrayList<PatientAlert>();
		PatientAlert alert;
		for(int index = 1; index<= num; index++){
			alert = new PatientAlert();
			alert.patientID = b.getLong("id" + String.valueOf(index));
			alert.painlevel = b.getString("painlevel" + String.valueOf(index));
			alert.canEatDrink = b.getBoolean("canEatDrink"+ String.valueOf(index));
			alerts.add(alert);
		}
		return alerts;
	}
}
