package com.jeannius.tallycap.validators;

import android.widget.EditText;

public class TextValidator {
	
	
	public String validate(EditText edittext, Boolean required, int minChars){
		String valid="";
		
		if(required && edittext.getText().length()==0) valid = "is required";
		else if((required && edittext.getText().length()>0) || (required= false)){
			
			if(!meetMinChar(minChars, edittext)) valid ="is below minimum character required";
			else valid ="Good";
		}
		else valid = "Good";
		
		
		return valid;
	}
	
	
	private Boolean meetMinChar(int minChars, EditText edittext){
		Boolean meets = true;
		if(edittext.getText().toString().length()>= minChars) meets = true;
		else meets = false;
		
		return meets;
		
	}

}
