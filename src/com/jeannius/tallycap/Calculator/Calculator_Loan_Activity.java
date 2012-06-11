package com.jeannius.tallycap.Calculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannius.tallycap.R;
import com.jeannius.tallycap.TrackedActivity;
import com.jeannius.tallycap.validators.NumberValidator;

public class Calculator_Loan_Activity extends TrackedActivity {
	
	private Button calculate, whatIf;
	private Button mdatePicker;
	private EditText loanAmountText, InterestRateText , termsChooser;
	private Calendar mCalendar;
	private Button mortgage, auto, general;
	private Spinner spinnerTermsChooser, spinnerPayFrequencyChooser, spinnerTermFreq;
	private TextView result,  FrequencyTextView;
	private ArrayAdapter<CharSequence> adapterAuto, adapterMortgage, adapterPaymentFrequency, adapterTermFreq;
	private RadioGroup myRadioGroup;
	private String loanState, valResultLoan, valResultInterest, valGeneralTerms;
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	private CalculatorLogic mCalc = new CalculatorLogic();
	private NumberValidator mVal = new NumberValidator();
	private Double i, p, x;
	private Integer n;
	private Boolean isItDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
				
		
		setContentView(R.layout.calculator_loan);

		setup();		//to set up all variables
		if(savedInstanceState!=null){
			//updateTerms(savedInstanceState.getString("loanState"));
			if (savedInstanceState.getString("loanState").equals(auto.getText())) auto.performClick();
			else if (savedInstanceState.getString("loanState").equals(mortgage.getText())) mortgage.performClick();
			else general.performClick();
			if(savedInstanceState.containsKey("result")){
				result.setText(savedInstanceState.getString("result"));
				whatIf.setEnabled(true);
				i = savedInstanceState.getDouble("i");
				p = savedInstanceState.getDouble("p");
				x = savedInstanceState.getDouble("x");
				n = savedInstanceState.getInt("n");
				//Toast.makeText(getApplicationContext(), String.format("i: %f\np: %f\nx: %f\nn: %d", i,p,x,n), Toast.LENGTH_LONG).show();
			}
		}
		
	
		
	}
	
	

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		outState.putString("loanState", ((RadioButton)findViewById(myRadioGroup.getCheckedRadioButtonId())).getText().toString());
		if(result.length()!=0){
			outState.putString("result", result.getText().toString());
			outState.putDouble("i", i);
			outState.putDouble("p", p);
			outState.putDouble("x", x);
			outState.putInt("n", n);
		}
		
	}
	
	
	//this function sets up the initial variables for the view
	private void setup() {		
			isItDate = false;	
		result = (TextView)findViewById(R.id.loanResultText);
		whatIf = (Button)findViewById(R.id.loanWhatIfButton);
		mdatePicker= (Button)findViewById(R.id.dateButton);
		loanAmountText = (EditText)findViewById(R.id.loan);
		InterestRateText= (EditText)findViewById(R.id.interest);
		mCalendar= Calendar.getInstance(); // calendar stuff
		mortgage = (Button)findViewById(R.id.calculator_mortgage_selector_button);
		auto = (Button)findViewById(R.id.calculator_auto_selector_button);
		general = (Button)findViewById(R.id.calculator_general_selector_button);
		spinnerTermsChooser =(Spinner) findViewById(R.id.spinner_terms);
		termsChooser = (EditText)findViewById(R.id.terms);
		calculate = (Button)findViewById(R.id.loanCalculateButton);
		myRadioGroup = (RadioGroup)findViewById(R.id.loanRadioGroup);
		spinnerTermFreq = (Spinner)findViewById(R.id.spinner_length_frequency);
		spinnerPayFrequencyChooser = (Spinner)findViewById(R.id.spinner_frequency);
		FrequencyTextView = (TextView)findViewById(R.id.tv4);
		
		adapterMortgage = ArrayAdapter.createFromResource(this, R.array.mortgage_terms, android.R.layout.simple_spinner_item);
		adapterMortgage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapterAuto = ArrayAdapter.createFromResource(this, R.array.auto_terms, android.R.layout.simple_spinner_item);
		adapterAuto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapterPaymentFrequency=ArrayAdapter.createFromResource(this, R.array.loan_frequency_chooser, android.R.layout.simple_spinner_item);
        adapterPaymentFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
        adapterTermFreq = ArrayAdapter.createFromResource(this, R.array.term_frequency_chooser	, android.R.layout.simple_spinner_item);
        adapterTermFreq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
		updateDateButtonText();
		termsChooser.setVisibility(View.GONE);
		spinnerTermsChooser.setVisibility(View.VISIBLE);		
		spinnerTermsChooser.setAdapter(adapterMortgage);
		spinnerPayFrequencyChooser.setVisibility(View.INVISIBLE);
		spinnerPayFrequencyChooser.setAdapter(adapterPaymentFrequency);
		spinnerTermFreq.setAdapter(adapterTermFreq);
		loanState = mortgage.getText().toString();
		
		spinnerTermFreq.setVisibility(View.GONE);
		FrequencyTextView.setVisibility(View.INVISIBLE);
		
	
		mortgage.setOnClickListener(stateSelect);
		auto.setOnClickListener(stateSelect);
		general.setOnClickListener(stateSelect);
		calculate.setOnClickListener(calculatorF);
		whatIf.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "Interest rate "+valResultInterest, Toast.LENGTH_SHORT).show();
				grapher();
			}
		});		
		
		
		 mdatePicker.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					isItDate=true;
					showDialog(0);				
				}
			});	 
		
	}
	
	
	//graph intent calls from what if button
	
	private void grapher(){
		
		Intent intent = new Intent(this, Calculator_Loan_Grapher.class);
		intent.putExtra("Activity", "Loan");
		
		
		
		intent.putExtra("p", p);
		intent.putExtra("i", i);
		intent.putExtra("x", x);
		if(loanState==general.getText()){
			intent.putExtra("PayFreq", spinnerPayFrequencyChooser.getSelectedItem().toString());
			int unit = preconverter();
			intent.putExtra("n", unit);
		}
		else {
			intent.putExtra("PayFreq", "Monthly");
			intent.putExtra("n", n);
		}
	
		intent.putExtra("date", mCalendar.getTime().getTime());
		
		startActivity(intent);
		//Toast.makeText(getApplicationContext(), "n: "+ mCalendar.getTime().toString(), Toast.LENGTH_LONG).show();
	}
	
	//preconverter function before grapher starts... this puts n in the same unit as the payfrequency, this only gets call in general state (see intent above)
	
	private int preconverter(){
		int unit=0;
		Calendar cal1 = (Calendar) mCalendar.clone();
		Calendar cal2 = (Calendar) cal1.clone();
		int after;
		int before = cal1.get(Calendar.DAY_OF_WEEK);
		//String ccccc = spinnerPayFrequencyChooser.getSelectedItem().toString();
		if(spinnerTermFreq.getSelectedItem().toString().equals("years")) cal2.add(Calendar.YEAR, n);		
		else if(spinnerTermFreq.getSelectedItem().toString().equals("months"))	cal2.add(Calendar.MONTH, n);		
		else cal2.add(Calendar.WEEK_OF_YEAR, n);
		after =cal2.get(Calendar.DAY_OF_WEEK);
		
		if(spinnerPayFrequencyChooser.getSelectedItem().toString().equals("Monthly"));
		else cal2.add(Calendar.DAY_OF_MONTH, before-after);
		
		while(cal1.before(cal2)){
			if(spinnerPayFrequencyChooser.getSelectedItem().toString().equals("Monthly")){
				cal1.add(Calendar.MONTH, 1);
				unit++;
			}
			else if(spinnerPayFrequencyChooser.getSelectedItem().toString().equals("Biweekly")){
				cal1.add(Calendar.WEEK_OF_YEAR, 2);
				unit++;
			}else{
				cal1.add(Calendar.WEEK_OF_YEAR, 1);
				unit++;
			}
		}
		
//		result.setText( String.format("n before: %d\nunit: %d %s\ncal1: %s\n\ncal2: %s", n, unit, ccccc,cal1.getTime().toString(),
//				cal2.getTime().toString()));
		return unit;
	}

	//logic to switch states
	private OnClickListener stateSelect = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Button rb = (Button) v;				
			loanState = rb.getText().toString();
			updateTerms(rb.getText().toString());						
		}
	};
	
	// logic to show and choose date on datebutton
	@Override
	protected Dialog onCreateDialog(int id) {
		if(isItDate) {
			isItDate= false;
			dialog = showDatePicker();
		}
		else super.onCreateDialog(TrackedActivity.MENU_SELECTOR_DIALOG);
		
		return dialog;
	}


	private DatePickerDialog showDatePicker() {
		DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mCalendar.set(Calendar.YEAR, year);
				mCalendar.set(Calendar.MONTH, monthOfYear);
				mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateDateButtonText();
			}

			
		}, mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH) );
		return datePicker;
	}
	
	private void updateDateButtonText() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		String dateForButton = dateFormat.format(mCalendar.getTime());
		mdatePicker.setText(dateForButton);
		
	}
	
	private void updateTerms(String state){
		
		if(state == mortgage.getText()){			
			termsChooser.setVisibility(View.GONE);
			spinnerTermsChooser.setVisibility(View.VISIBLE);
			spinnerTermsChooser.setAdapter(adapterMortgage);
			spinnerPayFrequencyChooser.setVisibility(View.INVISIBLE);
			FrequencyTextView.setVisibility(View.INVISIBLE);
			spinnerTermFreq.setVisibility(View.GONE);
			
		}
		else if(state == auto.getText()){
			termsChooser.setVisibility(View.GONE);
			spinnerTermsChooser.setVisibility(View.VISIBLE);
			spinnerTermsChooser.setAdapter(adapterAuto);
			spinnerPayFrequencyChooser.setVisibility(View.INVISIBLE);
			FrequencyTextView.setVisibility(View.INVISIBLE);
			spinnerTermFreq.setVisibility(View.GONE);
		}
		else{
			termsChooser.setVisibility(View.VISIBLE);
			spinnerTermsChooser.setVisibility(View.GONE);
			spinnerPayFrequencyChooser.setVisibility(View.VISIBLE);
			FrequencyTextView.setVisibility(View.VISIBLE);
			spinnerTermFreq.setVisibility(View.VISIBLE);
		}
	}
	
	
	//this function picks the first 2 letters of the spinner to get "n"
	private int two_letter_return(Spinner spin){	
		String red2 ="";
		String sel_text="";
		
			sel_text = spinnerTermsChooser.getSelectedItem().toString();		
			red2 = String.valueOf(sel_text.charAt(0))+ String.valueOf(sel_text.charAt(1));
		
		
		return n_getter(red2);		
	}
	
	private int n_getter(String spinnerString){
		
		if(loanState == auto.getText()){
			return Integer.valueOf(spinnerString);
		}
		else if(loanState ==mortgage.getText()){
			return ((Integer.valueOf(spinnerString)*12));
		}
		else return Integer.valueOf(spinnerString);
		
	}
	
	//this function does the validation when
	private OnClickListener calculatorF= new OnClickListener() {
			
		@Override
		public void onClick(View v) {			
			valResultInterest = mVal.validate(InterestRateText, 0.01, true, 5000.0,false);
			valResultLoan = mVal.validate(loanAmountText, 1.0, true, 999999.0,false);
			String sh="";
			

			
			
			
			
			//interest rate validation
			if(!valResultInterest.equals("Good")){
				sh+= "Intereset rate " + valResultInterest+"\n";
				InterestRateText.setBackgroundResource(R.drawable.fight);
			}
			else{
				InterestRateText.setBackgroundResource(R.drawable.edit_text);
				i = Double.valueOf(InterestRateText.getText().toString());
			}
			
			
			//loan amount validation
			if(!valResultLoan.equals("Good")){
				sh += "Loan Amount "+ valResultLoan +"\n";
				loanAmountText.setBackgroundResource(R.drawable.fight);
			}
			else{
				loanAmountText.setBackgroundResource(R.drawable.edit_text);
				p = Double.valueOf(loanAmountText.getText().toString());
			}
			
			
			//if general, term chooser validation
			if(loanState==general.getText()){
				if(spinnerTermFreq.getSelectedItem().toString().equals("years"))valGeneralTerms = mVal.validate(termsChooser, 1.0, true, 99.0,false);
				else valGeneralTerms = mVal.validate(termsChooser, 1.0, true, 999.0,false);
				if(valGeneralTerms.equals("Good")){
					termsChooser.setBackgroundResource(R.drawable.edit_text);

				}else{
					termsChooser.setBackgroundResource(R.drawable.fight);
					sh+= "Loan term "+valGeneralTerms;
					
				}
			}
			
			if(sh.length()>0){
				int valpaz ;
				if(loanState== auto.getText()) valpaz=1;
				else if(loanState== mortgage.getText()) valpaz=2;
				else valpaz=3;
				
				globalTracker.trackEvent("ui_interaction", "bad_calc", "Calculator_Loan_Activity", valpaz);
				Toast.makeText(getApplicationContext(), sh, Toast.LENGTH_LONG).show();
			}
			else {
				if(loanState==general.getText()) n =Integer.valueOf(termsChooser.getText().toString());
				else n = two_letter_return(spinnerTermsChooser);
//				Toast.makeText(getApplicationContext(), String.format("State: %s \n I: %f\nP: %f\n N:%d", loanState, i, p, n), Toast.LENGTH_LONG).show();
				calculate_real();
			}

		}
	};
	
	private void calculate_real(){
		result.setText("");
		int valpaz ;
		if(loanState== auto.getText()) valpaz=1;
		else if(loanState== mortgage.getText()) valpaz=2;
		else valpaz=3;
		
		globalTracker.trackEvent("ui_interaction", "calculate", "Calculator_Loan_Activity", valpaz);
		new backgroundCalculator().execute();
		
	}
	
class backgroundCalculator extends AsyncTask<Void, Void, Double>{

	@Override
	protected Double doInBackground(Void... params) {
		String s;
		String lengthFrequency ;
		if(loanState == general.getText()){
			s = spinnerPayFrequencyChooser.getSelectedItem().toString();
			lengthFrequency = spinnerTermFreq.getSelectedItem().toString();
		}
		else{
			lengthFrequency="months";
			s ="Monthly";
		}
		
		
		
		Calendar temcaleer = Calendar.getInstance();
		temcaleer = (Calendar) mCalendar.clone();
		x = mCalc.loanPaymentCalculator(i, p, n, s, temcaleer,lengthFrequency);
		
		return x;
	}
	@Override
	protected void onPostExecute(Double result2) {
		// TODO Auto-generated method stub
		super.onPostExecute(result2);
		whatIf.setEnabled(true);
		result.setText("Your payments are: $"+String.valueOf(result2));
	}

	
}
	
}







