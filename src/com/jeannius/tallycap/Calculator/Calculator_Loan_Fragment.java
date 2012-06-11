package com.jeannius.tallycap.Calculator;

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeannius.tallycap.validators.NumberValidator;

public class Calculator_Loan_Fragment extends Fragment {
	CharSequence mLabel;
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
	private View vF;
	private Context c;
	
	/**
     * Create a new instance of Calculator_Loan_Fragment that will be initialized
     * with the given arguments.
     */
	public static Calculator_Loan_Fragment newInstance(CharSequence label){
		Calculator_Loan_Fragment f = new Calculator_Loan_Fragment();
		Bundle b = new Bundle();
		b.putCharSequence("label", label);
		f.setArguments(b);	
		
		return f;
	}
	
	//can not do it via XML!!!
	
	
	/**
     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		if(args!=null) mLabel = args.getCharSequence("label", mLabel);
	}

	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v =inflater.inflate(com.jeannius.tallycap.R.layout.calculator_loan, container, false);
		vF = v;
//		setup();
		return v;
	}
	
	
	
//	
//	//this function sets up the initial variables for the view
//	private void setup() {		
//			isItDate = false;	
//		result = (TextView)vF.findViewById(R.id.loanResultText);
//		whatIf = (Button)vF.findViewById(R.id.loanWhatIfButton);
//		mdatePicker= (Button)vF.findViewById(R.id.dateButton);
//		loanAmountText = (EditText)vF.findViewById(R.id.loan);
//		InterestRateText= (EditText)vF.findViewById(R.id.interest);
//		mCalendar= Calendar.getInstance(); // calendar stuff
//		mortgage = (Button)vF.findViewById(R.id.calculator_mortgage_selector_button);
//		auto = (Button)vF.findViewById(R.id.calculator_auto_selector_button);
//		general = (Button)vF.findViewById(R.id.calculator_general_selector_button);
//		spinnerTermsChooser =(Spinner) vF.findViewById(R.id.spinner_terms);
//		termsChooser = (EditText)vF.findViewById(R.id.terms);
//		calculate = (Button)vF.findViewById(R.id.loanCalculateButton);
//		myRadioGroup = (RadioGroup)vF.findViewById(R.id.loanRadioGroup);
//		spinnerTermFreq = (Spinner)vF.findViewById(R.id.spinner_length_frequency);
//		spinnerPayFrequencyChooser = (Spinner)vF.findViewById(R.id.spinner_frequency);
//		FrequencyTextView = (TextView)vF.findViewById(R.id.tv4);
//		c = getActivity().getApplicationContext();
//		adapterMortgage = ArrayAdapter.createFromResource(c, R.array.mortgage_terms, android.R.layout.simple_spinner_item);
//		adapterMortgage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		
//		adapterAuto = ArrayAdapter.createFromResource(c, R.array.auto_terms, android.R.layout.simple_spinner_item);
//		adapterAuto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		
//		adapterPaymentFrequency=ArrayAdapter.createFromResource(c, R.array.loan_frequency_chooser, android.R.layout.simple_spinner_item);
//        adapterPaymentFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		
//        adapterTermFreq = ArrayAdapter.getcreateFromResource(c, R.array.term_frequency_chooser	, android.R.layout.simple_spinner_item);
//        adapterTermFreq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        
//		updateDateButtonText();
//		termsChooser.setVisibility(View.GONE);
//		spinnerTermsChooser.setVisibility(View.VISIBLE);		
//		spinnerTermsChooser.setAdapter(adapterMortgage);
//		spinnerPayFrequencyChooser.setVisibility(View.INVISIBLE);
//		spinnerPayFrequencyChooser.setAdapter(adapterPaymentFrequency);
//		spinnerTermFreq.setAdapter(adapterTermFreq);
//		loanState = mortgage.getText().toString();
//		
//		spinnerTermFreq.setVisibility(View.GONE);
//		FrequencyTextView.setVisibility(View.INVISIBLE);
//		
//	
//		mortgage.setOnClickListener(stateSelect);
//		auto.setOnClickListener(stateSelect);
//		general.setOnClickListener(stateSelect);
//		calculate.setOnClickListener(calculatorF);
//		whatIf.setOnClickListener(new OnClickListener() {			
//			
//			@Override
//			public void onClick(View v) {
//				//Toast.makeText(getApplicationContext(), "Interest rate "+valResultInterest, Toast.LENGTH_SHORT).show();
//				grapher();
//			}
//		});		
//		
//		
//		 mdatePicker.setOnClickListener(new View.OnClickListener() {			
//				@Override
//				public void onClick(View v) {
//					isItDate=true;
//					showDialog(0);				
//				}
//			});	 
//		
//	}
//	
//	
//	
//	
//	
//	//graph intent calls from what if button
//	
//	private void grapher(){
//		
//		Intent intent = new Intent(c, Calculator_Loan_Grapher.class);
//		intent.putExtra("Activity", "Loan");
//		
//		
//		
//		intent.putExtra("p", p);
//		intent.putExtra("i", i);
//		intent.putExtra("x", x);
//		if(loanState==general.getText()){
//			intent.putExtra("PayFreq", spinnerPayFrequencyChooser.getSelectedItem().toString());
//			int unit = preconverter();
//			intent.putExtra("n", unit);
//		}
//		else {
//			intent.putExtra("PayFreq", "Monthly");
//			intent.putExtra("n", n);
//		}
//	
//		intent.putExtra("date", mCalendar.getTime().getTime());
//		
//		startActivity(intent);
//		//Toast.makeText(getApplicationContext(), "n: "+ mCalendar.getTime().toString(), Toast.LENGTH_LONG).show();
//	}
	
	
}
