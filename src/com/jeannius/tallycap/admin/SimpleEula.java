package com.jeannius.tallycap.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jeannius.tallycap.R;

public class SimpleEula {
	
	 private String EULA_PREFIX = "eula_";
	    private Activity mActivity;
	 
	    public SimpleEula(Activity context) {
	        mActivity = context;
	    }
	 
	    private PackageInfo getPackageInfo() {
	        PackageInfo pi = null;
	        try {
	             pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
	        } catch (PackageManager.NameNotFoundException e) {
	            e.printStackTrace();
	        }
	        return pi;
	    }
	 
	     public void show() {
	        PackageInfo versionInfo = getPackageInfo();
	 
	        // the eulaKey changes every time you increment the version number in the AndroidManifest.xml
	        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
	        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
	        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
	        if(hasBeenShown == false){
	 
	            // Show the Eula
	            String title = mActivity.getString(R.string.app_name) + " v" + versionInfo.versionName;
	 
	            //Includes the updates as well so users know what changed.
	            String message = mActivity.getString(R.string.updates) + "\n\n" + mActivity.getString(R.string.eula);
	            TextView myte =new TextView(mActivity);
	            ScrollView MYS = new ScrollView(mActivity);
	            MYS.setPadding(15, 0, 15, 15);
	            MYS.addView(myte);
	            myte.setTextSize(12);
	            myte.setText(message);
	            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
	                    .setTitle(title)
	                    
	                    .setPositiveButton("Accept", new Dialog.OnClickListener() {
	 
	                        @Override
	                        public void onClick(DialogInterface dialogInterface, int i) {
	                            // Mark this version as read.
	                            SharedPreferences.Editor editor = prefs.edit();
	                            editor.putBoolean(eulaKey, true);
	                            editor.commit();
	                            dialogInterface.dismiss();
	                        }

							
	                    })
	                    .setNegativeButton("Decline", new Dialog.OnClickListener() {
	 
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            // Close the activity as they have declined the EULA
	                            mActivity.finish();
	                        }
	 
	                    });
	            builder.setView(MYS);
	            builder.create().show();
	        }
	    }

}
