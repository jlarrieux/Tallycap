package com.jeannius.tallycap.admin;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
import com.jeannius.tallycap.validators.NumberValidator;
import com.jeannius.tallycap.validators.TextValidator;

public class UpdateDialog extends Dialog {
	
	public Button Update, Cancel, signUp, forgotPassword, backtoSignin, send;
	
	private Context c;
	
	
	public Spinner loginCountrySpinner,phoneCarrierSpinner;
	private TextView phoneAlertTextView, phoneNumberTextView, phoneCarrierTextView;
	public CheckBox phoneAlertCheckBox, emailAlertCheckbox;
	public EditText phoneNumberEditText, emailEditText, passwordEditText, retypePasswordEditText, firstNameEditText,
					lastNameEditText;
	
	private String firstNameVal, LastNameVal, phoneNumberVal ;
	
	private TextView email, password, repass;
	public long dateInM;
	
	public UpdateDialog(Context context, long dateInMilli) {
		super(context);
		c=context;
		this.setContentView(com.jeannius.tallycap.R.layout.create_account);
		this.setTitle("Update Account");
		LayoutParams p = this.getWindow().getAttributes();
		p.height = LayoutParams.WRAP_CONTENT;
		p.width = LayoutParams.FILL_PARENT;
		this.getWindow().setAttributes(p);
		setter();
		
		dateInM = dateInMilli;
		
	}
	
	
	//setup the functions
	private void setter(){
		Update = new Button(c);
		Cancel = new Button(c);
		signUp = new Button(c);
		email = new TextView(c);
		password = new TextView(c);
		repass = new TextView(c);
		send  = new Button(c);
		forgotPassword = new Button(c);
		backtoSignin = new Button(c);
	
		phoneAlertCheckBox = new CheckBox(c);
		emailAlertCheckbox = new CheckBox(c);
		phoneAlertTextView = new TextView(c);
		phoneCarrierSpinner = new Spinner(c);
		phoneCarrierTextView = new TextView(c);
		phoneNumberTextView = new TextView(c);
		phoneNumberEditText = new EditText(c);
		emailEditText = new EditText(c);
		passwordEditText = new EditText(c);
		retypePasswordEditText = new EditText(c);
		firstNameEditText = new EditText(c);
		lastNameEditText = new EditText(c);
		
		Update = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginSignInButton);		
		Cancel = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginCancelButton);
		signUp = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginSignUpButton);
		
		phoneAlertCheckBox = (CheckBox) this.findViewById(com.jeannius.tallycap.R.id.LoginPhoneAlertCheckBox);
		emailAlertCheckbox = (CheckBox) this.findViewById(com.jeannius.tallycap.R.id.LoginEmailAlertCheckBox);
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
		email = (TextView) this.findViewById(com.jeannius.tallycap.R.id.LoginEmailTextView);
		password = (TextView) this.findViewById(com.jeannius.tallycap.R.id.LoginPasswordTextView);
		repass = (TextView) this.findViewById(com.jeannius.tallycap.R.id.RetypePasswordTextView);
		forgotPassword = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginForgotPasswordButton);
		backtoSignin = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginCancel2Button);
		send = (Button) this.findViewById(com.jeannius.tallycap.R.id.LoginSendPasswordButton);
		
		email.setVisibility(View.INVISIBLE);
		password.setVisibility(View.INVISIBLE);
		repass.setVisibility(View.INVISIBLE);
		emailEditText.setVisibility(View.GONE);
		passwordEditText.setVisibility(View.GONE);
		retypePasswordEditText.setVisibility(View.INVISIBLE);
		send.setVisibility(View.GONE);
		forgotPassword.setVisibility(View.GONE);
		backtoSignin.setVisibility(View.GONE);
		
		android.widget.RelativeLayout.LayoutParams repassLay = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		repassLay.setMargins(0, 0, 0, 0);
		
		repass.setLayoutParams(repassLay);
		email.setLayoutParams(repassLay);
		password.setLayoutParams(repassLay);
		
		email.setHeight(0);
		password.setHeight(0);
		repass.setHeight(0);
		emailEditText.setHeight(0);
		passwordEditText.setHeight(0);
		retypePasswordEditText.setHeight(0);
		
		
		
		firstNameVal ="Good";
		LastNameVal ="Good";
		phoneNumberVal ="Good";
		
		Update.setText("Update");
		signUp.setVisibility(View.GONE);
		
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
		
		
		Cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			cancel();
				
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
			
			if(loginCountrySpinner.getSelectedItemPosition()==224){
				phoneAlertTextView.setVisibility(View.VISIBLE);
				phoneAlertCheckBox.setVisibility(View.VISIBLE);
			if(phoneAlertCheckBox.isChecked()){
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
			}
			else{
				phoneAlertTextView.setVisibility(View.GONE);
				phoneAlertCheckBox.setVisibility(View.GONE);
				phoneNumberEditText.setVisibility(View.GONE);
				phoneNumberTextView.setVisibility(View.GONE);
				phoneCarrierSpinner.setVisibility(View.GONE);
				phoneCarrierTextView.setVisibility(View.GONE);
			}
			
		}
	};
	
	
	//this function verify the input for the signup screen
	public String verifyer(){
		
		TextValidator textval = new TextValidator();
		NumberValidator numbval = new NumberValidator();
		String valRes ="";
	
		
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
		
	
		
		

		
		//firstName
		if(!firstNameVal.equals("Good")){
			firstNameEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "First name red"+ firstNameVal+"\n";
		}
		else firstNameEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
		
		
		//lastName
		if(!LastNameVal.equals("Good")){
			lastNameEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.fight);
			valRes += "Last name "+ LastNameVal+"\n";
		}
		else lastNameEditText.setBackgroundResource(com.jeannius.tallycap.R.drawable.edit_text);
		
		
		
		if(valRes.length()>0) Toast.makeText(c, valRes, Toast.LENGTH_LONG).show();
		
			
		
		return valRes;
		
		
	}
	
	
	
	
	
	}




