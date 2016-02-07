package com.androidcapstone.symptommanagementdoctor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

import com.androidcapstone.symptommanagement.repository.Checkin;
import com.androidcapstone.symptommanagement.repository.MedicationTaken;
import com.androidcapstone.symptommanagement.repository.Patient;
import com.androidcapstone.symptommanagementdoctor.data.PatientAlert;
import com.androidcapstone.symptommanagementdoctor.data.ServiceRequestsConstants;
import com.androidcapstone.symptommanagementdoctor.interfaces.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagementdoctor.receivers.SymptomPollingReceiver;
import com.androidcapstone.symptommanagementdoctor.services.PatientCheckinPollingService;
import com.androidcapstone.symptommanagementdoctor.services.SecuredRestBuilder;
import com.androidcapstone.symptommanagementdoctor.services.SymptomManagementSvc;
import com.google.common.collect.Lists;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import retrofit.RestAdapter;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity"; 
	public ListView patientListView;
	public PatientListAdapter patientListAdapter;
	
	private Messenger mReqMessengerRef = null;
	private Messenger mReplyMessenger = new Messenger(new ReplyHandler());
	
	private ServiceConnection patientMonitorConnection;
	private Vibrator alertVibrator;
	private AlarmManager mAlarmManager;
	private long doctorID;
	
	private String username;
	private String password;
	
	private EditText searchText;
	
	protected ArrayList<Patient> patients;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
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
			Toast.makeText(this, "There are alerts", Toast.LENGTH_SHORT);
		}
		
		setContentView(R.layout.activity_main);
		View v = findViewById(R.id.patientslistView);
		
		patientListView = (ListView)findViewById(R.id.patientslistView);
		patientListAdapter = new PatientListAdapter(this, R.layout.patient_list_item);
		patientListView.setAdapter(patientListAdapter);
		
		patientListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Patient p = patientListAdapter.getItem(position);
				Intent intent = new Intent(MainActivity.this,PatientDetailsActivity.class);
				intent.putExtra(LoginActivity.PATIENTID, p.getId());
				intent.putExtra(LoginActivity.PATIENTNAME, (p.getFirstName() + " " + p.getLastName()));
				intent.putExtra(LoginActivity.USERNAME, username);
				intent.putExtra(LoginActivity.PASSWORD, password);
				startActivity(intent);
			}
		});
		
		
		searchText = (EditText)findViewById(R.id.searchtext);
		searchText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterList();
			}

			@Override
			public void afterTextChanged(Editable s) {}
			
		});
		
		alertVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	}
	
	public void clearSearchClick(View v){
		if(filteredOutPatients.size() > 0)patients.addAll(filteredOutPatients);
		filteredOutPatients.clear();
		searchText.setText("");
	}
	
	private ArrayList<Patient> filteredOutPatients = new ArrayList<Patient>();
	
	protected void filterList(){
		String searchString = searchText.getText().toString().toLowerCase();
		if(filteredOutPatients.size()>0){
			patients.addAll(filteredOutPatients);
			filteredOutPatients.clear();
		}
		for(int i=patients.size()-1 ;i>=0; i--){
			Patient p = patients.get(i);
			String lastname = p.getLastName().toLowerCase();
			if(lastname.startsWith(searchString) == false){
				patients.remove(p);
				filteredOutPatients.add(p);
			}
		}
		patientListAdapter.clear();
		patientListAdapter.addAll(patients);
		patientListAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onStart() {
		Log.i(TAG,"onStart");
		// TODO Auto-generated method stub
		super.onStart();
		//Intent intent = CheckinService.makeIntent(this); // may want to add handler callback unless notification is used
		//startService(intent); // May need to test first if running.
		new PatientAsyncTask().execute(doctorID);
		
		if(patientMonitorConnection == null){
			buildPatientMonitorServiceConnection();
		}
		//if(mReqMessengerRef == null){
			bindService(PatientCheckinPollingService.makeIntent(this),patientMonitorConnection,Context.BIND_AUTO_CREATE);
		//}

	}
	
	
	private final int POLLING_CODE = 19223; 
	private final long TEST_ALARM_INTERVAL = 20*1000; // 10 seconds.
	
	private void activateCheckinPolling(){
		 Log.i(TAG,"activateCheckinPolling");
		 Intent intent = new Intent(MainActivity.this, SymptomPollingReceiver.class);
		 PendingIntent pendingintent = PendingIntent.getBroadcast(MainActivity.this, POLLING_CODE, intent, 0);
		// mAlarmManager.cancel(mNotificationReceiverPendingIntent); // Cancel any prior alarm set of same PendingIntent
		 mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME,  TEST_ALARM_INTERVAL, pendingintent);
		// mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, TEST_ALARM_INTERVAL, TEST_ALARM_INTERVAL, pendingintent);
	}

	private void deactivateCheckinPolling(){
		Intent intent = new Intent(MainActivity.this, SymptomPollingReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, POLLING_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//PendingIntent mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
		mAlarmManager.cancel(pendingIntent); // Cancel any prior alarm set of same PendingIntent
		Intent service = new Intent(this,PatientCheckinPollingService.class);
		//if(patientMonitorConnection != null)unbindService(patientMonitorConnection);
		//stopService(service);
	}
	
	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		try{
			unbindService(patientMonitorConnection);
		}catch(Exception e){
			Log.i(TAG,e.getStackTrace().toString());
		}
		super.onStop();
	}

	// This perhaps should be called from onStart
	private void buildPatientMonitorServiceConnection(){
		patientMonitorConnection = new ServiceConnection(){

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				Log.i(TAG,"onServiceConnected");
				mReqMessengerRef = new Messenger(service);
				sendRequestForReplyConnection();
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.i(TAG,"onServiceDisconnected");
				mReqMessengerRef = null;
			}
		};
	}
	
	private void sendRequestForReplyConnection(){
		Log.i(TAG,"sendRequestForReplyConnection");
		try{
			if(mReqMessengerRef != null){
				Message requestMessage = new Message();
				requestMessage.what = ServiceRequestsConstants.GET_ALERTS;
				requestMessage.replyTo = mReplyMessenger;
				mReqMessengerRef.send(requestMessage);
			}
		}catch(RemoteException e){
			e.printStackTrace();
		}
	}
	
	// Wont just be reply but a handler for message pushed into activity
	class ReplyHandler extends Handler{
		public void handleMessage(Message msg){
			Log.i(TAG,"ReplyHandler handleMessage");
			
			ArrayList<PatientAlert> alerts = PatientCheckinPollingService.getPatientAlerts(msg);
			
			// *** Use instead a list for alerts *** //
			
//			PatientAlert alert = alerts.get(0);
//			long pid = alert.getPatientID(); //ck.getPatientID();
//			
//			Patient p = null; 
//			boolean found = false;
//			int row = 0;
//			for(int i = 0; i < patientListAdapter.getCount(); i++){
//				p = patientListAdapter.getItem(i);
//				if(p.getId() == pid){
//					found = true;
//					row = i;
//					break;
//				}
//			}
//			String painLevel = alert.getPainlevel();
//			String info;
//			if(p != null){
//				info = "Patient " + p.getFirstName() + " " + p.getLastName() + " with " + painLevel + " pain.";
//				patientListView.setSelection(row);
//				patientListView.setSelector(R.color.listitemselected);
//			}else{
//				info = "Unknown patient " + alert.getPatientID() + " wiht " + painLevel + " pain.";
//			}
//			
//			Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
//			 // Vibrate for 500 milliseconds
			alertVibrator.vibrate(1000);
		}
	}
	
	
	// UI Construction:
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if(id == R.id.polling_settings){
			if (item.isChecked()){
				item.setChecked(false);
				deactivateCheckinPolling();
			}else{
				item.setChecked(true);
				activateCheckinPolling();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	class PatientAsyncTask extends AsyncTask<Long, Integer, ArrayList<Patient>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Patient> doInBackground(Long... params) {
			// TODO Auto-generated method stub
			long doctorID = params[0];
			SymptomManagementSvcApi Svc = SymptomManagementSvc.init(SymptomManagementSvc.getTestUrl());
			
//			SymptomManagementSvcApi Svc = new RestAdapter.Builder()
//			.setEndpoint(SymptomManagementSvc.getTestUrl())
//			.setLogLevel(LogLevel.FULL).build()
//			.create(SymptomManagementSvcApi.class);
			
			ArrayList<Patient> patients = Lists.newArrayList(Svc.getPatients(doctorID));
			return patients;
		}

		@Override
		protected void onPostExecute(ArrayList<Patient> result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			patients = result;
			patientListAdapter.clear();
			patientListAdapter.addAll(patients);
		}
		
	}
	
	public static class PatientListAdapter extends ArrayAdapter<Patient>{

		
		public PatientListAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			if(row == null){
				row = LayoutInflater.from(getContext()).inflate(R.layout.patient_list_item, parent,false);
			}
			
			Patient patient = getItem(position);
			
			TextView fntv = (TextView)row.findViewById(R.id.firstNameTextView);
			fntv.setText(patient.getFirstName());
			
			TextView lntv = (TextView)row.findViewById(R.id.lastNameTextView);
			lntv.setText(patient.getLastName());
			
			TextView dobtv = (TextView)row.findViewById(R.id.dobTextView);
			long dob = patient.getBirthDate();
			SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
			
			dobtv.setText(format.format(dob));
			
			
			TextView mrntv = (TextView)row.findViewById(R.id.mrnTextView);
			mrntv.setText(patient.getMrn());
			
			return row;
		}

	}
}
