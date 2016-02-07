package com.androidcapstone.symptommanagementpatient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.androidcapstone.symptommanagement.repository.Checkin;
import com.androidcapstone.symptommanagement.repository.MedicationPrescribed;
import com.androidcapstone.symptommanagement.repository.MedicationTaken;
import com.androidcapstone.symptommanagementpatient.services.CheckinService;
import com.androidcapstone.symptommanagementpatient.services.SymptomManagementSvc;
import com.androidcapstone.symptommanagementpatient.views.CheckinFragment;
import com.androidcapstone.symptommanagementpatient.views.MediaFragment;
import com.androidcapstone.symptommanagementpatient.views.PhysioFragment;
import com.androidcapstone.symptommanagementpatient.dialogs.TimeMedTakenDialog;
import com.androidcapstone.symptommanagementpatient.interfaces.SymptomManagementSvcApi;
import com.google.common.collect.Lists;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
//import android.os.Handler;
import android.util.Log;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String PATIENT_CHECKIN = "Checkin";
	private static final String PATIENT_MEDIA = "Media";
	private static final String PATIENT_PHYSIO = "Physio";
	
	public static String CLASS_TAG = "MainActivity";
	private String painLevel = "mild";
	//private SymptomManagementSvcApi Svc;
	private ArrayList<MedicationPrescribed> medsPrescribed;
	private ArrayList<MedicationTaken> medsTaken  = new ArrayList<MedicationTaken>();
	
	private long patientID;
	public ListView prescribedMedsListView;
	private FragmentManager mFragmentManager;
	private CustomAdapter medListAdapter;
	
	private CheckinFragment checkinFragment;
	private MediaFragment mediaFragment;
	private PhysioFragment physioFragment;
	
	private String username;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin);
		
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
		//******************
		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				
		checkinFragment = new CheckinFragment();
		tabBar.addTab(tabBar.newTab().setText(PATIENT_CHECKIN).setTabListener(new TabListener(checkinFragment)));
				
		mediaFragment = new MediaFragment();
		tabBar.addTab(tabBar.newTab().setText(PATIENT_MEDIA).setTabListener(new TabListener(mediaFragment)));
				
		physioFragment = new PhysioFragment();
		tabBar.addTab(tabBar.newTab().setText(PATIENT_PHYSIO).setTabListener(new TabListener(physioFragment)));
		//*********************
		
		
//		prescribedMedsListView = (ListView)findViewById(R.id.medsTakenlistView);
		medListAdapter = new CustomAdapter(this, R.layout.medlistitem);
		mFragmentManager = getFragmentManager();
//	
//		prescribedMedsListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				//Toast.makeText(getApplicationContext(), "onItemClick", Toast.LENGTH_LONG).show();
//				TimeMedTakenDialog td = new TimeMedTakenDialog();
//				td.position = position;
//				td.show(mFragmentManager, "timemedtaken");
//			}
//		});
	}
	
	// Called from fragment
	public void setPrescribedMedsListView(ListView v){
		prescribedMedsListView = v;
		prescribedMedsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "onItemClick", Toast.LENGTH_LONG).show();
				MedicationTaken mt = medsTaken.get(position);
				mt.setTaken(!mt.isTaken());
				TimeMedTakenDialog td = new TimeMedTakenDialog();
				td.position = position;
				td.show(mFragmentManager, "timemedtaken");
			}
		});
		medListAdapter.clear();
		medListAdapter.addAll(medsTaken);
		prescribedMedsListView.setAdapter(medListAdapter);
		
	}
	
	public void setTimeMedTaken(int position, int hour, int minute){
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),hour,minute);
		medsTaken.get(position).setTimeTaken(cal.getTimeInMillis());
		medListAdapter.notifyDataSetChanged();
	}
	
	
//	public void getDateClicked(View v){
//		TimeMedTakenDialog td = new TimeMedTakenDialog();
//		td.position = position;
//		td.show(mFragmentManager, "timemedtaken");
//	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(CLASS_TAG,"onStart");
		getPrescribedMedications();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(CLASS_TAG,"onResume");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch(item.getItemId()){
			case R.id.alarmset:
				intent = new Intent(this,AlarmSetActivity.class);
				break;
			case R.id.passcodeset:
				intent = new Intent(this,PassCodeSetActivity.class);
				break;
//			case R.id.viewlog:
//				intent = new Intent(this,ViewCheckinLogActivity.class);
//				break;
			case R.id.refresh:
				getPrescribedMedications();
				break;
			default:
				return super.onOptionsItemSelected( item);
		}
		if(intent != null){
			startActivity(intent);
		}
		return true;
	}

	// User Event Handlers
	
//	public void onEatCheckBoxClick(View v){
//		CheckBox cb = (CheckBox)v;
//		canEat = cb.isChecked();
//	}
//	
//	
//	public void onDrinkCheckBoxClick(View v){
//		CheckBox cb = (CheckBox)v;
//		canDrink = cb.isChecked();
//	}

	public void onPainLevelRadioButtonClicked(View v){
		boolean checked = ((RadioButton) v).isChecked();
		if(checked){
			switch(v.getId()){
				case R.id.radioMild:
					painLevel = "mild";
					break;
				case R.id.radio1Medium:
					painLevel = "medium";
					break;
				case R.id.radioSevere:
					painLevel = "severe";
					break;
			}
		}
	}
	
	public void submitCheckinClick(View v){
		Log.d(CLASS_TAG,"submitCheckinClick");
		Intent intent = CheckinService.makeIntent(this);
		// TODO: Need to substitute values for patientID and canEatDrink for hardcoded values
		
		RadioGroup myRadioGroup = (RadioGroup)findViewById(R.id.painlevelRadioGroup);
		switch(myRadioGroup.getCheckedRadioButtonId()){
			case R.id.radioMild:
				painLevel = "mild";
				break;
			case R.id.radio1Medium:
				painLevel = "medium";
				break;
			case R.id.radioSevere:
				painLevel = "severe";
				break;
		};
		
		CheckBox cbeat = (CheckBox)findViewById(R.id.eatcheckBox);
		CheckBox cbdrink = (CheckBox)findViewById(R.id.drinkCheckBox);
		boolean canEatDrink = !cbeat.isChecked() && !cbdrink.isChecked();
		
		Bundle chkin = Checkin.createBundle(patientID, this.painLevel, canEatDrink, medsTaken);
		intent.putExtras(chkin);
		startService(intent); // Note the server is actually running so this get queued and processed almost immediately.
	}
	
	private void getPrescribedMedications(){
		new Thread(new PrescriptionsRunnable(patientID)).start();
	}
	

	private final Handler handler = new Handler(); // may not need
	
	private class PrescriptionsRunnable implements Runnable{
		private long patientID;
		private SymptomManagementSvcApi servercApi;
		public PrescriptionsRunnable(Long patientID){
			this.patientID = patientID;
			servercApi = SymptomManagementSvc.init(SymptomManagementSvc.TEST_URL);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			medsPrescribed = Lists.newArrayList(servercApi.getMedications(patientID));
			MedicationTaken m;
			medsTaken.clear();
			for(int i=0 ; i<medsPrescribed.size(); i++){
				m = new MedicationTaken(false,medsPrescribed.get(i),Calendar.getInstance().getTimeInMillis());
				medsTaken.add(m);
			}
			
			handler.post(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					medListAdapter.clear();
					medListAdapter.addAll(medsTaken); // Note medsTaken is initially null;
					prescribedMedsListView.setAdapter(medListAdapter);
				}
				
			});
		}
	}
	
	public static class CustomAdapter extends ArrayAdapter<MedicationTaken>{

		
		public CustomAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			if(row == null){
				row = LayoutInflater.from(getContext()).inflate(R.layout.medlistitem, parent,false);
			}
			MedicationTaken medTaken = getItem(position);
			
			TextView nametv = (TextView)row.findViewById(R.id.medname);
			nametv.setText(medTaken.getMedicationPrescribed().getMedicationName()+"?");
			
			TextView dosagetv = (TextView)row.findViewById(R.id.meddosage);
			dosagetv.setText(medTaken.getMedicationPrescribed().getDosage());
			
			TextView rxtv = (TextView)row.findViewById(R.id.medtime);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(medTaken.getTimeTaken());
			
			//SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
			SimpleDateFormat format = new SimpleDateFormat("h:mm a");
			//System.out.println(format.format(cal.getTime()));
			
			rxtv.setText(format.format(cal.getTime()));
			
			CheckBox cb = (CheckBox)row.findViewById(R.id.medtakencheckbox);
			cb.setChecked(medTaken.isTaken());
			
			return row;
		}
		
		@Override
		public boolean isEnabled(int position)
		{
		    return true;
		}	
		
	}
	
	
	private Uri fileUri;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	
	public void takePicture(){
		
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);  // create a file to save the video
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

	   // intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

	    // start the Video Capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		 if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            File destination = new File(fileUri.getPath());
	            FileInputStream in;
				try {
					in = new FileInputStream(destination);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 10;
					Bitmap img = BitmapFactory.decodeStream(in,null,options);
					mediaFragment.displayImage(img);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } else if (resultCode == RESULT_CANCELED) {
	        	Toast.makeText(this,"User cancelled the image capture",Toast.LENGTH_LONG).show();
	        } else {
	        	Toast.makeText(this,"Image capture failed, advise user",Toast.LENGTH_LONG).show(); 
	        }
		}
	}
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile = null;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    }

	    return mediaFile;
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
				ft.replace(R.id.checkinholder, mFragment);
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
}
