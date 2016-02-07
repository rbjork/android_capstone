package com.androidcapstone.symptommanagementpatient.services;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;




import com.androidcapstone.symptommanagementpatient.LoginActivity;
import com.androidcapstone.symptommanagementpatient.interfaces.SymptomManagementSvcApi;

public class SymptomManagementSvc {
	//http://10.0.2.2:8080/patient/checkin/1
	
	public static String username = "";
	public static String password = "";
	
	public static String getTestUrl(){
		return TEST_URL;
	}
	
	private static final String CLIENT_ID = "mobile";
	private static SymptomManagementSvcApi Svc;
	
	public static String TEST_URL = "http://10.0.0.5:8080"; // is the server when testing
	
//	public static synchronized SymptomManagementSvcApi init(String server){
//		Svc = new RestAdapter.Builder()
//		.setEndpoint(server)
//		.setLogLevel(LogLevel.FULL).build()
//		.create(SymptomManagementSvcApi.class);
//		return Svc;
//	}
	
//	public static SymptomManagementSvcApi init(String server){
//		
//		Svc = new SecuredRestBuilder()
//		.setLoginEndpoint(TEST_SECURE_URL + SymptomManagementSvcApi.TOKEN_PATH )
//		.setUsername(SymptomManagementSvc.username)
//		.setPassword(SymptomManagementSvc.password)
//		.setClientId(CLIENT_ID)
//		.setEndpoint(server)
//		.build()
//		.create(SymptomManagementSvcApi.class);
//		return Svc;
//	}
	
	private static String USERNAME = "admin";
	private static String PASSWORD = "pass";
	//private final String CLIENT_ID = "mobile";
	private final String READ_ONLY_CLIENT_ID = "mobileReader";
	// FROM UNIT TEST of secure
//	private SymptomManagementSvcApi symptomManagementService = new SecuredRestBuilder()
//	.setLoginEndpoint(TEST_URL + SymptomManagementSvcApi.TOKEN_PATH)
//	.setUsername(USERNAME)
//	.setPassword(PASSWORD)
//	.setClientId(CLIENT_ID)
//	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
//	.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
//	.create(SymptomManagementSvcApi.class);
	
	// match above but in function and Easy Client
	public static synchronized SymptomManagementSvcApi init(String server){
		
		Svc = new SecuredRestBuilder()
		.setLoginEndpoint(TEST_SECURE_URL + SymptomManagementSvcApi.TOKEN_PATH)
		.setUsername(SymptomManagementSvc.username)
		.setPassword(SymptomManagementSvc.password)
		.setClientId(CLIENT_ID)
		.setClient(new ApacheClient(new EasyHttpClient()))
		.setEndpoint(TEST_SECURE_URL).setLogLevel(LogLevel.FULL).build()
		.create(SymptomManagementSvcApi.class);
		return Svc;
	}
	
	// SECURE CONNECTION BELOW:
	
	public static String TEST_SECURE_URL = "https://10.0.0.5:8443";
	
	public static SymptomManagementSvcApi initSecure(String server, String user, String pass){
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
