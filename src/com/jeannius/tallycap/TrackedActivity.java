/**
 * 
 */
package com.jeannius.tallycap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

/**
 * @author Jeannius
 *
 */
public class TrackedActivity extends Activity {
	
	public  GoogleAnalyticsTracker globalTracker;
	public static int MENU_SELECTOR_DIALOG;
	protected Dialog dialog;
	protected ListView choice;
	protected AdView adview;
	protected  int width, height;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		
		globalTracker = GoogleAnalyticsTracker.getInstance();

		globalTracker.startNewSession(getString(R.string.ga_api_key), 1, this);
//		globalTracker.setDebug(true);
//		globalTracker.setDryRun(true)0;
		
		Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		
		
//		adview = new AdView(activity, adSize, adUnitId);
		
	}
	
	
//	@Override
//	protected void onStart() {		
//		super.onStart();
//		FlurryAgent.onStartSession(this, String.valueOf(R.string.flurry_api_key));
//	}
//	
//	@Override
//	protected void onStop() {		
//		super.onStop();
//		FlurryAgent.onEndSession(this);
//	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		
		globalTracker.trackPageView("/" + this.getLocalClassName());
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		new MenuInflater(getApplication()).inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
		
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId()== R.id.MenuAbout)MENU_SELECTOR_DIALOG=1;
		else MENU_SELECTOR_DIALOG=2;
		
		showDialog(MENU_SELECTOR_DIALOG);
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		
		if(id==1){
			dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.about_dialog);
		}
		else{			

			CharSequence[] items = getResources().getStringArray(R.array.feedBackOptions);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.sendFeedback);
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					emailIntent.setType("plain/text");
			    	
					if(item ==0){
						emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"android.comments@tallycap.com"});
						emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android General Comment");
					}
					else{
						emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"android.requests@tallycap.com"});
						emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Feature request");
					}
					
					startActivity(Intent.createChooser(emailIntent, "Send email..."));
			    	
			    }
			});
			dialog = builder.create();
	
		}
		return dialog;
	}
	
	
	
	
	
}
