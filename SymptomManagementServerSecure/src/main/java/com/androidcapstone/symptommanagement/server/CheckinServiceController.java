package com.androidcapstone.symptommanagement.server;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;



import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RequestPart;


//import antlr.collections.List;

import com.androidcapstone.symptommanagement.server.repository.Checkin;
//import com.androidcapstone.symptommanagement.server.repository.Medication;
import com.androidcapstone.symptommanagement.server.repository.MedicationPrescribed;
import com.androidcapstone.symptommanagement.server.repository.CheckinRepository;
import com.androidcapstone.symptommanagement.server.repository.MedicationTaken;
import com.androidcapstone.symptommanagement.server.repository.MedicationTakenRepository;
import com.androidcapstone.symptommanagement.server.repository.MedicationsPrescribedRepository;
import com.google.common.collect.Lists;

@Controller
public class CheckinServiceController{// implements SymptomManagementPatientSvcApi {
	
	@Autowired
	private CheckinRepository checkins;
	
	@Autowired
	private MedicationTakenRepository medsTaken; // perhaps not needed
	
	@Autowired
	private MedicationsPrescribedRepository medsPrescribed;
	

	@RequestMapping(value=SymptomManagementSvcApi.PATIENT_GET_MEDICATIONS + "/{patientID}" ,method=RequestMethod.GET)
	public  @ResponseBody Collection<MedicationPrescribed> getMedications(@PathVariable("patientID") long patientID) {
		// TODO Auto-generated method stub
		
		return  Lists.newArrayList(medsPrescribed.findByPatientIDAndCurrent(patientID,true));
	}

	@RequestMapping(value=SymptomManagementSvcApi.PATIENT_CHECKIN + "/{patientID}" ,method=RequestMethod.GET)
	public  @ResponseBody Collection<Checkin> getCheckins(@PathVariable("patientID") long patientID) {
		// TODO Auto-generated method stub
		return Lists.newArrayList(checkins.findByPatientID(patientID));
	}

	@RequestMapping(value=SymptomManagementSvcApi.PATIENT_CHECKIN + "/{patientID}" ,method=RequestMethod.POST)
	public @ResponseBody Checkin submitCheckin(@RequestBody Checkin chkin, @PathVariable("patientID") long patientID) {
		// TODO Auto-generated method stub
		Collection<MedicationTaken> meds = chkin.getMedTaken();
		medsTaken.save(meds);
		chkin = checkins.save(chkin);
		return chkin;
	}
	
	@RequestMapping(value = SymptomManagementSvcApi.PATIENT_MEDSTAKEN + "/{patientID}")
	public boolean submitMedsTaken(@RequestBody ArrayList<MedicationTaken> medstaken, @PathVariable("patientID") long id, HttpServletResponse response){
		medsTaken.save(medstaken);
		
		response.setStatus(HttpStatus.OK.value());
		return true;
	}
	
	
	
//	@RequestMapping(value=SymptomManagementSvcApi.PATIENT_CHECKIN_MULTIPART + "/{patientID}" ,method=RequestMethod.PUT)
//	public @ResponseBody boolean submitCheckinTwo(@RequestPart("chkin") Checkin chkin, @RequestPart("medsTaken") Collection<MedicationTaken> medicationsTaken, @PathVariable("patientID") long patientID, HttpServletResponse response) {
//		// TODO Auto-generated method stub
//		checkins.save(chkin);
//		medsTaken.save(medicationsTaken);
//		response.setStatus(HttpStatus.OK.value());
//		return true;
//	}
}
