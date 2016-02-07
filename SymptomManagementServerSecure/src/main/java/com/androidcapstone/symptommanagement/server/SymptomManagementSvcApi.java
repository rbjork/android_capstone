package com.androidcapstone.symptommanagement.server;

import java.util.ArrayList;
import java.util.Collection;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Multipart;
//import retrofit.http.Query;

//import javax.servlet.http.HttpServletResponse;



//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;



import antlr.collections.List;

import com.androidcapstone.symptommanagement.server.repository.*;
//import com.fitbit.api.common.model.activities.Activities;
//import com.fitbit.api.common.model.bp.Bp;
//import com.fitbit.api.common.model.heart.Heart;

import retrofit.http.*;

public interface SymptomManagementSvcApi {
	
	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String PATIENT_SVC_PATH = "/patient";
	public static final String PATIENT_GET_MEDICATIONS = PATIENT_SVC_PATH + "/medications"; // Get Medications list
	public static final String PATIENT_MEDSTAKEN = PATIENT_SVC_PATH + "/medicationstaken";
	public static final String PATIENT_CHECKIN = PATIENT_SVC_PATH + "/checkin"; // Get Checkin list or Post a checkin
	public static final String PATIENT_CHECKIN_MULTIPART = PATIENT_SVC_PATH + "/multipart/checkin"; // Get Checkin list or Post a checkin
	public static final String PATIENT_PHYSIO_RECORDING = PATIENT_SVC_PATH + "/physio"; 
	
	public static final String DOCTOR_SVC_PATH = "/doctor";
	public static final String DOCTORS_PATIENTS = DOCTOR_SVC_PATH + "/patients"; // Get Patient list
	public static final String DOCTORS_PATIENT_MEDICATIONS = DOCTORS_PATIENTS + "/medications"; // Get Medications list
	public static final String DOCTORS_PATIENT_MEDICATION_REMOVE = DOCTORS_PATIENTS + "/removemedication"; // Get Medications list
	
	public static final String DOCTORS_PATIENT_CHECKINS = DOCTOR_SVC_PATH + "/patient/checkins"; // Get a Patient checkins
	public static final String DOCTORS_PATIENT_PHYSIO =  DOCTOR_SVC_PATH + "/patient/physio";
	public static final String DOCTORS_MEDICATIONS = DOCTOR_SVC_PATH + "/medications";
	public static final String DOCTORS_PATIENTS_DISTRESSED = DOCTOR_SVC_PATH + "/distress";
	
	public static final String CARETAKER_SVC_PATH = "/caretaker";
	public static final String CARETAKER_MEDCHECK = CARETAKER_SVC_PATH + "/checkmedstaken";
	
	public static final String GENERATE_MED_LIST = "/generatemedlist";
	public static final String GENERATE_PATIENT_LIST = "/generatepatientlist";
	public static final String GENERATE_DOCTOR_LIST = "/generatedoctorlist";
	public static final String GENERATE_DOCTOR_PATIENT_LIST = "/generatedoctorpatientlist";
	
	public static final String LOGIN_DOCTOR ="/logindoctor";
	public static final String LOGIN_PATIENT = "/loginpatient";
	
	public static final String DOCTORS_PATIENT_HEARTRATE = DOCTORS_PATIENT_PHYSIO + "/heartrate";
	public static final String DOCTORS_PATIENT_BLOODPRESSURE = DOCTORS_PATIENT_PHYSIO + "/bloodpressure";
	public static final String DOCTORS_PATIENT_ACTIVITIES = DOCTORS_PATIENT_PHYSIO + "/activites";
	
	
	// PATIENT API
	@GET(PATIENT_GET_MEDICATIONS + "/{patientID}")
	public Collection<MedicationPrescribed> getMedications(@Path("patientID") long id);
	
	@GET(PATIENT_CHECKIN + "/{patientID}")
	public Collection<Checkin> getCheckins(@Path("patientID") long id);
	
	@POST(PATIENT_CHECKIN + "/{patientID}")
	public Checkin submitCheckin(@Body Checkin chkin, @Path("patientID") long id);
	
	//@Multipart
	//@PUT(PATIENT_CHECKIN_MULTIPART + "/{patientID}")
	//public boolean submitCheckinTwo(@Part("chkin") Checkin chkin, @Part("medsTaken") Collection<MedicationTaken> medsTaken, @Path("patientID") long id);
	
	@POST(PATIENT_MEDSTAKEN + "/{patientID}")
	public boolean submitMedsTaken(@Body ArrayList<MedicationTaken> medstaken, @Path("patientID") long id);
	
	//@POST(PATIENT_PHYSIO_RECORDING)
	//public Void submitPhysio(@Body Physio ps);

	@GET(LOGIN_PATIENT + "/{username}")
	public long checkPatientLogin(@Path("username") String username);
	
	@GET(LOGIN_DOCTOR + "/{username}")
	public long checkDoctorLogin(@Path("username") String username);
	
	
	
	// DOCTOR API
	
	// PHYSIO
//	@GET(DOCTORS_PATIENT_HEARTRATE + "/{patientID}")
//	public Collection<Heart> getPatientHeartRateTimeSeries(@Path("patientID")long id);
//	
//	@GET(DOCTORS_PATIENT_BLOODPRESSURE + "/{patientID}")
//	public Collection<Bp> getPatientBloodPressureTimeSeries(@Path("patientID")long id);
//	
//	@GET(DOCTORS_PATIENT_ACTIVITIES + "/{patientID}")
//	public Activities getPatientActivities(@Path("patientID")long id);
	// ***
	
	
	@GET(DOCTORS_PATIENTS_DISTRESSED + "/{doctorID}")
	public Collection<Checkin> getRecentSeriousCheckins(@Path("doctorID") long id);
	
	@GET(DOCTORS_PATIENTS + "/{doctorID}")
	public Collection<Patient> getPatients(@Path("doctorID") long id);
	
	@GET(DOCTORS_PATIENT_CHECKINS + "/{patientID}")
	public Collection<Checkin> getPatientCheckins(@Path("patientID") long id);
	
	@GET(SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS + "/{patientID}")
	public Collection<MedicationPrescribed> getPrescribedMedications(@Path("patientID") long patientID);
	
	@GET(SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS)
	public Collection<MedicationPrescribed> getAllPrescribedMedications();
	
	@GET(SymptomManagementSvcApi.DOCTORS_MEDICATIONS)
	public Collection<Medication> getMedicationsList();
	
	@POST(SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS + "/{patientID}")
	public boolean submitMedication(@Body MedicationPrescribed med, @Path("patientID") long patientID);
	
	
	@GET(value=SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATION_REMOVE + "/{patientID}")
	public @ResponseBody boolean removePatientMedicationPrescriptions(@Path("patientID") long patientID);
	
	
	// SETUP DATA Normally down outside mobile application
	@GET(SymptomManagementSvcApi.GENERATE_MED_LIST)
	public boolean generateMedicationList();
	
	@GET(SymptomManagementSvcApi.GENERATE_PATIENT_LIST)
	public boolean generatePatientList();
	
	@GET(SymptomManagementSvcApi.GENERATE_DOCTOR_LIST)
	public boolean generateDoctorList();
	
	@GET(SymptomManagementSvcApi.GENERATE_DOCTOR_PATIENT_LIST)
	public Collection<Doctor> generateDoctorAndPatientList();
	
	
	//@GET(DOCTORS_PATIENT_PHYSIO + "/{patientID}")
	//public Physio getPatientPhysio((@Path("patientID") long id);
	
	// CARETAKER API
	//@GET(CARETAKER_MEDCHECK)
	//public Collection<Medication>getMedicationsTaken();
	
}
