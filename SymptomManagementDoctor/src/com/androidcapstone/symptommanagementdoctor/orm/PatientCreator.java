package com.androidcapstone.symptommanagementdoctor.orm;

import java.util.ArrayList;

import com.androidcapstone.symptommanagementdoctor.provider.SMSchema;

import android.content.ContentValues;
import android.database.Cursor;

public class PatientCreator {
	/**
	 * Create a ContentValues from a provided PatientData.
	 * 
	 * @param data
	 *            PatientData to be converted.
	 * @return ContentValues that is created from the PatientData object
	 */
	public static ContentValues getCVFromPatient(){
		ContentValues cv = new ContentValues();
		return cv;
	}
	
	/**
	 * Get all of the StoryData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor to get StoryData(s) of.
	 * @return ArrayList<StoryData\> The set of StoryData
	 */
	public static ArrayList<PatientData> getPatientDataArrayListFromCursor(
			Cursor cursor) {
		ArrayList<PatientData> rValue = new ArrayList<PatientData>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					rValue.add(getPatientDataFromCursor(cursor));
				} while (cursor.moveToNext() == true);
			}
		}
		return rValue;
	}
	/**
	 * Get the first StoryData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return StoryData object
	 */
	public static PatientData getPatientDataFromCursor(Cursor cursor) {

		long rowID = cursor.getLong(cursor
				.getColumnIndex(SMSchema.Patient.Cols.ID));
		long loginId = cursor.getLong(cursor
				.getColumnIndex(SMSchema.Patient.Cols.LOGIN_ID));
		long patientId = cursor.getLong(cursor
				.getColumnIndex(SMSchema.Patient.Cols.PATIENT_ID));
		String firstname = cursor.getString(cursor
				.getColumnIndex(SMSchema.Patient.Cols.FIRSTNAME));
		String lastname = cursor.getString(cursor
				.getColumnIndex(SMSchema.Patient.Cols.LASTNAME));
		long dob = cursor.getLong(cursor
				.getColumnIndex(SMSchema.Patient.Cols.DOB));
		String mrn = cursor.getString(cursor
				.getColumnIndex(SMSchema.Patient.Cols.MRN));
		

		//String firstName,String lastName,int birthDate,String mrn
		// construct the returned object
		PatientData rValue = new PatientData(patientId,firstname,lastname,dob, mrn);

		return rValue;
	}

}
