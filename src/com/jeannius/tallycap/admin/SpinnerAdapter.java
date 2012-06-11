package com.jeannius.tallycap.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SpinnerAdapter extends ArrayAdapter<String> {
	 Context context;
	 String[] items = new String[] {};
	 private int textSize=40;
	 
	    public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
	        super(context, textViewResourceId, objects);
	        this.items = objects;
	        this.context = context;
	        
	    }
	    public SpinnerAdapter(final Context context, final int resource, final int textViewResourceId ){
	    	super(context, resource, textViewResourceId);
	    	this.items = context.getResources().getStringArray(resource);
	    	
	    	Toast.makeText(context, String.valueOf(this.getSpinnerTextSize()), Toast.LENGTH_LONG).show();
	    }
	    

	    @Override
	    public View getDropDownView(int position, View convertView,
	            ViewGroup parent) {

	        if (convertView == null) {
	            LayoutInflater inflater = LayoutInflater.from(context);
	            convertView = inflater.inflate(
	                    android.R.layout.simple_spinner_item, parent, false);
	        }

	        TextView tv = (TextView) convertView
	                .findViewById(android.R.id.text1);
	        tv.setText(items[position]);
	        //tv.setTextColor(Color.BLUE);
	        tv.setTextSize(textSize);
	        return convertView;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        if (convertView == null) {
	            LayoutInflater inflater = LayoutInflater.from(context);
	            convertView = inflater.inflate(
	                    android.R.layout.simple_spinner_item, parent, false);
	        }

	        // android.R.id.text1 is default text view in resource of the android.
	        // android.R.layout.simple_spinner_item is default layout in resources of android.

	        TextView tv = (TextView) convertView
	                .findViewById(android.R.id.text1);
	        tv.setText(items[position]);
	        //tv.setTextColor(Color.BLUE);
	        tv.setTextSize(textSize);
	        return convertView;
	    }
	    
	    public void setSpinnerTextSize(int size){
	    	
	    	textSize= size;
	    }
	    
	    public int getSpinnerTextSize(){
	    	return textSize;
	    }
	    
}
