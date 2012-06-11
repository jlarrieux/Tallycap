package com.jeannius.tallycap.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jeannius.tallycap.HomeScreenActivity;
import com.jeannius.tallycap.RemindersActivity;

public class LocalOperations {

	private SQLiteDatabase db;	
	private Context c;
	
	public LocalOperations(Context context){
		c= context;
		
	}
	
	
	
	/*
	 * This function create a reminder locally
	 */	
	public void ReminderCreate(ContentValues values){
		
		db = new MyDbOpenHelper(c).getWritableDatabase();
		db.insertOrThrow(RemindersActivity.REMINDER,null, values);	
		db.close();		
	}
	
	
	/*
	 * This function updates a reminder locally
	 */
	public void ReminderUpdate(ContentValues values, long onlineID){
		db = new MyDbOpenHelper(c).getWritableDatabase();
		
		db.update(RemindersActivity.REMINDER, values,"onlineID="+ onlineID, null);
		
		db.close();	
		NetworkOperations no = new NetworkOperations(c);
		no.ReminderUpdate(HomeScreenActivity.nvReminderSetUp(values), onlineID);
	}
	
	
	/*
	 * This function delete a reminder locally
	 */
	public int ReminderDelete( long onlineID){
		db = new MyDbOpenHelper(c).getWritableDatabase();
		int t= db.delete(RemindersActivity.REMINDER,"onlineID="+ onlineID, null);	
		db.close();	
		return t;
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
}
