package com.jeannius.tallycap.Reminders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannius.tallycap.R;

public class RemindersAddItem extends Dialog {
	
	private Context c;
	public EditText nameEditText, amountEditText;
	public Spinner typeSpinner, frequencySpinner, reminderSpinner;
	public Button createButton, cancelButton;
	public TextView chosenDateTextView, daysPriors;
	private RemindersAddItem me;
	public ImageButton choseDateImageButton;
	public String tit, comp, sel;
	private int h;
	private ArrayAdapter<CharSequence> adapterMonth, adapterWeek, adapterYear;
	public static String reminderFr;
	public CalendarPopUp pop;
	public List<Date> datage;
	public StringBuffer buff;
	private String Date_Format = "MMM dd, yyyy";
	private String ADD_ITEM ="Add Item";
	private String UPDATE_ITEM ="Update Item";
	private RemindersElement ele;
	public static String POP_IS_SHOWING ="popShowing";
	
	
	public RemindersAddItem(Context context) {
		super(context);
		c= context;
		setContentView(R.layout.reminder_add_item_dialog);
		setTitle(ADD_ITEM);
		me = this;
		tit = ADD_ITEM;
		sel="monthly";
		 setter();
	}
	
	
	public RemindersAddItem(Context context, RemindersElement el){
		super(context);
		c= context;
		setContentView(R.layout.reminder_add_item_dialog);
		setTitle(UPDATE_ITEM);
		me = this;
		tit = UPDATE_ITEM;
		
		ele = new RemindersElement(c);
		ele = el;
		setter();
		
		
	}
	

	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	//this function instantiate the variables
	private void setter() {
		reminderFr ="";
		
		comp="";
		nameEditText = new EditText(c);
		amountEditText = new EditText(c);
		datage = new ArrayList<Date>();
		chosenDateTextView = new  TextView(c);
		daysPriors = new  TextView(c);
		
		typeSpinner = new Spinner(c);
		frequencySpinner = new Spinner(c);
		reminderSpinner = new Spinner(c);
		
		createButton = new Button(c);
		cancelButton = new Button(c);
		
		choseDateImageButton = new ImageButton(c);
		
		nameEditText = (EditText) this.findViewById(R.id.reminderAddItemNameEditText);
		amountEditText = (EditText) this.findViewById(R.id.reminderAddItemAmountEditText);
		
		typeSpinner = (Spinner) this.findViewById(R.id.reminderAddItemTypeSpinner);
		frequencySpinner =(Spinner) this.findViewById(R.id.reminderAddItemFrequencySpinner);
		createButton = (Button) this.findViewById(R.id.reminderAddItemCreateButton);
		cancelButton = (Button) this.findViewById(R.id.reminderAddItemCancelButton);
		reminderSpinner = (Spinner) this.findViewById(R.id.reminderAddItemReminderSpinner);
		chosenDateTextView = (TextView) this.findViewById(R.id.reminderAddItemChosenDateTextView);
		choseDateImageButton = (ImageButton) this.findViewById(R.id.reminderAddItemDateImageButton);
		daysPriors = (TextView) this.findViewById(R.id.reminderAddItemDaysPriorTextView);
		
		
		choseDateImageButton.setOnClickListener(calendarPop);
			adapterMonth = ArrayAdapter.createFromResource(c, R.array.reminder_array_month, R.layout.my_spin);
			adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
			
			adapterWeek = ArrayAdapter.createFromResource(c, R.array.reminder_array_week, R.layout.my_spin);
			adapterWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
			
			adapterYear = ArrayAdapter.createFromResource(c, R.array.reminder_array_year, R.layout.my_spin);
			adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
			
				
			chosenDateTextView.setTextSize(10);	
			
			
			frequencySpinner.setOnItemSelectedListener(reminderRange);
			
			if(tit.equals(ADD_ITEM)){
				frequencySpinner.setSelection(3);
				typeSpinner.setSelection(3);
			}
			else editCopy(ele);;
			
			
		
		
		
		reminderSpinner.setOnItemSelectedListener(textChange);
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				me.cancel();
				
			}
		});
	}
	
	//this creates the calendar popup 
	
	private View.OnClickListener calendarPop = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Date D =  new Date();
			showPop(D, false);
		}
	};
	
	//shows the date popup
	private void showPop(Date dd, Boolean dateSet){
		h =0;
		StringBuffer s=new StringBuffer();
		if(frequencySpinner.getSelectedItem().toString().equals("semimonthly")){
			h=2;
			s.append("Select the two dates of the month");
		}
		else{
			h =1;
			if(frequencySpinner.getSelectedItem().toString().equals("weekly"))s.append("Select a day of the week");
			else if(frequencySpinner.getSelectedItem().toString().equals("biweekly"))s.append("Select a date when this occurs");
			else s.append("Select a date");
		}
		reminderFr = frequencySpinner.getSelectedItem().toString();
		pop = new CalendarPopUp(c,h, s.toString(), dd, dateSet);
		pop.okButton.setOnClickListener(dateListener);
		pop.show();
	}
	
	//date selection
	private View.OnClickListener dateListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String red = "";
			if(CalendarPopUp.nu ==h)  {
				datage=CalendarPopUp.selectation();
				if(datage.size()>0)showDate();
				pop.cancel();
				
			}
			else {
				
				if(CalendarPopUp.nu==0) red = "Please select a date";
				else red = "Please select an extra date";
			}
			
			if(red.length()>0) Toast.makeText(c, red, Toast.LENGTH_LONG).show();
			
		}

	
	};
	
	//this show the chosen date
	private void showDate() {
		buff = new StringBuffer();
		Calendar myd = Calendar.getInstance();
		SimpleDateFormat g = new SimpleDateFormat("EEEE");
		myd.setTime(datage.get(0));
		
//		String dateForButton = dateFormat.format(mCalendar.getTime());
		if(frequencySpinner.getSelectedItem().toString().equals("weekly"))			
			buff.append(String.format("Every %s", g.format(myd.getTime())));		
		
		else if(frequencySpinner.getSelectedItem().toString().equals("biweekly")){
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Format);
			buff.append(String.format("Starts on \n%s", dateFormat.format(myd.getTime())));
		}		
		
		else if(frequencySpinner.getSelectedItem().toString().equals("semimonthly")){			
			Calendar myd2 = Calendar.getInstance();
			myd2.setTime(datage.get(1));
			buff.append(String.format("On the %s and the %s of every month", dateEnding(myd.get(Calendar.DAY_OF_MONTH)),dateEnding( myd2.get(Calendar.DAY_OF_MONTH))));
		}
		
		else if(frequencySpinner.getSelectedItem().toString().equals("monthly"))
			buff.append(String.format("On the %s of every month", dateEnding(myd.get(Calendar.DAY_OF_MONTH))));
		
		else if(frequencySpinner.getSelectedItem().toString().equals("yearly")){
			
			StringBuffer datef = new StringBuffer();
			datef.append("MMM ");
			SimpleDateFormat dateFormat = new SimpleDateFormat(datef.toString());
			buff.append(String.format("On %s %s of every year", dateFormat.format(myd.getTime()), dateEnding(myd.get(Calendar.DAY_OF_MONTH))));
		}
			
		
		chosenDateTextView.setText( buff);
	}
	
	
	//this is just aesthetics
	private AdapterView.OnItemSelectedListener textChange = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		
			if(arg3<2) daysPriors.setVisibility(View.GONE);
			else {
				daysPriors.setVisibility(View.VISIBLE);
				if(arg3==2) daysPriors.setText("day prior");
				else daysPriors.setText("days prior");
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}
	};
	
	
	
	//this determine the reminder spinner's range
	public AdapterView.OnItemSelectedListener reminderRange = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			if(!sel.equals(frequencySpinner.getSelectedItem().toString())){
				datage.clear();
				chosenDateTextView.setText("");
				sel = frequencySpinner.getSelectedItem().toString();
				
				adap((TextView)arg1);
			}

			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}
	};
	
	

	
	//this creates the reminderspinner items
	private void adap(TextView arg1){
		
		String temp = ((TextView)arg1).getText().toString();
		
		if(tit.equals("Add Item") || !comp.equals(frequencySpinner.getSelectedItem().toString())){
			if(temp.equals("weekly")) reminderSpinner.setAdapter(adapterWeek);
			
			else if (temp.equals("yearly")) reminderSpinner.setAdapter(adapterYear);
			
			else reminderSpinner.setAdapter(adapterMonth);
			comp ="";
			
		}
		//Toast.makeText(c, String.format("Freq: %s \nComp: %s", frequencySpinner.getSelectedItem().toString(), comp), Toast.LENGTH_LONG).show();
	}
	

	
	
	//this function is used for editing
	private void editCopy(RemindersElement el){

		this.nameEditText.setText(el.NameEditText.getText().toString());
		this.amountEditText.setText(el.AmountEditText.getText().toString());
		this.datage = el.datageElement;
//			Toast.makeText(c, this.datage.get(0).toString()+"\n"+el.datageElement.get(0).toString(), Toast.LENGTH_LONG).show();
		String[] type = c.getResources().getStringArray(R.array.item_type);
		List<String> type2 = Arrays.asList(type);
		this.typeSpinner.setSelection(type2.indexOf(el.TypeEditText.getText().toString()));
		String[] frequency = c.getResources().getStringArray(R.array.item_frequency_chooser);
		List<String> frequency2 = Arrays.asList(frequency);
		comp=frequencySpinner.getSelectedItem().toString();
		this.frequencySpinner.setSelection(frequency2.indexOf(el.FrequencyEditText.getText().toString()));
		if(datage.size()>0)showDate();
		
		this.frequencySpinner.setOnItemSelectedListener(reminderRange);
		this.createButton.setText("Update");
		this.reminderSpinner.setSelection(numberFromSring(el.ReminderEditText.getText().toString()));
		sel = frequencySpinner.getSelectedItem().toString();

	}
	
	
	
	//this function gets a string and return then add the number portion until it comes across a letter (1st item must be number)
	public static int numberFromSring(String temp){
		String fin="";
		int red=0;
		if(Character.isDigit(temp.charAt(0))){			
		
			for(int i=0; i<temp.length(); i++){
				Character z = temp.charAt(i);
				if(Character.isDigit(z)) fin+= z;
				else break;
			}
			
			red = Integer.valueOf(fin)+1;
		}
		else if(temp.equals("None")) red=0;
		else red=1;
		 
		return red;
	}
	
	//this function adds th or st or nd or rd depending on the number
	public static StringBuffer dateEnding(int dat){
		
		StringBuffer ff = new StringBuffer();
		if(dat==1) ff.append(dat+"st");
		else if(dat==2)ff.append(dat+"nd");
		else if(dat==3) ff.append(dat+"rd");
		else if(dat==21)ff.append(dat+"st");
		else if(dat==23) ff.append(dat+"rd");
		else if(dat==31)ff.append(dat+"st");
		else ff.append(dat+"th");
		
		return ff;
	}
	
	//this function populates the field based on a bundle
	public void editCopy(Bundle b){
		
		this.nameEditText.setText(b.getString("name"));
		this.amountEditText.setText(b.getString("amount"));
//		this.datage = el.datageElement;
			
		String[] type = c.getResources().getStringArray(R.array.item_type);
		List<String> type2 = Arrays.asList(type);
		this.typeSpinner.setSelection(type2.indexOf(b.getString("type")));
		String[] frequency = c.getResources().getStringArray(R.array.item_frequency_chooser);
		List<String> frequency2 = Arrays.asList(frequency);
		comp=frequencySpinner.getSelectedItem().toString();
		this.frequencySpinner.setSelection(frequency2.indexOf(b.getString("frequency")));
//		
		
		this.frequencySpinner.setOnItemSelectedListener(reminderRange);
//		this.setTitle(b.getShort("title"));
		this.reminderSpinner.setSelection(numberFromSring(b.getString("reminder")));
		sel = frequencySpinner.getSelectedItem().toString();
		
		if(b.containsKey(POP_IS_SHOWING)){
			if(b.getString(POP_IS_SHOWING).equals("yes")){
				
				Calendar cal = Calendar.getInstance(); 
				cal.set(Calendar.YEAR, b.getInt("popYear"));
				cal.set(Calendar.MONTH, b.getInt("popMonth"));
				showPop(cal.getTime(), true);
	
			}
		}
		if(b.containsKey("dateList")){
			long[] lo = b.getLongArray("dateList");
			
			for(int u=0; u<lo.length; u++){
//				datage.add(ds.setTime(lo[u]));
				datage.add(new Date(lo[u]));
			}
		}
		
		if(datage.size()>0)showDate();
	}
	
	

}
