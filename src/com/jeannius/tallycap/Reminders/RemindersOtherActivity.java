//package com.jeannius.tallycap.Reminders;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.jeannius.tallycap.R;
//import com.jeannius.tallycap.TrackedActivity;
//import com.jeannius.tallycap.validators.DatePickerValidator;
//import com.jeannius.tallycap.validators.NumberValidator;
//import com.jeannius.tallycap.validators.TextValidator;
//
//public class RemindersOtherActivity extends TrackedActivity {
//	
//	
//	private ImageButton addButton;
//	private LinearLayout generalcon;
//	private String boxAmountValid, boxNameValid,boxDateValid, boxSemiDate1Valid, boxSemiDate2Valid, boxMonthlyDateValid ;
//	private NumberValidator numval;
//	private TextValidator textval;
//	private billBox billb;
//	private int selectedChildForMenu;
//	private Context cont;
//	private DatePickerValidator dateval;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {		
//		super.onCreate(savedInstanceState);
//		cont = RemindersOtherActivity.this;
//		setContentView(R.layout.budget_settings_general_container);
//		addButton = (ImageButton) findViewById(R.id.budgetSettingsAddButton);
//		generalcon = new LinearLayout(cont);
//		generalcon = (LinearLayout) findViewById(R.id.BudgetSettingsGeneralContainerLayout);
//		textval = new TextValidator();
//		numval = new NumberValidator();
//		dateval = new DatePickerValidator();
//		boxAmountValid = new String();
//		boxNameValid = new String();
//		boxDateValid = new String();
//		boxSemiDate1Valid=new String();
//		boxSemiDate2Valid=new String();
//		boxMonthlyDateValid =new String();
//		 
//		addButton.setOnClickListener(new OnClickListener() {			
//			@Override
//			public void onClick(View v) {
//				showDialogAdder();	
//			}
//		});
//		if(savedInstanceState!=null) restoreMe(savedInstanceState);
//	
//	}
//	
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {		
//		super.onSaveInstanceState(outState);
//		outState.putString("red", "red");
//	}
//	
//	@SuppressWarnings("unchecked")
//	private void restoreMe(Bundle outState){
//		
//		if(outState!=null){
//			Object lll = new Object();
//			if(getLastNonConfigurationInstance()!=null){
//				lll = getLastNonConfigurationInstance();
//				ArrayList<RemindersElement> t2 = new ArrayList<RemindersElement>();
//				t2=(ArrayList<RemindersElement>) lll;
//				for (int j=0; j<t2.size(); j++){
//					generalcon.addView(t2.get(j),	j);
//				}
//			}
//		}
//		
//	}
//
//		@Override
//		public Object onRetainNonConfigurationInstance() {		
//			
//			ArrayList<RemindersElement> templist = new ArrayList<RemindersElement>();
//			
//			for(int i=0; i<generalcon.getChildCount(); i++){
//				templist.add((RemindersElement) generalcon.getChildAt(i));
//			}
//			
//			Object lll = new Object();
//			lll = templist;
//			generalcon.removeAllViews();
//			return lll	;		
//		}
//
//	
//	private void showDialogAdder(){
//
//		billb= new billBox(cont, "Other");
//		billb.show();
//		billb.billBoxAddButton.setOnClickListener(verify);
//	}
//	
//	private OnClickListener verify =new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			String s="";
//			boxNameValid = textval.validate(billb.billName, true, 2);
//			boxAmountValid = numval.validate(billb.billAmount, 1.0, true, 999999.0,false);
//			
//			
//			if(!boxNameValid.equals("Good")){
//				s+= "Bill name "+ boxNameValid+ "\n";
//				billb.billName.setBackgroundResource(R.drawable.fight);
//			}
//			else billb.billName.setBackgroundResource(R.drawable.edit_text);
//			
//			if(!boxAmountValid.equals("Good")){
//				s+="Bill amount "+ boxAmountValid+"\n";
//				billb.billAmount.setBackgroundResource(R.drawable.fight);				
//			}
//			else billb.billAmount.setBackgroundResource(R.drawable.edit_text);
//			
//			
//			//for biweekly variables saving			
//			if(billb.otherState.equals(billb.biweekly.getText().toString())){				
//				Calendar minDate = Calendar.getInstance();
//				minDate.set(Calendar.YEAR, 1900);
//				
//				Calendar maxDate = Calendar.getInstance();				
//				maxDate.add(Calendar.DATE, 1);				
//				
//				boxDateValid = dateval.validate(billb.billBoxDatePicker, minDate, maxDate);
//				if(!boxDateValid.equals("Good")) s+= "Date "+ boxDateValid+"\n";				
//			}
//			
//			
//			//for semimonthly variables saving
//			if(billb.otherState.equals(billb.semimonthly.getText().toString())){
//								
//				boxSemiDate1Valid = numval.validate(billb.billBoxNumberEditText1, 1.0, true, 31.0,true);
//				boxSemiDate2Valid = numval.validate(billb.billBoxNumberEditText2, 1.0, true, 31.0,true);
//				
//				if(!boxSemiDate1Valid.equals("Good")){
//					s+="Date 1 "+ boxSemiDate1Valid+"\n";
//					billb.billBoxNumberEditText1.setBackgroundResource(R.drawable.fight);
//				}
//				else billb.billBoxNumberEditText1.setBackgroundResource(R.drawable.edit_text);
//				
//				if(!boxSemiDate2Valid.equals("Good")){
//					s+="Date 2 "+ boxSemiDate2Valid+"\n";
//					billb.billBoxNumberEditText2.setBackgroundResource(R.drawable.fight);
//				}
//				else billb.billBoxNumberEditText2.setBackgroundResource(R.drawable.edit_text);
//			}
//			
//			
//			//for monthly variables saving
//			if(billb.otherState.equals(billb.monthly.getText().toString())){
//				boxMonthlyDateValid =numval.validate(billb.billBoxNumberEditText2, 1.0, true, 31.0, true);
//				
//				if(!boxMonthlyDateValid.equals("Good")){
//					s+="Date "+ boxMonthlyDateValid+"\n";
//					billb.billBoxNumberEditText2.setBackgroundResource(R.drawable.fight);
//				}
//				else billb.billBoxNumberEditText2.setBackgroundResource(R.drawable.edit_text);
//			}
//			
//			
//			if(s.length()>0)Toast.makeText(cont, s, Toast.LENGTH_LONG).show();
//			else{
//				
//				if(billb.getTitle().charAt(0)=="Add".charAt(0)) billAdder();
//				else billEditer2();
//			}
//		}
//	};
//	
//	
//	/**
//	 * *******************************
//	 * function to add bill 
//	 * **********************************
//	 */
//	private void billAdder(){
//		RemindersElement ele = new RemindersElement(cont);
//		
//		generalcon.addView(ele);
//		String name = billb.billName.getText().toString();
//		String amount = billb.billAmount.getText().toString();
//		String dueDate = billb.billDueDate.getText().toString();
//		String reminder = billb.billSpin.getSelectedItem().toString();
//		
//		
//		ele.budgetSettingsNameEditText.setText(name);
//		ele.budgetSettingsAmountEditText.setText(amount);
//		ele.budgetSettingsDueDateEditText.setText(dueDate);
//		ele.budgetSettingsReminderEditText.setText(reminder);
//		ele.otherState = billb.otherState;
//		registerForContextMenu(ele);
//		if(billb.otherState.equals("Biweekly")){
//			ele.otherDay=billb.billBoxDatePicker.getDayOfMonth();
//			ele.otherMonth = billb.billBoxDatePicker.getMonth();
//			ele.otherYear = billb.billBoxDatePicker.getYear();
//		}
//		else if(billb.otherState.equals(billb.semimonthly.getText().toString())){
//			ele.otherSemiDate1 = billb.billBoxNumberEditText1.getText().toString();
//			ele.otherSemiDate2 = billb.billBoxNumberEditText2.getText().toString();
//		}
//		else if (billb.otherState.equals(billb.monthly.getText().toString())){
//			
//			ele.otherMonthlyDate = billb.billBoxNumberEditText2.getText().toString();
//		}
//		
//		
//		billb.dismiss();
//	}
//	
//	/**
//	 * *******************************
//	 * context menu to ask what to do with bill
//	 * **********************************
//	 */
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {		
//		super.onCreateContextMenu(menu, v, menuInfo);
//		getMenuInflater().inflate(R.menu.budget_settings_menu, menu);
//		menu.setHeaderTitle(((RemindersElement)v).budgetSettingsNameEditText.getText().toString());
//	
//		selectedChildForMenu = ((LinearLayout)v.getParent()).indexOfChild(v);
//	}
//	
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {		
//		switch (item.getItemId()) {
//		case R.id.budgetSettingsMenuDelete:
//			areyousure();
//			break;
//		case R.id.budgetSettingsMenuEdit:
//			billEditer();
//			break;	
//		}
//		
//		return super.onContextItemSelected(item);
//		
//	}
//	/**
//	 * *******************************
//	 * Where bills get deleted!!!
//	 * **********************************
//	 */
//	
//	private void areyousure(){
//		
//		AlertDialog.Builder b = new AlertDialog.Builder(cont);
//		b.setMessage("Are you sure you want to delete this item?")
//			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					generalcon.removeViewAt(selectedChildForMenu);
//					
//				}
//			})
//			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.cancel();
//					
//				}
//			});
//		AlertDialog alert = b.create();
//		alert.show();
//		
//	}
//	
//	/**
//	 * *******************************
//	 * function to edit bill by create billbox to edit
//	 * **********************************
//	 */
//	
//	private void billEditer(){
//		billb = new billBox(cont, "Other");
//		billb.makeTitle("Edit Other");
//		RemindersElement e= ((RemindersElement)generalcon.getChildAt(selectedChildForMenu) );
//		billb.billName.setText( e.budgetSettingsNameEditText.getText());
//		billb.billAmount.setText(  e.budgetSettingsAmountEditText.getText() );
//		billb.billDueDate.setText( e.budgetSettingsDueDateEditText.getText());
//		billb.otherState = e.otherState;
//		if(billb.otherState.equals("Weekly")) billb.weekly.performClick();
//		else if(billb.otherState.equals("Biweekly")){
//			billb.biweekly.performClick();
//			billb.billBoxDatePicker.updateDate(e.otherYear, e.otherMonth, e.otherDay);
//		}
//		else if(billb.otherState.equals("Semimonthly")){
//			billb.semimonthly.performClick();
//			billb.billBoxNumberEditText1.setText(e.otherSemiDate1);
//			billb.billBoxNumberEditText2.setText(e.otherSemiDate2);
//		}
//		else if(billb.otherState.equals("Monthly")){
//			billb.monthly.performClick();
//			billb.billBoxNumberEditText2.setText(e.otherMonthlyDate);
//		}
//		
//		String ls = String.valueOf(( e.budgetSettingsReminderEditText.getText().charAt(0))) +
//		 String.valueOf(( e.budgetSettingsReminderEditText.getText().charAt(1)));
//				
//		Character l = new Character(ls.charAt(0));
//		if(  Character.isDigit(l)){
//			if(String.valueOf(ls.charAt(1)).equals(" ")) billb.billSpin.setSelection(Integer.valueOf(String.valueOf(l))+1);
//			else billb.billSpin.setSelection(Integer.valueOf(ls)+1);
//		}
//		else if(String.valueOf(l).equals("N")) billb.billSpin.setSelection(0);
//		else billb.billSpin.setSelection(1);
//		
//		billb.show();		
//		billb.billBoxAddButton.setOnClickListener(verify);
//	}
//	
//	/**
//	 * *******************************
//	 * function to edit bill by modifying element when saving to view again
//	 * **********************************
//	 */
//	private void billEditer2(){
//		RemindersElement temp = (RemindersElement) generalcon.getChildAt(selectedChildForMenu);
//		temp.budgetSettingsAmountEditText.setText( billb.billAmount.getText().toString());
//		temp.budgetSettingsNameEditText.setText( billb.billName.getText().toString());
//		temp.budgetSettingsDueDateEditText.setText( billb.billDueDate.getText().toString());
//		temp.budgetSettingsReminderEditText.setText(billb.billSpin.getSelectedItem().toString());
//		temp.otherState = billb.otherState;
//		if(billb.otherState.equals("Biweekly")){
//			temp.otherDay=billb.billBoxDatePicker.getDayOfMonth();
//			temp.otherMonth = billb.billBoxDatePicker.getMonth();
//			temp.otherYear = billb.billBoxDatePicker.getYear();
//		}
//		else if(billb.otherState.equals("Semimonthly")){
//			temp.otherSemiDate1 =billb.billBoxNumberEditText1.getText().toString();
//			temp.otherSemiDate2 = billb.billBoxNumberEditText2.getText().toString();
//
//		}
//		else if(billb.otherState.equals("Monthly")){
//			temp.otherMonthlyDate = billb.billBoxNumberEditText2.getText().toString();
//		}
//		billb.dismiss();
//	}
//}
