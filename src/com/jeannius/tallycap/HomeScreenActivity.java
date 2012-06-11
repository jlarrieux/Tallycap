package com.jeannius.tallycap;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.everbadge.connect.Everbadge;
import com.jeannius.tallycap.Reminders.RemindersAddItem;
import com.jeannius.tallycap.admin.LocalOperations;
import com.jeannius.tallycap.admin.MyCrypt;
import com.jeannius.tallycap.admin.MyDbOpenHelper;
import com.jeannius.tallycap.admin.MyImageButton;
import com.jeannius.tallycap.admin.NetworkOperations;
import com.jeannius.tallycap.admin.ScrollTextView;
import com.jeannius.tallycap.admin.SignupDialog;
import com.jeannius.tallycap.admin.SimpleEula;
import com.tapjoy.TapjoyConnect;


public class HomeScreenActivity extends TrackedActivity {
	public static final String[] daysOfTheWeek = {"Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	public static final String[] daysOfTheWeekLong = {"Sunday","Monday", "Tueday", "Wednesday", "Thursday", "Friday", "Saturday"};
	public static final String[] monthArray = {"January", "february", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	//public static GradientDrawable myBackgroundGradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x2E62A6, 0x40627c});
	
	public static final int[] color1 = {0xFFe48701, 0xFFa5bc4e, 0xFF1b95d9, 0xFFcaca9e, 0xFF6693b0, 0xFFf05e27, 0xFF86d1e4,
	0xFFe4f9a0, 0xFFffd512, 0xFF75b000, 0xFF0662b0, 0xFFede8c6, 0xFFcc3300, 0xFFd1dfe7,
	0xFF52d4ca, 0xFFc5e05d, 0xFFe7c174, 0xFFfff797, 0xFFc5f68f, 0xFFbdf1e6, 0xFF9e987d,
	0xFFeb988d, 0xFF91c9e5, 0xFF93dc4a, 0xFFffb900, 0xFF9ebbcd, 0xFF009797, 0xFF0db2c2};
	
	public static final String APPLICATION_PREFERENCE= "appPrefs";
	public static final String settings_registered = "has_register";
	public static final String settings_auto_login = "auto_login";
	public static final String settings_show_register_prompt = "show_prompt";
	public static final String settings_email = "";
	public static final String MASTER_PASSWORD ="mwen te renmen fabienne"; // this is used for cryptographic purposes
	private SignupDialog su;
	private NetworkOperations netO;
	private LocalOperations locO;
	
	private AlertDialog a;
	MyImageButton ReminderButton, CalculatorsButton, AccountButton, CalendarButton;
	Intent in;
	
	public static int w, h;
	private  String CREATE_AN_ACCOUNT = "Create an account";
	private  String LOGIN = "Login";
	private Context c;

	private String customerID, email, password;
	private long localDateModified;
	private ContentValues cv, cv2;
	private List<NameValuePair> nameValuePairs, nv;
//	private TextView t;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_main_main);
        c =HomeScreenActivity.this.getApplicationContext();
        netO = new NetworkOperations(c);
        locO = new LocalOperations(c);
        w= width;
        h= height;
        
//        Toast.makeText(c, getString(R.string.ga_api_key), Toast.LENGTH_LONG).show();
        
        SharedPreferences red = getSharedPreferences(APPLICATION_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor ed = red.edit();
       
        if(!red.contains(settings_auto_login)){        	
        	ed.putBoolean(settings_auto_login, true);
        }
        
        globalTracker.setCustomVar(1, "app_started", "yes", 2);
        if(!red.contains(settings_registered)) ed.putBoolean(settings_registered, false);
        if(!red.contains(settings_show_register_prompt)) ed.putBoolean(settings_show_register_prompt, true);
      
        ed.commit();
        new SimpleEula(this).show();

        setter();
        
			        if(savedInstanceState!=null){
			        	if(savedInstanceState.containsKey("data")){
//			        		Toast.makeText(c, savedInstanceState.getString("data"), Toast.LENGTH_LONG).show();
			        		if(!savedInstanceState.getString("data").equals("null")){
			        			restoreDialog(savedInstanceState);
			        		}
			        	}
			        }
		TapjoyConnect.requestTapjoyConnect(getApplicationContext(), "06a4e933-3926-4e54-84af-236ff5377b67", "eFtChN98Ks5JDIQ3avU6");      
        Everbadge eb = new Everbadge();
        eb.requestConnection(getApplicationContext());
    }
    
    @Override
    protected void onDestroy() {
    	
    	super.onDestroy();
    	globalTracker.stopSession();
    }
    
    //this restores the dialog
    private void restoreDialog(Bundle b){
    	
    	if(b.containsKey("dialog")){
	    	if(b.getString("dialog").equals("a")) chooser();
	    	else if(b.getString("dialog").equals("su")){
	    		su = new SignupDialog(HomeScreenActivity.this, b.getString("title"));
	    		su.show();
	    		su.emailEditText.setText(b.getString("email"));
	    		if(b.getString("title").equals(CREATE_AN_ACCOUNT)){	    			
	    		
		    		su.firstNameEditText.setText(b.getString("firstName"));
		    		su.lastNameEditText.setText(b.getString("lastName"));
		    		su.loginCountrySpinner.setSelection(b.getInt("country"));
		    		su.emailAlertCheckbox.setChecked(b.getBoolean("emailAlert"));
		    		if(b.containsKey("phoneAlert")){
		    			su.phoneAlertCheckBox.setChecked(b.getBoolean("phoneAlert"));
		    			if(b.getBoolean("phoneAlert")){
		    				su.phoneCarrierSpinner.setSelection(b.getInt("phoneCarrier"));
		    				su.phoneNumberEditText.setText(b.getString("phoneNumber"));
		    			}
		 
		    		}
	    		}
	    	}
    	}
    }
    
   

    @Override
    protected void onPause() {    	
    	super.onPause();
    	
    	if(su!= null) if(su.isShowing()) su.dismiss();
    	
    	if(a!=null) if(a.isShowing())a.dismiss();
    	
    }
    
    //saves the bundle
    @Override
    protected void onSaveInstanceState(Bundle outState) {
   
    	super.onSaveInstanceState(outState);
    	outState.putString("data", "null");
    	if(su!=null){
    		
    		outState.putString("data", "red");
    		if(su.isShowing()){
    			outState.putString("dialog", "su");
    			outState.putString("title", su.DTitle);
    			outState.putString("email", su.emailEditText.getText().toString());
    			if(su.DTitle.equals(CREATE_AN_ACCOUNT)){   			
    			
	    			outState.putString("firstName", su.firstNameEditText.getText().toString());
	    			outState.putString("lastName", su.lastNameEditText.getText().toString());
	    			outState.putInt("country", su.loginCountrySpinner.getSelectedItemPosition());
	    			outState.putBoolean("emailAlert", su.emailAlertCheckbox.isChecked());
	    			if(su.loginCountrySpinner.getSelectedItem().toString().equals("United States")){
	    				outState.putBoolean("phoneAlert", su.phoneAlertCheckBox.isChecked());
	    				if(su.phoneAlertCheckBox.isChecked()){
	    					outState.putInt("phoneCarrier", su.phoneCarrierSpinner.getSelectedItemPosition());
	    					outState.putString("phoneNumber", su.phoneNumberEditText.getText().toString());
	    				}
	    			}
    			}
    		}
    	}
    	if(a!=null){
    		outState.putString("data", "red");
    		if(a.isShowing()){
    	
    		outState.putString("dialog", "a");
    		}
    	}
    	
    	
    }
    
    //set all the variables
	private void setter() {
		
		ReminderButton = new MyImageButton(getApplicationContext());
		CalendarButton = new MyImageButton(getApplicationContext());
		AccountButton = new MyImageButton(getApplicationContext());
		CalculatorsButton = new MyImageButton(getApplicationContext());
//		home = new RelativeLayout(getApplicationContext());
		ScrollTextView scrolltext = (ScrollTextView) findViewById(R.id.scrolltext);
		scrolltext.setText("Please send us feedback by clicking on the menu button and selecting the feedback option.");
		scrolltext.startScroll();
		
		ReminderButton = (MyImageButton) findViewById(R.id.homeReminderMyImageButton);
		CalendarButton = (MyImageButton) findViewById(R.id.homeCalendarMyImageButton);
		CalculatorsButton = (MyImageButton) findViewById(R.id.homeCalculatorMyImageButton);
		AccountButton = (MyImageButton) findViewById(R.id.homeAccountMyImageButton);
		
		

		ReminderButton.imageButton.setOnClickListener(Acter);
		CalendarButton.imageButton.setOnClickListener(Acter);
		AccountButton.imageButton.setOnClickListener(Acter);
		CalculatorsButton.imageButton.setOnClickListener(Acter);
		
		RelativeLayout.LayoutParams r = (LayoutParams) ReminderButton.getLayoutParams();
		RelativeLayout.LayoutParams r2 = (LayoutParams) AccountButton.getLayoutParams();
		RelativeLayout.LayoutParams r3 = (LayoutParams) CalendarButton.getLayoutParams();
//		r.setMargins(0, h/10, w/100, h/100);
		r.topMargin = h/8;
		r3.leftMargin=w/10;
		r2.topMargin=h/8;
		
		
//		Toast.makeText(c, String.format("Width: %d\nHeight: %d", w, h), Toast.LENGTH_LONG).show();
		if(w>700 && h>700 ){
			
//			calendarGridItemTextView.setTypeface(Typeface.DEFAULT_BOLD);
//			calendarGridItemTextView.setTextSize(18);
			ReminderButton.text.setTypeface(Typeface.DEFAULT_BOLD);
			CalendarButton.text.setTypeface(Typeface.DEFAULT_BOLD);
			CalculatorsButton.text.setTypeface(Typeface.DEFAULT_BOLD);
			AccountButton.text.setTypeface(Typeface.DEFAULT_BOLD);
			
			ReminderButton.text.setTextSize(18);
			CalendarButton.text.setTextSize(18);
			CalculatorsButton.text.setTextSize(18);
			AccountButton.text.setTextSize(18);
			
			
		}
		Button b2 = new Button(c);
		b2 = (Button) findViewById(R.id.homeLocalResetButton);
		b2.setText("Local Reset");
		b2.setOnClickListener(res2);
		b2.setVisibility(View.GONE);
			
		//THIS IS THE Master RESET BUTTON
		Button b = new Button(c);
		b = (Button) findViewById(R.id.homeMasterResetButton);
		b.setText("Master Reset");
		b.setOnClickListener(res);
		b.setVisibility(View.GONE);
		
		
	}
	@Override
	protected void onResume() {
		
		super.onResume();
		update();
	}
	
	//this function compares each bill to the ones online
	private void update() {
		
		
		SharedPreferences red = getSharedPreferences(APPLICATION_PREFERENCE, MODE_PRIVATE);

		if(red.getBoolean(settings_registered, false)) 	new backgroundCompareCustomer().execute();
		
	}


	private OnClickListener res2 = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SharedPreferences red = getSharedPreferences(APPLICATION_PREFERENCE, MODE_PRIVATE);
	        SharedPreferences.Editor ed = red.edit();
	        ed.clear();
	        ed.commit();
	        c.deleteDatabase(MyDbOpenHelper.DATABASE_NAME);
			
		}
	};
	
	//onclick listerner for the reset button	
	private OnClickListener res = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SharedPreferences red = getSharedPreferences(APPLICATION_PREFERENCE, MODE_PRIVATE);
	        SharedPreferences.Editor ed = red.edit();
	        ed.clear();
	        ed.commit();
	        c.deleteDatabase(MyDbOpenHelper.DATABASE_NAME);
	        
	     // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://www.tallycap.com/tallyphp/AndroidTruncate.php");
		 // Execute HTTP Post Request
	        try {
				 httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	
    //onclick listener to start the various activities
   private OnClickListener Acter = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		ImageButton i = (ImageButton) v;
		MyImageButton m = (MyImageButton) i.getParent();
		String s = m.getText();
		
		SharedPreferences red = getSharedPreferences(APPLICATION_PREFERENCE, MODE_PRIVATE);

     
		if(s.equals(ReminderButton.getText())){
			if(!red.getBoolean(settings_registered, false)) chooser();
			else startActivity(in = new Intent(getApplicationContext(), RemindersActivity.class));
		}
		else if(s.equals(CalendarButton.getText())){
			if(!red.getBoolean(settings_registered, false)) chooser();
			else startActivity(in = new Intent(getApplicationContext(), CalendarActivity.class));
		}
		else if(s.equals( CalculatorsButton.getText())){
//			startActivity(in = new Intent(getApplicationContext(), MainActivity.class));
			startActivity(in = new Intent(getApplicationContext(), CalculatorActivity.class));
		}
		
		else if(s.equals( AccountButton.getText())){
			if(!red.getBoolean(settings_registered, false)) chooser();
			else startActivity(in = new Intent(getApplicationContext(), AccountSettingsActivity.class));
		}
		
        
	        
        
       
	
	}


};


 //this function ask the user to create an account, sign in, or cancel       
private void chooser(){
	
	
	AlertDialog.Builder b = new AlertDialog.Builder(HomeScreenActivity.this);
	b.setMessage("This feature requires an Account");
	b.setPositiveButton("Signup", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {	
			su= new SignupDialog(HomeScreenActivity.this, CREATE_AN_ACCOUNT);
			su.show();
			su.emailAlertCheckbox.setChecked(true);
			
		}
	});
	b.setNeutralButton("Sign in", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {	
			su = new SignupDialog(HomeScreenActivity.this, LOGIN);
		
			su.show();
//			new SignupDialog(HomeScreenActivity.this, LOGIN).show();
		}
	});
	b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
			
		}
	});
	a = b.create();
	a.show();
}



	 public static final String[] COUNTRIES = new String[] {
		    "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
		    "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
		    "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
		    "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
		    "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
		    "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
		    "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
		    "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada", "Cape Verde",
		    "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
		    "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
		    "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
		    "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
		    "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
		    "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland",
		    "Former Yugoslav Republic of Macedonia", "France", "French Guiana", "French Polynesia",
		    "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar",
		    "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
		    "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary",
		    "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
		    "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
		    "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
		    "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
		    "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
		    "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
		    "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand",
		    "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Marianas",
		    "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
		    "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
		    "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe", "Saint Helena",
		    "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon",
		    "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal",
		    "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
		    "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Korea",
		    "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden",
		    "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "The Bahamas",
		    "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
		    "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
		    "Ukraine", "United Arab Emirates", "United Kingdom",
		    "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
		    "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara",
		    "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"
		  };




	

	 
	 
	 
	 /**
	  * 
	  * ********************************************************************
	  * this next asynctask compare and updates the database customer table
	  * ********************************************************************
	  */
	 
	 
	 class backgroundCompareCustomer extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
			String select = "SELECT * FROM customer";
			Cursor res = db.rawQuery(select, null);		
			res.moveToFirst();
			nameValuePairs = new ArrayList<NameValuePair>();
			localDateModified = Long.valueOf( res.getString(10));
			
			try {
				email=(MyCrypt.decrypt(HomeScreenActivity.MASTER_PASSWORD, res.getString(3)));
				password = (MyCrypt.decrypt(HomeScreenActivity.MASTER_PASSWORD, res.getString(4)));
				 nameValuePairs.add(new BasicNameValuePair("email", email));
			     nameValuePairs.add(new BasicNameValuePair("password", password));
			     
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}

			
	        nameValuePairs.add(new BasicNameValuePair("firstName", res.getString(0)));
	        nameValuePairs.add(new BasicNameValuePair("lastName", res.getString(1)));
	        nameValuePairs.add(new BasicNameValuePair("country", res.getString(2)));
	        
	       
	        nameValuePairs.add(new BasicNameValuePair("dateCreated", res.getString(7)));
	        nameValuePairs.add(new BasicNameValuePair("phoneAlert", res.getString(8)));
	        nameValuePairs.add(new BasicNameValuePair("emailAlert", res.getString(9)));
	        
	        if(Boolean.valueOf(res.getString(8))){
	        	 nameValuePairs.add(new BasicNameValuePair("phoneNumber", res.getString(5)));
			     nameValuePairs.add(new BasicNameValuePair("phoneCarrier", res.getString(6)));
	        }
	        
	        nameValuePairs.add(new BasicNameValuePair("dateModified", res.getString(10)));
	        nameValuePairs.add(new BasicNameValuePair("phoneVerified", res.getString(11)));
	        nameValuePairs.add(new BasicNameValuePair("ops", "Update"));
	        
	       String temp =(new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/authenticateAndroid.php", nameValuePairs);
			
	       res.close();
	       db.close();
			return temp;
		}
		 
		 
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			if(result!=null){
			if(result.length()<50) Toast.makeText(c, result, Toast.LENGTH_LONG).show();
			
			else{
				
				long dateModified;
				cv = new ContentValues();
				cv2 = new ContentValues();
				SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
				int y=0;
				try {
					
					
					JSONObject json = new JSONObject(result);
					customerID = json.getString("customerID");
					dateModified = Long.valueOf(json.getString("dateModified"));
					
					cv.put("firstName", json.getString("firstName"));
				    cv.put("lastName", json.getString("lastName"));
				    cv.put("country", json.getString("country"));
				    cv.put("emailAlert",  json.getString("emailAlert"));
				    cv.put("phoneAlert",  json.getString("phoneAlert"));
				    cv.put("dateCreated",  json.getString("dateCreated"));
				    cv.put("dateModified",  json.getString("dateModified"));
				    

				    if(json.getString("phoneVerified").equals("null"))  cv.put("phoneVerified", "false");
				    else cv.put("phoneVerified", json.getString("phoneVerified"));
				    
					customerID = json.getString("customerID");
					
					if(json.getString("phoneAlert").equals("true")){
						cv.put("phone",  json.getString("phone"));
						cv.put("phoneCarrier",  json.getString("phoneCarrier"));
					}
					
					
					StringBuffer red= new StringBuffer();
					
					red.append("CUSTOMER\n\n");
					if(dateModified> localDateModified){
						db.update("customer",cv, null, null);
						red.append(String.format("Going\nLocal: $d\nOnline: %d", localDateModified, dateModified));
						y=1;
					}
					else if(dateModified< localDateModified){
						
						String select = "SELECT * FROM customer";
						
						Cursor res = db.rawQuery(select, null);						
						res.moveToFirst();
						List<NameValuePair> nvp =nvCustomerSetup(res);
						res.close();
						(new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/androidUpdateUser.php", nvp);
						y=2;
					}
					else red.append("Executed but equal\n"+String.format("Local: %d\nOnline: %d", localDateModified, dateModified));
					if(y!=0)Toast.makeText(c, "Customer information successfully updated", Toast.LENGTH_LONG).show();
					db.close();
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
				finally{
					db.close();
				}

				
				new backgroundCompareReminder().execute();
			}
			
			}
			else Toast.makeText(c, "result is null", Toast.LENGTH_LONG).show();
			
			
			
			
		}
		 
	 }
	 
	 

	 
	 /**
	  * *******************************************************************
	  * this next asynctask compare and updates the database reminder table
	  * *******************************************************************
	  */
	 
	 
	 class backgroundCompareReminder extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("customerID", customerID));
			
			String st = (new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/androidReminderTableFetch.php", nameValuePairs);
			return st;
		}
		 
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);

							
							
						if(result.length()>10){
							try {
								Iterator(result);
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
//						else t.setText("Null result");
					
		
		}
		 
		 
		 
	 }
	 
	 
	 
	 /*
	  * This function decides what to do
	  */
	 
	 private void Iterator(String result) throws NumberFormatException, JSONException, FileNotFoundException{
		
		 JSONArray ja = null;
		
			ja = new JSONArray(result);
					
		 
		 SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
		 Cursor res = db.rawQuery("SELECT * FROM reminder", null );
		 		 
		 res.moveToFirst();
		 String s="";
		 
		s =localI( ja, result);
		s += onlineI( ja, result);


		 
		 res.close();
		 db.close();
		 if(s.indexOf("ucces")>0)Toast.makeText(c, "Synching successfull\n", Toast.LENGTH_LONG).show();
//		 else Toast.makeText(c, s, Toast.LENGTH_LONG).show();
	 }
	 
	 
	 
	 
	 //this function iterate based on the local table
	 private String localI( JSONArray ja, String result) throws NumberFormatException, JSONException, FileNotFoundException{
		 
		 String r="", s123="local";
		
		 String onlineID = "";
		 SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM reminder", null );
		res.moveToFirst();
		nv = new ArrayList<NameValuePair>();							
		nv.add(new BasicNameValuePair("email", email));
		netO = new NetworkOperations(c);
		locO = new LocalOperations(c);
		
		JSONObject jdata = new JSONObject();
				 for(int i=0; i<res.getCount(); i++){
											 
						 if(res.getString(9).equals(RemindersActivity.Create_LOCAL_ONLY)){
							 nvReminderSetup(res);
							 r = netO.ReminderCreate(nv);
//							 q+="about to create from local to online for "+ res.getString(0)+"....";
							
							 if(r.endsWith("success")){
								s123+= "success1"; 
//								q+="create online successfull\n";
								ContentValues cs = new ContentValues();
								int id = RemindersAddItem.numberFromSring(r)-1;
								cs.put("onlineID", id);							
								cs.put("status", RemindersActivity.GOOD);
								onlineID = res.getString(11);
								locO.ReminderUpdate(cs, Long.valueOf(onlineID));

							 }
						 }
						 else{
							 onlineID = res.getString(11);
							 
								 if(res.getString(9).equals(RemindersActivity.DELETE)){
									 nvReminderSetup(res);
									 s123+="success2";
//									 q+="about to delete online from local for "+  res.getString(0)+"....";
									 r= netO.ReminderDelete(nv, Long.valueOf(onlineID));
//									 t.setText(t.getText()+ "Response: "+r);
									 if(r.endsWith("success")){	
//										 t.setText(t.getText()+" about to delete "+ onlineID);
										locO.ReminderDelete(Long.valueOf(onlineID));
//										 Toast.makeText(c, String.format("Number of rows affected by deleted: %d", t1), Toast.LENGTH_LONG).show();
//										 q+="delete online and local successfull";
									 }
									 else Toast.makeText(c, r, Toast.LENGTH_LONG).show();
								 }
								 else{
//									 q+="about to check for updates online from local for "+  res.getString(0)+".....";
										
										for(int k=0; k<ja.length();k++){
													
														jdata = ja.getJSONObject(k);													
												
												if(Long.valueOf(jdata.getString("reminderID"))== Long.valueOf(onlineID)){
													
													if(compareDateModified(res, jdata)!=0)	s123="success3";												
													break;
												}												
										}									
										
								 }
							 
						 }


					 res.moveToNext();
			 }
				 
				 res.close();
				 db.close();

				
			return s123;
	 }
	 
	 
	 
	 
	 //this function iterates based on online table	 
	 private String onlineI(JSONArray ja, String result) throws JSONException{
		 String  s="online ";
		 String onlineID = "";
		 
		nv = new ArrayList<NameValuePair>();							
		nv.add(new BasicNameValuePair("email", email));
//		t.setText("Online database as based\n\n\n");
		SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
		Cursor res13 = db.rawQuery("SELECT * FROM reminder", null );
	
		 	for(int i=0; i<ja.length(); i++){
		 		res13.moveToFirst();
		 			JSONObject jdata= ja.getJSONObject(i);
					onlineID = jdata.getString("reminderID");
					
			if(jdata.getString("status").equals(RemindersActivity.Create_LOCAL_ONLY)){
				cv2Setup(jdata);
				s+="success1";
				locO.ReminderCreate(cv2);
				nvReminderSetup(jdata);
				onlineID = jdata.getString("reminderID");
				netO.ReminderUpdate(nv, Long.valueOf(onlineID));
			}
			else if(jdata.getString("status").equals(RemindersActivity.DELETE)){
//				
//				Toast.makeText(c, String.format("Number of rows affected by deleted: %d", t1), Toast.LENGTH_LONG).show();
				if(locO.ReminderDelete(Long.valueOf(onlineID))>0) s+="success2";
				nvReminderSetup(jdata);
				netO.ReminderDelete(nv,Long.valueOf(onlineID));
			}
			else{
				
				for(int k=0; k<res13.getCount(); k++){
					
//					String cccc = "i: "+String.valueOf(i)+"---k: "+String.valueOf(k)+"---resname:"+res13.getString(0)+"---jaName:"+jdata.getString("name")+"............";
									
					if(Long.valueOf(jdata.getString("reminderID"))== Long.valueOf(res13.getString(11))){
						if(compareDateModified(res13, jdata)!=0) s +="success3";
						break;
					}
					
					
					res13.moveToNext();
					
					if(k==res13.getCount()-1){
						cv2Setup(jdata);
						locO.ReminderCreate(cv2);
						break;
					}
				}
				
				
				
			}
		 		
					
		 		
		 	}
		 	res13.close();
			db.close();
			
			
			
		 return s;
	 }
	 
	 
	 
	 
	 
	 //this function setups the navevalue pair nv based on current cursor res for reminder table
	 private void nvReminderSetup(Cursor res){
			nv.add(new BasicNameValuePair("name", res.getString(0)));
			nv.add(new BasicNameValuePair("amount", res.getString(1)));	
			nv.add(new BasicNameValuePair("dateModified", res.getString(3)));
			nv.add(new BasicNameValuePair("dateCreated", res.getString(2)));
			nv.add(new BasicNameValuePair("frequency", res.getString(4)));
			nv.add(new BasicNameValuePair("parameter", res.getString(5)));
			nv.add(new BasicNameValuePair("reminder1", res.getString(6)));
			nv.add(new BasicNameValuePair("type", res.getString(10)));
	 }
	 
	 //This function setups the navevalue pair nv based on jsonObject jdata for reminder table
	private void nvReminderSetup(JSONObject jdata){
		
		try{
			nv.add(new BasicNameValuePair("name", jdata.getString("name")));
			nv.add(new BasicNameValuePair("amount", jdata.getString("amount")));	
			nv.add(new BasicNameValuePair("dateModified", jdata.getString("dateModified")));
			nv.add(new BasicNameValuePair("dateCreated", jdata.getString("dateCreated")));
			nv.add(new BasicNameValuePair("frequency", jdata.getString("frequency")));
			nv.add(new BasicNameValuePair("parameter", jdata.getString("parameter")));
			nv.add(new BasicNameValuePair("reminder1",jdata.getString("reminder1")));
			nv.add(new BasicNameValuePair("type", jdata.getString("type")));
		}catch (JSONException e) {
			// TODO: handle exception
		}
		
	}
	
	public static List<NameValuePair> nvReminderSetUp(ContentValues cv){
		List<NameValuePair> nap =new  ArrayList<NameValuePair>();
		
		nap.add(new BasicNameValuePair("name", cv.getAsString("name")));
		nap.add(new BasicNameValuePair("amount", cv.getAsString("amount")));	
		nap.add(new BasicNameValuePair("dateModified", cv.getAsString("dateModified")));
		nap.add(new BasicNameValuePair("dateCreated", cv.getAsString("dateCreated")));
		nap.add(new BasicNameValuePair("frequency", cv.getAsString("frequency")));
		nap.add(new BasicNameValuePair("parameter", cv.getAsString("parameter")));
		nap.add(new BasicNameValuePair("reminder1",cv.getAsString("reminder1")));
		nap.add(new BasicNameValuePair("type", cv.getAsString("type")));
		nap.add(new BasicNameValuePair("status", cv.getAsString("type")));
		nap.add(new BasicNameValuePair("reminderID", cv.getAsString("onlineID")));
		
		return nap;
	}
	 
	//this function setups the Contentvalue based on jsonObject for reminder table
	private void cv2Setup(JSONObject jdata) throws JSONException{
		cv2.put("name", jdata.getString("name"));
		cv2.put("amount", jdata.getString("amount"));
		cv2.put("dateCreated", jdata.getString("dateCreated"));
		cv2.put("dateModified", jdata.getString("dateModified"));
		cv2.put("frequency", jdata.getString("frequency"));
		cv2.put("parameter", jdata.getString("parameter"));
		cv2.put("reminder1", jdata.getString("reminder1"));
		cv2.put("type", jdata.getString("type"));
		cv2.put("onlineID", jdata.getString("reminderID"));
		cv2.put("status", RemindersActivity.GOOD);
	}
	 
	 
	
	
	
	
	//this function sets up a list of name value pairs for the customer table
	private List<NameValuePair> nvCustomerSetup(Cursor res){
		List<NameValuePair> nap =new  ArrayList<NameValuePair>();
		nap.add(new BasicNameValuePair("firstName", res.getString(0)));
		nap.add(new BasicNameValuePair("lastName", res.getString(1)));
		nap.add(new BasicNameValuePair("email", email));
		nap.add(new BasicNameValuePair("password", password));
		
		nap.add(new BasicNameValuePair("dateCreated", res.getString(7)));
		nap.add(new BasicNameValuePair("phoneAlert", res.getString(8)));
		nap.add(new BasicNameValuePair("emailAlert", res.getString(9)));
		nap.add(new BasicNameValuePair("dateModified", res.getString(10)));
		
		nap.add(new BasicNameValuePair("country", res.getString(2)));
		
		//phone alert
		if(res.getString(8).equals("true")){
			nap.add(new BasicNameValuePair("phone", res.getString(5)));
			nap.add(new BasicNameValuePair("phoneCarrier", res.getString(6)));
			nap.add(new BasicNameValuePair("phoneVerified", res.getString(11)));
		}
		
		
		
		return nap;
	}
	
	
	 //this function compares the dateModified field and updates accordingly
	private int compareDateModified(Cursor res, JSONObject jdata) throws NumberFormatException, JSONException{
		long localDatemodified = Long.valueOf(res.getString(3));
		int z =0;
		long onlineDatemodified = Long.valueOf(jdata.getString("dateModified"));
		if(localDatemodified!= onlineDatemodified){	
							
			if(localDatemodified>onlineDatemodified){
				nvReminderSetup(res);
				z=1;
				netO.ReminderUpdate(nv,Long.valueOf(res.getString(11)));
				
			}
			else if(onlineDatemodified>localDatemodified){
				cv2Setup(jdata);
				locO.ReminderUpdate(cv2, Long.valueOf(jdata.getString("reminderID")));
				z=2;
			}
//			t.setText(t.getText()+String.format("Local name: %s\nlocal ID: %S \n\nOnline Name: %s\nOnlineID: %S"+"\n\n\n\n", res.getString(0), res.getString(11),
//					jdata.getString("name"), jdata.getString("reminderID")));
		}
		
		return z;
	}
	 
	
	
	
	
	
}