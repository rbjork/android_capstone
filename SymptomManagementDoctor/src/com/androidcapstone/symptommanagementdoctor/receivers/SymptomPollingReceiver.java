package com.androidcapstone.symptommanagementdoctor.receivers;

import com.androidcapstone.symptommanagementdoctor.services.PatientCheckinPollingService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SymptomPollingReceiver extends BroadcastReceiver {
	
	private static final String TAG = "SymptomPollingReceiver"; 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onReceive");
		Bundle b = intent.getExtras();
		Intent service = new Intent(context,PatientCheckinPollingService.class);
		context.startService(service);
	}

}
