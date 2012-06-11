package com.jeannius.tallycap.admin;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jeannius.tallycap.R;



public class PhoneVerificationDialog extends Dialog {

	
	
	public Button verify, resend, cancel;
	public EditText codeEditText;
	private Context c;
	
	public PhoneVerificationDialog(Context context) {
		super(context);
		this.setTitle("Please verify phone number");
		this.setContentView(com.jeannius.tallycap.R.layout.phone_verification_layout);
		c= context;
		setter();
	}

	//this function instanciate all the text and button
	private void setter() {
	
		verify = new Button(c);
		resend = new Button(c);
		cancel = new Button(c);
		codeEditText = new EditText(c);
		
		
		verify = (Button) this.findViewById(R.id.phoneVerificationVerifyButton);
		resend = (Button) this.findViewById(R.id.phoneVerificationResendButton);
		cancel = (Button) this.findViewById(R.id.PhoneVerificationCancelButton);
		codeEditText = (EditText) this.findViewById(R.id.phoneVerificationCodeEditText);
		
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
				
			}
		});
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
