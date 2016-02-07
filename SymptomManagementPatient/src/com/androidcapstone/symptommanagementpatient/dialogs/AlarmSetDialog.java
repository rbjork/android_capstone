package com.androidcapstone.symptommanagementpatient.dialogs;

import java.util.Calendar;

import com.androidcapstone.symptommanagementpatient.AlarmSetActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class AlarmSetDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private AlarmSetActivity alarmSetActivity;
	 @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
	 
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		alarmSetActivity = (AlarmSetActivity)activity;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		alarmSetActivity.setAlarmTime(position, hourOfDay, minute);
	}
	public int position;
}
