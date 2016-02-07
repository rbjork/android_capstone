package com.androidcapstone.symptommanagementdoctor.provider;



import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SMContentProvider extends ContentProvider {
    private final static String LOG_TAG = SMContentProvider.class.getCanonicalName();
    
    
    public final static Uri PATIENT_CONTENT_URI = SMSchema.Patient.CONTENT_URI;
    public static final int PATIENT_ALL_ROWS = SMSchema.Patient.PATH_TOKEN;
    public static final int PATIENT_SINGLE_ROW = SMSchema.Patient.PATH_FOR_ID_TOKEN;

    private static final UriMatcher uriMatcher = SMSchema.URI_MATCHER;
    
	private SMDataDBAdapter mDB;
	
	@Override
	synchronized public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "onCreate()");
        mDB = new SMDataDBAdapter(getContext());
        mDB.open();
		return true;
	}

	@Override
	synchronized public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		switch(uriMatcher.match(uri)){
			case PATIENT_ALL_ROWS:
				return query(uri,projection,SMSchema.Patient.TABLE_NAME,selection,selectionArgs,sortOrder);
//			case CHECKIN_ALL_ROWS:
//			break;
//			break PRESCRIPTIONS_ALL_ROWS:
//			break;
			default:
	            throw new UnsupportedOperationException("URI " + uri
	                    + " is not supported.");
	   }
	}
	
	private Cursor query(Uri uri, String[] projection,final String tableName, String selection,
			String[] selectionArgs, String sortOrder){
		return mDB.query(tableName, projection, selection, selectionArgs, sortOrder);
	}

	@Override
	synchronized public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType()");
        switch (uriMatcher.match(uri)) {

        // ST:createContentTypeReturnsforRelations:begin
        case PATIENT_ALL_ROWS:
        	return SMSchema.Patient.CONTENT_TYPE_DIR;
        case PATIENT_SINGLE_ROW:
        	return SMSchema.Patient.CONTENT_ITEM_TYPE;
        default:
            throw new UnsupportedOperationException("URI " + uri
                    + " is not supported.");
        }
	
	}

	@Override
	public Uri insert(Uri uri, ContentValues assignedValues) {
		// TODO Auto-generated method stub
		final int match = uriMatcher.match(uri);
		switch(match){
			case PATIENT_ALL_ROWS:
				//ContentValues values = SMSchema.Patient.initializeWithDefaults...  // will assume all values present
				final long rowID = mDB.insert(SMSchema.Patient.TABLE_NAME, assignedValues);
	            if (rowID < 0) {
	                Log.d(LOG_TAG, "query()");
	                return null;
	            }
	            final Uri insertedID = ContentUris.withAppendedId(PATIENT_CONTENT_URI, rowID);
	            getContext().getContentResolver().notifyChange(insertedID, null);
	            return ContentUris.withAppendedId(PATIENT_CONTENT_URI, rowID);
		}
		return null;
	}

	@Override
	synchronized public int delete(Uri uri, String whereClause,
            String[] whereArgs) {

        switch (uriMatcher.match(uri)) {
        // ST:createDeleteforRelations:begin
        case PATIENT_SINGLE_ROW:
            whereClause = whereClause + SMSchema.Patient.Cols.ID + " = "
                    + uri.getLastPathSegment();
            // no break here on purpose
        case PATIENT_ALL_ROWS: {
            return deleteAndNotify(uri, SMSchema.Patient.TABLE_NAME,
                    whereClause, whereArgs);
        }
       
        // ST:createDeleteforRelations:finish

        default:
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }
	
	private int deleteAndNotify(final Uri uri, final String tableName,
            final String whereClause, final String[] whereArgs) {
        int count = mDB.delete(tableName, whereClause, whereArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
