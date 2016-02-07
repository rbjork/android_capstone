package com.androidcapstone.symptommanagement.server;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.Path;

import com.androidcapstone.symptommanagement.server.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagement.server.repository.Checkin;
import com.androidcapstone.symptommanagement.server.repository.Doctor;
import com.androidcapstone.symptommanagement.server.repository.Medication;
import com.androidcapstone.symptommanagement.server.repository.MedicationPrescribed;
import com.androidcapstone.symptommanagement.server.repository.Patient;
import com.androidcapstone.symptommanagement.server.repository.CheckinRepository;
import com.androidcapstone.symptommanagement.server.repository.DoctorsRepository;
import com.androidcapstone.symptommanagement.server.repository.MedicationRepository;
import com.androidcapstone.symptommanagement.server.repository.MedicationsPrescribedRepository;
import com.androidcapstone.symptommanagement.server.repository.PatientRepository;
import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


@Controller
public class PatientManagementServiceController {
	
	@Autowired
	private PatientRepository patients;
	
	@Autowired
	private CheckinRepository checkins;
	
	@Autowired
	private DoctorsRepository doctors;
	
	@Autowired
	private MedicationsPrescribedRepository medsPrescribed;
	
	@Autowired
	private MedicationRepository meds;
	
	
	private void createMedicationList(){
		Medication med = new Medication("Codeine","10mg");
		meds.save(med);
		med = new Medication("Morphine","2mg");
		meds.save(med);
		med = new Medication("Percodane","5mg");
		meds.save(med);
		med = new Medication("Zinex","100ug");
		meds.save(med);
	}
	
	
	
	private void createDoctorList(){
		doctors.deleteAll();
		Doctor doc = new Doctor("Phil", "Smith", "Chemo", "doctor1");
		doc = doctors.save(doc);
		doc = new Doctor("Carol", "Cook", "Chemo", "doctor2");
		doc = doctors.save(doc);
	}
	
	@SuppressWarnings("deprecation")
	private void createPatientList(ArrayList<Doctor> doctorList){
		patients.deleteAll();
		int patientNum = 1;
		for(Doctor doc : doctorList){
			Patient patient = new Patient("Sam", "Carson", "male", "9908989");
			patient.setUsername("patient" + String.valueOf(patientNum));
			Calendar cal = Calendar.getInstance();
			cal.set(2014-25,Calendar.FEBRUARY, 12);
			patient.setBirthDate(cal.getTimeInMillis());
			patient.setDoctor(doc);
			patients.save(patient);
			patientNum++;
			patient = new Patient("Carol", "Johnson", "female", "9908990");
			patient.setUsername("patient" + String.valueOf(patientNum));
			cal.set(2014-60,Calendar.APRIL, 28);
			patient.setBirthDate(cal.getTimeInMillis());
			patient.setDoctor(doc);
			patients.save(patient);
			patientNum++;
		}
//		Patient patient = new Patient("Sam", "Shane", "male", "9908989");
//		patient.setUsername("sshane");
//		patients.save(patient);
//		patient = new Patient("Tonia", "Gallenger", "male", "9908989");
//		patient.setUsername("tgallenger");
//		patients.save(patient);
//		patient = new Patient("Mary", "Ponish", "male", "9908989");
//		patient.setUsername("mponish");
//		patients.save(patient);
//		patient = new Patient("Cherl", "Bonnet", "male", "9908989");
//		patient.setUsername("cbonnet");
//		patients.save(patient);
		
	}
	
	private void createDoctorPatientList(){
		
		
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENTS_DISTRESSED + "/{doctorID}" ,method=RequestMethod.GET)
	public @ResponseBody Collection<Checkin> getRecentSeriousCheckins(@PathVariable("doctorID") long id){
		ArrayList<Checkin> chks = Lists.newArrayList(checkins.findByPainLevelOrderByCheckinTimeDesc("severe"));
		ArrayList<Checkin> chks2 = Lists.newArrayList(checkins.findByCanEatDrinkOrderByCheckinTimeDesc(false));
		Calendar cal = Calendar.getInstance();
		ArrayList<Checkin> last24HoursCheckins = new ArrayList<Checkin>();
		long twentyFourHoursAgo = cal.getTimeInMillis() - 24*60*60*1000;
		
		for(Checkin c : chks){
			if(c.getCheckinTime()>twentyFourHoursAgo){
				last24HoursCheckins.add(c);
			}
		}
		for(Checkin c : chks2){
			if(c.getCheckinTime() > twentyFourHoursAgo){
				last24HoursCheckins.add(c);
			}
		}
		
		return last24HoursCheckins;
	}
	
	@RequestMapping(value = SymptomManagementSvcApi.LOGIN_PATIENT + "/{username}")
	public @ResponseBody long checkPatientLogin(@PathVariable("username") String username, 
			HttpServletResponse response,
			Principal p){
		String name = p.getName();
		ArrayList<Patient> ps = Lists.newArrayList(patients.findByUsername(username));
		Patient patient = ps.get(0); // Should only be one.
		if(name.equals(username)){
			response.setStatus(HttpStatus.OK.value());
			return patient.getId();
		}else{
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return -1L;
		}
		
	}
	
	@RequestMapping(value = SymptomManagementSvcApi.LOGIN_DOCTOR + "/{username}")
	public @ResponseBody long checkDoctorLogin(@PathVariable("username") String username, 
			HttpServletResponse response,
			Principal p){
		String name = p.getName();
		ArrayList<Doctor> docs = Lists.newArrayList(doctors.findByUsername(username));
		Doctor doctor = docs.get(0); // Should only be one.
		if(name.equals(username)){
			response.setStatus(HttpStatus.OK.value());
			return doctor.getId();
		}else{
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return -1L;
		}
		
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.GENERATE_MED_LIST,method=RequestMethod.GET)
	public @ResponseBody boolean generateMedicationList(HttpServletResponse response){
		createMedicationList();
		response.setStatus(HttpStatus.OK.value());
		return true;
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.GENERATE_DOCTOR_LIST,method=RequestMethod.GET)
	public @ResponseBody Collection<Doctor> generateDoctorList(HttpServletResponse response){
		createDoctorList();
		return Lists.newArrayList(doctors.findAll());
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.GENERATE_PATIENT_LIST,method=RequestMethod.GET)
	public @ResponseBody boolean generatePatientList(HttpServletResponse response){
		ArrayList<Doctor> doctorList = Lists.newArrayList(doctors.findAll());
		createPatientList(doctorList);
		response.setStatus(HttpStatus.OK.value());
		return true;
	}
	//Principal p,  HttpServletResponse response
	
	@RequestMapping(value=SymptomManagementSvcApi.GENERATE_DOCTOR_PATIENT_LIST,method=RequestMethod.GET)
	public @ResponseBody Collection<Doctor> generateDoctorAndPatientList(HttpServletResponse response){
		createDoctorList();
		ArrayList<Doctor> doctorList = Lists.newArrayList(doctors.findAll());
		createPatientList(doctorList);
		response.setStatus(HttpStatus.OK.value());
		return doctorList;
	}
	
	
	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATION_REMOVE + "/{patientID}" ,method=RequestMethod.GET)
	public @ResponseBody boolean removePatientMedicationPrescriptions(@PathVariable("patientID") long patientID, HttpServletResponse response){
		boolean success = false;
		Collection<MedicationPrescribed> meds = medsPrescribed.findByPatientIDAndCurrent(patientID,true);
		for(MedicationPrescribed mp : meds){
			mp.setCurrent(false);
		}
		medsPrescribed.save(meds);
		//meds = medsPrescribed.findByPatientID(patientID);
		//response.setStatus(HttpStatus.OK.value());
		return true;
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS + "/{patientID}" ,method=RequestMethod.POST)
	public @ResponseBody boolean submitMedication(
				@RequestBody MedicationPrescribed med, 
				@PathVariable("patientID") long patientID,
				Principal p,
				HttpServletResponse response) {
		// TODO Auto-generated method stub
		String username = p.getName();
		Doctor doctor = Lists.newArrayList(doctors.findByUsername(username)).get(0);
		if(doctor == null && username.equals("admin") == false){
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return false;
		}else{
			med.setPatientID(patientID);
			medsPrescribed.save(med);
			response.setStatus(HttpStatus.OK.value());
			return true;
		}
	}
	
	
	//Principal p,
	//HttpServletResponse response
	//@PreAuthorize
	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENTS + "/{doctorID}" ,method=RequestMethod.GET)
	public @ResponseBody Collection<Patient> getPatients(@PathVariable("doctorID") long doctorID, Principal p, HttpServletResponse response){
		String username = p.getName();
		Doctor doctor = doctors.findOne(doctorID);
		if(doctor.getUsername().equals(username) || username.equals("admin")){ // Thus we know the user is a doctor - not a patient - usernames are assumed unique
			response.setStatus(HttpStatus.OK.value());
			return Lists.newArrayList(patients.findByDoctor(doctor));
		}else{
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENT_CHECKINS + "/{patientID}",method=RequestMethod.GET)
	public @ResponseBody Collection<Checkin> getPatientCheckins(@PathVariable("patientID") long patientID, Principal p,HttpServletResponse response){
		
		String username = p.getName();
		Doctor doctor = Lists.newArrayList(doctors.findByUsername(username)).get(0);
		if(doctor == null && username.equals("admin") == false){
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}else{
			return Lists.newArrayList(checkins.findByPatientID(patientID));
		}
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS + "/{patientID}", method=RequestMethod.GET)
	public @ResponseBody Collection<MedicationPrescribed> getPrescribedMedications(
				@PathVariable("patientID") long patientID,
				Principal p,
				HttpServletResponse response){
		Collection<MedicationPrescribed> mp = medsPrescribed.findByPatientIDAndCurrent(patientID,true);
		String username = p.getName();
		Doctor doctor = Lists.newArrayList(doctors.findByUsername(username)).get(0);
		if(doctor == null && username.equals("admin") == false){
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}else{
			return Lists.newArrayList(mp);
		}
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENT_MEDICATIONS, method=RequestMethod.GET)
	public @ResponseBody Collection<MedicationPrescribed> getAllPrescribedMedications(
				Principal p,
				HttpServletResponse response){
		String username = p.getName();
		Doctor doctor =Lists.newArrayList(doctors.findByUsername(username)).get(0);
		if(doctor == null && username.equals("admin") == false){
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}else{
			Collection<MedicationPrescribed> mp = Lists.newArrayList(medsPrescribed.findAll());
			return mp;
		}
	}
	
	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_MEDICATIONS, method=RequestMethod.GET)
	public @ResponseBody Collection<Medication> getMedicationsList(HttpServletResponse response){
		//String username = p.getName();
		//Doctor doctor = doctors.findByUsername(username);
		//if(doctor == null){
		//	response.setStatus(HttpStatus.BAD_REQUEST.value());
		//	return null;
		//}else{
			return Lists.newArrayList(meds.findAll());
		//}
	}
	
	
	
}
