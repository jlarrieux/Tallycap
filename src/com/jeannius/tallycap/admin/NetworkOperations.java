package com.jeannius.tallycap.admin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import com.jeannius.tallycap.RemindersActivity;

public class NetworkOperations {
	
	private Context c;
	private String REMINDER_CRUD ="http://www.tallycap.com/tallyphp/androidReminderTableCRUD.php";
	private String REMINDER_FORGOT_PASSWORD = "http://www.tallycap.com/tallyphp/forgotPassword.php";
	private String  url, r2;
	private LocalOperations lo;
	private List<NameValuePair> l6;
	
	
	public NetworkOperations(Context context){
		c= context;
		lo = new LocalOperations(c);
	}
	
	
	/*
	 * this function create a reminder on the servers online
	 */
	public String ReminderCreate(List<NameValuePair> nameValuePairs){
		nameValuePairs.add(new BasicNameValuePair("ops", RemindersActivity.CREATE));
		nameValuePairs.add(new BasicNameValuePair("status", RemindersActivity.GOOD));
//		result = SignupDialog.contactOnline(REMINDER_CRUD, nameValuePairs, c);
//		l6 =nameValuePairs;
//		url =REMINDER_CRUD;
		r2 = contactOnline(REMINDER_CRUD, nameValuePairs);
//		r2 =getR2();
		return r2;
	}
	
	
	/*
	 * this function update a reminder on the servers online
	 */
	public String ReminderUpdate(List<NameValuePair> nameValuePairs, long onlineID){
		nameValuePairs.add(new BasicNameValuePair("ops", RemindersActivity.UPDATE));
		nameValuePairs.add(new BasicNameValuePair("reminderID", String.valueOf(onlineID)));
		nameValuePairs.add(new BasicNameValuePair("status", RemindersActivity.GOOD));
//		result = SignupDialog.contactOnline(REMINDER_CRUD, nameValuePairs, c);
//		l6 =nameValuePairs;
//		url =REMINDER_CRUD;
		r2 = contactOnline(REMINDER_CRUD, nameValuePairs);
		
		if(r2.endsWith("success")){
			ContentValues co = new ContentValues();
			co.put("status", RemindersActivity.GOOD);
			co.put("onlineID", onlineID);
			lo.ReminderUpdate(co, onlineID);
		}
		return r2;
	}
	
	
	/*
	 * this function deletes a reminder on the servers online
	 */
	public String ReminderDelete(List<NameValuePair> nameValuePairs, Long onlineID){
		
		nameValuePairs.add(new BasicNameValuePair("ops", RemindersActivity.DELETE));
		nameValuePairs.add(new BasicNameValuePair("reminderID", String.valueOf(onlineID)));
//		result = SignupDialog.contactOnline(REMINDER_CRUD, nameValuePairs, c);
//		l6 =nameValuePairs;
//		url =REMINDER_CRUD;
		r2 = contactOnline(REMINDER_CRUD, nameValuePairs);
		return r2;
	}
	
	
	//this function resend the password to the customer's email
	public String CustomerResendPassword(String email){
		List<NameValuePair> n = new ArrayList<NameValuePair>();
		n.add(new BasicNameValuePair("email", email));
//		result =SignupDialog.contactOnline(REMINDER_FORGOT_PASSWORD, n, c);
//		url = REMINDER_FORGOT_PASSWORD;
//		l6=n;
		r2 = contactOnline(REMINDER_FORGOT_PASSWORD, n);
		return r2;
	}
	
	
	
	
	
	
	
	//this function does the online talking
	public String contactOnline(String u, List<NameValuePair> z){
		
		l6=z;
		url =u;	
		
		String s="";
		Resender r= new Resender();
		r.execute();
		try {
			s= r.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return s;
		 
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * this is where the actual online contact happens
	 * @author Jeannius
	 *
	 */
	
	class Resender extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			
//			List<NameValuePair> l5 = l6;
//			String s= SignupDialog.contactOnline(url, l5, c);			
//			return s;
			
			
			StringBuffer responseBody = new StringBuffer();
			//determine if any network type is available
		    ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		    if(cm.getActiveNetworkInfo()!=null){
		    		if(cm.getActiveNetworkInfo().isConnectedOrConnecting()){
						 HttpClient httpclient = new DefaultHttpClient();
						 HttpPost httppost = new HttpPost(url);
						 
						 
						  try {
							httppost.setEntity(new UrlEncodedFormEntity(l6));
							
							// Execute HTTP Post Request
					        HttpResponse response = httpclient.execute(httppost);
					        responseBody.append(EntityUtils.toString(response.getEntity() ));
					        
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			  
		    		}  
					 else responseBody.append("No data connection");
				    }
				    
				    else responseBody.append("Network is not available.");
		    		
		    return responseBody.toString();  
		}
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			r2 = result;
		}
	}

}
