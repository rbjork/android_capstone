package com.androidcapstone.symptommanagement.server;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import retrofit.http.GET;
import retrofit.http.Path;

//import com.fitbit.api.FitbitAPIException;
//import com.fitbit.api.client.FitbitAPIEntityCache;
//import com.fitbit.api.client.FitbitApiClientAgent;
//import com.fitbit.api.client.FitbitApiCredentialsCache;
//import com.fitbit.api.client.FitbitApiCredentialsCacheMapImpl;
//import com.fitbit.api.client.FitbitApiSubscriptionStorage;
//import com.fitbit.api.client.FitbitApiSubscriptionStorageInMemoryImpl;
//import com.fitbit.api.client.LocalUserDetail;
//import com.fitbit.api.client.service.FitbitAPIClientService;
//import com.fitbit.api.common.model.activities.Activities;
//import com.fitbit.api.common.model.bp.Bp;
//import com.fitbit.api.common.model.heart.Heart;
//import com.fitbit.api.common.model.heart.HeartLog;
//import com.fitbit.api.common.model.timeseries.TimeSeriesResourceType;
//import com.fitbit.api.model.APICollectionType;





@Controller
public class PhysioServiceController  { //implements InitializingBean  {
	
	// @Value ???
	
	protected Log log = LogFactory.getLog(getClass());

	private static final int APP_USER_COOKIE_TTL = 60 * 60 * 24 * 7 * 4;
	private static final String APP_USER_COOKIE_NAME = "exampleClientUid";

    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String OAUTH_VERIFIER = "oauth_verifier";

    private static final String NOTIFICATION_UPDATES_SUBSCRIBER_ID = "1";
    
    
    private LocalDate localdate;
    
//    private LocalUserDetail localuserdetail;
//
//    @Resource
//    private FitbitAPIEntityCache entityCache;
//    
//   // @Autowired
//  //  public FitbitAPIEntityCache entityCache;
//    
//    @Resource
//    private FitbitApiCredentialsCache credentialsCache;
//    
//  //  @Autowired
//   // public FitbitApiCredentialsCache credentialsCache;
//    
//    @Resource
//    private FitbitApiSubscriptionStorage subscriptionStore;
    
   // @Autowired
   // public FitbitApiSubscriptionStorage subscriptionStore;
    
    
    

    @Value("#{config['fitbitSiteBaseUrl']}")
    private String fitbitSiteBaseUrl;
    
    @Value("#{config['apiBaseUrl']}")
    private String apiBaseUrl;
    
    @Value("#{config['exampleBaseUrl']}")
    private String exampleBaseUrl;

    @Value("#{config['clientConsumerKey']}")
    private String clientConsumerKey;
    
    @Value("#{config['clientSecret']}")
    private String clientSecret;
    
//	private FitbitAPIClientService<FitbitApiClientAgent> apiClientService;
//	
//	
//	@Override
//    public void afterPropertiesSet() throws Exception {
//        apiClientService = new FitbitAPIClientService<FitbitApiClientAgent>(
//                new FitbitApiClientAgent(getApiBaseUrl(), getFitbitSiteBaseUrl(), credentialsCache),
//                clientConsumerKey,
//                clientSecret,
//                credentialsCache,
//                entityCache,
//                subscriptionStore
//        );
//    }
//	public String getFitbitSiteBaseUrl() {
//        return fitbitSiteBaseUrl;
//    }
//
//    public String getApiBaseUrl() {
//        return apiBaseUrl;
//    }
//
//    
//    
//	
//	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENT_HEARTRATE + "/{patientID}" ,method=RequestMethod.GET)
//	public HeartLog getPatientHeartRateTimeSeries(@PathVariable("patientID")long id){
//		FitbitApiClientAgent fbc = apiClientService.getClient();
//	//	fbc.getTimeSeries(user, resourceType, startDate, endDate)
//		return null;
//	}
//	
//	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENT_BLOODPRESSURE + "/{patientID}" ,method=RequestMethod.GET)
//	public HeartLog getPatientBloodPressureTimeSeries(@PathVariable("patientID")long id){
//		FitbitApiClientAgent fbc = apiClientService.getClient();
//		return null;
//		
//	}
//	
//	@RequestMapping(value=SymptomManagementSvcApi.DOCTORS_PATIENT_ACTIVITIES + "/{patientID}" ,method=RequestMethod.GET)
//	public Activities getPatientActivities(@PathVariable("patientID")long id) throws FitbitAPIException{
//		return apiClientService.getActivities(localuserdetail, localdate);
//	}

}
