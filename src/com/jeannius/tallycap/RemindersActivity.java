package com.jeannius.tallycap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jeannius.tallycap.Reminders.CalendarPopUp;
import com.jeannius.tallycap.Reminders.RemindersAddItem;
import com.jeannius.tallycap.Reminders.RemindersElement;
import com.jeannius.tallycap.admin.MyCrypt;
import com.jeannius.tallycap.admin.MyDbOpenHelper;
import com.jeannius.tallycap.admin.NetworkOperations;
import com.jeannius.tallycap.validators.TextValidator;

public class RemindersActivity extends TrackedActivity {

	private ImageButton addItem;	
	private Context c;
	private RemindersAddItem ra;
	private LinearLayout lin;
	private String nameVal, command;
	private TextValidator textVal;
	private int selectedChildForMenu;
	public static int screenWidth, screenHeight;
	public static String CREATE ="Create";
	public static String UPDATE ="Update";
	public static String DELETE = "Delete";
	public static String REMINDER = "reminder";
	public static String Update_LOCAL_ONLY="UpdateLocalOnly";
	public static String Create_LOCAL_ONLY="CreateLocalOnly";
	public static String GOOD ="Good";
	private List<NameValuePair> nameValuePairs;
	private ContentValues cv;	
	private LinearLayout.LayoutParams par;
	private RemindersElement re6;
	private ProgressDialog pr ;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {			
			super.onCreate(savedInstanceState);	
			setContentView(com.jeannius.tallycap.R.layout.reminder_main_layout);

//			Debug.startMethodTracing("REAL.trace");
			c = RemindersActivity.this.getApplicationContext();
			pr = new ProgressDialog(RemindersActivity.this);
			setter();
						if(savedInstanceState!=null){
							if(savedInstanceState.containsKey("data")){
								if(!savedInstanceState.getString("data").equals("null")){
									restoreDialog(savedInstanceState);
									
								}
			
							}
						}

				
		}
		
		//this restores the dialog
		private void restoreDialog(Bundle b) {
			ra = new RemindersAddItem(RemindersActivity.this);			
			ra.show();
			ra.editCopy(b);
		}

		
		
		//saves the bundles
		@Override
		protected void onSaveInstanceState(Bundle outState) {			
			super.onSaveInstanceState(outState);
			
			if(ra!=null ){
				if(ra.isShowing()){
//					Toast.makeText(c, "Is showign", Toast.LENGTH_LONG).show();
					outState.putString("data", "good");
					outState.putString("name", ra.nameEditText.getText().toString());
					outState.putString("amount", ra.amountEditText.getText().toString());
					outState.putString("type", ra.typeSpinner.getSelectedItem().toString());
					outState.putString("frequency", ra.frequencySpinner.getSelectedItem().toString());					
					outState.putString("title", ra.tit);
					outState.putString("reminder", ra.reminderSpinner.getSelectedItem().toString());
					if(ra.pop!=null){
						if(ra.pop.isShowing()){
							outState.putString(RemindersAddItem.POP_IS_SHOWING, "yes");
							outState.putInt("popMonth", CalendarPopUp.grid.month);
							outState.putInt("popYear", CalendarPopUp.grid.year);
						}
						else outState.putString(RemindersAddItem.POP_IS_SHOWING, "no");
					}
					if(ra.datage.size()>0){
						long[] list1 = new long[ra.datage.size()];
						for(int p=0; p<ra.datage.size(); p++){
							list1[p]= (ra.datage.get(p)).getTime();
						}
						outState.putLongArray("dateList", list1);
						
					}
				}
				
			}
			else outState.putString("data", "null");
		}
		
		//dismiss the dialog before closing so it doesn't leak context
		@Override
		protected void onPause() {			
			super.onPause();
			if(ra!=null ){
				if(ra.isShowing())ra.dismiss();
				if(ra.pop!=null){
					if(ra.pop.isShowing()) ra.pop.dismiss();
				}
			}
			
		}
		
		//this function instantiate all the variables
		private  void setter(){
			
			addItem = new ImageButton(c);
			addItem = (ImageButton) findViewById(R.id.ReminderMainImageButton);
			addItem.setOnClickListener(adder);
			par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			par.setMargins(0, 0, 0, 10);
			
			Display dis= ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
			screenWidth = dis.getWidth();
			screenHeight = dis.getHeight();
			lin = new LinearLayout(c);
			lin = (LinearLayout) findViewById(R.id.reminderMainLinearLyout);
	
	
			populate();
				
		}
		
		//this methods pulls the save items from the database
		private void populate() {
			SQLiteDatabase db = new MyDbOpenHelper(c).getReadableDatabase();
			
			String select = "SELECT * FROM reminder";
			Cursor res = db.rawQuery(select, null);		
			res.moveToFirst();
			
			
			if(res.getCount()>0){
				
					for(int i =0; i <res.getCount(); i++){
					

							if(!res.getString(9).equals(DELETE)){							
							
									RemindersElement el = new RemindersElement(c);
									el.editCopy(res);
									lin.addView(el,par);				
									registerForContextMenu(el);
									
							}
							res.moveToNext();
					}			
			}

			res.close();		
			db.close();
		}

		


		//this function instantiate and display the "add item" dialog
		private OnClickListener adder = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ra = new RemindersAddItem(RemindersActivity.this);
				ra.show();				
				ra.createButton.setOnClickListener(verifyer);
			}
		};
		
		
		//verify the inputs of the reminderAddItem
		private View.OnClickListener verifyer = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textVal = new TextValidator();
				nameVal = textVal.validate(ra.nameEditText, true, 2);
				
				String valres="";
				
				if(!nameVal.equals("Good")){
					ra.nameEditText.setBackgroundResource(R.drawable.fight);
					valres+= "Name "+ nameVal+"\n";
					globalTracker.trackEvent("reminder_user_error", "name", "ReminderActivity", 0);
				}
				else ra.nameEditText.setBackgroundResource(R.drawable.edit_text);
				
				
				if(ra.chosenDateTextView.length()==0){
					ra.choseDateImageButton.getBackground().setColorFilter(0xFFFF0000, Mode.MULTIPLY);
					valres += "Date must be chosend";
					globalTracker.trackEvent("reminder_user_error", "chosenDateNotSelected", "ReminderActivity", 0);
				}
				else  ra.choseDateImageButton.setBackgroundResource(android.R.drawable.btn_default);
					
				
				
				if(valres.length()>0) Toast.makeText(c, valres, Toast.LENGTH_LONG).show();
				else{
					
					if(ra.tit.equals("Add Item"))creator();
					else updator();
				}
				
			}		

		
		};
		
		
		//updates the reminderElement in the main view
		private void updator() {
			re6 = (RemindersElement) lin.getChildAt(selectedChildForMenu);
			
			cvCreator();
			command = UPDATE;
			new saveToDb().execute();
			re6.editCopy(ra);
			ra.dismiss();
			globalTracker.trackEvent("ui_interaction", "update_reminder", "ReminderActivity", 0);
		}
		
		
		
		//creates the bill in the main view and fires up the asynctask to save in database (local and online)
		private void creator() {
						
			cvCreator();
			command= CREATE;
			ra.dismiss();
			new saveToDb().execute();	
			globalTracker.trackEvent("ui_interaction", "create_reminder", "ReminderActivity", 0);
			
		}
		
		
		//gets item ready for database saving or updating
		private void cvCreator(){
			StringBuffer b = new StringBuffer();
			Calendar cat = Calendar.getInstance();
			cat.setTime(ra.datage.get(0));
			cv= new ContentValues();
			Date test = new Date();
			nameValuePairs = new ArrayList<NameValuePair>();
			
			SQLiteDatabase db = new MyDbOpenHelper(c).getReadableDatabase();
			
			String select = "SELECT email FROM customer";
			Cursor res = db.rawQuery(select, null);		
			res.moveToFirst();
			try {

				nameValuePairs.add(new BasicNameValuePair("email", MyCrypt.decrypt(HomeScreenActivity.MASTER_PASSWORD, res.getString(0))));
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			res.close();
			db.close();
//			name 0, amount 1, dateCreated 2, dateModified 3, frequency 4, parameter 5, reminder1 6, status 7, reminder2 8, reminder3 9)");
			
			cv.put("name", new StringBuffer(ra.nameEditText.getText().toString()).toString());
			nameValuePairs.add(new BasicNameValuePair("name", ra.nameEditText.getText().toString()));
			
			cv.put("amount", new StringBuffer(ra.amountEditText.getText().toString()).toString());
			nameValuePairs.add(new BasicNameValuePair("amount", ra.amountEditText.getText().toString()));
			
			cv.put("dateCreated", new StringBuffer(String.valueOf(test.getTime())).toString());
			nameValuePairs.add(new BasicNameValuePair("dateCreated", String.valueOf(test.getTime()).toString()));
			
			cv.put("dateModified", new StringBuffer(String.valueOf(test.getTime())).toString());
			nameValuePairs.add(new BasicNameValuePair("dateModified", String.valueOf(test.getTime()).toString()));
			
			cv.put("frequency", ra.frequencySpinner.getSelectedItem().toString());
			nameValuePairs.add(new BasicNameValuePair("frequency",ra.frequencySpinner.getSelectedItem().toString()));
			
			//logic for the frequency parameter
			if(ra.frequencySpinner.getSelectedItem().toString().equals("weekly")){
				cv.put("parameter", b.append(cat.get(Calendar.DAY_OF_WEEK)).toString());
				nameValuePairs.add(new BasicNameValuePair("parameter",b.toString()));
			}
			else if(ra.frequencySpinner.getSelectedItem().toString().equals("biweekly")){
				cv.put("parameter", String.valueOf(cat.getTime().getTime()));
				nameValuePairs.add(new BasicNameValuePair("parameter", String.valueOf(cat.getTime().getTime())));
			}
			else if(ra.frequencySpinner.getSelectedItem().toString().equals("semimonthly")){
				Calendar cat2 = Calendar.getInstance();
				cat2.setTime(ra.datage.get(1));
				b.append(cat.get(Calendar.DAY_OF_MONTH)+"-"+ cat2.get(Calendar.DAY_OF_MONTH));
				cv.put("parameter",	b.toString());
				nameValuePairs.add(new BasicNameValuePair("parameter",b.toString()));
			}
				
			else if(ra.frequencySpinner.getSelectedItem().toString().equals("monthly")){
				cv.put("parameter", b.append(cat.get(Calendar.DAY_OF_MONTH)).toString());
				nameValuePairs.add(new BasicNameValuePair("parameter",b.toString()));
			}
			else if(ra.frequencySpinner.getSelectedItem().toString().equals("yearly")){
				cv.put("parameter", String.valueOf(cat.getTime().getTime()));
				nameValuePairs.add(new BasicNameValuePair("parameter",String.valueOf(cat.getTime().getTime())));
			}
			
			
			//logic for the reminder 		
			if(ra.reminderSpinner.getSelectedItemPosition()==0){
				cv.put("reminder1", "None");
				nameValuePairs.add(new BasicNameValuePair("reminder1", "None"));
			}
			else if(ra.reminderSpinner.getSelectedItemPosition()==1){
				cv.put("reminder1", "Same Day");
				nameValuePairs.add(new BasicNameValuePair("reminder1", "Same Day"));
			}
			else{
				cv.put("reminder1", String.valueOf(ra.reminderSpinner.getSelectedItemPosition()-1));
				nameValuePairs.add(new BasicNameValuePair("reminder1", String.valueOf(ra.reminderSpinner.getSelectedItemPosition()-1)));
			}
			
			
			cv.put("status", GOOD);
			nameValuePairs.add(new BasicNameValuePair("status",GOOD));
			
			cv.put("type",ra.typeSpinner.getSelectedItem().toString());
			nameValuePairs.add(new BasicNameValuePair("type",ra.typeSpinner.getSelectedItem().toString()));
		}
		
	
		//Present the context menu about what to do with the item that was long pressed
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {			
			super.onCreateContextMenu(menu, v, menuInfo);
			
			getMenuInflater().inflate(R.menu.reminder_settings_menu, menu);
			menu.setHeaderTitle(((RemindersElement)v).NameEditText.getText().toString());	
			
			selectedChildForMenu = ((LinearLayout)v.getParent()).indexOfChild(v);
		}
		
		
		//does something based on the context menu selected
		@Override
		public boolean onContextItemSelected(MenuItem item) {
			switch(item.getItemId()){
			case R.id.reminderSettingsMenuDelete: 
				areyousure();
				break;
			case R.id.reminderSettingsMenuEdit: 
				reminderEditer();
				break;
			
			}
			return super.onContextItemSelected(item);
		}



		//brings the edit item
		private void reminderEditer() {

			ra = new RemindersAddItem(RemindersActivity.this, (RemindersElement)(lin.getChildAt(selectedChildForMenu)));
			ra.show();
	
			ra.createButton.setOnClickListener(verifyer);	
			
		}




		private void areyousure() {
			AlertDialog.Builder b = new AlertDialog.Builder(RemindersActivity.this);
			b.setMessage("Are you sure you want to delete this item?")
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						command= DELETE;
						cv= new ContentValues();
						nameValuePairs = new ArrayList<NameValuePair>();
						re6 = (RemindersElement) lin.getChildAt(selectedChildForMenu);
						new saveToDb().execute();
						globalTracker.trackEvent("ui_interaction", "delete_reminder", "ReminderActivity", 0);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
					}
				});
			AlertDialog alert = b.create();
			alert.show();
			
		}


		
		/**
		 * *************************
		 * ASYNCTASK
		 * *************************
		 ** 
		 */
	class saveToDb extends AsyncTask<Void, Void, String>{
		@Override
			protected void onPreExecute() {				
				super.onPreExecute();
				String p = command.replace("te", "ting");
				pr.setMessage(p+" data...");
				pr.show();
			}
		@Override
		protected String doInBackground(Void... params) {
									
			String red="";
		    SQLiteDatabase db2 = new MyDbOpenHelper(c).getWritableDatabase();
			
			NetworkOperations n = new NetworkOperations(c);
			
			if(command.equals(CREATE)) red =n.ReminderCreate(nameValuePairs);
						
			else if (command.equals(UPDATE))		
				red =n.ReminderUpdate(nameValuePairs, ((RemindersElement)lin.getChildAt(selectedChildForMenu)).onlineID);
			
			else if(command.equals(DELETE))	{
				
				
				String select2 = "SELECT email FROM customer";
				Cursor res = db2.rawQuery(select2, null);		
				res.moveToFirst();
				try {
					nameValuePairs.add(new BasicNameValuePair("email", MyCrypt.decrypt(HomeScreenActivity.MASTER_PASSWORD, res.getString(0))));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				res.close();
				red =n.ReminderDelete(nameValuePairs, ((RemindersElement)lin.getChildAt(selectedChildForMenu)).onlineID);
			}

			
			db2.close();
						 
			 
			
		    
			
			return red;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);
			pr.dismiss();
			SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
			String rion = result;
			if(result.indexOf("ucce")>0) rion ="Password successfully sent. Please wait a few minutes and check your email.";
			else if(result.indexOf("etwork")>0 ) rion =". Data will update later.";
			else if(result.indexOf("data")>0) rion = ". Data will update later.";			
			else rion ="An error occurred. Please try again later.";

			String s= " Reminder will update later";
			if(rion.indexOf("occurred")!=0){
				if(command.equals(CREATE)){
					RemindersElement re = new RemindersElement(c);
					re.editCopy(ra);				
						
						if(result.endsWith("success")){		
							
							int id = RemindersAddItem.numberFromSring(result)-1;
							re.onlineID =id;
							cv.put("onlineID", id);
							cv.put("status", GOOD);
							Toast.makeText(c, "Item successfully created", Toast.LENGTH_LONG).show();
							
						}
						else{
							
							Random rand = new Random();
							cv.put("status", Create_LOCAL_ONLY);
							cv.put("onlineID", rand.nextInt());
							Toast.makeText(c, result+ s, Toast.LENGTH_LONG).show();
						}
					
						StringBuffer ops = new StringBuffer();
						BasicNameValuePair bn = (BasicNameValuePair) nameValuePairs.get(nameValuePairs.size()-1);
						ops.append(bn.getValue());
						
						db.insertOrThrow(REMINDER, null, cv);	
											
						lin.addView(re, par);
						registerForContextMenu(re);
										
				}
				else if(command.equals(UPDATE)){
					
					if(!result.toString().endsWith("success")){
						cv.put("status",Update_LOCAL_ONLY);
						Toast.makeText(c, result+s, Toast.LENGTH_LONG).show();
					}
					else{
						cv.put("status", GOOD);
						Toast.makeText(c, "Item successfully updated", Toast.LENGTH_LONG).show();
					}
					
					db.update(REMINDER, cv, "onlineID="+re6.onlineID, null);
				}
				else if(command.equals(DELETE)){
					if(!result.toString().endsWith("success")){
						cv.put("status", DELETE);
						db.update(REMINDER, cv, "onlineID="+re6.onlineID, null);
						Toast.makeText(c, result+s, Toast.LENGTH_LONG).show();
					}
					else{
						db.delete(REMINDER, "onlineID="+re6.onlineID, null);
						Toast.makeText(c, "Item successfully deleted", Toast.LENGTH_LONG).show();
						
					}
					lin.removeViewAt(selectedChildForMenu);
					
				}

		}
			else Toast.makeText(c, rion, Toast.LENGTH_LONG).show();
			db.close();
			
		}
		
		
		
	}
	
	
	
	
	


}














