package com.androidcapstone.symptommanagementdoctor.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class SMDataDBAdapter {
	private static final String LOG_TAG = SMDataDBAdapter.class.getCanonicalName();
    private static final String DATABASE_NAME = "SMDatabase.db";
    private static final String Patient_KEY_ID = SMSchema.Patient.Cols.ID;
    private static final String Patient_LoginId = SMSchema.Patient.Cols.LOGIN_ID;
    private static final String Patient_FirstName = SMSchema.Patient.Cols.FIRSTNAME;
    private static final String Patient_LastName = SMSchema.Patient.Cols.LASTNAME;
    private static final String Patient_MRN = SMSchema.Patient.Cols.MRN;
    private static final String Patient_DOB = SMSchema.Patient.Cols.DOB;
    
    static final String DATABASE_TABLE_PATIENTS = SMSchema.Patient.TABLE_NAME;
   // static final String DATABASE_TABLE_TAGS = MoocSchema.Tags.TABLE_NAME;
    
    static final int DATABASE_VERSION = 1;
    
    private static final String DATABASE_CREATE_PATIENTS = "create table "
            + DATABASE_TABLE_PATIENTS + " (" // start table
            + Patient_KEY_ID + " integer primary key autoincrement, " // setup
            + Patient_LoginId + " INTEGER ," //
            + Patient_FirstName + " TEXT ," //
            + Patient_LastName + " TEXT ," //
            + Patient_MRN + " TEXT ," //
            + Patient_DOB + " INTEGER  " //

            + " );"; // end table
    
    // Variable to hold the database instance.
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private myDbHelper dbHelper;
    // if the DB is in memory or to file.
    private boolean MEMORY_ONLY_DB = false;
    
    public SMDataDBAdapter(Context _context){
    	context = _context;
    }
    
    public SMDataDBAdapter open() throws SQLException {
        Log.d(LOG_TAG, "open()");
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException ex) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }
    
    public Cursor query(final String table, final String[] projection,
            final String selection, final String[] selectionArgs,
            final String sortOrder) {
        return  db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
    }
    
    public long insert(final String table, final ContentValues cv) {
        Log.d(LOG_TAG, "insert(CV)");
        return db.insert(table, null, cv);
    }
    
    public int update(final String table, final ContentValues values,
            final String whereClause, final String[] whereArgs) {
        return db.update(table, values, whereClause, whereArgs);
    }
    
    public int delete(final String table, final String whereClause,
            final String[] whereArgs) {
        Log.d(LOG_TAG, "delete(" + whereClause + ") ");
        return db.delete(table, whereClause, whereArgs);
    }
    
    private static class myDbHelper extends SQLiteOpenHelper {

        public myDbHelper(Context context, String name, CursorFactory factory,
                int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "DATABASE_CREATE: version: " + DATABASE_VERSION);
            db.execSQL(DATABASE_CREATE_PATIENTS);
         //   db.execSQL(DATABASE_CREATE_PRESCRIPTIONS);
         //   db.execSQL(DATABASE_CREATE_CHECKINS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PATIENTS);
            onCreate(db);
        }

    }
}
