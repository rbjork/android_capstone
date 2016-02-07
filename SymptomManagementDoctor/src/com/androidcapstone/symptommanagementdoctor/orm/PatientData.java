package com.androidcapstone.symptommanagementdoctor.orm;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidcapstone.symptommanagement.repository.Patient;

public class PatientData extends Patient implements Parcelable {

	public PatientData(long id, String firstName,String lastName,long birthDate,String mrn){
		this.setBirthDate(birthDate);
		this.setMrn(mrn);
		this.setFirstName(firstName);
		this.setLastName(lastName);
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

	public ContentValues getCV(){
		return PatientCreator.getCVFromPatient();
	}
}
