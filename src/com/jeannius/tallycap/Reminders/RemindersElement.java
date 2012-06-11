package com.jeannius.tallycap.Reminders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeannius.tallycap.R;

public class RemindersElement extends RelativeLayout {

//	private TextView budgetSettingsAmountTextView, budgetSettingsDueDateTextView, budgetSettingsReminderTextView;
	public TextView AmountEditText, DueDateEditText, ReminderEditText, NameEditText, TypeEditText, FrequencyEditText;
	public String otherState;
	public int otherYear, otherMonth, otherDay;
	public String otherSemiDate1, otherSemiDate2, otherMonthlyDate;
	private Context c;
	public List<Date> datageElement;
	public long onlineID;
	
	public RemindersElement(Context context) {
		super(context);		
		LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.reminder_element, this);
		
		c= context;
		setBackgroundResource(R.drawable.my_border);
//		
		
		setter();
		
		
	}
	
	private void setter(){

		NameEditText = new TextView(c);
		AmountEditText = new TextView(c);
		DueDateEditText = new TextView(c);
		ReminderEditText = new TextView(c);
		TypeEditText = new TextView(c);
		FrequencyEditText = new TextView(c);
		datageElement = new ArrayList<Date>();
		
		NameEditText = (TextView) this.findViewById(R.id.reminderElementNameTextView);
		AmountEditText = (TextView) this.findViewById(R.id.reminderElementAmountEditText);
		DueDateEditText = (TextView) this.findViewById(R.id.reminderElementDateEditText);
		ReminderEditText = (TextView) this.findViewById(R.id.reminderElementReminderEditText);
		TypeEditText = (TextView) this.findViewById(R.id.reminderElementTypeEditText);
		FrequencyEditText = (TextView) this.findViewById(R.id.reminderElementFrequenctyEditText);			
		
	}
	
	
	//this populates the reminder element based on the reminderAddItem
	public void editCopy(RemindersAddItem ra){
		NameEditText.setText(ra.nameEditText.getText().toString());
		if(ra.amountEditText.getText().toString().length()>0) AmountEditText.setText(ra.amountEditText.getText().toString());
		else AmountEditText.setText("");
		FrequencyEditText.setText(ra.frequencySpinner.getSelectedItem().toString());
		TypeEditText.setText(ra.typeSpinner.getSelectedItem().toString());
		
		String f = ra.frequencySpinner.getSelectedItem().toString();
		if(ra.reminderSpinner.getSelectedItemPosition()==0) ReminderEditText.setText("None");
		else if(ra.reminderSpinner.getSelectedItemPosition()==1) ReminderEditText.setText("Same Day");
		else if(ra.reminderSpinner.getSelectedItemPosition()==2)ReminderEditText.setText(String.valueOf(ra.reminderSpinner.getSelectedItemPosition()-1)+ " day prior");
		else ReminderEditText.setText(String.valueOf(ra.reminderSpinner.getSelectedItemPosition()-1)+ " days prior");
		
		Calendar cat = Calendar.getInstance();
		cat.setTime(ra.datage.get(0));
		datageElement = ra.datage;
//		Toast.makeText(c, this.datageElement.get(0).toString()+"\n"+ra.datage.get(0).toString(), Toast.LENGTH_LONG).show();
		String param="";
		if(f.equals("weekly")) param = String.valueOf((cat.get(Calendar.DAY_OF_WEEK)));
		else if(f.equals("biweekly")) param = String.valueOf(cat.getTime().getTime());
		else if(f.equals("semimonthly")){
			Calendar cat2 = Calendar.getInstance();
			cat2.setTime(ra.datage.get(1));
			param = String.valueOf(cat.get(Calendar.DAY_OF_MONTH)+"-"+ cat2.get(Calendar.DAY_OF_MONTH));
		}
		else if(f.equals("monthly")) param = String.valueOf(cat.get(Calendar.DAY_OF_MONTH));
		else if(f.equals("yearly")) param = String.valueOf(cat.getTime().getTime());
			
		DueDateEditText.setText(dateConform(param, f));
//		Toast.makeText(c,"before: \n"+DueDateEditText.getText().toString(), Toast.LENGTH_LONG).show();
//		shower();
//		Toast.makeText(c,"after shower: \n"+DueDateEditText.getText().toString(), Toast.LENGTH_LONG).show();
		 
	}

	
	
	//this populates the reminder element based on the cursor from the database
	public void editCopy(Cursor res){
		
		NameEditText.setText(res.getString(0));
		AmountEditText.setText(res.getString(1));
		FrequencyEditText.setText(res.getString(4));
		String f = res.getString(4);
		TypeEditText.setText(res.getString(10));
		if(Character.isDigit(res.getString(11).charAt(0)))onlineID = Long.valueOf(res.getString(11));
		
		DueDateEditText.setText(dateConform(res.getString(5), res.getString(4)));
		String d = res.getString(5);
		if(res.getString(6).equals("1")) ReminderEditText.setText(String.format("%s day prior",res.getString(6)));
		else if(res.getString(6).equals("None")) ReminderEditText.setText(res.getString(6));
		else if(res.getString(6).equals("Same Day")) ReminderEditText.setText(res.getString(6));
		else ReminderEditText.setText(String.format("%s days prior",res.getString(6)));
		Calendar red = Calendar.getInstance();
		
		if(f.equals("weekly")) red.set(Calendar.DAY_OF_WEEK, Integer.valueOf(d));
		else if(f.equals("biweekly")) red.setTimeInMillis(Long.valueOf(d));
		else if(f.equals("semimonthly")){
			List<String> y = dissectSemiMonthly(d);
			Calendar r2 = Calendar.getInstance();
			red.set(Calendar.DAY_OF_MONTH, Integer.valueOf(y.get(0)));
			r2.set(Calendar.DAY_OF_MONTH, Integer.valueOf(y.get(1)));
			datageElement.add(0, red.getTime());
			datageElement.add(1, r2.getTime());
		}
		else if(f.equals("monthly")) red.set(Calendar.DAY_OF_MONTH, Integer.valueOf(d));
		else if(f.equals("yearly")) red.setTimeInMillis(Long.valueOf(d));
		
		if(!f.equals("semimonthly"))datageElement.add(0,red.getTime());
		
		
	}
	
	
	//this function takes a date and a string and based on that output the correct string format
	private String dateConform(String param, String Frequency){
		StringBuffer s= new StringBuffer();
		Calendar tempDate = Calendar.getInstance();
		Calendar tempdate2 = Calendar.getInstance();
		SimpleDateFormat g = new SimpleDateFormat("EEEE");
		if (Frequency.equals("weekly")) {
			tempDate.set(Calendar.DAY_OF_WEEK, Integer.valueOf(param));
			s.append((String.format("Every %ss", g.format(tempDate.getTime()))));
			tempDate.set(Calendar.DAY_OF_WEEK, Integer.valueOf(param));
		}
		else if (Frequency.equals("biweekly")){
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy ");
			Calendar clp = Calendar.getInstance();			
			clp.setTimeInMillis(Long.valueOf(param));
			s.append(String.format("Starts on %s", dateFormat.format(clp.getTime())));
			tempDate.setTimeInMillis(Long.valueOf(param));
		}
		else if (Frequency.equals("semimonthly")){
			List<String> tor = dissectSemiMonthly(param);
			s.append(String.format("On the %s and the %s of every month", RemindersAddItem.dateEnding(Integer.valueOf(tor.get(0))), 
					RemindersAddItem.dateEnding(Integer.valueOf(tor.get(1)))));
			tempDate.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tor.get(0)));
			tempdate2.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tor.get(1)));
//			Toast.makeText(c, String.format("Param: %s\n Tor size: %d\nTor 1st: %s ", param,tor.size(), tor.get(0)), Toast.LENGTH_LONG).show();
			
		}
		else if (Frequency.equals("monthly")){
			s.append(String.format("On the %s of every month", RemindersAddItem.dateEnding(Integer.valueOf(param))));
			
		}
		else if (Frequency.equals("yearly")){
			StringBuffer datef = new StringBuffer();
			Calendar clp = Calendar.getInstance();
			clp.setTimeInMillis(Long.valueOf(param));
			datef.append("MMM ");
			SimpleDateFormat dateFormat = new SimpleDateFormat(datef.toString());
			s.append((String.format("On %s %s of every year", dateFormat.format(clp.getTime()), RemindersAddItem.dateEnding(clp.get(Calendar.DAY_OF_MONTH)))));

		}
		
		
		
		return s.toString();
	}
	
	
	
	
	
	//this is to show the text in the due date box
//	private void shower() {
//		
//		StringBuffer buff = new StringBuffer();
//		Calendar myd = Calendar.getInstance();
//		SimpleDateFormat g = new SimpleDateFormat("EEEE");
//		myd.setTime(datageElement.get(0));
//		String fr = FrequencyEditText.getText().toString();
////		String dateForButton = dateFormat.format(mCalendar.getTime());
//		if(fr.equals("weekly"))			
//			buff.append(String.format("Every %s", g.format(myd.getTime())));		
//		
//		else if(fr.equals("biweekly")){
//			
//			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
//			buff.append(String.format("Starts on %s", dateFormat.format(myd.getTime())));
//			
//		}		
//		
//		else if(fr.equals("semimonthly")){			
//			Calendar myd2 = Calendar.getInstance();
//			myd2.setTime(datageElement.get(1));
//			buff.append(String.format("On the %s and the %s of every month", RemindersAddItem.dateEnding(myd.get(Calendar.DAY_OF_MONTH)),RemindersAddItem.dateEnding( myd2.get(Calendar.DAY_OF_MONTH))));
//		}
//		
//		else if(fr.equals("monthly"))
//			buff.append(String.format("On the %s of every month", RemindersAddItem.dateEnding(myd.get(Calendar.DAY_OF_MONTH))));
//		
//		else if(fr.equals("yearly")){
//			
//			StringBuffer datef = new StringBuffer();
//			datef.append("MMM");
//			SimpleDateFormat dateFormat = new SimpleDateFormat(datef.toString());
//			buff.append(String.format("On %s %s of every year", dateFormat.format(myd.getTime()), RemindersAddItem.dateEnding(myd.get(Calendar.DAY_OF_MONTH))));
//		}
//			
//		DueDateEditText.setText(buff);
//	}
	

	public static List<String> dissectSemiMonthly(String r){
		
		List<String> tor = new ArrayList<String>();
		StringBuffer g = new StringBuffer();
		
//		StringBuffer temp = new StringBuffer();
		int i=0;
		for(i =0; i <r.length(); i++){
			Character z = r.charAt(i);
			if(Character.isDigit(z)) g.append(z);
			else{
				tor.add(g.toString());
				
				g.delete(0,g.length());
			}
			

		}
		
		tor.add(g.toString());
		
		return tor;
	}
	
	
	
	


}








