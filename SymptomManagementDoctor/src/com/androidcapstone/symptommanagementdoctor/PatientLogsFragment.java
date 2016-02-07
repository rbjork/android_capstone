package com.androidcapstone.symptommanagementdoctor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.androidcapstone.symptommanagement.repository.MedicationPrescribed;
import com.androidcapstone.symptommanagement.repository.MedicationTaken;
import com.google.common.collect.Lists;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PatientLogsFragment extends Fragment {
	private TextView dateView;
	private ListView logList;
	private ListView medsTakenListView;
	private PatientDetailsActivity activity;
	
	protected MedTakenAdapter medtakenAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		RelativeLayout rlayout = (RelativeLayout)inflater.inflate(R.layout.patientlogs_fragment, container, false);
		
		dateView = (TextView)rlayout.findViewById(R.id.dateview);
		
		SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d");
		dateView.setText(format.format(Calendar.getInstance().getTimeInMillis()) + " -last 24 hours");
		
		logList = (ListView)rlayout.findViewById(R.id.loglist);
		medsTakenListView = (ListView)rlayout.findViewById(R.id.medlistView);
		logList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ArrayList<MedicationTaken> meds = Lists.newArrayList(activity.checkinlist.get(position).getMedTaken());
				medtakenAdapter.clear();
				medtakenAdapter.addAll(meds);
				medtakenAdapter.notifyDataSetChanged();
			}
			
		});
		return rlayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		activity = (PatientDetailsActivity)getActivity();
		logList.setAdapter(activity.patientLogsAdapter);
		
		medtakenAdapter = new MedTakenAdapter(getActivity(), R.layout.medtaken_item);
		medsTakenListView.setAdapter(medtakenAdapter);
	}
	
	public static class MedTakenAdapter extends ArrayAdapter<MedicationTaken>{

		public MedTakenAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			
			if(row == null){
				row = LayoutInflater.from(getContext()).inflate(R.layout.medtaken_item,parent,false);
			}
			
			MedicationTaken med = getItem(position);
			MedicationPrescribed mp = med.getMedicationPrescribed();
			TextView tvname = (TextView)row.findViewById(R.id.medname);
			TextView tvdos = (TextView)row.findViewById(R.id.meddosage);
			TextView tvtime = (TextView)row.findViewById(R.id.medtime);
			CheckBox cb = (CheckBox)row.findViewById(R.id.medtakencheckbox);
			boolean state = med.isTaken();
			cb.setChecked(state);
			tvname.setText(mp.getMedicationName());
			SimpleDateFormat format = new SimpleDateFormat("h:mm a");
			tvtime.setText(format.format(med.getTimeTaken()));
			tvdos.setText(mp.getDosage());
			return row;
		}
		
		
	}
}
