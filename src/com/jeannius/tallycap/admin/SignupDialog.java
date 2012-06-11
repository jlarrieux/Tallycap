package com.jeannius.tallycap.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannius.tallycap.HomeScreenActivity;
import com.jeannius.tallycap.validators.EmailValidator;
import com.jeannius.tallycap.validators.NumberValidator;
import com.jeannius.tallycap.validators.TextValidator;

public class SignupDialog extends Dialog {
	
	private Button SignUp, Cancel, SignIn, ForgotPassword, SendPassword, Cancel2;
	public String DTitle;
	private String customerID;
	private Context c;
	private RelativeLayout rel;
	//private static final String CREATE_AN_ACCOUNT = "Create an account";
	private static final String LOGIN = "Login";
	
	private TextView phoneAlertTextView, phoneNumberTextView, phoneCarrierTextView, retypePasswordTextView, emailTextView, passwordTextView;
	public CheckBox phoneAlertCheckBox, emailAlertCheckbox;
	public EditText phoneNumberEditText, emailEditText, passwordEditText, retypePasswordEditText, firstNameEditText,
					lastNameEditText;
	public Spinner phoneCarrierSpinner, loginCountrySpinner;
	private String emailVal, passwordVal, retypePasswordVal,firstNameVal, LastNameVal, phoneNumberVal, Err ;
	private Dialog me;
	List<NameValuePair> nameValuePairs;
	ProgressDialog pr;
	
	
	public SignupDialog(Context context, String title) {
		super(context);
		c= context;
		this.setContentView(com.jeannius.tallycap.R.layout.create_account);
		this.setTitle(title);
		DTitle = title;
		LayoutParams p = this.getWindow().getAttributes();
		p.height = LayoutParams.WRAP_CONTENT;
		p.width = LayoutParams.FILL_PARENT;
		this.getWindow().setAttributes(p);
		setter();
//		me = new Dialog(c);
//		me = this;
	}
	
	//setup the functions
	private void setter(){
		SignIn = new Button(c);
		Cancel = new Button(c);
		SignIn = new Button(c);
		ForgotPassword = new Button(c);
		SendPassword = new Button(c);
		Cancel2 = new Button(c);
		rel= new RelativeLayout(c);
		phoneAlertCheckBox = new CheckBox(c);
		emailAlertCheckbox = new CheckBox(c);
		phoneAlertTextView = new TextView(c);
		passwordTextView = new TextView(c);
		retypePasswordTextView = new TextView(c);
		phoneCarrierSpinner = new Spinner(c);
		phoneCarrierTextView = new TextView(c);
		phoneNumberTextView = new TextView(c);
		phoneNumberEditText = new EditText(c);
		emailEditText = new EditText(c);
		passwordEditText = new EditText(c);
		retypePasswordEditText = new EditText(c);
		firstNameEditText = new EditText(c);
		lastNameEditText = new EditText(c);
		emailTextView = new TextView(c);
		me = new Dialog(c);
		me = this;
		
		pr = new ProgressDialog(c);
		pr.setIndeterminate(true);
		pr.setCancelable(false);
		
		
		SignIn = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginSignInButton);
		SignUp = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginSignUpButton);
		Cancel = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginCancelButton);
		ForgotPassword = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginForgotPasswordButton);
		Cancel2 = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginCancel2Button);
		SendPassword = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginSendPasswordButton);
		rel = (RelativeLayout) this.findViewById(com.jeannius.tallycap.R.id.LoginSignUpRelativeLayout);
		phoneAlertCheckBox = (CheckBox) this.findViewById(com.jeannius.tallycap.R.id.LoginPhoneAlertCheckBox);
		emailAlertCheckbox = (CheckBox) this.findViewById(com.jeannius.tallycap.R.id.LoginEmailAlertCheckBox);
		passwordTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.LoginPasswordTextView);
		phoneAlertTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.TextAlertTextView);		
		phoneCarrierSpinner = (Spinner) this.findViewById(com.jeannius.tallycap.R.id.LoginPhoneCarrierSpinner);
		phoneCarrierTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.PhoneCarrierTextView);
		phoneNumberTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.PhoneNumberTextView);
		phoneNumberEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginPhoneNumberEditText);
		emailEditText =(EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginEmailEditText);
		passwordEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginPasswordEditText);
		retypePasswordEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginRetypePasswordEditText);
		firstNameEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginFirstNameEditText);
		lastNameEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginLastNameEditText);
		retypePasswordTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.RetypePasswordTextView);
		emailTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.LoginEmailTextView);
		emailVal ="Good";
		passwordVal="Good";
		retypePasswordVal="Good";
		firstNameVal ="Good";
		LastNameVal ="Good";
		phoneNumberVal ="Good";
		
		phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
		ArrayAdapter<CharSequence> CarrierSpinnerArrayAdapter = ArrayAdapter.createFromResource(c, com.jeannius.tallycap.R.array.us_phone_carriers, R.layout.simple_spinner_item);
		CarrierSpinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		phoneCarrierSpinner.setAdapter(CarrierSpinnerArrayAdapter);
		
		loginCountrySpinner = new Spinner(c);
		loginCountrySpinner = (Spinner) this.findViewById(com.jeannius.tallycap.R.id.LoginCountrySpinner);
		ArrayAdapter<String> CountrySpinnerArrayAdapter = new ArrayAdapter<String>(c, R.layout.simple_spinner_item, HomeScreenActivity.COUNTRIES);

		loginCountrySpinner.setAdapter(CountrySpinnerArrayAdapter);
	
		loginCountrySpinner.setSelection(224, false);
		loginCountrySpinner.setOnItemSelectedListener(phoneAlertShowHide);
		
		phoneAlertCheckBox.setOnCheckedChangeListener(phoneDetailShowHide);
		
		
		ForgotPassword.setOnClickListener(passwordResending);
		
		SendPassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EmailValidator eval = new EmailValidator();
						pr.setMessage("Sending password..");
						pr.show();
				String valRes ="";
				emailVal = eval.validate(emailEditText.getText().toString(), true);
				
				if(!emailVal.equals("Good")){
					emailEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
					valRes += "Email "+ emailVal+"\n";
				}
				else emailEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
				
				if(valRes.length()>0){
					Toast.makeText(c, valRes, Toast.LENGTH_LONG).show();
					if(pr!=null)pr.dismiss();
				}
				else {
					String g ="";
					NetworkOperations k = new NetworkOperations(c);
					g = k.CustomerResendPassword(emailEditText.getText().toString());
					String rion="";
					if(g.indexOf("Email was sent")>0) rion ="Password successfully sent. Please wait a few minutes and check your email.";
					else if(g.indexOf("etwork")>0 ) rion =g +". Data will update later.";
					else if(g.indexOf("data")>0) rion =g + ". Data will update later.";
					else if(g.indexOf("No match")>0) rion = "Invalid user";
					else rion ="An error occurred. Please try again later.";
					if(pr!=null)pr.dismiss();
					Toast.makeText(c, 	rion, Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		Cancel2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dSetUp();
				
			}
		});
		
		Cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			cancel();
				
			}
		});
		dSetUp();
		
		SignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				verifyerSignUp();
			}
		});
		
		SignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				verifyerSignIn();
				
			}
		});
		
		phoneAlertCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					phoneNumberEditText.setVisibility(View.VISIBLE);
				
					phoneNumberTextView.setVisibility(View.VISIBLE);
					phoneCarrierSpinner.setVisibility(View.VISIBLE);
					phoneCarrierTextView.setVisibility(View.VISIBLE);
					phoneNumberEditText.requestFocus();
					AlertDialog.Builder b = new Builder(c);
					b.setMessage("Standard Text Messaging rates apply. Contact your cell phone carrier for details.");
					b.setCancelable(false);
					b.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							
						}
					});
					
					b.show();
				}
				else{
					phoneNumberTextView.setVisibility(View.GONE);
					phoneCarrierSpinner.setVisibility(View.GONE);
					phoneCarrierTextView.setVisibility(View.GONE);
				}
			}
		});
		
	}
	
	//this view listener makes the dialog looks like a password resend
	private View.OnClickListener passwordResending = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			passwordEditText.setVisibility(View.GONE);
			passwordTextView.setVisibility(View.GONE);
			ForgotPassword.setVisibility(View.GONE);
			
			SignIn.setVisibility(View.GONE);
			setTitle("Retrieve Password");
			SendPassword.setVisibility(View.VISIBLE);
			Cancel2.setVisibility(View.VISIBLE);
			
		}
	};
	
	//this function verify the input for the signin screen
	private void verifyerSignIn(){
		EmailValidator eval = new EmailValidator();
		TextValidator textval = new TextValidator();
		
		String valRes ="";
		
		emailVal = eval.validate(emailEditText.getText().toString(), true);
		passwordVal = textval.validate(passwordEditText, true, 1);
		
		
		//email
		if(!emailVal.equals("Good")){
			emailEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "Email "+ emailVal+"\n";
		}
		else emailEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
		
		//password
		if(!passwordVal.equals("Good")){
			passwordEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "Password "+ passwordVal+"\n";
		}
		else passwordEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
		
		if(valRes.length()>0) Toast.makeText(c, valRes, Toast.LENGTH_LONG).show();
		else new backgroundSignInTask().execute();
		
	}
	
	
	
	
	//this function verify the input for the signup screen
	private void verifyerSignUp(){
		EmailValidator eval = new EmailValidator();	
		TextValidator textval = new TextValidator();
		NumberValidator numbval = new NumberValidator();
		String valRes ="";
		emailVal = eval.validate(emailEditText.getText().toString(), true);
		passwordVal = textval.validate(passwordEditText, true, 3);
		if(passwordEditText.getText().toString().equals(retypePasswordEditText.getText().toString())) retypePasswordVal ="Good";
		else retypePasswordVal =" does not match password";
		firstNameVal = textval.validate(firstNameEditText, true, 2);
		LastNameVal = textval.validate(lastNameEditText, false, 2);
		if(phoneAlertCheckBox.isChecked()){
			phoneNumberVal = numbval.phoneNumberValidate(phoneNumberEditText, true);
			
			//phone number
			if(!phoneNumberVal.equals("Good")){
				phoneNumberEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
				valRes += "Phone Number "+ phoneNumberVal+"\n";
			}
			
		}
		
			
		
		//email
		if(!emailVal.equals("Good")){
			emailEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "Email "+ emailVal+"\n";
		}
		else emailEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
		
		//password
		if(!passwordVal.equals("Good")){
			passwordEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "Password "+ passwordVal+"\n";
		}
		else passwordEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
				
		//retypePassword
		if(!retypePasswordVal.equals("Good")){
			retypePasswordEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "Retype "+ retypePasswordVal+"\n";
		}
		else retypePasswordEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
		
		//firstName
		if(!firstNameVal.equals("Good")){
			firstNameEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "First name "+ firstNameVal+"\n";
		}
		else firstNameEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
		
		
		//lastName
		if(!LastNameVal.equals("Good")){
			lastNameEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "Last name "+ LastNameVal+"\n";
		}
		else lastNameEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
		
		
		if(valRes.length()>0) Toast.makeText(c, valRes, Toast.LENGTH_LONG).show();
		else{
			new backgroundSignUp().execute("Red");
			
		}
		
	}
	

	//this functions takes out the dashes from the phone number
	
	public static String takeDahesOut(EditText red){
		
		String s = "";
		
		if(red.getText().toString().length()==0) s="";
		else{
			String temp = red.getText().toString();
			String t2 ="";
			for(int i =0; i<temp.length(); i++){
				t2 = String.valueOf(temp.charAt(i));
				if(!t2.equals("-"))
				s+= t2;
			}
			
		}
		
		return s;
	}
	
	
	//this function add dashes to a phone number
	public static String addDash(String red){
		String s="";
		
		if(red.length()==0) s="";
		else{
			String temp = red;
			ArrayList<String> g =new ArrayList<String>();
			for(int i=0; i< temp.length(); i++){
				g.add(i, String.valueOf(temp.charAt(i)));
			}
			g.add(3, "-");
			g.add(7, "-");
			
			for(int j=0; j<g.size(); j++){
				s+= g.get(j);
			}
			
		}
		
		
		return s;
	}
	
	
	
	//this function hide or show the layout for login vs signup
	private void dSetUp(){
		
		setTitle(DTitle);
		ForgotPassword.setVisibility(View.GONE);
		SendPassword.setVisibility(View.GONE);
		Cancel2.setVisibility(View.GONE);
			if(DTitle.equals(LOGIN)){
				rel.setVisibility(View.GONE);
				retypePasswordEditText.setVisibility(View.GONE);
				retypePasswordTextView.setVisibility(View.GONE);
				SignUp.setVisibility(View.GONE);
				Cancel.setVisibility(View.VISIBLE);
				ForgotPassword.setVisibility(View.VISIBLE);
				passwordEditText.setVisibility(View.VISIBLE);
				passwordTextView.setVisibility(View.VISIBLE);
				SignIn.setVisibility(View.VISIBLE);
				RelativeLayout.LayoutParams gg =  (android.widget.RelativeLayout.LayoutParams) emailTextView.getLayoutParams();
				gg.leftMargin=50;
				
				RelativeLayout.LayoutParams l1 = (android.widget.RelativeLayout.LayoutParams) emailEditText.getLayoutParams();
				l1.width=-1;
				l1.rightMargin=10;
				
				RelativeLayout.LayoutParams l2 = (android.widget.RelativeLayout.LayoutParams) passwordEditText.getLayoutParams();
				l2.width=-1;
				l2.rightMargin=10;
			}
			else{
				rel.setVisibility(View.VISIBLE);
				retypePasswordEditText.setVisibility(View.VISIBLE);
				retypePasswordTextView.setVisibility(View.VISIBLE);
				SignIn.setVisibility(View.GONE);
				SignUp.setVisibility(View.VISIBLE);
			}
	
	}
	
	//this function hide or show phone alert if US not selected
	
	private OnItemSelectedListener phoneAlertShowHide = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(loginCountrySpinner.getSelectedItemPosition()==224){
				phoneAlertTextView.setVisibility(View.VISIBLE);
				phoneAlertCheckBox.setVisibility(View.VISIBLE);
				if(phoneAlertCheckBox.isChecked()){
					phoneNumberEditText.setVisibility(View.VISIBLE);
					phoneNumberTextView.setVisibility(View.VISIBLE);
					phoneCarrierSpinner.setVisibility(View.VISIBLE);
					phoneCarrierTextView.setVisibility(View.VISIBLE);
					
					
					phoneNumberEditText.requestFocus();
				}
			}
			else {
				phoneAlertTextView.setVisibility(View.GONE);
				phoneAlertCheckBox.setVisibility(View.GONE);
				phoneNumberEditText.setVisibility(View.GONE);
				phoneNumberTextView.setVisibility(View.GONE);
				phoneCarrierSpinner.setVisibility(View.GONE);
				phoneCarrierTextView.setVisibility(View.GONE);
			}			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}
	};
		
	//this function hide phonenumber and phonecarrier if checkbox not selected	
		private OnCheckedChangeListener phoneDetailShowHide = new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(phoneAlertCheckBox.isChecked()){
					phoneNumberEditText.setVisibility(View.VISIBLE);
					phoneNumberTextView.setVisibility(View.VISIBLE);
					phoneCarrierSpinner.setVisibility(View.VISIBLE);
					phoneCarrierTextView.setVisibility(View.VISIBLE);
					phoneNumberEditText.requestFocus();
				}
				else{
					phoneNumberEditText.setVisibility(View.GONE);
					phoneNumberTextView.setVisibility(View.GONE);
					phoneCarrierSpinner.setVisibility(View.GONE);
					phoneCarrierTextView.setVisibility(View.GONE);
				}
				
			}
		};
		
	
		
	
			
			
			
		
		
		//this background task create the user on the server in the clouds
		class backgroundSignUp extends AsyncTask<String, Void, String>{

			@Override
			protected void onPreExecute() {				
				super.onPreExecute();
				pr.setMessage("Creating User...");
				pr.show();
				
			}
			
			@Override
			protected String doInBackground(String... arg0) {
						    
			    String temp ="";		    
			
			    	Date now = new Date();
			    			    	
			        nameValuePairs = new ArrayList<NameValuePair>(13);
			        nameValuePairs.add(new BasicNameValuePair("firstName", firstNameEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("lastName", lastNameEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("country", loginCountrySpinner.getSelectedItem().toString()));
			        nameValuePairs.add(new BasicNameValuePair("email", emailEditText.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("password", passwordEditText.getText().toString()));
			       
			        nameValuePairs.add(new BasicNameValuePair("dateCreated", String.valueOf(now.getTime())));
			        nameValuePairs.add(new BasicNameValuePair("phoneAlert", String.valueOf(phoneAlertCheckBox.isChecked())));
			        nameValuePairs.add(new BasicNameValuePair("emailAlert", String.valueOf(emailAlertCheckbox.isChecked())));
			        
			        if(phoneAlertCheckBox.isChecked()){
			        	 nameValuePairs.add(new BasicNameValuePair("phoneNumber", takeDahesOut(phoneNumberEditText)));
					     nameValuePairs.add(new BasicNameValuePair("phoneCarrier", phoneCarrierSpinner.getSelectedItem().toString()));
			        }
			        			      
			        temp =String.valueOf(Html.fromHtml((new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/AndroidcreateUserTest.php", nameValuePairs)));
			        
			    
			    
				return temp;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(String result) {				
				super.onPostExecute(result);
				if(pr!=null)pr.dismiss();
				
				
				if(result.indexOf("ser")>0){
					me.dismiss();
					
					 SharedPreferences red = c.getSharedPreferences(HomeScreenActivity.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
				     SharedPreferences.Editor ed = red.edit();
				     try {
						String encry_email = MyCrypt.encrypt(HomeScreenActivity.MASTER_PASSWORD, emailEditText.getText().toString());
						String encry_pass = MyCrypt.encrypt(HomeScreenActivity.MASTER_PASSWORD, passwordEditText.getText().toString());
						

						ed.putBoolean(HomeScreenActivity.settings_registered, true);
					     ed.commit();
						ArrayList<String> arg = new ArrayList<String>();
						arg.add(0,encry_email);
						arg.add(1, encry_pass);
						arg.add(2, firstNameEditText.getText().toString());
						arg.add(3, lastNameEditText.getText().toString());
						arg.add(4, loginCountrySpinner.getSelectedItem().toString());
						arg.add(5, String.valueOf(emailAlertCheckbox.isChecked()));
						arg.add(6, String.valueOf(phoneAlertCheckBox.isChecked()));
						
						if(phoneAlertCheckBox.isChecked()){
							arg.add(7, takeDahesOut(phoneNumberEditText));
							arg.add(8, phoneCarrierSpinner.getSelectedItem().toString());
						}
						new dbCreator().execute(arg);
						
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				     
				     
				     
				}
				Toast.makeText(c, result, Toast.LENGTH_LONG).show();
			}
			
		}
	
		
		/**
		 * 
		 * @author Jeannius
		 * this task create the user on the local database
		 *
		 */
		//
	class dbCreator extends AsyncTask<ArrayList<String>, Void, String>{

		@Override
			protected void onPreExecute() {				
				super.onPreExecute();
				pr.setMessage("Creating local files...");
				pr.show();
			}
		
		@Override
		protected String doInBackground(ArrayList<String>... params) {
			SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
			ContentValues cv = new ContentValues();
			Date myd = new Date();

			
			cv.put("email", params[0].get(0));
			cv.put("password", params[0].get(1));
			cv.put("firstName", params[0].get(2));
			cv.put("lastName", params[0].get(3));
			cv.put("country", params[0].get(4));
			cv.put("emailAlert", params[0].get(5));
			cv.put("phoneAlert", params[0].get(6));
			cv.put("dateCreated", myd.getTime());
			cv.put("dateModified", myd.getTime());
			cv.put("phoneVerified", "false");
			if(phoneAlertCheckBox.isChecked()){
				cv.put("phone", params[0].get(7));
				cv.put("phoneCarrier", params[0].get(8));
			}
			
			db.insertOrThrow("customer", null, cv);
			
			db.close();
			
			return null;
		}
		
		@Override
			protected void onPostExecute(String result) {
				
				super.onPostExecute(result);
				if(pr!=null)pr.dismiss();
				
				
			}

			
		
	}
	
	
	
	
	
	/**
	 * ******************************************************
	 * ******************************************************
	 * @author Jeannius
	 * This next asynctask check for user during signin
	 * ********************************************************
	 *********************************************************
	 */
	
	//this check for user
	class backgroundSignInTask extends AsyncTask<Void, Void, String> {

		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pr.setMessage("Checking for user...");
			pr.show();
		}
		
		
		@Override
		protected String doInBackground(Void... params) {
			// Create a new HttpClient and Post Header
		    
		    String temp ="";	 
		    	
		    	nameValuePairs = new ArrayList<NameValuePair>();
		        
		        nameValuePairs.add(new BasicNameValuePair("email", emailEditText.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("password", passwordEditText.getText().toString()));
		        		       
		        temp =String.valueOf(Html.fromHtml((new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/authenticateAndroid.php", nameValuePairs)));		        
		    	    	
			return temp;
		
		}
		
		
		protected void onPostExecute(String result) {
			
			if(pr!=null)pr.dismiss();

			if(result.length()<100) Toast.makeText(c,  result+"\n", Toast.LENGTH_LONG).show();
			else if(result.equals("e")) Toast.makeText(c, Err, Toast.LENGTH_LONG).show();
			else{
				 SharedPreferences red = c.getSharedPreferences(HomeScreenActivity.APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
			     SharedPreferences.Editor ed = red.edit();
				try {
					JSONObject json = new JSONObject(result);
					Toast.makeText(c,  "Login Successful", Toast.LENGTH_LONG).show();
										
					
					SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
					ContentValues cv = new ContentValues();
					
					
					String encry_email = MyCrypt.encrypt(HomeScreenActivity.MASTER_PASSWORD, emailEditText.getText().toString());
					String encry_pass = MyCrypt.encrypt(HomeScreenActivity.MASTER_PASSWORD, passwordEditText.getText().toString());
					ed.putBoolean(HomeScreenActivity.settings_registered, true);
				    ed.commit();
				    cv.put("email", encry_email);
					
				    cv.put("password", encry_pass);
				    
				    
				    cv.put("firstName", json.getString("firstName"));
				    cv.put("lastName", json.getString("lastName"));
				    cv.put("country", json.getString("country"));
				    cv.put("emailAlert",  json.getString("emailAlert"));
				    cv.put("phoneAlert",  json.getString("phoneAlert"));
				    cv.put("dateCreated",  json.getString("dateCreated"));
				    cv.put("dateModified",  json.getString("dateModified"));
//				    Toast.makeText(c, json.getString("phoneVerified"), Toast.LENGTH_LONG).show(); 
				    if(json.getString("phoneVerified").equals("null"))  cv.put("phoneVerified", "false");
				    else cv.put("phoneVerified", json.getString("phoneVerified"));
					customerID = json.getString("customerID");
					
					if(json.getString("phoneAlert").equals("true")){
						cv.put("phone",  json.getString("phone"));
						cv.put("phoneCarrier",  json.getString("phoneCarrier"));
					}
					
					
					db.insertOrThrow("customer", null, cv);
					db.close();

					new dataFetch().execute();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					Cancel.performClick();
				

				
			}
			
		};
	
	
	}
	
	
	/**
	 * 
	 * this next asynctask fetch the data from the clouds
	 * 
	 */
	
	class dataFetch extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {			
			super.onPreExecute();
			pr.setMessage("Updating data...");
			pr.show();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("customerID", customerID));
			
			String st = (new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/androidReminderTableFetch.php", nameValuePairs);
			return st;
		}
		
		
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);
			if(pr!=null)pr.dismiss();

//			Toast.makeText(c, "Result length: "+ String.valueOf(result.length()), Toast.LENGTH_LONG).show();
			if(result.length()>10){
				SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
			try {
				JSONArray ja= new JSONArray(result);
				
				for(int i=0; i<ja.length(); i++){
					JSONObject jdata = ja.getJSONObject(i);
					ContentValues co = new ContentValues();
					
					co.put("name", jdata.getString("name"));
					co.put("amount", jdata.getString("amount"));
					co.put("dateCreated", jdata.getString("dateCreated"));
					co.put("dateModified", jdata.getString("dateModified"));
					co.put("frequency", jdata.getString("frequency"));
					co.put("parameter", jdata.getString("parameter"));
					co.put("reminder1", jdata.getString("reminder1"));
					co.put("type", jdata.getString("type"));
					co.put("onlineID", jdata.getString("reminderID"));
					co.put("status", "Good");
					
					db.insertOrThrow("reminder", null, co);
					
				}
				
				
				
				
			} catch (JSONException e) {
				Log.e("Error", "Error parsing Json data "+ e.toString());
				e.printStackTrace();
			}
			finally{
				db.close();
			}
			
			}
			
			
		}
		
	}
	
	
	
	
	
}

