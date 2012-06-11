package com.jeannius.tallycap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannius.tallycap.admin.ChangePasswordDialog;
import com.jeannius.tallycap.admin.MyCrypt;
import com.jeannius.tallycap.admin.MyDbOpenHelper;
import com.jeannius.tallycap.admin.NetworkOperations;
import com.jeannius.tallycap.admin.PhoneVerificationDialog;
import com.jeannius.tallycap.admin.SignupDialog;
import com.jeannius.tallycap.admin.UpdateDialog;
import com.jeannius.tallycap.validators.NumberValidator;

public class AccountSettingsActivity extends TrackedActivity {
	
	
	private TextView email, firstName, lastName, Country, emailAlert, phoneAlert, phoneCarrier, phoneNumber;
	private RelativeLayout lay;
	private Context c;
	private Button edit, changePassword, verifyButton, resendButton;
	private UpdateDialog up;
	private ChangePasswordDialog ch;
	public static String z;
	private long dateInMilli;
	private String ops, code, em;
	private PhoneVerificationDialog ph;
	ProgressDialog pr;
	private ContentValues cv;
	private List<NameValuePair>  nameValuePairs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_settings_main);
		
		c = AccountSettingsActivity.this.getApplicationContext();

		setter();
		restore(savedInstanceState);
	}
	
	private void restore(Bundle b){
		
		if(b!=null){
			if(b.containsKey("dialog")){
				if(b.getString("dialog").equals("ch")) ch.show();
				else if(b.getString("dialog").equals("up")){
					Date now = new Date();
					dateInMilli =now.getTime();
					up =	new UpdateDialog(AccountSettingsActivity.this, dateInMilli);
					up.show();
					up.firstNameEditText.setText(b.getString("firstName"));
		    		up.lastNameEditText.setText(b.getString("lastName"));
		    		up.loginCountrySpinner.setSelection(b.getInt("country"));
		    		up.emailAlertCheckbox.setChecked(b.getBoolean("emailAlert"));
		    		if(b.containsKey("phoneAlert")){
		    			up.phoneAlertCheckBox.setChecked(b.getBoolean("phoneAlert"));
		    			if(b.getBoolean("phoneAlert")){
		    				up.phoneCarrierSpinner.setSelection(b.getInt("phoneCarrier"));
		    				up.phoneNumberEditText.setText(b.getString("phoneNumber"));
		    			}
		 
		    		}
				}
			}
			
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	
		super.onSaveInstanceState(outState);
		if(ch!=null)if(ch.isShowing()) outState.putString("dialog", "ch");
		
		if(up!=null) if(up.isShowing()){
			outState.putString("dialog", "up");
			outState.putString("firstName", up.firstNameEditText.getText().toString());
			outState.putString("lastName", up.lastNameEditText.getText().toString());
			outState.putInt("country", up.loginCountrySpinner.getSelectedItemPosition());
			outState.putBoolean("emailAlert", up.emailAlertCheckbox.isChecked());
			if(up.loginCountrySpinner.getSelectedItem().toString().equals("United States")){
				outState.putBoolean("phoneAlert", up.phoneAlertCheckBox.isChecked());
				if(up.phoneAlertCheckBox.isChecked()){
					outState.putInt("phoneCarrier", up.phoneCarrierSpinner.getSelectedItemPosition());
					outState.putString("phoneNumber", up.phoneNumberEditText.getText().toString());
				}
			}
		}
		
	}

	@Override
	protected void onPause() {
	
		super.onPause();
		if(ch!=null) if(ch.isShowing()) ch.dismiss();
		if(up!=null) if(up.isShowing()) up.dismiss();
	}
	
	//this function instantiate the variables
	private void setter() {
		
		z="";
		email = new TextView(c);
		firstName = new TextView(c);
		lastName = new TextView(c);
		Country = new TextView(c);
		emailAlert = new TextView(c);
		phoneAlert = new TextView(c);
		phoneCarrier = new TextView(c);
		phoneNumber = new TextView(c);
		lay = new RelativeLayout(c);
		edit = new Button(c);
		changePassword = new Button(c);
		verifyButton = new Button(c);
		resendButton= new Button(c);
		
		pr = new ProgressDialog(AccountSettingsActivity.this);
		pr.setIndeterminate(true);
		pr.setCancelable(false);
		ch = new ChangePasswordDialog(AccountSettingsActivity.this, "Change your password", email.getText().toString());
		
		email = (TextView) findViewById(R.id.accountSettingsEmailEditText);
		firstName = (TextView) findViewById(R.id.accountSettingsFirstNameEditText);
		lastName = (TextView) findViewById(R.id.accountSettingsLastNameEditText);
		Country = (TextView) findViewById(R.id.accountSettingsCountryEditText);
		emailAlert = (TextView) findViewById(R.id.accountSettingsEmailAlertEditText);
		phoneAlert = (TextView) findViewById(R.id.accountSettingsPhoneAlertEditText);
		phoneCarrier = (TextView) findViewById(R.id.accountSettingsPhoneCarrierEditText);
		phoneNumber = (TextView) findViewById(R.id.accountSettingsPhoneNumberEditText);
		lay = (RelativeLayout) findViewById(R.id.AccountSettingsLinLayout);
		lay.setVisibility(View.GONE);
		edit = (Button) findViewById(R.id.accountSettingsEditButton);
		changePassword = (Button) findViewById(R.id.accountSettingsChangePasswordButton);
		verifyButton = (Button) findViewById(R.id.accountSettingsVerifyPhoneButton);
		resendButton = (Button) findViewById(R.id.accountSettingsResendButton);
		
		verifyButton.setOnClickListener(phoneVerification);
		resendButton.setOnClickListener(phoneVerification);
		edit.setOnClickListener(editor);
		changePassword.setOnClickListener(change);
			

		populate();
	}

	
	private View.OnClickListener phoneVerification = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String s= ((Button)v).getText().toString();
			
			if(s.equals("Verify Phone Number")){
				ph = new PhoneVerificationDialog(AccountSettingsActivity.this);
				ph.show();
				ph.verify.setOnClickListener(verifyer);
				ph.resend.setOnClickListener(sender);
			}
			else{
				ops= "resend";
				new phoneVerifyResend().execute();
				
			}
			
			
		}
	};
	
	
	//this verify the phoneverification dialog input
	private View.OnClickListener verifyer = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String val = new NumberValidator().validate(ph.codeEditText, 0.1, true, 999999999.0, false);
			
			if(val.equals("Good")){
				ph.codeEditText.setBackgroundResource(R.drawable.edit_text);
				ops = "verify";
				code = ph.codeEditText.getText().toString();
				new phoneVerifyResend().execute();
			}
			else{
				ph.codeEditText.setBackgroundResource(R.drawable.fight);
				Toast.makeText(c, "Code "+val, Toast.LENGTH_LONG).show();
			}
			
		}
	};
	
	
	//this function calls the phoneverification online 
	private View.OnClickListener sender = new View.OnClickListener() {		
		
		@Override
		public void onClick(View v) {
			ops= "resend";
			new phoneVerifyResend().execute();
			
		}
	};
	
	//this functions populate the fields
	private void populate() {		
		
		SQLiteDatabase db = new MyDbOpenHelper(c).getReadableDatabase();		
		String select = "SELECT * FROM customer";
		Cursor res = db.rawQuery(select, null);		
		res.moveToFirst();
		
		try {
			email.setText(MyCrypt.decrypt(HomeScreenActivity.MASTER_PASSWORD, res.getString(3)));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
			
	    
		firstName.setText(res.getString(0));
		
		
		lastName.setText(res.getString(1));
		
		Country.setText(res.getString(2));
		

		if(res.getString(9).equals("false")) emailAlert.setText("No");
		else emailAlert.setText("Yes");
		
		
		if(res.getString(8).equals("false")){
			phoneAlert.setText("No");
			
			verifyButton.setVisibility(View.GONE);
			resendButton.setVisibility(View.GONE);
			lay.setVisibility(View.GONE);
		}
		else{
			phoneAlert.setText("Yes");
			lay.setVisibility(View.VISIBLE);
						
			phoneNumber.setText(SignupDialog.addDash(res.getString(5)));
			
			phoneCarrier.setText(res.getString(6));
			
			if(res.getString(11).equals("true")){
				verifyButton.setVisibility(View.GONE);
				resendButton.setVisibility(View.GONE);
			}
			else{
				verifyButton.setVisibility(View.VISIBLE);
				resendButton.setVisibility(View.VISIBLE);
			}
			
		}
		
		res.close();
		db.close();
		
	}
	
	//this function sets the change password dialog
	private OnClickListener change = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			ch.show();
			
		}
	};
	
	
	//this function edits the fields
	
	private OnClickListener editor =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		Date now = new Date();
		dateInMilli =now.getTime();
		up =	new UpdateDialog(AccountSettingsActivity.this, dateInMilli);
		up.show();
		up.firstNameEditText.setText(firstName.getText().toString());
		up.lastNameEditText.setText(lastName.getText().toString());
		
		if(emailAlert.getText().toString().equals("Yes")) up.emailAlertCheckbox.setChecked(true);
		else up.emailAlertCheckbox.setChecked(false);
		
		if(phoneAlert.getText().toString().equals("Yes")){
			up.phoneAlertCheckBox.setChecked(true);
			up.phoneNumberEditText.setText(phoneNumber.getText().toString());
			
			List<String> carrierList = Arrays.asList(getResources().getStringArray(R.array.us_phone_carriers));
			up.phoneCarrierSpinner.setSelection(carrierList.indexOf(phoneCarrier.getText().toString()));
		}
		else up.phoneAlertCheckBox.setChecked(false);
		
		
		List<String> paisList =Arrays.asList(HomeScreenActivity.COUNTRIES);
		up.loginCountrySpinner.setSelection(paisList.indexOf(Country.getText().toString()));
		
		up.Update.setOnClickListener(actualUpdate);
		
		
		
		
			
		}
	};
	
	private OnClickListener actualUpdate = new OnClickListener() {
		
		@Override
		public void onClick(View v) {			


			if(!(up.verifyer().length()>0)){
				cvCreator();
				new backgrounUpdate().execute();
			}


			
		}
	};
	
	
	
	
	/**
	 * *********************
	 * @author Jeannius
	 * updates the user data online
	 ******************************
	 */
	//update the 
	class updateDbBackground extends AsyncTask<Void, Void, String>{

		
		@Override
		protected String doInBackground(Void... params) {
			SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();				
			
			db.update("customer",cv, null, null);
			db.close();
			return null;
		}
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);
			populate();
		}
		
	}
	
	
	//this function creates the cd and namvelues
	
	private void cvCreator(){
		SQLiteDatabase db = new MyDbOpenHelper(c).getReadableDatabase();		
		String select = "SELECT email FROM customer";
		Cursor res = db.rawQuery(select, null);		
		res.moveToFirst();
		
		// Add your data
        nameValuePairs = new ArrayList<NameValuePair>(13);
        nameValuePairs.add(new BasicNameValuePair("firstName", up.firstNameEditText.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("lastName", up.lastNameEditText.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("country", up.loginCountrySpinner.getSelectedItem().toString()));
        try {
			nameValuePairs.add(new BasicNameValuePair("email", MyCrypt.decrypt(HomeScreenActivity.MASTER_PASSWORD, res.getString(0))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		        
       
        nameValuePairs.add(new BasicNameValuePair("dateModified", String.valueOf(up.dateInM)));
        nameValuePairs.add(new BasicNameValuePair("phoneAlert", String.valueOf(up.phoneAlertCheckBox.isChecked())));
        nameValuePairs.add(new BasicNameValuePair("emailAlert", String.valueOf(up.emailAlertCheckbox.isChecked())));
        
        if(up.phoneAlertCheckBox.isChecked()){
        	 nameValuePairs.add(new BasicNameValuePair("phoneNumber", SignupDialog.takeDahesOut(up.phoneNumberEditText)));
		     nameValuePairs.add(new BasicNameValuePair("phoneCarrier", up.phoneCarrierSpinner.getSelectedItem().toString()));
        }
		
        db.close();
        res.close();
        
        cv =  new ContentValues();
        cv.put("firstName",up.firstNameEditText.getText().toString());
		cv.put("lastName", up.lastNameEditText.getText().toString());
		cv.put("country", up.loginCountrySpinner.getSelectedItem().toString());
		cv.put("emailAlert",String.valueOf( up.emailAlertCheckbox.isChecked()));
		cv.put("phoneAlert",String.valueOf(up.phoneAlertCheckBox.isChecked()));
		cv.put("dateModified", dateInMilli);
		
		if(up.phoneAlertCheckBox.isChecked()){
			cv.put("phone", SignupDialog.takeDahesOut(up.phoneNumberEditText));
			cv.put("phoneCarrier", up.phoneCarrierSpinner.getSelectedItem().toString());
		}
		else cv.put("phoneVerified", "false");
		
	}
	
	/**
	 * 
	 * this asynctask verify phone number or resend verification
	 * 
	 * 
	 */
	
	public class phoneVerifyResend extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {			
			super.onPreExecute();
			
			StringBuffer mes= new StringBuffer();
			if (ops.equals("verify")) mes.append("Verifying code...");
			else mes.append("Resending code...");
			
			pr.setMessage(mes.toString());
			pr.show();
		}
		
		
		@Override
		protected String doInBackground(Void... params) {
			
			
			String temp ="";
		
			SQLiteDatabase db = new MyDbOpenHelper(c).getReadableDatabase();		
			String select = "SELECT email FROM customer";
			Cursor res = db.rawQuery(select, null);		
			res.moveToFirst();	
			
			List<NameValuePair> l = new ArrayList<NameValuePair>();
			l.add(new BasicNameValuePair("ops",ops));
			try {
				em = String.valueOf(MyCrypt.decrypt(HomeScreenActivity.MASTER_PASSWORD,res.getString(0)));
				l.add(new BasicNameValuePair("email",em));
				l.add(new BasicNameValuePair("ops", ops));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			if(ops.equals("verify")) l.add(new BasicNameValuePair("code",code));
			
			temp = String.valueOf(Html.fromHtml((new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/androidPhoneVerification.php",l)));
		    
			res.close();
			db.close();
			return temp;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			pr.dismiss();
//			Toast.makeText(c, ops+"\n"+em, Toast.LENGTH_LONG).show();
			String resend ="Please check your text messages for the verification code";
			if(ops.equals("verify")){
				
				if(result.endsWith("success")){
					ph.dismiss();
					Toast.makeText(c, "Phone number successfully verified", Toast.LENGTH_LONG).show();
					ContentValues cv = new ContentValues();
					cv.put("phoneVerified", "true");
					SQLiteDatabase db = new MyDbOpenHelper(c).getReadableDatabase();	
					
					db.update("customer", cv, null, null);
					db.close();
					populate();
				}
				else Toast.makeText(c, result, Toast.LENGTH_LONG).show();
			}
			else{
			 if (result.endsWith("success")) 	Toast.makeText(c, resend, Toast.LENGTH_LONG).show();
			 else Toast.makeText(c, result, Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	
	
	
	/**
	 * 
	 * 
	 * this function updates the user account online
	 * @author Jeannius
	 *
	 */
	
	class backgrounUpdate extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {

		    String temp = (new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/androidUpdateUser.php", nameValuePairs);
		   

			return temp;
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			
			String s="";
			if(result.indexOf("ucce")>0)	s= "User data successfully updated";
				
			
			else if(result.indexOf("etwork")>0) s= result+". Data will update later.";
			else if (result.indexOf("data")>0) s= result+". Data will update later.";
			else s ="An error occurred. Please try again later."+ result;
			
			up.dismiss();
			Toast.makeText(c, s, Toast.LENGTH_LONG).show();
			new updateDbBackground().execute();
			
			
		}
		
	}
	
	

}
