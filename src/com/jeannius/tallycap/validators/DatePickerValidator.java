package com.jeannius.tallycap.validators;

import java.util.Calendar;

import android.widget.DatePicker;

public class DatePickerValidator {
	
	
	public String validate (DatePicker datepicker, Calendar minDate, Calendar maxDate){
		
		String valid="";
		
		int date = datepicker.getDayOfMonth();
		int month =datepicker.getMonth();
		int year = datepicker.getYear();
		Calendar current = Calendar.getInstance();
		current.set(year, month, date);
		
		if(minDate.getTime().getTime()> current.getTime().getTime()) valid=" is below minimum allowable date";
		else{
			if(maxDate.getTime().getTime()<current.getTime().getTime()) valid="is above maximum allowable date";
			else valid = "Good";
		}
		
		return valid;
		
	}

}
