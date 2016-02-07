package com.androidcapstone.symptommanagementdoctor;

import java.util.ArrayList;

import com.androidcapstone.symptommanagement.repository.Medication;
import com.androidcapstone.symptommanagement.repository.MedicationTaken;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class PatientPrescriptionsFragment extends Fragment {
	private ListView medlist;
	private PatientDetailsActivity pa;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		RelativeLayout llo = (RelativeLayout)inflater.inflate(R.layout.patientprescriptions_fragment, container, false);
		medlist = (ListView)llo.findViewById(R.id.medslist);
		medlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				MedicationChoice mc = pa.medchoices.get(position);
				mc.setPrescribed(!mc.isPrescribed());
				pa.medlistAdapter.notifyDataSetChanged();
			}
		});
		return llo;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		pa = (PatientDetailsActivity)getActivity();
		medlist.setAdapter(pa.medlistAdapter);
	}

}
