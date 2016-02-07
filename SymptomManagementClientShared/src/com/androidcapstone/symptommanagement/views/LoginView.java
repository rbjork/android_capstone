package com.androidcapstone.symptommanagement.views;



import com.androidcapstone.symptommanagementrepository.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class LoginView extends Fragment {
	
	private EditText unTextView;
	private EditText pTextView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.login_fragment, container,false);
		unTextView = (EditText)v.findViewById(R.id.unTextView);
		pTextView = (EditText)v.findViewById(R.id.pTextView);
		return v;
	}
	
	public String getUserName(){
		return unTextView.getText().toString();
	}
	
	public String getPassword(){
		return pTextView.getText().toString();
	}
}
