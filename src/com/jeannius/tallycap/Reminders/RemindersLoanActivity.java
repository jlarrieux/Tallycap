//package com.jeannius.tallycap.Reminders;
//
//import java.util.ArrayList;
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
//import com.jeannius.tallycap.validators.NumberValidator;
//import com.jeannius.tallycap.validators.TextValidator;
//
//public class RemindersLoanActivity extends TrackedActivity {
//	
//	private ImageButton addButton;
//	private LinearLayout generalcon;
//	private String boxAmountValid, boxNameValid, boxDueDateValid;
//	private NumberValidator numval;
//	private TextValidator textval;
//	private billBox billb;
//	private int selectedChildForMenu;
//	private Context cont;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {		
//		super.onCreate(savedInstanceState);
//		cont = RemindersLoanActivity.this;
//		setContentView(R.layout.budget_settings_general_container);
//		addButton = (ImageButton) findViewById(R.id.budgetSettingsAddButton);
//		generalcon = new LinearLayout(cont);
//		generalcon = (LinearLayout) findViewById(R.id.BudgetSettingsGeneralContainerLayout);
//		textval = new TextValidator();
//		numval = new NumberValidator();
//		boxAmountValid = new String();
//		boxNameValid = new String();
//		boxDueDateValid = new String();
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
//		billb= new billBox(cont, "Loan");
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
//			boxDueDateValid = numval.validate(billb.billDueDate, 1.0, true, 31.0, true);
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
//			if(!boxDueDateValid.equals("Good")){
//				billb.billDueDate.setBackgroundResource(R.drawable.fight);
//				s+= "Bill due date "+boxDueDateValid +"\n";
//			}
//			else billb.billDueDate.setBackgroundResource(R.drawable.edit_text);
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
//		ele.budgetSettingsNameEditText.setText(name);
//		ele.budgetSettingsAmountEditText.setText(amount);
//		ele.budgetSettingsDueDateEditText.setText(dueDate);
//		ele.budgetSettingsReminderEditText.setText(reminder);
//		registerForContextMenu(ele);
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
//		billb = new billBox(cont, "Loan");
//		billb.makeTitle("Edit Loan");
//		billb.billName.setText( ((RemindersElement)generalcon.getChildAt(selectedChildForMenu) ).budgetSettingsNameEditText.getText());
//		billb.billAmount.setText(  ((RemindersElement)generalcon.getChildAt(selectedChildForMenu) ).budgetSettingsAmountEditText.getText() );
//		billb.billDueDate.setText( ((RemindersElement)generalcon.getChildAt(selectedChildForMenu) ).budgetSettingsDueDateEditText.getText());
//		
//		
//		String ls = String.valueOf(( ((RemindersElement)generalcon.getChildAt(selectedChildForMenu) ).budgetSettingsReminderEditText.getText().charAt(0))) +
//		 String.valueOf(( ((RemindersElement)generalcon.getChildAt(selectedChildForMenu) ).budgetSettingsReminderEditText.getText().charAt(1)));
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
//	 * function to edit bill by modifying element
//	 * **********************************
//	 */
//	private void billEditer2(){
//		RemindersElement temp = (RemindersElement) generalcon.getChildAt(selectedChildForMenu);
//		temp.budgetSettingsAmountEditText.setText( billb.billAmount.getText().toString());
//		temp.budgetSettingsNameEditText.setText( billb.billName.getText().toString());
//		temp.budgetSettingsDueDateEditText.setText( billb.billDueDate.getText().toString());
//		temp.budgetSettingsReminderEditText.setText(billb.billSpin.getSelectedItem().toString());
//		billb.dismiss();
//	}
//	
//	
//}
//
//
//
//
//
//
//
//

