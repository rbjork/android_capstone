package com.androidcapstone.symptommanagementdoctor.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import retrofit.RestAdapter;
import android.app.Notification;
import android.app.NotificationManager;

import com.androidcapstone.symptommanagement.repository.Checkin;
import com.androidcapstone.symptommanagement.repository.Patient;
import com.androidcapstone.symptommanagementdoctor.LoginActivity;
import com.androidcapstone.symptommanagementdoctor.MainActivity;
import com.androidcapstone.symptommanagementdoctor.PatientDetailsActivity;
import com.androidcapstone.symptommanagementdoctor.R;
import com.androidcapstone.symptommanagementdoctor.data.PatientAlert;
import com.androidcapstone.symptommanagementdoctor.data.ServiceRequestsConstants;
import com.androidcapstone.symptommanagementdoctor.helperutils.AssembleAlertsFromCheckins;
import com.androidcapstone.symptommanagementdoctor.interfaces.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagementdoctor.receivers.SymptomPollingReceiver;
import com.google.common.collect.Lists;

import retrofit.RestAdapter.LogLevel;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

// Most likely a started or started + bound service
public class PatientCheckinPollingService extends Service {
	
	private static final String TAG = "PatientCheckinPollingService";  
	
	private Messenger mReqMessenger = null;
	private SymptomManagementSvcApi serverApi;
	private AlarmManager mAlarmManager;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onCreate");
		mReqMessenger = new Messenger(new RequestHandler());
		serverApi = createSrv();
	}
	
	public static Intent makeIntent(Context context){
		Log.i(TAG,"makeIntent");
		return new Intent(context,PatientCheckinPollingService.class);
	}
	
	// TODO: Problem with this
	private SymptomManagementSvcApi createSrv(){
		SymptomManagementSvcApi Svc = SymptomManagementSvc.init(SymptomManagementSvc.getTestUrl());
		return Svc;
	}
	
	private long[] mVibrationPattern = {0,200,200,300,200,300};
	private static final int CHECKIN_NOTIFICATION_ID = 1;
	
	// This is called by receiver that got the re-occurring Alarm event.
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) { // what is startId used for?
		Log.i(TAG,"onStartCommand");
		new Thread(new Runnable() {
			@Override
			public void run() {
				 Log.i(TAG,"getRecentSeriousCheckins1");
				//ArrayList<Patient> patients = Lists.newArrayList(serverApi.getPatients(1L));
				
				 ArrayList<Checkin> ckns = Lists.newArrayList(serverApi.getRecentSeriousCheckins(1L));
				 Bundle alertdata = AssembleAlertsFromCheckins.getAlerts(ckns); // it may be better that this is a instance method
				 if(alertdata == null){
					 return;
				 }
				 Log.i(TAG,"getRecentSeriousCheckins2");
				 Message datamsg = new Message();
				 datamsg.setData(alertdata);
				
				 try{
					 if(replyMessenge != null){
						 replyMessenge.send(datamsg);
						 Log.i(TAG,"replyMessenge.send");
					 }else{
						 // send notification if App not running
						 Intent intent = null;
						 String msg = null;
//						 if(p-1 > 1){
//							 intent = new Intent(PatientCheckinPollingService.this, MainActivity.class);
//							 msg = "Patients with serious condition. Select this notification to open the application";
//						 }else{
							// Checkin c = ckns.get(0);
						 msg = "Patients with serious condition. Select this notification to open the application";
					    
						 
						 intent = new Intent(PatientCheckinPollingService.this, PatientDetailsActivity.class);

						 intent.putExtras(alertdata);
						 intent.putExtra(PatientAlert.IS_ALERT,true);
						 
						 intent.putExtra(LoginActivity.USERNAME,SymptomManagementSvc.username);
						 intent.putExtra(LoginActivity.PASSWORD,SymptomManagementSvc.password);
						 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						 PendingIntent pendingIntent = PendingIntent.getActivity( getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
						 Log.i(TAG,"replyMessenge is null");
						 
						 Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
							.setTicker(msg)
							.setSmallIcon(R.drawable.sunflower)
							.setContentTitle("Patient Alert")
							.setContentText("Patient is suffering")
							.setAutoCancel(true)
							.setContentIntent(pendingIntent)
							.setVibrate(mVibrationPattern);
					//		.setSound(soundURI);
						
						 NotificationManager notificationMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
						 notificationMgr.notify(CHECKIN_NOTIFICATION_ID, notificationBuilder.build());
					 }
				 }catch(Exception e){
					 Log.i(TAG,"onStartCommand send exception");
				 }
			}
		}).start();
		return START_STICKY;
	}
	
	private  Collection<Checkin> getRecentSeriousCheckins(long id){
		 Log.i(TAG,"getRecentSeriousCheckins1");
		 Collection<Checkin> ckns = serverApi.getRecentSeriousCheckins(id);
		 Log.i(TAG,"getRecentSeriousCheckins2");
		 return ckns;
	}
	
	private  ArrayList<Patient> getPatients(long id){
		 Log.i(TAG,"getPatients1");
		 ArrayList<Patient> patients = Lists.newArrayList(serverApi.getPatients(id));
		 Log.i(TAG,"getPatients2");
		 return patients;
	}
	
	private Messenger replyMessenge;
	
	private class RequestHandler extends Handler{

		public void handleMessage(Message request){
			 Log.i(TAG,"handleMessage");
			 replyMessenge = request.replyTo;
			 
			
			 switch(request.what){ // Alternative to doing in onStartCommand or maybe both 
			 	 case ServiceRequestsConstants.GET_ALERTS:
			 		new Thread(new Runnable() {
						@Override
						public void run() {
							ArrayList<Checkin> ckns = Lists.newArrayList(serverApi.getRecentSeriousCheckins(1L));
							Bundle alertdata = AssembleAlertsFromCheckins.getAlerts(ckns); // it may be better that this is a instance method
					// Message datamsg = new Message();
							Message reply = Message.obtain();
							reply.setData(alertdata);
							 try{
								 replyMessenge.send(reply);
							 }catch(RemoteException e){
								 e.printStackTrace();
							 }
						}
			 		}).start();
				 break;
				 
				 default:
					
			 }
			 
			
		}
	}
	
	public static ArrayList<PatientAlert> getPatientAlerts(Message msg){
		return PatientAlert.getAlertsFromBundle(msg.getData());
	};

	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onBind");
		if(mReqMessenger == null){
			return null;
		}else{
			return mReqMessenger.getBinder();  // If a request to bind from the activity has been made.
		}
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onRebind");
		super.onRebind(intent);
	}

}
