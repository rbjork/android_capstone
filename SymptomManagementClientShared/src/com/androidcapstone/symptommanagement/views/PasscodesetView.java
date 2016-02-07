package com.androidcapstone.symptommanagement.views;

import com.androidcapstone.symptommanagementrepository.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class PasscodesetView extends Fragment {
	private EditText passcodeText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.passcodeset_fragment, container, false);
		passcodeText = (EditText)layout.findViewById(R.id.passcodeSetTextView);
		return layout;
	}
	
	public void updatePasscodeText(String text){
		passcodeText.setText(text);
	}

}
