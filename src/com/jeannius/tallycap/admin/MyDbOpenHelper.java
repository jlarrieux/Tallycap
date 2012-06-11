package com.jeannius.tallycap.admin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbOpenHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION =1;
	public static final String DATABASE_NAME = "tallycapDB.db";
	
	
	public MyDbOpenHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE IF NOT EXISTS customer(firstName TEXT, lastName TEXT, country TEXT, email TEXT, password TEXT, phone TEXT, phoneCarrier TEXT, dateCreated TEXT, phoneALert TEXT, emailAlert TEXT, dateModified TEXT, phoneVerified TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS reminder(name TEXT, amount TEXT, dateCreated TEXT, dateModified TEXT, frequency TEXT, parameter TEXT, reminder1 TEXT, reminder2 TEXT, reminder3 TEXT, status TEXT, type TEXT, onlineID TEXT)");
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
