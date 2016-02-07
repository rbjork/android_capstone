package com.androidcapstone.symptommanagementpatient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.androidcapstone.symptommanagement.repository.Checkin;
import com.androidcapstone.symptommanagement.repository.MedicationTaken;
import com.androidcapstone.symptommanagementpatient.interfaces.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagementpatient.services.SymptomManagementSvc;
import com.google.common.collect.Lists;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewCheckinLogActivity extends Activity {

	private String username;
	private String password;
	private long patientID;
	protected ArrayList<Checkin> checkinlist;
	protected ArrayList<MedicationTaken> medsTaken;
	private ListView logList;
	protected CheckinAdapter patientLogsAdapter;
	private SymptomManagementSvcApi Svc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewlog);
		
		if(savedInstanceState != null){
			patientID = savedInstanceState.getLong(LoginActivity.PATIENTID);
			username = savedInstanceState.getString(LoginActivity.USERNAME);
			password = savedInstanceState.getString(LoginActivity.PASSWORD);
		}else{
			Intent intent = getIntent();
			patientID = intent.getLongExtra(LoginActivity.PATIENTID, 1L);
			username = intent.getStringExtra(LoginActivity.USERNAME);
			password = intent.getStringExtra(LoginActivity.PASSWORD);
		}
		SymptomManagementSvc.username = username;
		SymptomManagementSvc.password = password;
		Svc = SymptomManagementSvc.init(SymptomManagementSvc.getTestUrl());
		
		logList = (ListView)findViewById(R.id.loglist);
		patientLogsAdapter = new CheckinAdapter(this, R.layout.checkin_log_item);
		logList.setAdapter(patientLogsAdapter);
		new PatientLogsAsyncTask(patientID).execute(Svc);
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
			patientLogsAdapter.addAll(todaysCheckins);
			//mergeMedData();
		}
		
	}
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.logsmenu,menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		Intent intent;
//		switch(item.getItemId()){
//			case R.id.viewalarmsetfromlogs:
//				intent = new Intent(this,AlarmSetActivity.class);
//				break;
//			case R.id.viewpasscodefromlogs:
//				intent = new Intent(this,PassCodeSetActivity.class);
//				break;
//			case R.id.viewcheckinfromlogs:
//				intent = new Intent(this,MainActivity.class);
//				break;
//			default:
//				return super.onOptionsItemSelected(item);
//		}
//		startActivity(intent);
//		return true;
//	}
	
}
