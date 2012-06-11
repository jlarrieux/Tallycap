package com.jeannius.tallycap.Calculator;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannius.tallycap.HomeScreenActivity;
import com.jeannius.tallycap.R;
import com.jeannius.tallycap.TrackedActivity;
import com.jeannius.tallycap.validators.NumberValidator;

public class Calculator_CreditCard_Grapher extends TrackedActivity{
	
	private LinearLayout mGraph;
	private GraphicalView mChartView;	
	private Intent myIntent;
	private TextView extraText, extraText2, banner, dateText, dateText2, dateText3;
	private EditText extraInput;
	private Button Go;
	private XYMultipleSeriesDataset mDataSet = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private XYSeriesRenderer rendererModified = new XYSeriesRenderer();
	private XYSeriesRenderer rendererRegular = new XYSeriesRenderer();
	private Spinner mySpinner;
	private Bundle reshma; //bundle to know if its first time so we can execute graphing
	private Double currentBalance, apr, creditLimit, annualFee, monthlyFee, overTheLimitFee, minPay;
	private String monthOfAnnualFee, extraConValid;
	private static final String DATE_FORMAT2 = "MMMM dd yyyy";
	private static final SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT2);
	private NumberValidator numval = new NumberValidator();
	private NumberFormat numf = NumberFormat.getCurrencyInstance();
	private int width, height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		reshma = savedInstanceState;
		myIntent = getIntent();
		setContentView(R.layout.calculator_loan_graph);

		Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		creditCardGraph();
		restoreMe(savedInstanceState);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		if(dateText2.length()>0){
			outState.putString("dateText2", dateText2.getText().toString());
			outState.putString("dateText3", dateText3.getText().toString());
		}
		outState.putString("dateText", dateText.getText().toString());
	}
	
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
	
	private void creditCardGraph(){
		
		extraInput = new EditText(getApplicationContext());
		extraInput = (EditText) findViewById(R.id.extraInput);
		mySpinner = (Spinner) findViewById(R.id.graphSpinner);
		extraText2 = (TextView) findViewById(R.id.extraText2);
		extraText = (TextView) findViewById(R.id.extraText);
		extraText.setText("Fixed Payments of: %");
		banner = (TextView) findViewById(R.id.banner);
		mySpinner.setVisibility(View.GONE);
		extraText2.setVisibility(View.GONE);
		Go = (Button) findViewById(R.id.goButton);
		
		Go.setOnClickListener(goListener);
		mGraph = (LinearLayout) findViewById(R.id.graph);
		//Go.setOnClickListener(goClick);
		dateText = (TextView) findViewById(R.id.dateText);
		dateText.setTextColor(HomeScreenActivity.color1[0]);
		dateText2 = (TextView) findViewById(R.id.dateText2);
		dateText2.setTextColor(HomeScreenActivity.color1[1]);
		dateText3 = (TextView) findViewById(R.id.dateText3);
		dateText3.setTextColor(HomeScreenActivity.color1[5]);
		rendererRegular.setColor(HomeScreenActivity.color1[0]);
		rendererModified.setColor(HomeScreenActivity.color1[1]);
		mRenderer.setMarginsColor(0x2262A6);
		mRenderer.setXTitle("Years");
		mRenderer.setYTitle("$");
		mRenderer.setShowGrid(true);		
		mRenderer.setZoomEnabled(false, false);
		mRenderer.setPanEnabled(false, false);
		extraConValid="";
		
		
		//Intent 
		currentBalance = myIntent.getDoubleExtra("currentBalance", 0.0);
		apr = myIntent.getDoubleExtra("apr", 0.0);
		creditLimit = myIntent.getDoubleExtra("creditLimit", 0.0);
		annualFee = myIntent.getDoubleExtra("annualFee", 0.0);
		monthlyFee = myIntent.getDoubleExtra("monthlyFee", 0.0);
		overTheLimitFee = myIntent.getDoubleExtra("overTheLimitFee", 0.0);
		monthOfAnnualFee = myIntent.getStringExtra("monthOfAnnualFee");
		minPay = myIntent.getDoubleExtra("minPay", 0.0);
		if(height>width)banner.setText(String.format("Initial Balance: %s\nInterest Rate: %s\nFirst Payment: %s", numf.format(currentBalance), 
				numf.format(apr), numf.format(minPay)));
		else banner.setText(String.format("Initial Balance: %s\t\t\tInterest Rate: %s\nFirst Payment: %s", numf.format(currentBalance), 
				numf.format(apr), numf.format(minPay)));
		
		
		if(reshma==null)new creditCardBackgroungGrapher().execute();
	}
	
	
	private synchronized void charting(){			
									
				if(mChartView == null ){	
					
					mChartView = ChartFactory.getTimeChartView(getApplicationContext(), mDataSet, mRenderer, "MMM yyyy");
					mGraph.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					mGraph.setDrawingCacheEnabled(true);			
				}
				else mChartView.repaint();			
		}
	
	private OnClickListener goListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String sh = "";
			extraConValid = numval.validate(extraInput, minPay, true, currentBalance,false);
			if(!extraConValid.equals("Good")){
				if(extraConValid.equals("is below minimum value")) sh = " is less than first payment";
				else if (extraConValid.equals("is above maximum value")) sh = " is more than total balance";
				else if (extraConValid.equals("is required")) sh = " is required";
			}
			else{
				Go.setEnabled(false);
				globalTracker.trackEvent("ui_interaction", "whatif", "Calculator_CreditCard_Grapher", 0);
				new creditCardBackgroungGrapher().execute();
			}
			if(sh.length()>0) {
				globalTracker.trackEvent("ui_interaction", "bad_whatif", "Calculator_CreditCard_Grapher", 0);
				Toast.makeText(getApplicationContext(), "Fixed Payment "+ sh, Toast.LENGTH_LONG).show();
			}
		}
	};
	
	
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
	
	class creditCardBackgroungGrapher extends AsyncTask<Void, Void, XYSeries>{

		@Override
		protected XYSeries doInBackground(Void... params) {
			String title;
			
			if(mDataSet.getSeriesCount()==0) title = "Minimum Payment";
			else title = "Modified Payment";
			double extraCon =0 ;
			if(extraInput.length()>0) extraCon = Double.valueOf(extraInput.getText().toString());
			
			
			XYSeries myseries = CalculatorLogic.creditCardWhatIf(currentBalance, apr, monthlyFee, overTheLimitFee, creditLimit, title,
					monthOfAnnualFee, annualFee, getApplicationContext(), extraCon);
			
			return myseries;
		}
		
		
		@Override
		protected void onPostExecute(XYSeries result) {			
			super.onPostExecute(result);
			
			Go.setEnabled(true);
			NumberFormat numf = NumberFormat.getCurrencyInstance();
			if(mDataSet.getSeriesCount()==0){				
				//dateText.setText(String.format("*The final value of your Regular 401k before retirement will be %s", numf.format(finalAmountBeforeRetirement)));
				
				mRenderer.addSeriesRenderer(rendererRegular);
				mDataSet.addSeries(result);
				int kq =mDataSet.getSeriesAt(0).getItemCount();
				Date temper = new Date((long) mDataSet.getSeriesAt(0).getX(kq-1));
				double lastValue = mDataSet.getSeriesAt(0).getY(kq-1);
				if(lastValue> 0){
					dateText.setText("Base on the initial conditions, you can never finish paying your credit cards during "+
							"your lifetime!!!"+"\nPlease go back and change the inputs. \n"+ String.valueOf(lastValue));					
				}
				else dateText.setText("*Payoff date with the minimym payment schedule is: "+ dateformat.format(temper));
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
				double fine1=0, fine2=0;
				for(int zkz =0; zkz <kq; zkz++){
					fine1+= mDataSet.getSeriesAt(0).getY(zkz);
				}
				for(int zkz =0; zkz <kq1; zkz++){
					fine2+= mDataSet.getSeriesAt(1).getY(zkz);
				}
				
				Date temper2 = new Date((long) mDataSet.getSeriesAt(0).getX(kq1-1));
				dateText2.setText(String.format("*Payoff date with the modified payment schedule is %s", dateformat.format(temper2)));
				
				
				dateText3.setText(String.format("*Your will save %s by going with the modified schedule", numf.format(fine1-fine2)));
			}
			
			charting();
			
			
		}
		
	}
	
}










