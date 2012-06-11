package com.jeannius.tallycap.Calendar;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.jeannius.tallycap.CalendarActivity;
import com.jeannius.tallycap.R;
import com.jeannius.tallycap.Reminders.CalendarPopUp;
public class calendarGridWeek extends LinearLayout {

	private Context context;
	
	public int pos;
	private calendarGridWeek me;
	private Boolean selectable;
	private Boolean popable;
	
	
	public calendarGridWeek(Context context2,  Boolean sel, Boolean pop) {
		super(context2);
		context = context2;
		
		
		setOrientation(LinearLayout.HORIZONTAL);
		me= this;
		pos =0;
		selectable = sel;
		popable = pop;
		setup();
		
	}
	
	private void setup(){
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
		params.setMargins(0, 0	, 1, 1);
		
		
		for(int i=0; i<7; i++){
			
			calendarGridItem calgrid =new calendarGridItem(context, selectable);
			calgrid.setOnClickListener(Say);
			
			addView(calgrid, params);
		}
		
	}
	
	private View.OnClickListener Say = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			pos = me.indexOfChild(v);
			
			if(selectable){
				
				
				//if nu is not at max 
				if( CalendarPopUp.nu<CalendarPopUp.maxi){
					if(((calendarGridItem)v).selectedAlready){
						((calendarGridItem)v).setBackgroundColor(0x60888888);
						((calendarGridItem)v).selectedAlready =false;
						CalendarPopUp.nu--;
						CalendarPopUp.showDateSelected();
//						Toast.makeText(context, String.format("-nu: %d\nmaxi: %d", CalendarPopUp.nu, CalendarPopUp.maxi), Toast.LENGTH_LONG).show();
					}
					else{
						((calendarGridItem)v).setBackgroundColor(0x88FF0000);
						((calendarGridItem)v).selectedAlready =true;
						CalendarPopUp.nu++;
						CalendarPopUp.showDateSelected();
//						Toast.makeText(context, String.format("+nu: %d\nmaxi: %d", CalendarPopUp.nu, CalendarPopUp.maxi), Toast.LENGTH_LONG).show();
					}
					
				}
				else{
					
					if(((calendarGridItem)v).selectedAlready){
						((calendarGridItem)v).setBackgroundColor(0x60888888);
						((calendarGridItem)v).selectedAlready =false;
						CalendarPopUp.nu--;
						CalendarPopUp.showDateSelected();
//						Toast.makeText(context, String.format("-nu: %d\nmaxi: %d", CalendarPopUp.nu, CalendarPopUp.maxi), Toast.LENGTH_LONG).show();
					}
					else{

						GoogleAnalyticsTracker localtracker = GoogleAnalyticsTracker.getInstance();		
						localtracker.startNewSession(String.valueOf(R.string.ga_api_key), context);
						localtracker.trackEvent("reminder_user_error", "CalendarPop", "ReminderActivity", 0);
						localtracker.dispatch();
						Toast.makeText(context, "You have reach the maximum number of selectable items. Please click on a selected item to unselect it before making a new selection.", Toast.LENGTH_LONG).show();
					}
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(((calendarGridItem)v).getDate());
			
				
					
				
				
				if(CalendarPopUp.nu==0) CalendarPopUp.text.setText("");
				
			}
			
			if(popable){


				if(((calendarGridItem)v).ob.size()>0)CalendarActivity.poper((calendarGridItem)v);
			}
		}
	};
	
	
	
	//this function reset the background on all dates in the calendar except today's date
	public void reset(){
		//make sure nu=0 before calling this
		calendarGridItem g;
		Date innerDate;
		Date today = new Date();
		for(int i=0; i< getChildCount(); i++){

			g= (calendarGridItem) getChildAt(i);
//			g.setBackgroundColor(0x60888888);
			g.selectedAlready =false;
			innerDate =g.calendarGridItemDate;
			if(innerDate.getDate()== today.getDate() && innerDate.getMonth() == today.getMonth() &&
					innerDate.getYear()== today.getYear()) g.setBackgroundColor(0X805BA675);
			else g.setBackgroundColor(0x60888888);
		}
	}

}
