package com.androidcapstone.symptommanagementdoctor.orm;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.androidcapstone.symptommanagementdoctor.provider.SMSchema;


import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public class SMContentResolver {
	private ContentResolver cr;

	private Uri patientContentURI = SMSchema.Patient.CONTENT_URI;
	//private Uri tagsURI = SMSchema.Tags.CONTENT_URI;

	/**
	 * Constructor
	 * 
	 * @param activity
	 *            The Activity to get the ContentResolver from.
	 */
	public SMContentResolver(Activity activity) {
		cr = activity.getContentResolver();
	}

	/**
	 * ApplyBatch, simple pass-through to the ContentResolver implementation.
	 * 
	 * @param operations
	 * @return array of ContentProviderResult
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	public ContentProviderResult[] applyBatch(
			final ArrayList<ContentProviderOperation> operations)
			throws RemoteException, OperationApplicationException {
		return cr.applyBatch(SMSchema.AUTHORITY, operations);
	}
	
	/**
	 * Insert a group of PatientData all at once. Mainly useful for use on
	 * installation/first boot of an application. Allowing setup of the Database
	 * into a 'start state'
	 * 
	 * @param data
	 *            what is to be inserted into the ContentProvider
	 * @return number of rows inserted
	 * @throws RemoteException
	 */
	
	public int bulkInsertPatient(final ArrayList<PatientData> data)
			throws RemoteException {
		ContentValues[] values = new ContentValues[data.size()];
		int index = 0;
		for (PatientData patient : data) {
			values[index] = patient.getCV();
			++index;
		}
		return cr.bulkInsert(patientContentURI, values);
	}
	
	public int deletePatientData(final String selection,
			final String[] selectionArgs) throws RemoteException {
		return cr.delete(patientContentURI, selection, selectionArgs);
	}
	
	public String getType(Uri uri) throws RemoteException {
		return cr.getType(uri);
	}
	
	/**
	 * Insert a new PatientData object into the ContentProvider
	 * 
	 * @param patientObject
	 *            object to be inserted
	 * @return URI of inserted PatientData in the ContentProvider
	 * @throws RemoteException
	 */
	public Uri insert(final PatientData patientObject) throws RemoteException {
		ContentValues tempCV = patientObject.getCV();
		tempCV.remove(SMSchema.Patient.Cols.ID);
		return cr.insert(patientContentURI, tempCV);
	}
	
	/**
	 * Access files from the content provider, getting a AssetFileDescriptor
	 * 
	 * @param uri
	 * @param mode
	 * @return
	 * @throws RemoteException
	 * @throws FileNotFoundException
	 */
	public ParcelFileDescriptor openFileDescriptor(final Uri uri,
			final String mode) throws RemoteException, FileNotFoundException {
		return cr.openFileDescriptor(uri, mode);
	}
	
	
	/**
	 * Query for each PatientData, Similar to standard Content Provider query,
	 * just different return type
	 * 
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return an ArrayList of PatientData objects
	 * @throws RemoteException
	 */
	public ArrayList<PatientData> queryPatientData(final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) throws RemoteException {
		// query the C.P.
		Cursor result = cr.query(patientContentURI, projection, selection,
				selectionArgs, sortOrder);
		// make return object
		ArrayList<PatientData> rValue = new ArrayList<PatientData>();
		// convert cursor to reutrn object
		rValue.addAll(PatientCreator.getPatientDataArrayListFromCursor(result));
		result.close();
		// return 'return object'
		return rValue;
	}
}
