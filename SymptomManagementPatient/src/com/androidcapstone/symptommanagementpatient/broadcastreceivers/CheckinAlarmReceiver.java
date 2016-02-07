package com.androidcapstone.symptommanagementpatient.broadcastreceivers;

import java.text.DateFormat;
import java.util.Date;

import com.androidcapstone.symptommanagementpatient.MainActivity;
import com.androidcapstone.symptommanagementpatient.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CheckinAlarmReceiver extends BroadcastReceiver {
	private static final String TAG = "AlarmReceiver"; 
	private static final int CHECKIN_NOTIFICATION_ID = 1;
	private long[] mVibrationPattern = {0,200,200,300,200,300};
//	private Uri soundURI = Uri.parse("");
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent activityIntent = new Intent(context, MainActivity.class);
		PendingIntent mSymptomManagementIntent = PendingIntent.getActivity(context, 0, activityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		Log.i(TAG,"Alarm at:" + DateFormat.getDateTimeInstance().format(new Date()));
		
		
		Notification.Builder notificationBuilder = new Notification.Builder(context)
			.setTicker("Time for checkin on your symptom management. Select this notification to open the application")
			.setSmallIcon(R.drawable.ic_launcher)
			.setAutoCancel(true)
			.setContentIntent(mSymptomManagementIntent)
			.setVibrate(mVibrationPattern);
	//		.setSound(soundURI);
		
		NotificationManager notificationMgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationMgr.notify(CHECKIN_NOTIFICATION_ID, notificationBuilder.build());
	}

}
