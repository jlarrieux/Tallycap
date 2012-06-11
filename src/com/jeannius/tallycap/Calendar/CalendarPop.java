package com.jeannius.tallycap.Calendar;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.jeannius.tallycap.R;

public class CalendarPop extends Dialog {
	
	public calendarGridItem grid;
	public List<CalendarObject> ob1;
	public String title;
	private TextView total;
	private LinearLayout lay;
	private Context c;
	SimpleDateFormat d= new SimpleDateFormat("EEEE MMMM dd, yyyy");
	
	public CalendarPop(Context context, calendarGridItem g) {
		super(context);
		setContentView(R.layout.calendar_pop);
		c= context;
		grid = g;
		ob1 = grid.ob;
		setTitle(d.format(grid.getDate()));
		title = d.format(grid.getDate());
		setter();
	}

	public CalendarPop(Context context, List<CalendarObject> h, String ti){
		super(context);
		setContentView(R.layout.calendar_pop);
		c= context;
		
		ob1 = h;
		setTitle(ti);
		title = ti;
//		if(h!=null)Toast.makeText(c, String.valueOf(h.size()), Toast.LENGTH_LONG).show();
//		else Toast.makeText(c,"H is null", Toast.LENGTH_LONG).show();
		setter();
		
		GoogleAnalyticsTracker localtracker = GoogleAnalyticsTracker.getInstance();		
		localtracker.startNewSession(String.valueOf(R.string.ga_api_key), context);
		localtracker.trackEvent("ui_interaction", "calendar_item_pop_click", "CalendarActivity", 0);
		localtracker.dispatch();
		
	}
		
	private void setter(){
		total = new TextView(c);
		lay = new LinearLayout(c);
		
		
		total = (TextView) this.findViewById(R.id.calendarPopTotalText);
		lay = (LinearLayout) this.findViewById(R.id.calendarPopLinearLayout);
		
		populator();
		totaler();
	}

	//this method computes the total
	private void totaler() {
		double z=0.0;
		
		for(int i=0; i<lay.getChildCount(); i++){
			
			CalendarPopItem it = (CalendarPopItem) lay.getChildAt(i);
			z+= it.getAmount();			

		}
		
		total.setText(String.valueOf(z));
		if(z<0)total.setTextColor(Color.RED);
		else total.setTextColor(Color.GREEN);
	}

	//this methods add the items to the list 
	private void populator() {
		LinearLayout.LayoutParams l = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		l.setMargins(0, 0, 0, 5);
		
		for(int i=0; i<ob1.size(); i++){
			lay.addView(new CalendarPopItem(c, ob1.get(i)), l);
		}
		
	}
	
	
	
	
	
	
}
