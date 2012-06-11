package com.jeannius.tallycap.Reminders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannius.tallycap.R;
import com.jeannius.tallycap.Calendar.CalendarFull;
import com.jeannius.tallycap.Calendar.calendarGridItem;
import com.jeannius.tallycap.Calendar.calendarGridWeek;

public class CalendarPopUp extends Dialog {
	
	
	private Context c;
	public static CalendarFull grid;
	private LinearLayout lin;
	
	public static TextView text;
	
	private CalendarPopUp me;
	public Button cancel, okButton;
	private Button resetButton;
	private static TextView dateSelectedTextView;
	public static List<Date> datage;
	public static int nu, maxi;
	private Date dd;
	private boolean bool;
	
	public CalendarPopUp(Context context, int Max, String title, Date dat, Boolean dateSet) {
		super(context);
		c= context;
		
		setContentView(R.layout.reminder_date_chooser);
		maxi =Max;
		setTitle(title);
		dd = dat;
		bool = dateSet;
		setup();
	}
	
	
	
	
	
	private void setup() {
		lin = new LinearLayout(c);
		text = new TextView(c);
		cancel = new Button(c);
		resetButton = new Button(c);
		okButton = new Button(c);
		dateSelectedTextView = new TextView(c);
		datage = new ArrayList<Date>();
		datage.clear();
		nu=0;
		me = this;
		cancel = (Button) this.findViewById(R.id.reminderChooseDateCancelButton);
		resetButton = (Button) this.findViewById(R.id.reminderChooseDateResetButton);
		dateSelectedTextView = (TextView) this.findViewById(R.id.reminderChooseDateSelectedTextView);
		okButton = (Button) this.findViewById(R.id.reminderChooseDateOkButton);
		lin = (LinearLayout) findViewById(R.id.reminderChooseDateLinearLayout);
		grid = new CalendarFull(c, true, false, dd, bool);
		grid.mainLinearLayout.setBackgroundColor(Color.BLACK);
		lin.addView(grid);
		lin.addView(text);
		grid.setOnClickListener(test);
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				me.dismiss();
				
			}
		});
		
		resetButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nu=0;
				dateSelectedTextView.setText("");
				grid.myCalendarGridMonth.reset();
			}
		});
	}
	
	
	//testing clicks
	private View.OnClickListener  test = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	
//	//this function add an item to total number of date selected
//	public void itemAdd(){
//		nu++;
//	
//
//	}
//	
//	
//	//this function subtract an item to total number of date selected
//	public void itemSubtract(){
//		nu--;
//		
//	}
//	
	
	
	//this function collects all the selected dates (assuming no errors because checking was done prior) and puts them in a list
	public static List<Date> selectation(){
		
		datage.clear();
		
		for(int w=1; w<7; w++){
			
		calendarGridWeek week=	(calendarGridWeek) grid.myCalendarGridMonth.getChildAt(w);
			for(int i=0; i<7; i++){
				calendarGridItem item = (calendarGridItem) week.getChildAt(i);
				if(item.selectedAlready) datage.add(item.getDate());
			}
			
			
		}
		
		return datage;	
		
	}
	
	public static void showDateSelected(){
		
		String s="";
		List<Date> j = selectation();
		String Date_Format = "MMM dd, yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Format);
		if(j.size()==1) s= String.format("Selected Date: %s", dateFormat.format(j.get(0)));
		else if(j.size()==2) s= String.format("Selected Date: %s\t&\t%s", dateFormat.format(j.get(0)), dateFormat.format(j.get(1)));
		dateSelectedTextView.setText(s);
	}
	
	
	
	
}




