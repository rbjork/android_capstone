package com.androidcapstone.symptommanagementdoctor.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public class SMSchema {
	public static final String ORGANIZATIONAL_NAME = "com.bjorkcomputing";
	public static final String PROJECT_NAME = "symptommanagement";
	public static final String DATABASE_NAME = "symptommanagement.db";
	public static final int DATABASE_VERSION = 1;
	public static final String AUTHORITY = ORGANIZATIONAL_NAME + "." + PROJECT_NAME + ".smcontentprovider";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	
	public static final UriMatcher URI_MATCHER = buildUriMatcher();

    // register identifying URIs for Restaurant entity
    // the TOKEN value is associated with each URI registered
    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
      
        matcher.addURI(AUTHORITY, Patient.PATH, Patient.PATH_TOKEN);
        matcher.addURI(AUTHORITY, Patient.PATH_FOR_ID, Patient.PATH_FOR_ID_TOKEN);
        
//        matcher.addURI(AUTHORITY, Prescription.PATH, Prescription.PATH_TOKEN);
//        matcher.addURI(AUTHORITY, Prescription.PATH_FOR_ID, Prescription.PATH_FOR_ID_TOKEN);
//        
//        matcher.addURI(AUTHORITY, Checkin.PATH, Checkin.PATH_TOKEN);
//        matcher.addURI(AUTHORITY, Checkin.PATH_FOR_ID, Checkin.PATH_FOR_ID_TOKEN);
       
        return matcher;

    }
	
	public static class Patient{
		public static final String TABLE_NAME = "patient_table";

        // define a URI paths to access entity
        // BASE_URI/story - for list of story(s)
        // BASE_URI/story/* - retrieve specific story by id
        // the token value are used to register path in matcher (see above)
        public static final String PATH = "patient";
        public static final int PATH_TOKEN = 110;
        public static final String PATH_FOR_ID = "patient/*";
        public static final int PATH_FOR_ID_TOKEN = 120;

        // URI for all content stored as Restaurant entity
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH).build();
        public static final String CONTENT_TOPIC = "topic/com.bjorkcomputing.symptommanagement.patient";
        private final static String MIME_TYPE_END = "patient";

        // define the MIME type of data in the content provider
        public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME
                + ".cursor.dir/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
        public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME
                + ".cursor.item/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

        // the names and order of ALL columns, including internal use ones
        public static final String[] ALL_COLUMN_NAMES = {
                Cols.ID,
                Cols.LOGIN_ID, Cols.PATIENT_ID, Cols.FIRSTNAME, Cols.LASTNAME,
                Cols.DOB, Cols.MRN
        };
        public static class Cols {
            public static final String ID = BaseColumns._ID; // convention
            // The name and column index of each column in your database
            // ST:getColumnDeclaration:inline
            public static final String LOGIN_ID = "LOGIN_ID";
            public static final String PATIENT_ID = "STORY_ID";
          
            public static final String FIRSTNAME = "BODY";
            public static final String LASTNAME = "AUDIO_LINK";
            public static final String DOB = "VIDEO_LINK";
            public static final String MRN = "IMAGE_NAME";
           // public static final String TAGS = "TAGS";
            // ST:getColumnDeclaration:complete
        }


	}
	
	public static class Prescription{
		

	}
	
	public static class Checkin{
		


	}
}
