package com.jeannius.tallycap.Calculator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.jeannius.tallycap.HomeScreenActivity;
import com.jeannius.tallycap.R;
import com.jeannius.tallycap.TrackedActivity;
import com.jeannius.tallycap.validators.NumberValidator;


public class Calculator_Loan_Grapher extends TrackedActivity {
		
	NumberValidator mVal = new NumberValidator();
	TextView extraText, banner, dateText, dateText2, dateText3;
	EditText extraInput;
	LinearLayout mGraph;
	Intent mIntent;
	Double p, i, x, extra;
	Integer n;
	List<double[]> values;
	List<ArrayList<Double>> yval;
	List<int[]> xval;
	GraphicalView mChartView;
	DefaultRenderer rendererMain;
	CalculatorLogic calcLog;
	String valResultExta, date1,date2, nType;
	Button go;
	XYSeriesRenderer rendererRegular = new XYSeriesRenderer();
	XYSeriesRenderer rendererModified = new XYSeriesRenderer();
	String[] titles;	
	XYMultipleSeriesDataset mDataSet = new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	XYSeries mCurrentSeries;
	Date myDate;
	private static final String DATE_FORMAT2 = "MMMM dd, yyyy";
	private static final SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT2);
	
	

	
	@Override
	protected void onCreate(Bundle outState) {
		super.onCreate(outState);

		mIntent = getIntent();		
							
		if(mDataSet.getSeriesCount()>0){
			for(int uik=0; uik< mDataSet.getSeriesCount(); uik++){
				mDataSet.removeSeries(uik);
			}
		}
		
			setContentView(R.layout.calculator_loan_graph);

			banner = (TextView)findViewById(R.id.banner);
			mGraph = (LinearLayout)findViewById(R.id.graph);
			
			loanGraph();
			if(outState == null)asyncstatus(x);			
		
		 restoreMe(outState);
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
//		mChartView.setDrawingCacheEnabled(false);
//		mChartView.destroyDrawingCache();
		if(dateText2.length()>0){
			outState.putString("dateText2", dateText2.getText().toString());
			outState.putString("dateText3", dateText3.getText().toString());
		}
		outState.putString("dateText", dateText.getText().toString());
	}
//	
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
			
//			Toast.makeText(getApplicationContext(),"DataSet Count: "+ String.valueOf(mDataSet.getSeriesCount())+"\n"+
//					"Render count: "+ String.valueOf(mRenderer.getSeriesRendererCount()), Toast.LENGTH_LONG).show();
			dateText.setText(outState.getString("dateText"));
			if(outState.containsKey("dateText2")){
			dateText2.setText(outState.getString("dateText2"));
			dateText3.setText(outState.getString("dateText3"));
			}
		}
		//else Toast.makeText(getApplicationContext(), "Bundle is null!!"	, Toast.LENGTH_LONG).show();
	}

	
	//this function graphs loans
	private void loanGraph(){	
		
		p=mIntent.getDoubleExtra("p", 0.0);
		i=mIntent.getDoubleExtra("i", 0.0);
		n= mIntent.getIntExtra("n", 0);
		x =mIntent.getDoubleExtra("x", 0.0);
		nType = mIntent.getStringExtra("PayFreq");
		myDate = new Date( mIntent.getLongExtra("date", 0));
		
		calcLog = new CalculatorLogic();
		//Toast.makeText(getApplicationContext(), String.format("n: %d\nNtype: %s", n, nType), Toast.LENGTH_LONG).show();
		rendererRegular.setColor(HomeScreenActivity.color1[0]);
		rendererModified.setColor(HomeScreenActivity.color1[1]);
		String loanamount = String.valueOf(p);
		String interest = String.valueOf(i);
		banner.setTypeface(banner.getTypeface(), 1);
		banner.setText(getResources().getString(R.string.loan_amount)+" $ "+ loanamount+"\n"+ getResources().getString(R.string.interest_rate)+
				" " +interest+"%\nRegular Payments: "+String.valueOf(x));
		TextView textView2 = new TextView(getApplicationContext());
		textView2=(TextView) findViewById(R.id.extraText2);
		textView2.setVisibility(View.GONE);
		mRenderer.setMarginsColor(0x2262A6);
		Spinner spin = new Spinner(getApplicationContext());
			spin=(Spinner) findViewById(R.id.graphSpinner);
		spin.setVisibility(View.GONE);
		extraInput = (EditText)findViewById(R.id.extraInput);
		go = (Button)findViewById(R.id.goButton);
		dateText = (TextView)findViewById(R.id.dateText);
		dateText.setTextColor(HomeScreenActivity.color1[0]);
		dateText2 = (TextView)findViewById(R.id.dateText2);
		dateText3 = (TextView)findViewById(R.id.dateText3);
		dateText2.setText("");
		dateText3.setText("");
		dateText3.setTextColor(HomeScreenActivity.color1[5]);
		dateText2.setTextColor(HomeScreenActivity.color1[1]);
		
				
		
		
	}
	

	
	private OnClickListener grap = new OnClickListener() {		
		
		@Override
		public void onClick(View v) {
			valResultExta = mVal.validate(extraInput, 1.0, true,p ,false);
			
			if(extraInput.length()!=0 && valResultExta=="Good"){
				titles= new String[]{"Regular Payment", "Modified Payment"};
				double aa = x + Double.valueOf(extraInput.getText().toString());
				asyncstatus(aa);			
				 
			}
			
			else if(valResultExta=="not required"){
				titles= new String[]{"Regular Payment"};
				extra=0.0;			
				
				
			}
			
			else{
				//Toast.makeText(getApplicationContext(), "Extra payment is "+ valResultExta, Toast.LENGTH_LONG).show();
			}
			
		}
	};
		
	
	
	
	
	
	
	
	private void asyncstatus(Double payments){
		go.setEnabled(false);
		globalTracker.trackEvent("ui_interaction", "whatif", "Calculator_Loan_Grapher", 0);
		
		new backgroundworkforGraph().execute(payments);			
	}
	
	private double modified_payments_calc(){
		
		XYSeries firstSeries = mDataSet.getSeriesAt(0);
		XYSeries secondSeries = mDataSet.getSeriesAt(1);
		double saved=0.0;
		double regx, modx;
		regx = ((firstSeries.getItemCount()-3)* x)+ firstSeries.getY(firstSeries.getItemCount()-2);
		modx = ((secondSeries.getItemCount()-3)* (x + Double.valueOf(extraInput.getText().toString())))+ secondSeries.getY(secondSeries.getItemCount()-2);
		
		//Toast.makeText(getApplicationContext(), "Regx= "+ String.valueOf(regx)+"\t\t Modx= "+ String.valueOf(modx), Toast.LENGTH_LONG).show();
		saved = Math.round((regx - modx)*100);
		saved/= 100;
		return saved;
		
	}
	
	//to calculate the day/month/years in between (how much time you saving by going with modified payments)
	
	public String daysBetween(Date regularDate1, Date modifiedDate){
		Calendar tempcal = Calendar.getInstance();
		tempcal.setTime(modifiedDate);
		
		Calendar tempcal2 = Calendar.getInstance();
		tempcal2.setTime(regularDate1);
		Calendar myShortTempDate = (Calendar)tempcal.clone();
		long numberOfMonthsInbetween=0;
		
		while(myShortTempDate.before(tempcal2)){
			myShortTempDate.add(Calendar.MONTH, 1);
			numberOfMonthsInbetween++;
		}
		
		return (daysToDaysMonthYears(numberOfMonthsInbetween));
		
	}
	
	private String daysToDaysMonthYears(long numberOfDaysInBetween){
		int yearNumber = 0;
		int monthNumber =0;
		
		if(numberOfDaysInBetween> 11)yearNumber =(int) (numberOfDaysInBetween/12);
		
		monthNumber = (int) (numberOfDaysInBetween -((yearNumber)*12));
		
		if(yearNumber>0){
			String years= "year";
			if(yearNumber>1) years= "years";
			
			if(monthNumber>1) return String.format("%d %s and %d months ", yearNumber,years, monthNumber);
			else return String.format("%d %s and %d month ", yearNumber,years, monthNumber);
		}
		else{
			if(monthNumber>1) return String.format("%d months ", monthNumber);
			else return String.format(" %d month ",  monthNumber);
		}
		
		
	}
	private synchronized void charting(){			
						
				if(mChartView == null ){	
					
					mChartView = ChartFactory.getTimeChartView(getApplicationContext(), mDataSet, mRenderer, "MMM yyyy");
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
	
	class backgroundworkforGraph extends AsyncTask<Double, Void, XYSeries >{
		
		@Override
		protected XYSeries doInBackground(Double... params) {
			
			Double pay = params[0];
			String graphTitle = new String();
			if(mDataSet.getSeriesCount()==0){
				graphTitle = "Regular Payments";
			}
			else{
				graphTitle= "Modified Payments";
			}
			XYSeries myseries =	 calcLog.loanPaymentWhatif(p, i, n, pay, graphTitle, myDate.getTime(), nType);
			return myseries;			
			
		}
	
		
		
		@Override
		protected void onPostExecute(XYSeries result) {
			
			super.onPostExecute(result);
			
			//Toast.makeText(getApplicationContext(), String.valueOf(mCurrentSeries.getItemCount()), Toast.LENGTH_LONG).show();
			go.setEnabled(true);
			go.setOnClickListener(grap);
			
			mRenderer.setXTitle("Date");
			mRenderer.setYTitle("$");
			mRenderer.setShowGrid(true);
			
			mRenderer.setZoomEnabled(false, false);
			mRenderer.setPanEnabled(false, false);
			
			if(mDataSet.getSeriesCount()==0){
								
				
				mRenderer.addSeriesRenderer(rendererRegular);
				mDataSet.addSeries(result);
				int kq =mDataSet.getSeriesAt(0).getItemCount();
				Date temper = new Date((long) mDataSet.getSeriesAt(0).getX(kq-1));
				
				dateText.setText("*"+getResources().getString(R.string.regular)+" "+getResources().getString(R.string.loan)+" "+getResources().getString(R.string.end_date)+": "+ 
						dateformat.format(temper));

			}
			else {	
				
				if(mDataSet.getSeriesCount()>1) mDataSet.removeSeries(1);
				mDataSet.addSeries(result);
				
				
				if(mDataSet.getSeriesCount()==1){
					if(mRenderer.getSeriesRendererCount()==0)mRenderer.addSeriesRenderer(rendererRegular);
				}
				else if(mDataSet.getSeriesCount()==2){
					if(mRenderer.getSeriesRendererCount()==0){
						mRenderer.addSeriesRenderer(rendererRegular);
						mRenderer.addSeriesRenderer(rendererModified);
					}
					else if(mRenderer.getSeriesRendererCount()==1) mRenderer.addSeriesRenderer(rendererModified);
				}
				
				
				
				
					
//					if(mRenderer.getSeriesRendererCount()==1){						
//						mRenderer.addSeriesRenderer(rendererModified);
//					}				
				
//				if(mDataSet.getSeriesCount()>1){
//				
//						mDataSet.removeSeries(1);
//	
//						mDataSet.addSeries(result);
						int kq =mDataSet.getSeriesAt(0).getItemCount();
						Date temper = new Date((long) mDataSet.getSeriesAt(0).getX(kq-1));
						
						dateText.setText("*"+getResources().getString(R.string.regular)+" "+getResources().getString(R.string.loan)+" "+getResources().getString(R.string.end_date)+": "+ 
								dateformat.format(temper));
						dateText.setTextColor(HomeScreenActivity.color1[0]);
						
						int kq2 = mDataSet.getSeriesAt(1).getItemCount();
						Date temper2 = new Date((long) mDataSet.getSeriesAt(1).getX(kq2-1));
						dateText2.setText("*"+getResources().getString(R.string.modified)+" "+getResources().getString(R.string.loan)+" "+getResources().getString(R.string.end_date)+": "+ 
								dateformat.format(temper2));
						 
						dateText3.setText("*"+getResources().getString(R.string.you_would_save)+" $"+String.valueOf(modified_payments_calc())+" and "+ daysBetween(temper, temper2)+
							getResources().getString(R.string.by_going_with_modified_payments));
//				
//				}
			
			
				
			
		}		
			charting();
		
	}
	


	
		
	
	
	
	}
}
