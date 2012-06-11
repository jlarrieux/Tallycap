package com.jeannius.tallycap.Calendar;

public class CalendarObject {
	
	
	private String name1, frequency1, type1, parameter1;
	private long dateCreated1;
	private double amount1;
	
	public CalendarObject(String name,String frequency,String type,String parameter,long dateCreated){
		name1 = name;
		frequency1 = frequency;
		type1 = type;
		parameter1 = parameter;
		dateCreated1 = dateCreated;
	}
	
	public CalendarObject(String name,String frequency,String type,String parameter,long dateCreated, double amount){
		name1 = name;
		frequency1 = frequency;
		type1 = type;
		parameter1 = parameter;
		dateCreated1 = dateCreated;
		amount1 =amount;
	}
	
	
	public String getName(){
		
		return name1;
	}
	
	
	public String getFrequency(){
		
		return frequency1;
	}
	
	
	public String getType(){
	
	return type1;
	}
	
	
	public String getParameter(){
		
	return parameter1;
	}
	
	
	public long getDateCreated(){
		
	return dateCreated1;
	}
	public double getAmount(){
		
	return amount1;
	}
	
	
}
