package com.androidcapstone.symptommanagement.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;
import java.util.Date;



import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Before;
import org.junit.runners.MethodSorters;

import antlr.collections.List;

import com.androidcapstone.symptommanagement.server.client.SecuredRestBuilder;
import com.androidcapstone.symptommanagement.server.repository.Checkin;
import com.androidcapstone.symptommanagement.server.repository.Doctor;
import com.androidcapstone.symptommanagement.server.repository.Medication;
import com.androidcapstone.symptommanagement.server.repository.MedicationPrescribed;
import com.androidcapstone.symptommanagement.server.repository.MedicationTaken;
import com.androidcapstone.symptommanagement.server.repository.Patient;
import com.google.common.collect.Lists;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import static org.junit.Assert.*;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SymptomManagementSvcTest {
	
	private final String USERNAME = "admin";
	private final String PASSWORD = "pass";
	private final String CLIENT_ID = "mobile";
	private final String READ_ONLY_CLIENT_ID = "mobileReader";
	
 //Non Secure version	
//  private final String TEST_URL = "http://localhost:8080";
//	private SymptomManagementSvcApi symptomManagementService = new RestAdapter.Builder()
//		.setEndpoint(TEST_URL)
//		.setLogLevel(LogLevel.FULL).build()
//		.create(SymptomManagementSvcApi.class);

// Secure version
	private final String TEST_URL = "https://localhost:8443";
	private SymptomManagementSvcApi symptomManagementService = new SecuredRestBuilder()
		.setLoginEndpoint(TEST_URL + SymptomManagementSvcApi.TOKEN_PATH)
		.setUsername(USERNAME)
		.setPassword(PASSWORD)
		.setClientId(CLIENT_ID)
		.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
		.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
		.create(SymptomManagementSvcApi.class);
	
	private SymptomManagementSvcApi invalidSymptomManagementService = new SecuredRestBuilder()
		.setLoginEndpoint(TEST_URL + SymptomManagementSvcApi.TOKEN_PATH)
		.setUsername(UUID.randomUUID().toString())
		.setPassword(UUID.randomUUID().toString())
		.setClientId(UUID.randomUUID().toString())
		.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
		.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
		.create(SymptomManagementSvcApi.class);
	
//	private long medId = 1L;
//	private long doctorId = 1L;
//	private long patientId = 1L;
//	private long chkinId = 1L;
	
//	private Patient createPatient(){
//		Patient p = new Patient();
//		//p.setId(patientId++);;
//		p.setBirthDate(new Date());
//		p.setFirstName("Bill");
//		p.setLastName("Johnson");
//		p.setGender("male");
//		Doctor doctor = new Doctor();
//		p.setDoctor(doctor);
//		p.setMrn("99999999");
//		p.setUsername("bill");
//		return p;
//	}
//	
//	private Checkin createCheckin(long patientId){
//		Checkin chkin = new Checkin();
//		//chkin.id = chkinId++;
//		chkin.setPainLevel("mild");
//		chkin.setPatientID(patientId);
//		return chkin;
//	}
//	
//	private Doctor createDoctor(){
//		Doctor doctor = new Doctor();
//		doctor.setDepartment("Internal Medicine");
//		doctor.setFirstName("Phil");
//		doctor.setLastName("Smith");
//		//doctor.setId(doctorId++);
//		return doctor;
//	}
	
	private MedicationPrescribed createMedicationPrescribed(String name,String dose,String instructions){
		//Medication med = new Medication(name,dose);
		//med.id = medId++;
		
		MedicationPrescribed medPrescribed = new MedicationPrescribed(); 
		medPrescribed.setDoctorsName("Dr. Google");
		medPrescribed.setMedicationName(name);
		medPrescribed.setInstructions(instructions);
		
	//	medPrescribed.setMedication(med);
		medPrescribed.setRxnumber("RX39342142");
		medPrescribed.setPatientID(1);
		return medPrescribed;
	}
	
	private static Checkin chkin;
	private static Patient patient;
	private static Doctor doctor;
	private static MedicationPrescribed medPrescribed;
	private static ArrayList<Medication> medList;
	private static ArrayList<Doctor> doctors;
	private static ArrayList<Patient> patients;
	
	private static boolean ran = false;
	
	@Before
	public void setup(){
		if(SymptomManagementSvcTest.ran == false){
			symptomManagementService.generateMedicationList();
			//symptomManagementService.generateDoctorList();
			//symptomManagementService.generatePatientList();
			doctors = Lists.newArrayList(symptomManagementService.generateDoctorAndPatientList());
			SymptomManagementSvcTest.ran = true;
		}
	}
	
	@Test
	public void testAGetPatientList() throws Exception{
		doctor = doctors.get(0);
		patients = (ArrayList<Patient>)symptomManagementService.getPatients(doctor.getId());
		assertTrue("Number of patients is larger than zero",patients.size() > 0);
	}
	
	@Test
	public void testBGetMedicationList() throws Exception{
		medList = (ArrayList<Medication>)symptomManagementService.getMedicationsList();
		assertTrue("Number of medList is larger than zero",medList.size() > 0);
	}
	
//	@Test
//	public void testCSubmitMedicationToPatient() throws Exception{
//		Medication med = medList.get(0);
//		patient = patients.get(0);
//		medPrescribed = createMedicationPrescribed(med.getMedicationName(), med.getDosage(), "Take twice daily");
//		boolean success = symptomManagementService.submitMedication(medPrescribed, patient.getId());
//		assertTrue("submit success",success);
//		
//		med = medList.get(1);
//		medPrescribed = createMedicationPrescribed(med.getMedicationName(), med.getDosage(), "Take once daily");
//		success = symptomManagementService.submitMedication(medPrescribed, patient.getId());
//		assertTrue("submit success",success);
//		
//		Collection<MedicationPrescribed> meds = symptomManagementService.getAllPrescribedMedications();
//		MedicationPrescribed medp = Lists.newArrayList(meds).get(1);
//		
//		assertTrue("Number of meds prescribed is larger than zero",meds.size() > 0);
//		assertTrue("Med Rx is same",medp.getRxnumber().equals(medPrescribed.getRxnumber()));
//		
//		Patient p2 = patients.get(1);
//		med = medList.get(2);
//		medPrescribed = createMedicationPrescribed(med.getMedicationName(), med.getDosage(), "Take twice daily");
//		success = symptomManagementService.submitMedication(medPrescribed, p2.getId());
//		assertTrue("submit success",success);
//	}
//	
//	@Test
//	public void testDPrescribedMed(){
//		Collection<MedicationPrescribed> meds = symptomManagementService.getMedications(patient.getId());
//		assertTrue("Number of meds prescribed is larger than zero",meds.size() > 0);
//		MedicationPrescribed medp = Lists.newArrayList(meds).get(0);
//		assertTrue("Med Rx is same",medp.getRxnumber().equals(medPrescribed.getRxnumber()));
//		medPrescribed = Lists.newArrayList(meds).get(0);
//	}
//	
//	
//	@Test
//	public void testESubmitCheckin() throws Exception{
//		chkin = Checkin.factory(patient.getId(),"mild", true);
//		MedicationTaken medTaken = new MedicationTaken(true, medPrescribed,Calendar.getInstance().getTimeInMillis());
//		medTaken.setMedicationName(medPrescribed.getMedicationName());
//		chkin.setMedTaken(Lists.newArrayList(medTaken));
//		Checkin ck = symptomManagementService.submitCheckin(chkin, patient.getId());
//		
//		chkin = Checkin.factory(patient.getId(),"moderate", false);
//		chkin.setCheckinTime(Calendar.getInstance().getTimeInMillis() - 17*60*60*1000);
//		medTaken = new MedicationTaken(true, medPrescribed,Calendar.getInstance().getTimeInMillis());
//		medTaken.setMedicationName(medPrescribed.getMedicationName());
//		chkin.setMedTaken(Lists.newArrayList(medTaken));
//		ck = symptomManagementService.submitCheckin(chkin, patient.getId());
//		
//		chkin = Checkin.factory(patient.getId(),"severe", true);
//		chkin.setCheckinTime(Calendar.getInstance().getTimeInMillis() - 14*60*60*1000);
//		medTaken = new MedicationTaken(true, medPrescribed,Calendar.getInstance().getTimeInMillis());
//		medTaken.setMedicationName(medPrescribed.getMedicationName());
//		chkin.setMedTaken(Lists.newArrayList(medTaken));
//		ck = symptomManagementService.submitCheckin(chkin, patient.getId());
//		
//		chkin = Checkin.factory(patient.getId(),"severe", false);
//		chkin.setCheckinTime(Calendar.getInstance().getTimeInMillis() - 1*60*60*1000);
//		medTaken = new MedicationTaken(true, medPrescribed,Calendar.getInstance().getTimeInMillis());
//		medTaken.setMedicationName(medPrescribed.getMedicationName());
//		chkin.setMedTaken(Lists.newArrayList(medTaken));
//		ck = symptomManagementService.submitCheckin(chkin, patient.getId());
//		
//		assertTrue("Successfull submit of checkin",ck != null);
//
//
//	}
//	
//	@Test
//	public void testFGetRecentCheckins() throws Exception{
//		Collection<Checkin> chkins = symptomManagementService.getRecentSeriousCheckins(1L);
//		assertTrue("Successfull recent serious of checkin",chkins != null);
//		Checkin ck = Lists.newArrayList(chkins).get(0);
//		assertTrue("Number Serious is one",chkins.size() > 1);
//		//assertTrue("Occurred 12 hours ago", ck.getCheckinTime() <= (Calendar.getInstance().getTimeInMillis() - 12*60*60*1000));
//	}
//	
//	@Test
//	public void testGGetCheckins() throws Exception{
//		ArrayList<Checkin> checkins = Lists.newArrayList(symptomManagementService.getCheckins(patient.getId()));
//		Checkin chk = checkins.get(0);
//		assertTrue("Number of checkins is larger than zero",checkins.size() > 0);
//		ArrayList<MedicationTaken> mds = Lists.newArrayList(chk.getMedTaken());
//		assertTrue("Number of Meds is larger than zero",mds.size() > 0);
//		MedicationTaken md = mds.get(0);
//		assertTrue("Does it have the same name as prescribed", md.getMedicationName().equals(medPrescribed.getMedicationName()));
//	}
//	
//	@Test
//	public void testHRemoveSubscription() throws Exception{
//		
//		Medication med = medList.get(0);
//		patient = patients.get(1);
//		long patientID = patient.getId();
//		medPrescribed = createMedicationPrescribed(med.getMedicationName(), med.getDosage(), "Take twice daily");
//		boolean success = symptomManagementService.submitMedication(medPrescribed,patientID);
//		assertTrue("submit success",success);
//		success = symptomManagementService.removePatientMedicationPrescriptions(patientID);
//		assertTrue("submit success",success);
//		Collection<MedicationPrescribed> meds = symptomManagementService.getPrescribedMedications(patientID);
//		assertTrue("Number of meds prescribed is zero",meds == null || meds.size() == 0);
//	}
//	
//	@Test
//	public void testGGetCheckinsContent() throws Exception{
//		ArrayList<Checkin> checkins = (ArrayList<Checkin>)symptomManagementService.getCheckins(patient.getId());
//		Checkin c = checkins.get(0);
//		//assertTrue("Checkins contains chkin",c.id == chkin.);
//		assertTrue("Checkins contains chkin",c.getPainLevel().equals(chkin.getPainLevel()));
//	}
	
	// Doctor
	
	
	
	
	
}
