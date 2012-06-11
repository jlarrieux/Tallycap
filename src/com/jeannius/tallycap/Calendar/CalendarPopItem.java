package com.jeannius.tallycap.Calendar;

import com.jeannius.tallycap.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarPopItem extends RelativeLayout {

	
	private Context c;
	private TextView name, type, frequency, amount;
	private CalendarObject o;
	
	public CalendarPopItem(Context context, CalendarObject ob) {
		super(context);
		
		LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.calendar_item_pop, this);	
		c= context;		
		o= ob;
		setter();
	}

	private void setter() {
		name = new TextView(c);
		type =new TextView(c);
		frequency = new TextView(c);
		amount = new TextView(c);
		
		name = (TextView) this.findViewById(R.id.calendarItemPopName);
		type = (TextView) this.findViewById(R.id.calendarItemPopType);
		frequency = (TextView) this.findViewById(R.id.calendarItemPopFrequency);
		amount = (TextView) this.findViewById(R.id.calendarItemPopAmount);
		
		
		name.setText(o.getName());
		type.setText(o.getType());
		frequency.setText(o.getFrequency());
		if(type.getText().toString().equals("Income"))amount.setText(String.valueOf(o.getAmount()));
		else amount.setText("-"+String.valueOf(o.getAmount()));
		
	}
	
	public double getAmount(){
		return Double.valueOf(amount.getText().toString());
	}
	
	public String getType(){
		return type.getText().toString();
	}

}
