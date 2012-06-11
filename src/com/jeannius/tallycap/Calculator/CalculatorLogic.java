package com.jeannius.tallycap.Calculator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYSeries;

import com.jeannius.tallycap.HomeScreenActivity;

import android.content.Context;


public class CalculatorLogic {
	private static final String DATE_FORMAT2 = "MMMM dd yyyy"; //month, date year
	private static final String DATE_FORMAT3 = "EEEE MMMM dd yyyy"; //day month date year
	private static final SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT2);
	
	//this function calculates the payment
	public double loanPaymentCalculator(double i, double p, int n, String Payfrequency, Calendar endDate, String LengthFrequency){
		int newN = 0;		
		
		Calendar clonerFinalDate = Calendar.getInstance();
		clonerFinalDate =(Calendar) endDate.clone();
		
		Calendar clonerDate2 = Calendar.getInstance();
		clonerDate2 =(Calendar) endDate.clone();
		
		dateformat.applyPattern(DATE_FORMAT3);
		int before = clonerFinalDate.get(Calendar.DAY_OF_WEEK);
		
		if(LengthFrequency.equals("years")) clonerFinalDate.add(Calendar.YEAR, n);
		else if(LengthFrequency.equals("months")) clonerFinalDate.add(Calendar.MONTH, n);
		else if(LengthFrequency.equals("weeks")) clonerFinalDate.add(Calendar.WEEK_OF_MONTH, n);
		
		int after = clonerFinalDate.get(Calendar.DAY_OF_WEEK);
		
		clonerFinalDate.add(Calendar.DAY_OF_MONTH, (before-after));
		

		if(Payfrequency.equals( "Weekly")){			
			i = i/5200;
			while(clonerDate2.before(clonerFinalDate)){
				clonerDate2.add(Calendar.WEEK_OF_MONTH, 1);
				newN++;
			}			
			
		}
		else if(Payfrequency.equals("Biweekly")){
			i = i/2600;
			while(clonerDate2.before(clonerFinalDate)){
				clonerDate2.add(Calendar.WEEK_OF_MONTH, 2);
				newN++;
			}
		}
		else if(Payfrequency.equals("Monthly")){
			i = i/1200;
			while(clonerDate2.before(clonerFinalDate)){
				clonerDate2.add(Calendar.MONTH, 1);
				newN++;
			}
		}
		else{
			newN = n;
			i= i/1200;
		}			
		
		double u = Math.pow(1+i, newN);
		double top = p*(i*u);
		double bottom = (u-1);
		double a = top/bottom;
		
		long factor = (long)Math.pow(10, 2);
		a = a * factor;
		long tmp = Math.round(a);
		double fine = (double)tmp/factor;
		//zip = String.format("n: %d\npayfreq: %s\nlenghtfreq: %s\nNewN: %d\nPay: %f", n, Payfrequency,LengthFrequency, newN, fine);
		
		//return zip;
		return  fine;

	}
	
	//////////////////////////////////////////////////
	////////////////////////////////////////////////
	// this is for the graph payments for loans
	
	public XYSeries loanPaymentWhatif(Double p, Double i, int n,  Double payments, String title, long beginDate, String nType){
		//Debug.startMethodTracing("Math.Trace");
		

		TimeSeries mCurrentSeries = new TimeSeries(title);
		Date tempDate = new Date();
		tempDate.setTime(beginDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(tempDate);
		long pint = (int)Math.round(p*100);
		long xint = (int)Math.round(payments*100);
		long f;	
		if(nType.equals("Monthly"))	i = i/1200;
		else if(nType.equals("Biweekly"))	i = i/2600;
		else 	i = i/5200;
		int hh =0;		
		
		for(int j =0; j<n+1; j++){				
					
			if(nType.equals("Monthly"))	cal.add(Calendar.MONTH, hh);
			else if(nType.equals("Biweekly"))	cal.add(Calendar.WEEK_OF_MONTH, hh);
			else 	cal.add(Calendar.WEEK_OF_MONTH, hh);
						
			if(pint<0)	pint=0;				
				p=(double) pint/100;			
			mCurrentSeries.add(cal.getTime(), p);
			
			if(pint==0)	break;					
				f = (long) (pint* (1+i));
				f = f - xint;
				pint=f;
				if(nType.equals("Biweekly")) hh=2;
				else hh=1;
			
		}		

		return mCurrentSeries;	

	}
	
	////////////////////////////////////////////////////
	//////////////////////////////////////////////
	//this is for calculating 401k final amount
	
	public double k401kCalculator(double annualPay, double contributionAmount, String contributionType, int numberOfYearsBeforeRetirement,
			double rateOfReturnPercent, double annualIncrease, double currentSavings, double employerMatch, double employerLimit){
		
		double f, actualValueTowardsRetirement, a1,a2;//a1= amount your contributing, a2= amount employer is contributing
		f=0;
		rateOfReturnPercent = rateOfReturnPercent/100; // to percent
		employerLimit= employerLimit/100;
		employerMatch=employerMatch/100;
		annualIncrease= annualIncrease/100;
		actualValueTowardsRetirement=currentSavings;
		
		double u = 1 + rateOfReturnPercent;
		//String s="";
		
		for(int j=0; j<numberOfYearsBeforeRetirement; j++){
			//step 1 update a1
			if(contributionType=="fixed") a1 = contributionAmount;
			else a1= annualPay*contributionAmount/100;
			
			//step 2 update a2
			a2= a1*employerMatch;
			if(employerLimit>0.0)if(a2> annualPay*employerLimit) a2=annualPay*employerLimit;
						
			//step 3 update total a (actualValueTowardsRetirement
			actualValueTowardsRetirement = a1+a2;
			//s+= "f-before: "+ String.valueOf(f)+"\na-before: "+ String.valueOf(actualValueTowardsRetirement)+"\n";
			actualValueTowardsRetirement+=f;
			f= (actualValueTowardsRetirement*u);
			if(annualIncrease>0.0) annualPay = annualPay* (1+annualIncrease);
			//s+= "f-after: "+ String.valueOf(f)+"\na-after: "+ String.valueOf(actualValueTowardsRetirement)+"\nu: "+String.valueOf(u)+"\n\n\n";
		}
		
		
		long factor = (long)Math.pow(10, 2);
		f = f * factor;
		long tmp = Math.round(f);
		double fine = (double)tmp/factor;
		return fine;		
	}
	
	
	
	/**
	 * ****************************************************
	 * ****************************************************
	 * This function takes care of graphing for 401k
	 * ****************************************************
	 * ******************************************************
	 */
	
	public static XYSeries k401kPaymentWhatIf(double annualPay, double contributionAmount, String contributionType, int numberOfYearsBeforeRetirement,
			double rateOfReturnPercent, double annualIncrease, double currentSavings, double employerMatch, double employerLimit, String title, int OnYear, double extraCon){
		
		XYSeries myseries = new XYSeries(title);
		double f, actualValueTowardsRetirement, a1 = 0,a2 = 0;//a1= amount your contributing, a2= amount employer is contributing
		f=0;
		rateOfReturnPercent = rateOfReturnPercent/100; // to percent
		employerLimit= employerLimit/100;
		employerMatch=employerMatch/100;
		annualIncrease= annualIncrease/100;
		actualValueTowardsRetirement=currentSavings;
		long factor = (long)Math.pow(10, 2);
		double u = 1 + rateOfReturnPercent;
		//String s="";
		
		for(int j=0; j<numberOfYearsBeforeRetirement+1; j++){
			
			f = f * factor;
			long tmp = Math.round(f);
			f = (double)tmp/factor;
			
			//step -1 update contribution for whatif
			if(j>=OnYear && extraCon!=0){
				contributionAmount = extraCon;
			}
			
			//s+=String.format("j: %d\nf: %.2f\na: %.2f\na1: %.2f\na2: %.2f\n\n", j,f, actualValueTowardsRetirement, a1, a2);
			//step 0 add to xyseries
			myseries.add(j, f);
			//step 1 update a1
			if(contributionType=="fixed") a1 = contributionAmount;
			else a1= annualPay*contributionAmount/100;
			
			//step 2 update a2
			a2= a1*employerMatch;
			if(employerLimit>0.0)if(a2> annualPay*employerLimit) a2=annualPay*employerLimit;
						
			//step 3 update total a (actualValueTowardsRetirement
			actualValueTowardsRetirement = a1+a2;
			//s+= "f-before: "+ String.valueOf(f)+"\na-before: "+ String.valueOf(actualValueTowardsRetirement)+"\n";
			actualValueTowardsRetirement+=f;
			f= (actualValueTowardsRetirement*u);
			if(annualIncrease>0.0) annualPay = annualPay* (1+annualIncrease);	
		
		}
		//return s;
		return myseries;
}

	/**
	 * *********************************************************
	 * *********************************************************
	 * This function Calculates the credit card minimum payment
	 * *********************************************************
	 * *********************************************************
	 */
	private static int minpay = 15;
	
	public double creditCardMinimumPaymentCalculator(double currentBalance, double apr, double monthlyFee, double overTheLimitFee, double creditLimit){
		apr = apr/1200;
		double payment= currentBalance*apr + currentBalance*0.01+monthlyFee;		
		
		if(currentBalance> creditLimit) payment += overTheLimitFee;
		if(payment<minpay) payment =minpay;
		return payment;
	}
	
	
	public static XYSeries creditCardWhatIf(double currentBalance, double apr, double monthlyFee, double overTheLimitFee, double creditLimit, String title,
			String monthOfAnnualFee, double annualFee, Context con, double extra){
		
		TimeSeries myseries = new TimeSeries(title);
		Date mydate = new Date();
		Calendar cal = Calendar.getInstance();
		mydate.setTime(System.currentTimeMillis());
		cal.setTime(mydate);
		ArrayList<String> mylist = new ArrayList<String>(Arrays.asList( HomeScreenActivity.monthArray));
		int monthNumber = mylist.indexOf(monthOfAnnualFee);
		int j=0;
		apr = apr/1200;
		double payment=0, futureBalance=0 ;
		
		
		dateformat.applyLocalizedPattern(DATE_FORMAT2);
		
		
		while(true){
			
			
			//step 1 save and then update everything
			myseries.add(cal.getTime(), currentBalance);		
//			if(j>700)		
//				mys += 	String.format("J: %d\nCurrent Balance: %s\nPayments just applied: %s\nDate: %s\n\n",j, numf.format(currentBalance), 
//						numf.format(payment),dateformat.format(cal.getTime()));
			if(currentBalance==0)break;

			
			
			payment = currentBalance*apr + currentBalance*0.01+ monthlyFee+extra;
			if(payment<minpay)payment= minpay;			
			if(currentBalance> creditLimit) payment += overTheLimitFee;
			if(cal.getTime().getMonth()==monthNumber)payment+= annualFee;
			
			
			if(currentBalance<minpay)payment=currentBalance;
			currentBalance = currentBalance-payment;
			
			futureBalance = currentBalance*(1+apr)+ monthlyFee;
			if(futureBalance>creditLimit)futureBalance+= overTheLimitFee;
			if(cal.getTime().getMonth()== monthNumber)futureBalance+= annualFee;
			if(currentBalance<payment)futureBalance=0;
			currentBalance = futureBalance;			
			if(currentBalance<=0)futureBalance=0;
			
			cal.add(Calendar.MONTH, 1);
			j++;
			if(j>1000){
				
				//mys +=String.format("J: %d\nCurrent Balance: %s\nPayments just applied: %s\n\n\n",j, numf.format(currentBalance), numf.format(payment));
				break;
			}
		}
		
		
//		FileOutputStream fos;
//		try {
//			mys+="\n\n\n\n\n"+String.valueOf(TOTALPAY);
//			fos = con.openFileOutput("Red2.txt", Context.MODE_PRIVATE);
//			fos.write(mys.getBytes());
//			fos.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return myseries;
	}
	
}










