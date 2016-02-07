package com.androidcapstone.symptommanagementpatient.services;

import java.util.ArrayList;

import com.androidcapstone.symptommanagementpatient.interfaces.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagement.repository.Checkin;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


// This service should terminate itself once its delivered its checkin
// When check is delivered it should send a sticky notification and then terminate.
public class CheckinService extends IntentService {

	private static String CLASS_TAG = "CheckinService";
	private ConnectivityManager cm;
	private SymptomManagementSvcApi servercApi;
	
	private boolean isListening;
	private ArrayList<Intent> intentQueue;
	
	public CheckinService(){
		super("CheckinService Worker Thread");
	}
	
	public CheckinService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		intentQueue = new ArrayList<Intent>();
	}
	
	private Boolean isThereNetworkConnection(){
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// Poll for Wifi or Cell connection
		Log.d(CLASS_TAG,"onStartCommand");
		
		cm =(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		servercApi = SymptomManagementSvc.init(SymptomManagementSvc.TEST_URL);
		return super.onStartCommand(intent, flags,startId);
		//return Service.START_NOT_STICKY;
	}
	
	public static Intent makeIntent(Context context){
		Log.d(CLASS_TAG,"makeIntent");
		return new Intent(context,CheckinService.class);
	}

//	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(CLASS_TAG,"onHandleIntent");
		Boolean nc = isThereNetworkConnection();
		if(nc){
			// process intent
			Bundle bundleCheckin = intent.getExtras();
			Checkin chkin = Checkin.factory(bundleCheckin);
			long pid = chkin.getPatientID();
			Log.d(CLASS_TAG,"before submitCheckin");
			chkin = servercApi.submitCheckin(chkin,pid);
			Log.d(CLASS_TAG,"after submitCheckin");
			
		}else{
			// queue the intent;
			intentQueue.add(intent);
			// set listener - dont allow termination till created secondary queue
			if(!isListening){
				isListening = true;
				registerReceiver(new BroadcastReceiver(){

					@Override
					public void onReceive(Context context, Intent intent) {
						// TODO Auto-generated method stub
						// process intents that timed out
						NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
						if(activeNetwork == null){
							// Not connected
						}else{
							// connected ?
							while(intentQueue.size()>0){
								Bundle bundleCheckin = intentQueue.remove(0).getExtras();
								Checkin chkin = Checkin.factory(bundleCheckin);
								long pid = chkin.getPatientID();
								chkin = servercApi.submitCheckin(chkin,pid);
							}
						}
					}

				}, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
				
			}
		}
	}
	
	

}


