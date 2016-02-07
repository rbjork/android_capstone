package com.androidcapstone.symptommanagementpatient.interfaces;

import java.util.ArrayList;
import java.util.Collection;





//import javax.servlet.http.HttpServletResponse;

//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;






import com.androidcapstone.symptommanagement.repository.*;
import com.androidcapstone.symptommanagementpatient.interfaces.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagement.repository.Checkin;
import com.androidcapstone.symptommanagement.repository.Doctor;
import com.androidcapstone.symptommanagement.repository.Medication;
import com.androidcapstone.symptommanagement.repository.MedicationPrescribed;
import com.androidcapstone.symptommanagement.repository.Patient;

import retrofit.http.*;

public interface SymptomManagementSvcApi {
public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String PATIENT_SVC_PATH = "/patient";
	public static final String PATIENT_GET_MEDICATIONS = PATIENT_SVC_PATH + "/medications"; // Get Medications list
	public static final String PATIENT_CHECKIN = PATIENT_SVC_PATH + "/checkin"; // Get Checkin list or Post a checkin
	public static final String PATIENT_PHYSIO_RECORDING = PATIENT_SVC_PATH + "/physio"; 
	
	public static final String DOCTOR_SVC_PATH = "/doctor";
	public static final String DOCTORS_PATIENTS = DOCTOR_SVC_PATH + "/patients"; // Get Patient list
	public static final String DOCTORS_PATIENT_MEDICATIONS = DOCTORS_PATIENTS + "/medications"; // Get Medications list
	
	public static final String DOCTORS_PATIENT_CHECKINS = DOCTOR_SVC_PATH + "/patient/checkins"; // Get a Patient checkins
	public static final String DOCTORS_PATIENT_PHYSIO =  DOCTOR_SVC_PATH + "/patient/physio";
	public static final String DOCTORS_MEDICATIONS = DOCTOR_SVC_PATH + "/medications";
	
	public static final String CARETAKER_SVC_PATH = "/caretaker";
	public static final String CARETAKER_MEDCHECK = CARETAKER_SVC_PATH + "/checkmedstaken";
	
	public static final String GENERATE_MED_LIST = "/generatemedlist";
	public static final String GENERATE_PATIENT_LIST = "/generatepatientlist";
	public static final String GENERATE_DOCTOR_LIST = "/generatedoctorlist";
	public static final String GENERATE_DOCTOR_PATIENT_LIST = "/generatedoctorpatientlist";
	
	public static final String LOGIN_PATIENT ="/loginpatient";
	
	// PATIENT API
	@GET(PATIENT_GET_MEDICATIONS + "/{patientID}")
	public Collection<MedicationPrescribed> getMedications(@Path("patientID") long id);
	
	@GET(PATIENT_CHECKIN + "/{patientID}")
	public Collection<Checkin> getCheckins(@Path("patientID") long id);
	
	//@POST(PATIENT_CHECKIN + "/{patientID}")
	//public boolean submitCheckin(@Body Checkin chkin, @Path("patientID") long id);
	
	@POST(PATIENT_CHECKIN + "/{patientID}")
	public Checkin submitCheckin(@Body Checkin chkin, @Path("patientID") long id);
	
	//@GET(SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS + "/{patientID}")
	//public Collection<MedicationPrescribed> getPrescribedMedications(@Path("patientID") long patientID);
	
	//@POST(PATIENT_PHYSIO_RECORDING)
	//public Void submitPhysio(@Body Physio ps);

	@GET(LOGIN_PATIENT + "/{username}")
	public long checkPatientLogin(@Path("username") String username);
	
	@GET(DOCTORS_PATIENT_CHECKINS + "/{patientID}")
	public Collection<Checkin> getPatientCheckins(@Path("patientID") long id);
	

	// DOCTOR API
//	@GET(DOCTORS_PATIENTS + "/{doctorID}")
//	public Collection<Patient> getPatients(@Path("doctorID") long id);
//	
//	@GET(DOCTORS_PATIENT_CHECKINS + "/{patientID}")
//	public Collection<Checkin> getPatientCheckins(@Path("patientID") long id);
//	
//	@GET(SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS + "/{patientID}")
//	public Collection<MedicationPrescribed> getPrescribedMedications(@Path("patientID") long patientID);
//	
//	@GET(SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS)
//	public Collection<MedicationPrescribed> getAllPrescribedMedications();
//	
//	@GET(SymptomManagementSvcApi.DOCTORS_MEDICATIONS)
//	public Collection<Medication> getMedicationsList();
//	
//	@POST(SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS + "/{patientID}")
//	public boolean submitMedication(@Body MedicationPrescribed med, @Path("patientID") long patientID);
//	
//	
//	// SETUP DATA Normally down outside mobile application
//	@GET(SymptomManagementSvcApi.GENERATE_MED_LIST)
//	public boolean generateMedicationList();
//	
//	@GET(SymptomManagementSvcApi.GENERATE_PATIENT_LIST)
//	public boolean generatePatientList();
//	
//	@GET(SymptomManagementSvcApi.GENERATE_DOCTOR_LIST)
//	public boolean generateDoctorList();
//	
//	@GET(SymptomManagementSvcApi.GENERATE_DOCTOR_PATIENT_LIST)
//	public Collection<Doctor> generateDoctorAndPatientList();
	
	
	//@GET(DOCTORS_PATIENT_PHYSIO + "/{patientID}")
	//public Physio getPatientPhysio((@Path("patientID") long id);
	
	// CARETAKER API
	//@GET(CARETAKER_MEDCHECK)
	//public Collection<Medication>getMedicationsTaken();
	
}
