package com.jeannius.tallycap.Calculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.jeannius.tallycap.HomeScreenActivity;
import com.jeannius.tallycap.R;
import com.jeannius.tallycap.TrackedActivity;

public class Calculator_401k_Grapher extends TrackedActivity {
	
	private TextView extraText, extraText2, banner, dateText, dateText2, dateText3;
	private EditText extraInput;
	private Intent myIntent;
	private Button Go, repaint;
	private LinearLayout mGraph;
	private Spinner mySpinner;
	private double annualPay, rateOfReturnPercent, contributionPercent, finalAmountBeforeRetirement, annualIncrease, currentSavings, employerMatch,
					employerLimit;
	private String  contributionType;
	private int numberOfYearsBeforeRetirement;
	private XYMultipleSeriesDataset mDataSet = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	private GraphicalView mChartView;
	XYSeriesRenderer rendererModified = new XYSeriesRenderer();
	XYSeriesRenderer rendererRegular = new XYSeriesRenderer();
	
	private Bundle reshma; //bundle to know if its first time so we can execute graphing
	
	private int width, height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		reshma = savedInstanceState;
		myIntent = getIntent();
		setContentView(R.layout.calculator_loan_graph);

		if(mDataSet.getSeriesCount()>0){
			for(int uik=0; uik< mDataSet.getSeriesCount(); uik++){
				mDataSet.removeSeries(uik);
			}
		}
		Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();		
		
		k401kGraph();
		restoreMe(savedInstanceState);	
		
	}
	
	
	
	
	/**
	 * **********************************************
	 * this is the save instance bundle function
	 * **********************************************
	 */
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		if(dateText2.length()>0){
			outState.putString("dateText2", dateText2.getText().toString());
			outState.putString("dateText3", dateText3.getText().toString());
		}
		outState.putString("dateText", dateText.getText().toString());
	}
	
	
	//save these 2 data into an object for the configuration change
	@Override
	public Object onRetainNonConfigurationInstance() {
		Object[] lll = new Object[2];	
		lll[0] = mDataSet;
		lll[1] = mRenderer;
		return lll;			
	}
	
	
	private void restoreMe(Bundle outState){
		
		if(outState!=null){
			
			Object[] lll = new Object[2];
			if(getLastNonConfigurationInstance()!=null){
				lll = (Object[]) getLastNonConfigurationInstance();
				mDataSet = (XYMultipleSeriesDataset) lll[0];
				mRenderer = (XYMultipleSeriesRenderer) lll[1];
				charting();
			}
			

			dateText.setText(outState.getString("dateText"));
			if(outState.containsKey("dateText2")){
			dateText2.setText(outState.getString("dateText2"));
			dateText3.setText(outState.getString("dateText3"));
			}
		}
		
	}
	
	/**
	 * ********************************************************
	 * *******************************************************
	 * This function sets up the UI
	 * *****************************************************
	 * ********************************************************
	 */
	private void k401kGraph() {
		
		extraInput = new EditText(getApplicationContext());
		extraInput = (EditText) findViewById(R.id.extraInput);
		mySpinner = (Spinner) findViewById(R.id.graphSpinner);
		extraText2 = (TextView) findViewById(R.id.extraText2);
		extraText = (TextView) findViewById(R.id.extraText);
		banner = (TextView) findViewById(R.id.banner);
		mySpinner.setVisibility(View.VISIBLE);
		extraText2.setVisibility(View.VISIBLE);
		Go = (Button) findViewById(R.id.goButton);
		mGraph = (LinearLayout) findViewById(R.id.graph);
		Go.setOnClickListener(goClick);
		dateText = (TextView) findViewById(R.id.dateText);
		dateText.setTextColor(HomeScreenActivity.color1[0]);
		dateText2 = (TextView) findViewById(R.id.dateText2);
		dateText2.setTextColor(HomeScreenActivity.color1[1]);
		dateText3 = (TextView) findViewById(R.id.dateText3);
		dateText3.setTextColor(HomeScreenActivity.color1[5]);
		rendererRegular.setColor(HomeScreenActivity.color1[0]);
		rendererModified.setColor(HomeScreenActivity.color1[1]);
		mRenderer.setMarginsColor(0x2262A6);
		
		repaint = (Button) findViewById(R.id.repaint);

		
		repaint.setVisibility(View.GONE);
		
		///intent stuff		
		annualPay=myIntent.getDoubleExtra("annualPay",0.0);
		rateOfReturnPercent =myIntent.getDoubleExtra("rateOfReturnPecent",0.0);
		contributionPercent=myIntent.getDoubleExtra("contributionPercent",0.0);
		finalAmountBeforeRetirement=myIntent.getDoubleExtra("finalAmountBeforeRetirement",0.0);
		annualIncrease=myIntent.getDoubleExtra("annualIncrease",0.0);
		currentSavings=myIntent.getDoubleExtra("currentSavings",0.0);
		employerMatch=myIntent.getDoubleExtra("employerMatch",0.0);
		employerLimit=myIntent.getDoubleExtra("employerLimit",0.0);
		numberOfYearsBeforeRetirement=myIntent.getIntExtra("numberOfYearsBeforeRetirement",0);
		contributionType=myIntent.getStringExtra("contributionType");
		populator();
		String con= new String();
		if(contributionType.equals("fixed")){
			con =String.format("$ %.02f ", contributionPercent);
			extraText.setText("Change contribution to: $");
		}
		else{
			con = String.format("%.2f%% of salary", contributionPercent);
			extraText.setText("Change contribution to: %");
		}
		if(width<height)banner.setText(String.format("Annual Pay: $ %.02f\nContribution: %s\nYears to Retirement: %d\nRate of Return: %.02f%%", 
				annualPay,con, numberOfYearsBeforeRetirement, rateOfReturnPercent));
		else banner.setText(String.format("Annual Pay: $ %.02f\t\t\tContribution: %s\nYears to Retirement: %d\t\t\tRate of Return: %.02f%%", 
				annualPay,con, numberOfYearsBeforeRetirement, rateOfReturnPercent));
		
		
		if(reshma ==null) new k401kBackgroundGrapher().execute();
	}
	
	
	/**
	 * *********************************************************************************
	 * ********************************************************************************
	 * This functions populates the spinner based on the number of years to retirement
	 * *****************************************************************************
	 * *********************************************************************************
	 */
	private void populator(){
		
		ArrayList<Integer> templist = new ArrayList<Integer>();
		
			for(int i=1; i<numberOfYearsBeforeRetirement; i++){
				templist.add(i);
			}
		ArrayAdapter<Integer> myAdapter = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_spinner_item, templist);
		myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinner.setAdapter(myAdapter);	
				
	}
	
	
	/**
	 * *******************************************
	 * *******************************************
	 * The on click listener for the GO button
	 * *****************************************
	 * ****************************************
	 */
	private OnClickListener goClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Go.setEnabled(false);
			globalTracker.trackEvent("ui_interaction", "whatif", "Calculator_401k_Grapher", 0);
			new k401kBackgroundGrapher().execute();
			
		}
	};
	/**
	 * *********************************************
	 * **********************************************
	 * This function only deals with charting
	 * **********************************************
	 * *********************************************
	 */
	
	private synchronized void charting(){		
				
			if(mChartView == null ){	
				
				mChartView = ChartFactory.getLineChartView(getApplicationContext(), mDataSet, mRenderer);
				mGraph.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				mGraph.setDrawingCacheEnabled(true);			
			}
			else mChartView.repaint();		
		
	}
	
	
	/**
	 * ******************************************************************************
	 * ******************************************************************************
	 * *******************************************************************************
	 * *******************************************************************************
	 * *******************************************************************************
	 * THIS IS WHERE THE ASYNCTASK BEGINGS!!!
	 * ********************************************************************************
	 * ********************************************************************************
	 * ********************************************************************************
	 * ******************************************************************************** 
	 * ********************************************************************************
	 */
	
	class k401kBackgroundGrapher extends AsyncTask<Void, Void, XYSeries>{

		@Override
		protected XYSeries doInBackground(Void... params) {
			String title;
			
			if(mDataSet.getSeriesCount()==0)title="Regular";
			else title ="Modified";
			double extraCon;
			int spinNum;
			if(extraInput.length()>0){
				spinNum = Integer.valueOf(mySpinner.getSelectedItem().toString());
				extraCon = Double.valueOf(extraInput.getText().toString());
			}
			else {
				spinNum=0;
				extraCon=0;
			}
			
			
			XYSeries myseries = CalculatorLogic.k401kPaymentWhatIf(annualPay, contributionPercent, contributionType, numberOfYearsBeforeRetirement, 
					rateOfReturnPercent, annualIncrease, currentSavings, employerMatch, employerLimit, title, spinNum, extraCon);		
			
			return myseries;
		}
		
		@Override
		protected void onPostExecute(XYSeries result) {
			
			super.onPostExecute(result);
			
			Go.setEnabled(true);
			NumberFormat numf = NumberFormat.getCurrencyInstance();
			
			
			if(mDataSet.getSeriesCount()==0){				
				dateText.setText(String.format("*The final value of your Regular 401k before retirement will be %s", numf.format(finalAmountBeforeRetirement)));
				
				mRenderer.addSeriesRenderer(rendererRegular);
				mDataSet.addSeries(result);
			}
			else{			
						
					if(mDataSet.getSeriesCount()>1) mDataSet.removeSeries(1);				
					mDataSet.addSeries(result);
					
					if(mDataSet.getSeriesCount()==1){
						if(mRenderer.getSeriesRendererCount()==0) mRenderer.addSeriesRenderer(rendererRegular);
					}
					else if(mDataSet.getSeriesCount() ==2){
						if(mRenderer.getSeriesRendererCount()==0){
							mRenderer.addSeriesRenderer(rendererRegular);
							mRenderer.addSeriesRenderer(rendererModified);
						}
						else if(mRenderer.getSeriesRendererCount()==1) mRenderer.addSeriesRenderer(rendererModified);					
					}
					
					
					int kq1 = mDataSet.getSeriesAt(1).getItemCount();
					int kq = mDataSet.getSeriesAt(0).getItemCount();
					double fine2 = mDataSet.getSeriesAt(1).getY(kq1-1);
					dateText2.setText(String.format("*The final value of your Modified 401k before retirement will be %s", numf.format(fine2)));
					double fine1 = mDataSet.getSeriesAt(0).getY(kq-1);
					DecimalFormat decf = new DecimalFormat("$#,##0.00;-$#,##0.00");
					String ppl ="";
					if((fine2-fine1)>0)ppl ="Your 401K will have an addition";
					else ppl = "Your 401k will be reduced by";
					dateText3.setText(String.format("*%s %s by going with the modified schedule",ppl, decf.format(fine2-fine1)));
			}
			
						
			
			mRenderer.setXTitle("Years");
			mRenderer.setYTitle("$");
			mRenderer.setShowGrid(true);
			mRenderer.setXAxisMax((double)numberOfYearsBeforeRetirement+10);
			mRenderer.setZoomEnabled(false, false);
			mRenderer.setPanEnabled(false, false);
					
			
			charting();
			
			
		}
		
	}
	
	
	
	

}
















