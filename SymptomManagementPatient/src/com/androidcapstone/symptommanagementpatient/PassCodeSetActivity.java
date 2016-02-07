package com.androidcapstone.symptommanagementpatient;

import com.androidcapstone.symptommanagement.views.PasscodesetView;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class PassCodeSetActivity extends Activity {
	private FragmentManager mFragmentManager;
	private String username = "";
	private String password = "";
	
	private final PasscodesetView pssetview = new PasscodesetView();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setpasscode);
		Intent intent = getIntent();
		username = intent.getStringExtra(LoginActivity.USERNAME);
		password = intent.getStringExtra(LoginActivity.PASSWORD);
		
		mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.passcodesetview, pssetview);
		fragmentTransaction.commit();
	}
	
	public void setPasscodeClick(View v){
		SharedPreferences.Editor settingsEditor = getSharedPreferences(LoginActivity.LOGIN_SETTINGS, 0).edit();
		
		settingsEditor.putBoolean(LoginActivity.LOGIN_SET, true);
		settingsEditor.putString(LoginActivity.PASSCODE,passcodeEntered);
		settingsEditor.putString(LoginActivity.USERNAME,username);
		settingsEditor.putString(LoginActivity.PASSWORD,password);
		
		settingsEditor.apply();
	
		Toast.makeText(this, "Good, Now backtrack to login.", Toast.LENGTH_LONG).show();
	}
	
	private void setPasscode(String key){
		passcodeEntered += key;
		pssetview.updatePasscodeText(passcodeEntered);
	}
	
	public void clearPasscodeClick(View v){
		clearPasscode();
	}
	
	private void clearPasscode(){
		passcodeEntered = "";
		pssetview.updatePasscodeText(passcodeEntered);
	}
	
	private String passcodeEntered = "";
	
	public void keypadEnter(View v){
		switch(v.getId()){
			case R.id.keyset1:
				setPasscode("1");
				break;
			case R.id.keyset2:
				setPasscode("2");
				break;
			case R.id.keyset3:
				setPasscode("3");
				break;
			case R.id.keyset4:
				setPasscode("4");
				break;
			case R.id.keyset5:
				setPasscode("5");
				break;
			case R.id.keyset6:
				setPasscode("6");
				break;
			case R.id.keyset7:
				setPasscode("7");
				break;
			case R.id.keyset8:
				setPasscode("8");
				break;
			case R.id.keyset9:
				setPasscode("9");
				break;
			default:
				clearPasscode();
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(item.getItemId()){
		case R.id.viewcheckinfrompass:
			intent = new Intent(this,MainActivity.class);
			break;
		case R.id.viewalarmsetfrompass:
			intent = new Intent(this,AlarmSetActivity.class);
			break;
		case R.id.viewlogfrompass:
			intent = new Intent(this,ViewCheckinLogActivity.class);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.passcodemenu,menu);
		return true;
	}
	
}
