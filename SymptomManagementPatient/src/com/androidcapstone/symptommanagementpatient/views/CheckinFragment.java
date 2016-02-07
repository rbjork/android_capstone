package com.androidcapstone.symptommanagementpatient.views;


import com.androidcapstone.symptommanagementpatient.MainActivity;
import com.androidcapstone.symptommanagementpatient.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CheckinFragment extends Fragment {
	private ListView prescribedMedsListView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_main, container,false);
		prescribedMedsListView = (ListView)v.findViewById(R.id.medsTakenlistView);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		MainActivity activity = (MainActivity)this.getActivity();
		activity.setPrescribedMedsListView(prescribedMedsListView);
		super.onActivityCreated(savedInstanceState);
	}

}
