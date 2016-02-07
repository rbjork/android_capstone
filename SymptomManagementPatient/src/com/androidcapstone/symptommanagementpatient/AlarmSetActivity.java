package com.androidcapstone.symptommanagementpatient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.androidcapstone.symptommanagementpatient.broadcastreceivers.AlarmLoggerReceiver;
import com.androidcapstone.symptommanagementpatient.broadcastreceivers.CheckinAlarmReceiver;
import com.androidcapstone.symptommanagementpatient.dialogs.AlarmSetDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class AlarmSetActivity extends Activity {
	
	private FragmentManager mFragmentManager;
	private CustomAdapter alarmListAdapter;
	private ListView alarmListView;
	private AlarmManager mAlarmManager;
	
	public static final String ALARM_SETTINGS = "SymptomAlarms";
	
	private ArrayList<Long> alarmtimes = new ArrayList<Long>();
	
	private int lastselected;
	private View lastView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setalarms);

		alarmListView = (ListView)findViewById(R.id.alarmlistView);
		alarmListAdapter = new CustomAdapter(this, R.layout.alarmitem);
		alarmListView.setAdapter(alarmListAdapter);
		mFragmentManager = getFragmentManager();
		
		alarmListView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				view.setSelected(true);
				lastselected = position;
			}
			
		});
		
		
	}
	
	public void addAlarmClick(View v){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		alarmtimes.add(cal.getTimeInMillis());
		alarmListAdapter.clear();
		alarmListAdapter.addAll(alarmtimes);
		alarmListAdapter.notifyDataSetChanged();
		lastselected = alarmtimes.size() - 1;
		alarmListView.setSelection(lastselected);
		AlarmSetDialog alarmDialog = new AlarmSetDialog();
		alarmDialog.position = lastselected;
		alarmDialog.show(mFragmentManager, "alarmdialog");
	}
	
	public void deleteAlarmClick(View v){
		if(lastselected > 0){
			alarmtimes.remove(lastselected);
			alarmListAdapter.clear();
			alarmListAdapter.addAll(alarmtimes);
			alarmListAdapter.notifyDataSetChanged();
			lastselected = -1;
		}
	}

	public void setAlarmTime(int position, int hour, int minute){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE, minute);
		
		alarmtimes.set(position, cal.getTimeInMillis());
		alarmListAdapter.clear();
		alarmListAdapter.addAll(alarmtimes);
		alarmListAdapter.notifyDataSetChanged();
	}
	
	private static final long INITIAL_ALARM_DELAY = 20 * 1000L; // 20 seconds
	protected static final long JITTER = 5000L; // 5 seconds
	
	public void saveAlarmsClick(View v){
		
		mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		for(int i=0; i < alarmtimes.size(); i++){
			long newTime = alarmtimes.get(i);
			Intent mNotificationReceiverIntent = new Intent(AlarmSetActivity.this, CheckinAlarmReceiver.class);
			PendingIntent mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(AlarmSetActivity.this, 0, mNotificationReceiverIntent, 0);
			mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, newTime, AlarmManager.INTERVAL_DAY, mNotificationReceiverPendingIntent);
			
		}

	}
	
	// Use PersistenceManager for saving alarm times
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		SharedPreferences settings = getSharedPreferences(ALARM_SETTINGS, 0);
		int num = settings.getInt("alarmNumber", 0);
		alarmtimes = new ArrayList<Long>();
		for(int i=0; i<num; i++){
			long time = settings.getLong("alarm"+String.valueOf(i), 0L);
			alarmtimes.add(time);
		}
		alarmListAdapter.clear();
		alarmListAdapter.addAll(alarmtimes);
		alarmListAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferences settings = getSharedPreferences(ALARM_SETTINGS, 0);
		SharedPreferences.Editor ed = settings.edit();
		ed.putInt("alarmNumber", alarmtimes.size());
		for(int i=0; i < alarmtimes.size(); i++){
			long newTime = alarmtimes.get(i);
			ed.putLong("alarm"+String.valueOf(i),newTime);
		}
		ed.commit();
	}


	public static class CustomAdapter extends ArrayAdapter<Long>{

		
		public CustomAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			if(row == null){
				row = LayoutInflater.from(getContext()).inflate(R.layout.alarmitem, parent,false);
			}
			long alarm = getItem(position);
			
			TextView alarmtv = (TextView)row.findViewById(R.id.alarmtime);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(alarm);
			
			//SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
			SimpleDateFormat format = new SimpleDateFormat("h:mm a");
			//System.out.println(format.format(cal.getTime()));
			
			alarmtv.setText(format.format(cal.getTime()));
			
			return row;
		}
		
		@Override
		public boolean isEnabled(int position)
		{
		    return true;
		}
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.alarmsetmenu,menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		Intent intent;
//		switch(item.getItemId()){
//			case R.id.viewlogfromalarm:
//				intent = new Intent(this,ViewCheckinLogActivity.class);
//				break;
//			case R.id.viewpasscodefromalarm:
//				intent = new Intent(this,PassCodeSetActivity.class);
//				break;
//			case R.id.viewcheckinfromalarm:
//				intent = new Intent(this,MainActivity.class);
//				break;
//				default:
//					return super.onOptionsItemSelected(item);
//		}
//		startActivity(intent);
//		return true;
//	}

	
}
