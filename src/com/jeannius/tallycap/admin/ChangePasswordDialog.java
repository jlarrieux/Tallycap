package com.jeannius.tallycap.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannius.tallycap.HomeScreenActivity;
import com.jeannius.tallycap.R;
import com.jeannius.tallycap.validators.TextValidator;

public class ChangePasswordDialog extends Dialog {
	
	private Context c;
	public Button Update, Cancel, signUp, forgotPassword, backtoSignin, send;
	public EditText phoneNumberEditText, oldPasswordEditText, passwordEditText, retypePasswordEditText, firstNameEditText, lastNameEditText;
	public Spinner loginCountrySpinner;
	public CheckBox phoneAlertCheckBox, emailAlertCheckbox;
	private Dialog me;
	private TextView emailTextView, passwordTextView,  firstNameTextView, lastNameTextView, countryTextView, emailAlertTextView, phoneAlertTextView;
	private long dateInM;
	private String oldPasswordVal, newPasswordVal, retypePasswordVal;
	
	public ChangePasswordDialog(Context context, String title, String em) {
		super(context);
		c= context;
		this.setContentView(R.layout.create_account);
		this.setTitle(title);
		setter();
		me = new Dialog(c);
		me = this;
		comestics();
		
	}

	private void setter() {
		
		Update = new Button(c);
		Cancel = new Button(c);
		signUp = new Button(c);
		send  = new Button(c);
		forgotPassword = new Button(c);
		backtoSignin = new Button(c);
		
		emailTextView = new TextView(c);
		passwordTextView = new TextView(c);
		
		firstNameTextView= new TextView(c);
		lastNameTextView= new TextView(c); 
		countryTextView= new TextView(c); 
		emailAlertTextView= new TextView(c);
		phoneAlertTextView= new TextView(c);
		
		phoneNumberEditText = new EditText(c); 
		oldPasswordEditText= new EditText(c);  
		passwordEditText= new EditText(c); 
		retypePasswordEditText= new EditText(c); 
		firstNameEditText= new EditText(c); 
		lastNameEditText= new EditText(c); 
		
		phoneAlertCheckBox =new CheckBox(c); 
		emailAlertCheckbox =new CheckBox(c);
		
		loginCountrySpinner= new Spinner(c);
		
		Update = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginSignInButton);		
		Cancel = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginCancelButton);
		signUp = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginSignUpButton);
		forgotPassword = (Button) this.findViewById(R.id.LoginForgotPasswordButton);
		backtoSignin = (Button) this.findViewById(R.id.LoginCancel2Button);
		send = (Button) this.findViewById(R.id.LoginSendPasswordButton);
		
		phoneAlertCheckBox = (CheckBox) this.findViewById(com.jeannius.tallycap.R.id.LoginPhoneAlertCheckBox);
		emailAlertCheckbox = (CheckBox) this.findViewById(com.jeannius.tallycap.R.id.LoginEmailAlertCheckBox);
		phoneAlertTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.TextAlertTextView);
		emailAlertTextView = (TextView) findViewById(R.id.EmailAlertTextView);
		
		phoneNumberEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginPhoneNumberEditText);
		oldPasswordEditText =(EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginEmailEditText);
		passwordEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginPasswordEditText);
		retypePasswordEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginRetypePasswordEditText);
		firstNameEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginFirstNameEditText);
		lastNameEditText = (EditText) this.findViewById(com.jeannius.tallycap.R.id.LoginLastNameEditText);
		emailTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.LoginEmailTextView);
		passwordTextView = (TextView) this.findViewById(com.jeannius.tallycap.R.id.LoginPasswordTextView);
		
		firstNameTextView = (TextView) this.findViewById(R.id.LoginFirstNameTextView);
		lastNameTextView = (TextView) this.findViewById(R.id.LoginLastNameTextView);
		countryTextView = (TextView) this.findViewById(R.id.LoginCountryTextView);
		loginCountrySpinner = (Spinner) this.findViewById(R.id.LoginCountrySpinner);
		
		Update.setOnClickListener(verifyer);
		
		Cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				me.cancel();
				
			}
		});
		
	}
	
	
	//this is where the verification happens
	private android.view.View.OnClickListener verifyer = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			TextValidator textval = new TextValidator();
			String valRes ="";
			
			oldPasswordVal = textval.validate(oldPasswordEditText,true, 3);
			newPasswordVal = textval.validate(passwordEditText, true, 3);
			
			
			if (retypePasswordEditText.getText().length()==0) retypePasswordVal=" is required"; 
			else if(passwordEditText.getText().toString().equals(retypePasswordEditText.getText().toString())) retypePasswordVal="Good";			
			else retypePasswordVal =" does not match new password";
			
			if(oldPasswordEditText.getText().toString().endsWith(passwordEditText.getText().toString())) newPasswordVal =" can not be same as old password";
			
			
			if(!oldPasswordVal.equals("Good")){
				oldPasswordEditText.setBackgroundResource(R.drawable.fight);
				valRes +="Old password "+ oldPasswordVal+"\n";
			}
			else oldPasswordEditText.setBackgroundResource(R.drawable.edit_text);
			
			if(!newPasswordVal.equals("Good")){
				passwordEditText.setBackgroundResource(R.drawable.fight);
				valRes += "New password "+ newPasswordVal +"\n";
			}
			else passwordEditText.setBackgroundResource(R.drawable.edit_text);
			
			if(!retypePasswordVal.equals("Good")){
				retypePasswordEditText.setBackgroundResource(R.drawable.fight);
				valRes += "Retype " + retypePasswordVal + "\n";
			}
			else retypePasswordEditText.setBackgroundResource(R.drawable.edit_text);
			
			
			if(valRes.length()>0) Toast.makeText(c,valRes,Toast.LENGTH_LONG).show();
			else new backgroundUpdate().execute();
		}
	};
	
	
	//this functions does the UI changes to make it look proper
	private void comestics() {
		firstNameEditText.setVisibility(View.GONE);
		firstNameTextView.setVisibility(View.GONE);
		lastNameEditText.setVisibility(View.GONE);
		lastNameTextView.setVisibility(View.GONE);
		countryTextView.setVisibility(View.GONE);;
		loginCountrySpinner.setVisibility(View.GONE);
		emailAlertCheckbox.setVisibility(View.GONE);;
		emailAlertTextView.setVisibility(View.GONE);
		phoneAlertCheckBox.setVisibility(View.GONE);
		phoneAlertTextView.setVisibility(View.GONE);
		send.setVisibility(View.GONE);
		forgotPassword.setVisibility(View.GONE);
		backtoSignin.setVisibility(View.GONE);
		
		android.widget.RelativeLayout.LayoutParams repassLay = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		repassLay.setMargins(35, 30, 0, 32);		
		emailTextView.setLayoutParams(repassLay);
//		
		emailTextView.setText("Old Password:");
		oldPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());

		passwordTextView.setText("New Password:");
		signUp.setVisibility(View.GONE);
		Update.setText("Update");
	}



class backgroundUpdate extends AsyncTask<Void, Void, String>{

	@Override
	protected String doInBackground(Void... params) {		
    
	    String temp ="";    
	    
	   
	    	Date now = new Date();
	    	dateInM = now.getTime();
	    	
	    	SQLiteDatabase db = new MyDbOpenHelper(c).getReadableDatabase();
			
			String select = "SELECT email FROM customer";
			Cursor res = db.rawQuery(select, null);		
			res.moveToFirst();
			
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(13);
	        nameValuePairs.add(new BasicNameValuePair("oldPassword", oldPasswordEditText.getText().toString()));
	        try {
				nameValuePairs.add(new BasicNameValuePair("email", MyCrypt.decrypt(HomeScreenActivity.MASTER_PASSWORD, res.getString(0))));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	        nameValuePairs.add(new BasicNameValuePair("newPassword", passwordEditText.getText().toString()));	       
	        nameValuePairs.add(new BasicNameValuePair("dateModified", String.valueOf(dateInM)));    
	     
	        
	        
	   
	    temp =(new NetworkOperations(c)).contactOnline("http://www.tallycap.com/tallyphp/androidUpdatePassword.php", nameValuePairs);
	    
	    res.close();
	    db.close();
	   
		return temp;
	}

	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		Toast.makeText(c, result, Toast.LENGTH_LONG).show();
		if(result.equals("Password successfully updated")){
		new	backgroundDbUpdate().execute();
			
		}
	}
	
}



class backgroundDbUpdate extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
		SQLiteDatabase db = new MyDbOpenHelper(c).getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		try {
			String encry_pass = MyCrypt.encrypt(HomeScreenActivity.MASTER_PASSWORD, passwordEditText.getText().toString());
			cv.put("password",encry_pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		cv.put("dateModified", dateInM);
		db.update("customer", cv, null, null);
		db.close();
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
	
		super.onPostExecute(result);
//		Toast.makeText(c, "red", Toast.LENGTH_LONG).show();
		me.cancel();
	}
	
}

}






