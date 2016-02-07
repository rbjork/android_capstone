package com.androidcapstone.symptommanagementdoctor.services;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;

import com.androidcapstone.symptommanagementdoctor.interfaces.SymptomManagementSvcApi;
import com.androidcapstone.symptommanagementdoctor.LoginActivity;
import com.androidcapstone.symptommanagementdoctor.services.SecuredRestBuilder;


public class SymptomManagementSvc {
	
	public static String username = ""; // set by loginactivity when login successful note the service retains so not ideal
	public static String password = ""; // deliberate logout should reset to blank
	
	protected static String TEST_URL = "http://10.0.0.5:8080"; // is the server when testing
	private static final String CLIENT_ID = "mobile";
	
	public static String getTestUrl(){
		return TEST_URL;
	}
	
	private static SymptomManagementSvcApi Svc;
	
// Comment out code below and uncomment code following this
//	public static synchronized SymptomManagementSvcApi init(String server){
//		Svc = new RestAdapter.Builder()
//		.setEndpoint(server)
//		.setLogLevel(LogLevel.FULL).build()
//		.create(SymptomManagementSvcApi.class);
//		return Svc;
//	}
	
   // Comment out above instead for secure
	public static synchronized SymptomManagementSvcApi init(String server){
		Svc = new SecuredRestBuilder()
		.setLoginEndpoint(TEST_SECURE_URL + SymptomManagementSvcApi.TOKEN_PATH )
		.setUsername(SymptomManagementSvc.username)
		.setPassword(SymptomManagementSvc.password)
		.setClientId(CLIENT_ID)
		.setClient(new ApacheClient(new EasyHttpClient()))
		.setEndpoint(TEST_SECURE_URL)
		.build()
		.create(SymptomManagementSvcApi.class);
		return Svc;
	}
	
	// SECURE CONNECTION BELOW:
	
	public static String TEST_SECURE_URL = "https://10.0.0.5:8443";
	
	public static synchronized SymptomManagementSvcApi initSecure(String server, String user, String pass){
		Svc = new SecuredRestBuilder()
		.setLoginEndpoint(server + SymptomManagementSvcApi.TOKEN_PATH )
		.setUsername(user)
		.setPassword(pass)
		.setClientId(CLIENT_ID)
		.setClient(new ApacheClient(new EasyHttpClient()))
		.setEndpoint(server)
		.build()
		.create(SymptomManagementSvcApi.class);
		return Svc;
	}
	
	public static synchronized SymptomManagementSvcApi getOrShowLogin(Context ctx){
		if(Svc != null){
			return Svc;
		}else{
			Intent i = new Intent(ctx, LoginActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}
	
//	private SymptomManagementSvcApi symptomManagementService = new RestAdapter.Builder()
//		.setEndpoint(SYMPTOM_MANAGEMENT_URL)
//		.setLogLevel(LogLevel.FULL).build()
//		.create(SymptomManagementSvcApi.class);
}
