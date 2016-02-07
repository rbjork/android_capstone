package com.androidcapstone.symptommanagementpatient.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.androidcapstone.symptommanagementpatient.MainActivity;
import com.androidcapstone.symptommanagementpatient.R;

import android.net.Uri;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MediaFragment extends Fragment {
	
	
	
	private ImageView imageView;
	private Button cameraButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_meda,container,false);
		imageView = (ImageView)view.findViewById(R.id.throatimage);
		cameraButton = (Button)view.findViewById(R.id.camerabutton);
		
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).takePicture();
			}
		});
		return view;
	}
	
	public void displayImage(Bitmap img){
		imageView.setImageBitmap(img);
	}
	
	
	
	

}
