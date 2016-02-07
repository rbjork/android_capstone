package com.androidcapstone.symptommanagementdoctor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

import com.androidcapstone.symptommanagement.repository.Checkin;
import com.androidcapstone.symptommanagement.repository.Medication;
import com.androidcapstone.symptommanagement.repository.MedicationPrescribed;
import com.androidcapstone.symptommanagement.repository.MedicationTaken;
import com.androidcapstone.symptommanagement.repository.Patient;
import com.androidcapstone.symptommanagementdoctor.data.PatientAlert;
//import com.androidcapstone.symptommanagementdoctor.MainActivity.CustomAdapter;
import com.androidcapstone.symptommanagementdoctor.interfaces.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagementdoctor.services.SymptomManagementSvc;
import com.google.common.collect.Lists;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PatientDetailsActivity extends Activity {
	
	private static final String PATIENT_LOGS = "Logs";
	private static final String PATIENT_PRESCRIPTIONS = "Meds";
	private static final String PATIENT_CHART = "Chart";
	
	private long patientID;
	private long doctorID;
	
	private SymptomManagementSvcApi Svc;
	
	private ListView patientLogList;
	private ListView medListView;
	
	protected CheckinAdapter patientLogsAdapter;
	protected MedListAdapter medlistAdapter;
	
	protected ArrayList<MedicationTaken> medsTaken;
	protected ArrayList<Medication> medications;
	protected ArrayList<MedicationPrescribed> medsPrescribed;
	protected ArrayList<Checkin> checkinlist;
	
	private PatientLogsFragment patientLogsView;
	private PatientPrescriptionsFragment patientPrescriptionsView;
	private PatientChartFragment patientChartView;
	
	private String username;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		Bundle datapassed = intent.getExtras();
		
		doctorID = intent.getLongExtra(LoginActivity.DOCTORID, 1L);
		username = intent.getStringExtra(LoginActivity.USERNAME);
		password = intent.getStringExtra(LoginActivity.PASSWORD);
		
		if(savedInstanceState != null){
			doctorID = savedInstanceState.getLong(LoginActivity.DOCTORID,1L);
			username = savedInstanceState.getString(LoginActivity.USERNAME);
			password = savedInstanceState.getString(LoginActivity.PASSWORD);
		}
		
		SymptomManagementSvc.username = username;
		SymptomManagementSvc.password = password;
		
		boolean isAlert = intent.getBooleanExtra(PatientAlert.IS_ALERT, false);
		if(isAlert){
			Bundle alertdata = intent.getExtras();
			ArrayList<PatientAlert> alerts = PatientAlert.getAlertsFromBundle(alertdata);
			PatientAlert alert = alerts.get(0);
			patientID = alert.getPatientID();
			Toast.makeText(this, "This patient is in need", Toast.LENGTH_SHORT);
		}else{
			patientID = datapassed.getLong(LoginActivity.PATIENTID,1);
		}
		
		doctorID = datapassed.getLong(LoginActivity.DOCTORID,1);
		String patientname = datapassed.getString(LoginActivity.PATIENTNAME);
		setContentView(R.layout.activity_patientdetails);
		
		this.setTitle("Patient Details: "+patientname);
		
		patientLogsAdapter = new CheckinAdapter(this, R.layout.checkin_log_item);
		medlistAdapter = new MedListAdapter(this, R.layout.medication_item);
		
		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		patientLogsView = new PatientLogsFragment();
		tabBar.addTab(tabBar.newTab().setText(PATIENT_LOGS).setTabListener(new TabListener(patientLogsView)));
		
		patientPrescriptionsView = new PatientPrescriptionsFragment();
		tabBar.addTab(tabBar.newTab().setText(PATIENT_PRESCRIPTIONS).setTabListener(new TabListener(patientPrescriptionsView)));
		
		patientChartView = new PatientChartFragment();
		tabBar.addTab(tabBar.newTab().setText(PATIENT_CHART).setTabListener(new TabListener(patientChartView)));

		//patientLogList = (ListView)findViewById(R.id.loglist); // This list may not be available yet since its in the fragment
		//patientLogList.setAdapter(patientLogsAdapter);  // Assignment made in fragment instead - when its ready
		Svc = SymptomManagementSvc.init(SymptomManagementSvc.getTestUrl());
//		Svc = new RestAdapter.Builder()
//		.setEndpoint(SymptomManagementSvc.getTestUrl())
//		.setLogLevel(LogLevel.FULL).build()
//		.create(SymptomManagementSvcApi.class);

		new PatientLogsAsyncTask(patientID).execute(Svc); // Passing in Services needs testing
		new MedicationsAsyncTask(doctorID).execute(Svc);
		new MedicationPrescribedAsyncTask(patientID).execute(Svc);
	}
	
	
	protected ArrayList<MedicationChoice> medchoices = new ArrayList<MedicationChoice>();
	
	public void savePrescriptionsChangeClick(View v){
		ArrayList<MedicationPrescribed> mps = new ArrayList<MedicationPrescribed>();
		MedicationPrescribed mp;
		for(MedicationChoice mc : medchoices){
			if(mc.isPrescribed()){
				Medication med = mc.getMedication();
				mp = new MedicationPrescribed();
				mp.setDoctorsName("Dr Dooddle");
				mp.setDosage(med.getDosage());
				mp.setMedicationName(med.getMedicationName());
				mp.setInstructions("Take once daily");
				mp.setRxnumber("432424");
				mp.setPatientID(patientID);
				mps.add(mp);
			}
		}
		// Todo: Make server call to save change of medications for patient.
		new SavePrescriptionsChangeTask(mps,patientID).execute(Svc);
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.patientdetailmenu, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if (id == R.id.refresh) {
			new PatientLogsAsyncTask(patientID).execute(Svc);
			new MedicationPrescribedAsyncTask(patientID).execute(Svc);
		}
		return true;
	}



	public void mergeMedData(){
		if(medications == null || medsPrescribed == null)return;
		medchoices.clear();
		MedicationChoice mc;
		boolean prescribed = false;
		for(Medication md : medications){
			mc = new MedicationChoice(md);
			for(MedicationPrescribed mp : medsPrescribed){
				if(mp.getMedicationName().equals(md.getMedicationName())){
					mc.setPrescribed(true);
				}
			}
			medchoices.add(mc);
		}
		medlistAdapter.clear();
		medlistAdapter.addAll(medchoices);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public static class TabListener implements ActionBar.TabListener{
		
		private final Fragment mFragment;
		
		public TabListener(Fragment fragment){
			mFragment = fragment;
		}
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(mFragment != null){
				ft.replace(R.id.patientdetailsview, mFragment);
			}				    
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(mFragment != null){
				ft.remove(mFragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public static class MedListAdapter extends ArrayAdapter<MedicationChoice>{

		public MedListAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			if(row == null){
				row = LayoutInflater.from(getContext()).inflate(R.layout.medication_item, parent,false);
			}
			
			MedicationChoice medc = getItem(position);
			Medication med = medc.getMedication();
			
			TextView medtv = (TextView)row.findViewById(R.id.medicationnametextview);
			TextView dosagetv = (TextView)row.findViewById(R.id.dosagetextview);
			CheckBox prescribedCheckBox = (CheckBox)row.findViewById(R.id.prescribed);
			medtv.setText(med.getMedicationName());
			dosagetv.setText(med.getDosage());
			prescribedCheckBox.setChecked(medc.isPrescribed());
			return row;
		}
	}
	
	
	public static class CheckinAdapter extends ArrayAdapter<Checkin>{

		public CheckinAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			
			if(row == null){
				row = LayoutInflater.from(getContext()).inflate(R.layout.checkin_log_item, parent,false);
			}
			
			Checkin ck = getItem(position);
			TextView eattv = (TextView)row.findViewById(R.id.caneatdrinktextview);
			TextView paintv = (TextView)row.findViewById(R.id.painleveltextview);
			TextView timetv = (TextView)row.findViewById(R.id.checkintimetextview);
			
			paintv.setText(ck.getPainLevel());
			SimpleDateFormat format = new SimpleDateFormat("EEE,MMM d h:mm a");
			
			timetv.setText(format.format(ck.getCheckinTime()));
			eattv.setText(String.valueOf(ck.isCanEatDrink()));
			
			return row;
		}
		
	}
	
	// This should be done with a service which will also monitor for patient distress
	public class PatientLogsAsyncTask extends AsyncTask<SymptomManagementSvcApi,Integer,ArrayList<Checkin>>{
		
		private long patientID;
		public PatientLogsAsyncTask(long patientID){
			this.patientID = patientID;
		}
		
		@Override
		protected ArrayList<Checkin> doInBackground(SymptomManagementSvcApi... params) {
			// TODO Auto-generated method stub
			SymptomManagementSvcApi svc = params[0];
//			Svc = new RestAdapter.Builder()
//			.setEndpoint(SymptomManagementSvc.getTestUrl())
//			.setLogLevel(LogLevel.FULL).build()
//			.create(SymptomManagementSvcApi.class);
			ArrayList<Checkin> checkins = Lists.newArrayList(svc.getPatientCheckins(patientID));
			return checkins;
		}

		@Override
		protected void onPostExecute(ArrayList<Checkin> checkins) {
			// TODO Auto-generated method stub
			if(checkins.size() > 0){
				Checkin lastCheckin = checkins.get(checkins.size()-1);
				medsTaken = Lists.newArrayList(lastCheckin.getMedTaken());
			}
			checkinlist = checkins;
			Calendar c = Calendar.getInstance();
			ArrayList<Checkin> todaysCheckins = new ArrayList<Checkin>();
			for(int i = 0; i < checkins.size(); i++){
				if(checkins.get(i).getCheckinTime() > c.getTimeInMillis()-24*60*60*1000){
					todaysCheckins.add(checkins.get(i));
				}
			}
			patientLogsAdapter.clear();
			patientLogsAdapter.addAll(todaysCheckins);
			//mergeMedData();
		}
		
	}
	
	public class SavePrescriptionsChangeTask extends AsyncTask<SymptomManagementSvcApi, Integer,Boolean>{
		
		private ArrayList<MedicationPrescribed> meds;
		private long patientID;
		public SavePrescriptionsChangeTask(ArrayList<MedicationPrescribed> meds, long patient){
			this.meds = meds;
			this.patientID = patient;
		}
		
		@Override
		protected Boolean doInBackground(SymptomManagementSvcApi... params) {
			// TODO Auto-generated method stub
			SymptomManagementSvcApi svc = params[0];
			boolean success = true;
			boolean s = svc.removePatientMedicationPrescriptions(patientID);
			for(MedicationPrescribed mp : meds){
				s = svc.submitMedication(mp, patientID);
				success = success && s;
			}
			return success;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if(result){
				new MedicationPrescribedAsyncTask(patientID).execute(Svc);
			}
		}
	}
	
	public class MedicationPrescribedAsyncTask extends AsyncTask<SymptomManagementSvcApi, Integer,ArrayList<MedicationPrescribed>>{

		private long patientID;
		
		public MedicationPrescribedAsyncTask(long patientid){
			this.patientID = patientid;
		}
		@Override
		protected ArrayList<MedicationPrescribed> doInBackground(
				SymptomManagementSvcApi... params) {
			// TODO Auto-generated method stub
			SymptomManagementSvcApi svc = params[0];
			ArrayList<MedicationPrescribed> meds = Lists.newArrayList(svc.getPrescribedMedications(patientID));
			return meds;
		}

		@Override
		protected void onPostExecute(ArrayList<MedicationPrescribed> result) {
			// TODO Auto-generated method stub
			medsPrescribed = result;
			mergeMedData();
		}
		
	}
	
	public class MedicationsAsyncTask extends AsyncTask<SymptomManagementSvcApi, Integer,ArrayList<Medication>>{

		private long doctorID;
		
		public MedicationsAsyncTask(long doctorID){
			this.doctorID = doctorID;
		}
		
		@Override
		protected ArrayList<Medication> doInBackground(
				SymptomManagementSvcApi... params) {
			SymptomManagementSvcApi svc = params[0];
			ArrayList<Medication> meds = Lists.newArrayList(svc.getMedicationsList());
			return meds;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Medication> meds) {
			// TODO Auto-generated method stub
			medications = meds;
			mergeMedData();
		}
	}
	
	
	
}
