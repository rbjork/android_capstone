package com.androidcapstone.symptommanagementpatient.utils;

public class StringUtils {
	public static boolean isEmpty(String str){
		if(str == "")return true;
		if(str == null)return true;
		return false;
	}
	
	public static boolean isNotBlank(String str){
		if(str == "")return false;
		return true;
	}
}
