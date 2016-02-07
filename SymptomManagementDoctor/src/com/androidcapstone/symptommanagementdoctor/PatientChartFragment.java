package com.androidcapstone.symptommanagementdoctor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.androidcapstone.symptommanagement.repository.Checkin;



import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PatientChartFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LinearLayout v = (LinearLayout)inflater.inflate(R.layout.patientchart_fragment, container, false);
		PatientDetailsActivity act = (PatientDetailsActivity)getActivity();
		ChartView cv = new ChartView(act,act.checkinlist);
		v.addView(cv);
		return v;
	}
	
	protected class ChartView extends View {
		private final DisplayMetrics mDisplay;
		private final int mDisplayWidth, mDisplayHeight;
		private ArrayList<Checkin> checkins;
		public ChartView(Context context, ArrayList<Checkin> chkins) {
			super(context);
			// TODO Auto-generated constructor stub
			checkins = chkins;
			mDisplay = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(mDisplay);
			mDisplayWidth = mDisplay.widthPixels;
			mDisplayHeight = mDisplay.heightPixels;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			if(checkins == null || checkins.size() == 0)return;
			
			Paint linepaint = new Paint();
			linepaint.setARGB(255, 255, 128, 0);
			Paint textpaint = new Paint();
			textpaint.setARGB(255, 255, 255, 0);
			textpaint.setTextSize(30);

			SimpleDateFormat format = new SimpleDateFormat("EEE h:mm a");

			int len = checkins.size();
			
			Checkin c;
			float dHt = mDisplayHeight/5;
			float dWt = mDisplayWidth/(len+1);
			
			int left = 220;
			int top = 10;
			int right = mDisplayWidth-10;
			int bottom = mDisplayHeight-10;
			int painnum = 0;
			int lastpainnum = 0;
			c = checkins.get(0);
			String pain = c.getPainLevel();
			if(pain == "severe")lastpainnum = 1;
			else if(pain == "moderate")lastpainnum = 2;
			else lastpainnum = 3;
			canvas.drawCircle(left, lastpainnum*dHt, 10f, linepaint);
			canvas.drawText(format.format(c.getCheckinTime()), left-80, 3*dHt+120, textpaint);
			for(int i = 1; i< len; i++){
				c = checkins.get(i);
				pain = c.getPainLevel();
				if(pain.equals("severe"))painnum = 1;
				else if(pain.equals("moderate"))painnum = 2;
				else painnum = 3;
				canvas.drawLine((i-1)*dWt+left, lastpainnum*dHt, i*dWt+left, painnum*dHt, linepaint);
				canvas.drawCircle(i*dWt+left, painnum*dHt, 10f, linepaint);
				canvas.drawText(format.format(c.getCheckinTime()), i*dWt+left-80, 3*dHt+120, textpaint);
				lastpainnum = painnum;
			}
			
			canvas.drawText("MILD", 1, 3*dHt, textpaint);
			canvas.drawText("MODERATE", 1, 2*dHt, textpaint);
			canvas.drawText("SEVERE", 1, 1*dHt, textpaint);
			
			
		}

	}
	
}
