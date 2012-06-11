package com.jeannius.tallycap.Calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannius.tallycap.R;

public class CalendarFull extends LinearLayout {

	
	
	private Date today;
	public TextView monthAndYear;
	private Calendar  tempcal;
	private ImageButton leftButton;
	private ImageButton rightButton;
	public calendarGridMonth myCalendarGridMonth;
	public LinearLayout lin6, mainLinearLayout;

	private Context c;
	private Boolean selectable;
	private Boolean popable;
	public int month=0, year=0;
	
	
	
	public CalendarFull(Context context, Boolean sel, Boolean pop, Date da, Boolean dateSet) {
		super(context);
		c = context;
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate.inflate(R.layout.calendar_gridview, this);
		selectable = sel;
		popable = pop;
		today = new Date();
		if(dateSet)setup(da);
		else setup(today);
	}
	
	public CalendarFull(Context context, AttributeSet attrs){
		super(context, attrs);
		c= context;
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate.inflate(R.layout.calendar_gridview, this);
		selectable = false;
		popable =true;
		today = new Date();
		setup(today);
	}
	
	private void setup(Date thed) {
		lin6 = new LinearLayout(c);
		mainLinearLayout= new LinearLayout(c);
		
		rightButton = new ImageButton(c);
		leftButton = new ImageButton(c);
		
//		width = CalendarActivity.getWidth();
//		height = CalendarActivity.getHeight();
		rightButton = (ImageButton) findViewById(R.id.calendarRightButton);
		leftButton = (ImageButton) findViewById(R.id.calendarLeftButton);
		
		
		monthAndYear = new TextView(c);		
		monthAndYear = (TextView) findViewById(R.id.calendarMonthAndYearTextView);
		lin6 = (LinearLayout) findViewById(R.id.calendarMainLinearLayout);
		mainLinearLayout = (LinearLayout) findViewById(R.id.calendarMainContainerLinearLayout);
		myCalendarGridMonth = new calendarGridMonth(c,  selectable, popable);
		lin6.addView(myCalendarGridMonth);
		
		
		rightButton.setOnClickListener(timeMove);
		leftButton.setOnClickListener(timeMove);
		tempcal = Calendar.getInstance();
		tempcal.setTime(thed);
		populator(thed);
	}



	//this moves the time
	private OnClickListener timeMove = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(v.getId()== R.id.calendarLeftButton)  tempcal.add(Calendar.MONTH, -1);					
					
				else tempcal.add(Calendar.MONTH, 1);					
				
				populator(tempcal.getTime());
				
				
			}
		};

		//this function sets a custom date
		public void setCustomDate(Calendar theDate){
			
			populator(theDate.getTime());
		}
		
		
		
		
		//populate the date
		private void populator(Date theDate){		
			
			Calendar localCal = Calendar.getInstance();
			localCal.setTime(theDate);
			
//			Toast.makeText(c, String.format("INSIDE\n\nMonth: %d\nYear: %d",localCal.get(Calendar.MONTH),localCal.get(Calendar.YEAR)),Toast.LENGTH_LONG).show();
			calendarGridItem gridItem = new calendarGridItem(c, selectable);
			calendarGridWeek gridWeek = new calendarGridWeek(c, selectable, popable);
			Date innerDate = new Date();
			
			TextView innerText = new TextView(c);
			int todayDayOfWeek =localCal.get(Calendar.DAY_OF_WEEK);
			int todayWeekOfMonth = localCal.get(Calendar.WEEK_OF_MONTH);
			int position = 0;
			int todayPosition =7*(todayWeekOfMonth-1)+(todayDayOfWeek-1);
			int red =0;
			
			//String s= today.toString()+"\n"+theDate.toString();
			//Toast.makeText(cont, s, Toast.LENGTH_LONG).show();
			
			for (int j =1; j<7; j++){
				
				for(int k=1;k<8;k++){
					position = 7*(j-1)+k-1;		
					red = position -todayPosition;
					gridWeek = (calendarGridWeek) myCalendarGridMonth.getChildAt(j);
					gridItem= (calendarGridItem) gridWeek.getChildAt(k-1);
//					gridItem.setMinimumHeight(height/10);
					innerText = gridItem.calendarGridItemTextView;
					
					
					localCal.add(Calendar.DAY_OF_MONTH, red);	
					gridItem.setDate(localCal.getTime());
					innerDate = gridItem.calendarGridItemDate;
					
					
					if(popable){
						gridItem.resetInvisible();
						gridItem.ob.clear();
					}
					if(innerDate.getMonth() == theDate.getMonth()) innerText.setTextColor(0xFFE8E595);
					else innerText.setTextColor(0x80F2F2F2);
					if(innerDate.getDate()== today.getDate() && innerDate.getMonth() == today.getMonth() &&
							innerDate.getYear()== today.getYear()) gridItem.setBackgroundColor(0X805BA675);
					else gridItem.setBackgroundColor(0x60888888);
					localCal.setTime(theDate);
					
					
					//remove this
//					if(k==1 && j==1)g2 =gridItem;
					//innerText.setText(String.format("Text: %d\nbutton: %d\nTotal: %d", innerText.getMeasuredHeight(), im.getHeight(), gridItem.getHeight()));
				}
				localCal.setTime(theDate);
			}
			SimpleDateFormat d= new SimpleDateFormat("MMMM yyyy");
			monthAndYear.setText(d.format(tempcal.getTime()));
			month = tempcal.get(Calendar.MONTH);
			year = tempcal.get(Calendar.YEAR);
//			Toast.makeText(c, g2.getMeas(), Toast.LENGTH_LONG).show();
		}
		




}
