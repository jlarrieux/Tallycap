package com.jeannius.tallycap.Calculator;

import java.text.NumberFormat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannius.tallycap.R;
import com.jeannius.tallycap.TrackedActivity;
import com.jeannius.tallycap.validators.NumberValidator;

public class Calculator_401k_Activity extends TrackedActivity {
	
	private EditText annualPayEditText, contributionEditText, yearsToRetirementEditText, rateOfReturnEditText;
	private TextView currentSavingsTextView, annualIncreaseTextView, employerMatchTextView, employerLimitTextView, contributionTypeTextView;
	private EditText currentSavingsEditText, annualIncreaseEditText, employerMatchEditText, employerLimitEditText;
	private RadioGroup myRadioGroup;
	private RadioButton simple, advanced;
	private TextView k401kResultTextView;
	private Button calculate, whatIf;
	private NumberValidator numval = new NumberValidator();
	private String annualPayValid, contributionValid, yearsToRetirementValid, rateOfReturnValid, k401kState;
	private double annualPayNumber, rateOfReturnPercentValue, contributionPercentValue	, finalAmountBeforeRetirementValue;
	private double annualIncreaseNumber, currentSavingsNumber, employerMatchNumber, employerLimitNumber;
	private int numberOfYearsBeforeRetirementValue;
	private String contributionTypeString, contributionHint ="% of pay";
	private ArrayAdapter<CharSequence> contributionTypeAdapter;
	private Spinner contributionTypeSpinner;
	
		
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.calculator_401k);		
		k401ksetup();

		if(savedInstanceState!=null){
			if(savedInstanceState.getString("k401kState").equals(simple.getText())) simple.performClick();
			else advanced.performClick();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		
		outState.putString("k401kState", k401kState);
	}

	private void k401ksetup() {
		annualPayEditText = new EditText(getApplicationContext());
		contributionEditText = new EditText(getApplicationContext());
		yearsToRetirementEditText = new EditText(getApplicationContext());
		rateOfReturnEditText = new EditText(getApplicationContext());
		annualIncreaseEditText = new EditText(getApplicationContext());
		currentSavingsEditText= new EditText(getApplicationContext());
		employerLimitEditText= new EditText(getApplicationContext());
		employerMatchEditText= new EditText(getApplicationContext());
		k401kResultTextView= new TextView(getApplicationContext());
		
		myRadioGroup = (RadioGroup) findViewById(R.id.k401kRadioGroup);
		k401kResultTextView = (TextView) findViewById(R.id.k401kResultTextView);
		annualPayEditText = (EditText)findViewById(R.id.k401kAnnualPayEditText);
		contributionEditText = (EditText)findViewById(R.id.k401kContributionEditText);
		yearsToRetirementEditText = (EditText)findViewById(R.id.k401kYearsToRetirementEditText);
		rateOfReturnEditText = (EditText)findViewById(R.id.k401kRateOfReturnEditText);
		calculate = (Button)findViewById(R.id.k401kCalculateButton);
		whatIf = (Button)findViewById(R.id.k401kWhatIfButton);
		contributionTypeSpinner = (Spinner) findViewById(R.id.k401kContributionTypeSpinner);
		currentSavingsTextView= (TextView) findViewById(R.id.k401kCurrentSavingsTextView);
		annualIncreaseTextView= (TextView) findViewById(R.id.k401kAnnualIncreaseTextView);
		employerMatchTextView = (TextView) findViewById(R.id.k401kEmployerMatchTextView);
		employerLimitTextView = (TextView) findViewById(R.id.k401kEmployerLimitTextView);
		
		currentSavingsEditText = (EditText) findViewById(R.id.k401kCurrentSavingsEditText);
		annualIncreaseEditText = (EditText) findViewById(R.id.k401kAnnualIncreaseEditText);
		employerLimitEditText = (EditText) findViewById(R.id.k401kEmployerLimitEditText);
		employerMatchEditText = (EditText) findViewById(R.id.k401kEmployerMatchEditText);
		contributionTypeTextView = (TextView) findViewById(R.id.k401kContributionTypeTextview);
		simple = (RadioButton) findViewById(R.id.k401k_simple_state_selector);
		advanced = (RadioButton) findViewById(R.id.k401k_advanced_state_selector);
		contributionEditText.setHint(contributionHint);
		
		simple.setOnClickListener(stateSelect);
		advanced.setOnClickListener(stateSelect);
		
		contributionTypeAdapter = ArrayAdapter.createFromResource(this, R.array.k401k_contribution_type, R.layout.my_spin);
		contributionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		contributionTypeSpinner.setAdapter(contributionTypeAdapter);
		k401kState=((RadioButton)findViewById(myRadioGroup.getCheckedRadioButtonId())).getText().toString();
		whatIf.setEnabled(false);
		calculate.setOnClickListener(calculatek401k);
		whatIf.setOnClickListener(whatIf401K);
		//text watchers
		
	}
	
	////////////////////////////////////////////////
	/////////////////////////////////////////////////
	//////// Onclick listener for the radio buttons
	private OnClickListener stateSelect = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			RadioButton rb = (RadioButton) v;
			k401kState = rb.getText().toString();
			updateUIforState(k401kState);			
		}
	};
	
	////////////////////////////////////////////
	////////////////////////////////////////////
	///////this function updates the UI after the radio button has been clicked
	
	private void updateUIforState(String stateName){
		if(stateName == simple.getText().toString()){
			currentSavingsTextView.setVisibility(View.GONE);
			annualIncreaseTextView.setVisibility(View.GONE);
			employerMatchTextView.setVisibility(View.GONE);
			employerLimitTextView.setVisibility(View.GONE);
			contributionTypeTextView.setVisibility(View.GONE);
			currentSavingsEditText.setVisibility(View.GONE);
			annualIncreaseEditText.setVisibility(View.GONE);
			employerLimitEditText.setVisibility(View.GONE);
			employerMatchEditText.setVisibility(View.GONE);
			contributionTypeSpinner.setVisibility(View.GONE);
			contributionEditText.setHint(contributionHint);
		}
		else{
			currentSavingsTextView.setVisibility(View.VISIBLE);
			annualIncreaseTextView.setVisibility(View.VISIBLE);
			employerMatchTextView.setVisibility(View.VISIBLE);
			employerLimitTextView.setVisibility(View.VISIBLE);
			contributionTypeTextView.setVisibility(View.VISIBLE);
			currentSavingsEditText.setVisibility(View.VISIBLE);
			annualIncreaseEditText.setVisibility(View.VISIBLE);
			employerLimitEditText.setVisibility(View.VISIBLE);
			employerMatchEditText.setVisibility(View.VISIBLE);
			contributionTypeSpinner.setVisibility(View.VISIBLE);
			contributionEditText.setHint("");
		}
	}
	
	

	/**
	 *********************************************
	 ********************************************* 
	 * on click listener for calculate button (does the validation
	 * *********************************************
	 * *********************************************
	 */
	private OnClickListener calculatek401k = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
		annualPayValid=	numval.validate(annualPayEditText, 1.0, true, 99999999.0, false);
		
		if(k401kState==advanced.getText() && contributionTypeSpinner.getSelectedItem().toString().equals("fixed $"))
			contributionValid= numval.validate(contributionEditText, 1.0, true, 99999.0, false);
		
		else contributionValid= numval.validate(contributionEditText, 1.0, true, 99.0, false);
		yearsToRetirementValid = numval.validate(yearsToRetirementEditText, 1.0, true, 60.0,false);
		rateOfReturnValid = numval.validate(rateOfReturnEditText, 1.0, true, 99.9, false);
		String sh = "";
		
		//annual pay validator
		if(annualPayValid.equals("Good")) annualPayEditText.setBackgroundResource(R.drawable.edit_text);
		else {
			sh += "Annual pay "+annualPayValid+"\n";
			annualPayEditText.setBackgroundResource(R.drawable.fight);
		}
		
		//contribution validator
		if(contributionValid.equals("Good")) contributionEditText.setBackgroundResource(R.drawable.edit_text);
		else{
			contributionEditText.setBackgroundResource(R.drawable.fight);
			sh += "Contribution "+contributionValid+"\n";
		}
		
		//yearsToRetirement validator
		if(yearsToRetirementValid.equals("Good")) yearsToRetirementEditText.setBackgroundResource(R.drawable.edit_text);
		else{
			yearsToRetirementEditText.setBackgroundResource(R.drawable.fight);
			sh += "Years to Retirement "+yearsToRetirementValid+"\n";
		}
		
		//rateOfReturn validator
		if(rateOfReturnValid.equals("Good")) rateOfReturnEditText.setBackgroundResource(R.drawable.edit_text);
		else{
			rateOfReturnEditText.setBackgroundResource(R.drawable.fight);
			sh += "Rate of return "+rateOfReturnValid;
		}
		
		if(sh.length()>0){
			Toast.makeText(getApplicationContext(), sh, Toast.LENGTH_LONG).show();
			int valpaz;
			
			if(k401kState== simple.getText().toString()) valpaz=1;
			else valpaz=2;
			
			globalTracker.trackEvent("ui_interaction", "bad_calc", "Calculator_401k_Activity", valpaz);
		}
		else compute();
		}			
		
	};
	
	
	
	/**
	 *********************************************
	 ********************************************* 
	 * This next function is the on click listener for the whatif button
	 * *********************************************
	 * *********************************************
	 */
	private OnClickListener whatIf401K = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent myI = new Intent(getApplicationContext(), Calculator_401k_Grapher.class);
			myI.putExtra("k401kState", k401kState);
			myI.putExtra("annualPay", annualPayNumber);
			myI.putExtra("rateOfReturnPecent", rateOfReturnPercentValue);
			myI.putExtra("contributionPercent", contributionPercentValue);
			myI.putExtra("finalAmountBeforeRetirement", finalAmountBeforeRetirementValue);
			myI.putExtra("annualIncrease", annualIncreaseNumber);
			myI.putExtra("currentSavings", currentSavingsNumber);
			myI.putExtra("employerMatch", employerMatchNumber);
			myI.putExtra("employerLimit", employerLimitNumber);
			myI.putExtra("numberOfYearsBeforeRetirement", numberOfYearsBeforeRetirementValue);
			myI.putExtra("contributionType", contributionTypeString);
			startActivity(myI);
		}
	}; 
	
	
	//////////////////////////////////////////////
	//////////////////////////////////////////////
	//this function does the actual calculation
	private void compute() {
		// TODO convert to appropriate value based on fixed vs. % for all
		annualPayNumber = Double.valueOf(annualPayEditText.getText().toString());
		contributionPercentValue = Double.valueOf(contributionEditText.getText().toString());
		numberOfYearsBeforeRetirementValue = Integer.valueOf(yearsToRetirementEditText.getText().toString());
		rateOfReturnPercentValue = Double.valueOf(rateOfReturnEditText.getText().toString());
		int valpaz;
		
		if(k401kState== simple.getText().toString()) valpaz=1;
		else valpaz=2;
		
		globalTracker.trackEvent("ui_interaction", "calculate", "Calculator_401k_Activity", valpaz);
		new backgroun401kCalculator().execute();		
		//k401kResultTextView.setText(mycalcLogic.k401kCalculator(annualPayNumber, contributionPercentValue, numberOfYearsBeforeRetirementValue, rateOfReturnPercentValue));
		
	}
	
	////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////
	///////////////////////////////////////////////////
	//the asynctask
	
	class backgroun401kCalculator extends AsyncTask<Void, Void, Double>{

		@Override
		protected Double doInBackground(Void... params) {
			CalculatorLogic mycalcLogic = new CalculatorLogic();		
			
				contributionTypeString= "percent";
				annualIncreaseNumber=0.0;
				currentSavingsNumber= 0.0;
				employerLimitNumber=0.0;
				employerMatchNumber=0.0;
		
			if(k401kState==advanced.getText()){
				if(contributionTypeSpinner.getSelectedItem().toString().equals("fixed $")) contributionTypeString= "fixed";				
				if(annualIncreaseEditText.length()>0)annualIncreaseNumber= Double.valueOf(annualIncreaseEditText.getText().toString());
				if(currentSavingsEditText.length()>0)currentSavingsNumber= Double.valueOf(currentSavingsEditText.getText().toString());
				if(employerMatchEditText.length()>0)employerMatchNumber= Double.valueOf(employerMatchEditText.getText().toString());
				if(employerLimitEditText.length()>0)employerLimitNumber= Double.valueOf(employerLimitEditText.getText().toString());				
			}
//			String s =String.format("State: %s\nAnnualPay: %f\nContribution Percent: %f\nContribution Type: %s\nNumber of years: %d\n" +
//					"Annual Increase: %f\nCurrent Savings: %f\nEmployer Match: %f\nEmployer Limit: %f\n\n",k401kState, annualPayNumber, contributionPercentValue, 
//					contributionTypeString, numberOfYearsBeforeRetirementValue, annualIncreaseNumber,currentSavingsNumber, employerMatchNumber, employerLimitNumber);
			finalAmountBeforeRetirementValue = mycalcLogic.k401kCalculator(annualPayNumber, contributionPercentValue,contributionTypeString,
					numberOfYearsBeforeRetirementValue, rateOfReturnPercentValue, annualIncreaseNumber, currentSavingsNumber, employerMatchNumber, employerLimitNumber);
			return finalAmountBeforeRetirementValue;
		}
		
		@Override
		protected void onPostExecute(Double result) {			
			super.onPostExecute(result);
			//Toast.makeText(getApplicationContext(), k401kState, Toast.LENGTH_LONG).show();
			NumberFormat  nf = NumberFormat.getCurrencyInstance();
			nf.setGroupingUsed(true);
			k401kResultTextView.setText(String.format("The final amount of your 401K just before retirement will be %s", nf.format(result)));
			//k401kResultTextView.setText(result);
			if (k401kResultTextView.getText().length()>0) whatIf.setEnabled(true);
		}
		
	}
}











