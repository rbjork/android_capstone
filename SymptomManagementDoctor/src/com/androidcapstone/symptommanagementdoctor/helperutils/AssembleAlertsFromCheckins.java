package com.androidcapstone.symptommanagementdoctor.helperutils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import android.os.Bundle;

import com.androidcapstone.symptommanagement.repository.Checkin;
import com.androidcapstone.symptommanagementdoctor.data.PatientAlert;
import com.google.common.collect.Lists;

public class AssembleAlertsFromCheckins {
	public static Bundle getAlerts(ArrayList<Checkin> ckns){
		Calendar cal = Calendar.getInstance();
		long now = cal.getTimeInMillis();
		long twelveHoursAgo = 12*60*60*1000;
		long sixteenHoursAgo = 16*60*60*1000;
		
		//ArrayList<Checkin> ckns = Lists.newArrayList(getRecentSeriousCheckins(1L));
		//ArrayList<Checkin> ckns = Lists.newArrayList(serverApi.getRecentSeriousCheckins(1L));
		
		if(ckns == null || ckns.size() == 0)return null;
		Collections.sort(ckns, new Comparator<Checkin>() {
	        @Override
	        public int compare(Checkin  ck1, Checkin  ck2)
	        {
	            return  (int) (ck1.getPatientID() - ck2.getPatientID());
	        }
	    });
		
		long lastid = -1;
		long min = 24*60*60*1000;
		long max = 0;
		Bundle alertdata = new Bundle();
		int p = 1;
		
		// 12 hour severe pain filter
		boolean continueToNextPatient = false;
		Checkin lastc = null;
		for(int i=0;i<ckns.size(); i++){
		
			Checkin c = ckns.get(i);
			if(c.getPainLevel().equals("severe") == false && c.getCheckinTime() > (now - twelveHoursAgo)){
				continueToNextPatient = true;
			}
			long id = c.getPatientID();
			if(i == 0){
				lastid = id;
				lastc = c;
			}
			if(id == lastid){
				if(continueToNextPatient)continue;
				min = Math.min(min, c.getCheckinTime());
				max = Math.max(max, c.getCheckinTime());
				
			}else{
				if(i>0)continueToNextPatient = false;
				if(max - min >= twelveHoursAgo){
					PatientAlert.addToBundle(alertdata, lastc, p);
					p++;
				}
			}
			if(id == lastid && i == ckns.size()-1 && continueToNextPatient == false){
				if(max - min >= twelveHoursAgo){
					PatientAlert.addToBundle(alertdata, lastc, p);
					p++;
				}
			}
			if(id != lastid){
				min = c.getCheckinTime();
				max = c.getCheckinTime();
			}
			lastid = id;
			lastc = c;
		}
		
		// 16 hour severe or moderate pain filter
		lastc = null;
		continueToNextPatient = false;
		for(int i=0;i<ckns.size(); i++){
			Checkin c = ckns.get(i);
			if(c.getPainLevel().equals("mild") == true && c.getCheckinTime() > (now - sixteenHoursAgo)){
				continueToNextPatient = true;
			}
			long id = c.getPatientID();
			if(i == 0){
				lastid = id;
				lastc = c;
			}
			if(id == lastid){
				if(continueToNextPatient)continue;
				min = Math.min(min, c.getCheckinTime());
				max = Math.max(max, c.getCheckinTime());
			}else{
				if(i>0)continueToNextPatient = false;
				if(max - min >= sixteenHoursAgo){
					PatientAlert.addToBundle(alertdata, lastc, p);
					p++;
				}
			}
			if(id == lastid && i == ckns.size()-1 && continueToNextPatient == false){
				if(max - min >= sixteenHoursAgo){
					PatientAlert.addToBundle(alertdata, lastc, p);
					p++;
				}
			}
			if(id != lastid){
				min = c.getCheckinTime();
				max = c.getCheckinTime();
			}
			lastid = id;
			lastc = c;
		}
		
		
		// 12 hour cannot eat or drink
		lastc = null;
		continueToNextPatient = false;
		for(int i=0;i<ckns.size(); i++){
			Checkin c = ckns.get(i);
			if(c.isCanEatDrink() == false && c.getCheckinTime() >= (now - twelveHoursAgo)){
				continueToNextPatient = true;
			}
			long id = c.getPatientID();
			if(i == 0){
				lastid = id;
				lastc = c;
			}
			if(id == lastid){
				if(continueToNextPatient)continue;
				min = Math.min(min, c.getCheckinTime());
				max = Math.max(max, c.getCheckinTime());
			}else{
				if(i>0)continueToNextPatient = false;
				if(max - min >= twelveHoursAgo){
					PatientAlert.addToBundle(alertdata, lastc, p);
					p++;
				}
			}
			if(id == lastid && i == ckns.size()-1 && continueToNextPatient == false){
				if(max - min >= twelveHoursAgo){
					PatientAlert.addToBundle(alertdata, lastc, p);
					p++;
				}
			}
			if(id != lastid){
				min = c.getCheckinTime();
				max = c.getCheckinTime();
			}
			lastid = id;
			lastc = c;
		}
		alertdata.putInt(PatientAlert.NUMBER_OF_ALERTS, p-1);
		return alertdata;
	}
	
}
