package com.jeannius.tallycap.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	
	private String regExpn = 
		"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
	    +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
	      +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
	      +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
	      +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
	      +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
	
	public String validate(String inputEmail, Boolean isrequired){
		
		String valid="";
		Pattern patternObj = Pattern.compile(regExpn);
	    Matcher matcherObj = patternObj.matcher(inputEmail);
	    if(inputEmail.length()==0) valid = "is required";
	    else {
		    if (matcherObj.matches())   valid ="Good";	                 
		    
		    else valid="is not a valid email";		    
			
	    }
	    return valid;
	}
	
}
