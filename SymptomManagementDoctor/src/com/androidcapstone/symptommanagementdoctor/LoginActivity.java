package com.androidcapstone.symptommanagementdoctor;

import java.util.ArrayList;

import com.androidcapstone.symptommanagement.views.LoginView;
import com.androidcapstone.symptommanagement.views.PasscodeView;
import com.androidcapstone.symptommanagementdoctor.helperutils.EncryptUtil;
import com.androidcapstone.symptommanagementdoctor.interfaces.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagementdoctor.services.SymptomManagementSvc;




import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
//import android.widget.EditText;
//import android.widget.TextView;

public class LoginActivity extends Activity {
	private static final String TAG = "LoginActivity"; 
	public static final String LOGIN_SETTINGS = "loginsettings";
	public static final String LOGIN_SET = "passcodeset";
	public static final String PASSCODE = "passcode";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String PATIENTID = "patientID";
	public static final String DOCTORID = "doctorID";
	public static final String PATIENTNAME = "patientname";
	
	private FragmentManager mFragmentManager;
	
	private final PasscodeView psview = new PasscodeView();
	private final LoginView loginview = new LoginView();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mFragmentManager = getFragmentManager();
		
		
	}
	
	// Button handler
	// If user logs in by username , password, then we want them to setup their passcode
	public void login(View v){
		// send request via AsyncTask which allow app to open main or passcode set
		Intent intent = new Intent(this,PassCodeSetActivity.class);
		Bundle login = new Bundle();
		login.putString(USERNAME,loginview.getUserName());
		login.putString(PASSWORD,loginview.getPassword());
		
		intent.putExtras(login);
		startActivity(intent);
	}
	
	// Button handeler
	// But it should check if passcode is correct first
	public void loginByPassCodeClick(View v){
		EncryptUtil cipher = new EncryptUtil();
		SharedPreferences settings = getSharedPreferences(LOGIN_SETTINGS, 0);
		String passcode = settings.getString(PASSCODE,passcodeEntered);
		if(passcodeEntered.equals(passcode)){ // username and passcode encrypted then passcode should not be saved and username and password are encrypted in PS.
			
			String username = settings.getString(USERNAME,"");
			username = cipher.decrypt(passcode, username);
			String password = settings.getString(PASSWORD,"");
			password = cipher.decrypt(passcode, password);
			String[] params = {username,password};
			new LoginTask().execute(params);
		}else{
			Toast.makeText(this, "Passcode incorrrect", Toast.LENGTH_LONG);
		}
	}
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		SharedPreferences settings = getSharedPreferences(LOGIN_SETTINGS, 0);
		boolean loginset = settings.getBoolean(LOGIN_SET, false);
		
		if(loginset){
			showPasscode();
		}else{
			showLogin();
		}
	}
	
	private void showPasscode(){
		if(psview.isAdded())return;
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		if(loginview.isAdded()){
			fragmentTransaction.replace(R.id.loginview, psview);
		}else{
			fragmentTransaction.add(R.id.loginview, psview);
		}
		fragmentTransaction.commit();
	}
	
	private void showLogin(){
		if(loginview.isAdded())return;
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		if(psview.isAdded()){
			fragmentTransaction.replace(R.id.loginview, loginview);
		}else{
			fragmentTransaction.add(R.id.loginview, loginview);
		}
		fragmentTransaction.commit();
	}
	
	private void setPasscode(String key){
		passcodeEntered += key;
		psview.updatePasscodeText(passcodeEntered);
	}
	
	public void clearPasscodeClick(View v){
		clearPasscode();
	}
	private void clearPasscode(){
		passcodeEntered = "";
		psview.updatePasscodeText(passcodeEntered);
	}
	
	private String passcodeEntered = "";
	
	public void keypadEnter(View v){
		switch(v.getId()){
			case R.id.key1:
				setPasscode("1");
				break;
			case R.id.key2:
				setPasscode("2");
				break;
			case R.id.key3:
				setPasscode("3");
				break;
			case R.id.key4:
				setPasscode("4");
				break;
			case R.id.key5:
				setPasscode("5");
				break;
			case R.id.key6:
				setPasscode("6");
				break;
			case R.id.key7:
				setPasscode("7");
				break;
			case R.id.key8:
				setPasscode("8");
				break;
			case R.id.key9:
				setPasscode("9");
				break;
			default:
				clearPasscode();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
	// TODO 11/13
	
	class LoginTask extends AsyncTask<String,String,Long>{
		private String username;
		private String password;
		@Override
		protected Long doInBackground(String ... params) {
			// TODO Auto-generated method stub
			username = params[0];
			password = params[1];
			SymptomManagementSvcApi svc = SymptomManagementSvc.initSecure(SymptomManagementSvc.TEST_SECURE_URL, username, password);
			long doctorID = svc.checkDoctorLogin(username);
			return doctorID;
		}

		@Override
		protected void onPostExecute(Long doctorID) {
			// TODO Auto-generated method stub
			
			SymptomManagementSvc.username = username;
			SymptomManagementSvc.password = password;
			
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			//Bundle data = new Bundle();
			//data.putLong(LoginActivity.PATIENTID, patientID);
			intent.putExtra(LoginActivity.DOCTORID,doctorID);
			intent.putExtra(LoginActivity.USERNAME,username);
			intent.putExtra(LoginActivity.PASSWORD,password);
			
			startActivity(intent);
			super.onPostExecute(doctorID);
		}

	}
	
}
