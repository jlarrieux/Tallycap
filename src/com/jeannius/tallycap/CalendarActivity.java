package com.jeannius.tallycap;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.jeannius.tallycap.Calendar.CalendarFull;
import com.jeannius.tallycap.Calendar.CalendarObject;
import com.jeannius.tallycap.Calendar.CalendarPop;
import com.jeannius.tallycap.Calendar.calendarGridItem;
import com.jeannius.tallycap.Calendar.calendarGridWeek;
import com.jeannius.tallycap.Reminders.RemindersElement;
import com.jeannius.tallycap.admin.MyDbOpenHelper;

public class CalendarActivity extends TrackedActivity {

	
	private Context c;
	public static CalendarPop p;
	private CalendarFull full;
	private static Context c2;
	private List<CalendarObject> zz;
//	private FileOutputStream f;
	private Object[] lll;
	private static int h,w;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_main);
		c =CalendarActivity.this.getApplicationContext();
		
		Date dd = new Date();
		full = new CalendarFull(c, false, true, dd, false);
		full = (CalendarFull) findViewById(R.id.calendarCalendarFull);
		h =height;
		w =width;
		
		
		
		c2= CalendarActivity.this;
		full.monthAndYear.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				populator();
			}
		});
		populator();
		
		if(savedInstanceState!=null)restoreMe(savedInstanceState);
		
	}
	
	//this function restores the states
	@SuppressWarnings("unchecked")
	private void restoreMe(Bundle b){
		if(b!=null){
			
			
			if(getLastNonConfigurationInstance()!=null){
				
				
					Object[] lll =   (Object[]) getLastNonConfigurationInstance();
//					
					zz = (List<CalendarObject>) lll[0];
					if(zz!=null){
						String ti = (String) lll[1];
						p = new CalendarPop(CalendarActivity.this,  zz,ti);
						p.show();
					
				}
//				else Toast.makeText(c, "does not contain title", Toast.LENGTH_LONG).show();
			}
//			else Toast.makeText(c, "getlast is null", Toast.LENGTH_LONG).show();
		}
//		else Toast.makeText(c, "Bundle is null", Toast.LENGTH_LONG).show();
		
	}
	
	//dismiss the pop up if it exist and it is showing
	@Override
	protected void onPause() {		
		super.onPause();
	
		lll = new Object[2];
		if(p!=null)if(p.isShowing()){
			
			lll[0] = p.ob1;
			lll[1] = p.title;
			p.dismiss(); 
		}
		
	
		
	}
	
	//save this into an object for the configuration change
	@Override
	public Object onRetainNonConfigurationInstance() {		
		
		

		
		return lll;
	}
	
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		
//		super.onSaveInstanceState(outState);
//		if(p!=null){
//			if(p.isShowing()){
//				outState.putString("title", p.title);
//			}
//		}
//		
//	}
	
	// this function populates the calendar with items
	private void populator(){
		
		SQLiteDatabase db = new MyDbOpenHelper(c).getReadableDatabase();
		String select = "SELECT * FROM reminder";
		Cursor res = db.rawQuery(select, null);		
		res.moveToFirst();
		
		int nu = res.getCount();
		
		String name,   frequency, parameter, type;
		double amount;
		long datecreated;
		Calendar today = Calendar.getInstance();
		Calendar dateofit =Calendar.getInstance();
		Date red = new Date();
//		String g="";
		for (int i=0; i<nu; i++){
			name = res.getString(0);
			amount = res.getDouble(1);
			datecreated = res.getLong(2);
			frequency = res.getString(4);
			parameter = res.getString(5);
			type = res.getString(10);
//			g+=String.format("name: %s\t Frequency: %s\t parameter: %s \t type: %s", name, frequency, parameter, type);
			
			for(int j=1; j<7; j++){
							
				calendarGridWeek gridweek = (calendarGridWeek) full.myCalendarGridMonth.getChildAt(j);
				
				
				for(int k=1; k<8;k++){
					
					calendarGridItem gridItem = (calendarGridItem) gridweek.getChildAt(k-1);
					dateofit.setTime(gridItem.getDate());
//					g+=String.format("\nWEEK# %d  item # %d\t   d: %s\n", j, k, dateofit.getTime().toString());
					
					if(frequency.equals("weekly")){
//						g+= String.format("\n\n(Weekly)Date of gridItem: %s	\nParameter: %s", dateofit.getTime().toString(), parameter);
						if(dateofit.get(Calendar.DAY_OF_WEEK)== Integer.valueOf(parameter) && datecreated< dateofit.getTimeInMillis()) {
//							g+="\nWeekly about to show\n";
							typer(type, gridItem, name, frequency, parameter, datecreated, amount);
						}
							
											
					}
					
					else if(frequency.equals("biweekly")){
						red.setTime(Long.valueOf(parameter));
						today.setTime(red);
						today = setTomin(today);
						
//						g+=String.format("biweekly pre\t griditem %d\t baseOnParam: %d\n", dateofit.getTime().getTime(), today.getTime().getTime());
						while(dateofit.getTime().getTime()>today.getTime().getTime()){
//							g+= String.format("\n\n(Biweekly)Date of gridItem: %s	Date iterated: %s", dateofit.getTime().toString(), today.getTime().toString());
							
							
							if(dateofit.get(Calendar.DAY_OF_MONTH)==today.get(Calendar.DAY_OF_MONTH)&& dateofit.get(Calendar.MONTH)==today.get(Calendar.MONTH)&&dateofit.get(Calendar.YEAR)==today.get(Calendar.YEAR) ){
//								g+= "\nbiweekly about to show\n";
								typer(type, gridItem, name, frequency, parameter, datecreated, amount);	
								
								break;
							}	
							
							
							today.add(Calendar.WEEK_OF_YEAR, 2);
							
						}
					}
					else if(frequency.equals("semimonthly")){
//						g+= String.format("\n\n(semimonthly)Date of gridItem: %s	parameter: %s", dateofit.getTime().toString(), parameter);
						dateofit = setToMax(dateofit);
						List<String> v = RemindersElement.dissectSemiMonthly(parameter);
						if((dateofit.get(Calendar.DAY_OF_MONTH)==Integer.valueOf(v.get(0)) || dateofit.get(Calendar.DAY_OF_MONTH)==Integer.valueOf(v.get(1))) && datecreated< dateofit.getTimeInMillis()) {
//							g+="\nsemimonthly about to show\n";
							typer(type, gridItem, name, frequency, parameter, datecreated, amount);	
						}
						
					}
					else if(frequency.equals("monthly")){
						dateofit = setToMax(dateofit);
						
//						g+= String.format("\n\n(monthly)Date of gridItem: %s	date: %s\nmilliCreated: %d\t CurrentMilli: %d", dateofit.getTime().toString(), parameter, datecreated, dateofit.getTime().getTime());
						
						if(dateofit.get(Calendar.DAY_OF_MONTH)==Integer.valueOf(parameter)&& datecreated< dateofit.getTimeInMillis()) {
//							g+="\nMONTHLY about to show\n";
							typer(type, gridItem, name, frequency, parameter, datecreated, amount);							
						}
						
					}
					else if(frequency.equals("yearly")){
						red.setTime(Long.valueOf(parameter));
						today.setTime(red);
						dateofit = setToMax(dateofit);
//						g+= String.format("\n\n(yearlyy)Date of gridItem: %s	Date in year: %s", dateofit.getTime().toString(), today.getTime().toString());
						if(dateofit.get(Calendar.DAY_OF_MONTH)==today.get(Calendar.DAY_OF_MONTH) && dateofit.get(Calendar.MONTH)== today.get(Calendar.MONTH)&& datecreated< dateofit.getTimeInMillis()) {
//							g+="\nyearly about to show\n";
							typer(type, gridItem, name, frequency, parameter, datecreated, amount);
						}
					}

				}
				
			}
			
			res.moveToNext();
		}
			
//		FileOutputStream f;
//		try {
//			f = openFileOutput("calendar.txt", MODE_PRIVATE);
//			f.write(g.getBytes());
//			f.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
			

		
		
		
		res.close();
		db.close();
	}
	
	
	//shows the income for the type
	private void typer(String type, calendarGridItem gridItem,String name, String frequency,String parameter,long datecreated,double amount){
		if(type.equals("Loan") || type.equals("Utility")) gridItem.minusSign.setVisibility(View.VISIBLE);
		else if(type.equals("Other")) 	gridItem.star.setVisibility(View.VISIBLE);
		else if(type.equals("Income")) gridItem.plusSign.setVisibility(View.VISIBLE);
		
		if(amount>0)gridItem.ob.add(new CalendarObject(name, frequency, type, parameter, datecreated, amount));
		else gridItem.ob.add(new CalendarObject(name, frequency, type, parameter, datecreated));
		
	}
	
	
	
	public static void poper(calendarGridItem r){
		
		p = new CalendarPop(c2, r);
		p.show();
		
		
//		globalTracker.trackEvent("ui_interaction", "calendar_item_pop_click", "CalendarActivity", 0);
	}
	
	
	//set the time of the calendar to the minimum time allowed in that day
	private Calendar setTomin(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	//set the time of the calendar to the maximum time allowed in that day
	private Calendar setToMax(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal;
	}
	
	
public static int getHeight(){
		
		return h;
	}

public static int getWidth(){
		
		return w;
	}

}
