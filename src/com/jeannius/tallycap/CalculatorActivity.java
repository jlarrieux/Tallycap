package com.jeannius.tallycap;

import java.util.HashMap;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TabHost;

import com.jeannius.tallycap.Calculator.Calculator_401k_Activity;
import com.jeannius.tallycap.Calculator.Calculator_CreditCard_Activity;
import com.jeannius.tallycap.Calculator.Calculator_Loan_Activity;
import com.jeannius.tallycap.Calculator.Calculator_Loan_Fragment;



public class CalculatorActivity extends  TabActivity {

	public final static String  myID23= "a14de80de6e01fc";
	private int MENU_SELECTOR_DIALOG;
	private Dialog dialog;
	//public static final Boolean GOOGANALDEBUG = true;
	public final String[] emailList1 ={"jeanrodneylarrieux@gmail.com"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.calculator_tab_main);	
				
		//tab logic
		TabHost tabs = getTabHost();
		
		TabHost.TabSpec spec =  tabs.newTabSpec("tag1");
		spec.setContent(new Intent(getApplicationContext(), Calculator_Loan_Activity.class));
		spec.setIndicator("Loan");	
		tabs.addTab(spec);
		
		
		spec = tabs.newTabSpec("tag2");
		spec.setContent(new Intent(getApplicationContext(), Calculator_401k_Activity.class));
		spec.setIndicator("401K");
		tabs.addTab(spec);
		
		spec = tabs.newTabSpec("tag3");
		spec.setContent(new Intent(getApplicationContext(),Calculator_CreditCard_Activity.class));
		spec.setIndicator("Credit Card");
		tabs.addTab(spec);
		

	}	



}

