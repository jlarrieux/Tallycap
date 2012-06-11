package com.jeannius.tallycap.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannius.tallycap.R;

public class calendarGridItem extends LinearLayout {
	
	
	public TextView calendarGridItemTextView;
	
	private Context cont;
	public Date calendarGridItemDate;
	public Boolean selectedAlready, containsReminder;
	public LinearLayout calendarGridItemImageLayout;
	public List<CalendarObject> ob;
	public ImageView plusSign, minusSign, star;
	private int w, h;
	private Boolean selectable;
	
	public calendarGridItem(Context context, Boolean sel) {
		super(context);
		cont = context;
		selectable = sel;
		setOrientation(LinearLayout.VERTICAL);
//		setMinimumWidth(screenWidth/8);
		Display dis = ((WindowManager)cont.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		w = dis.getWidth();
		h = dis.getHeight();
		setMinimumHeight(h/10);
		selectedAlready = false;
		setBackgroundColor(0x60888888);
		setter();
		setGravity(Gravity.CENTER);
	}
	
	
	public String getMeas(){
		String s=String.format("Width: %d\nHeight: %d", w, h);
		return s;
	}
	
	
	private void setter(){
		calendarGridItemTextView = new TextView(cont);
		calendarGridItemDate = new Date();
		calendarGridItemImageLayout = new LinearLayout(cont);
		plusSign = new ImageView(cont);
		minusSign = new ImageView(cont);
		star = new ImageView(cont);
		containsReminder = false;
		calendarGridItemImageLayout.setOrientation(LinearLayout.HORIZONTAL);
		calendarGridItemImageLayout.setGravity(Gravity.CENTER);
		if(selectable) setMinimumHeight(0);
		if(w>700 && h>700 ){
			
			if(!selectable){calendarGridItemTextView.setTypeface(Typeface.DEFAULT_BOLD);
				calendarGridItemTextView.setTextSize(18);
			}
			else{
				setMinimumWidth(w/9);
				setMinimumHeight(h/10);
			}
		}
		
		
//		calendarGridItemImageLayout.setMinimumHeight(20);
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.setMargins(0, 0	, 5, 3);
			
		LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.setMargins(0, 0	, 0,0);
		calendarGridItemTextView.setGravity(Gravity.RIGHT);
		
		
		
//		plusSign.setImageResource(R.drawable.calendar_plus_sign);		
//		minusSign.setImageResource(R.drawable.calendar_minus_sign);
//		star.setImageResource(R.drawable.star12);
		
		
		plusSign.setImageBitmap(sizer(R.drawable.calendar_plus_sign));
		minusSign.setImageBitmap(sizer(R.drawable.calendar_minus_sign));
		star.setImageBitmap(sizer(R.drawable.star12));
		
//		plusSign.setScaleType(ScaleType.FIT_CENTER);
		minusSign.setScaleType(ScaleType.FIT_CENTER);
		star.setScaleType(ScaleType.FIT_CENTER);
		
		calendarGridItemImageLayout.addView(minusSign);
		calendarGridItemImageLayout.addView(star);
		calendarGridItemImageLayout.addView(plusSign);
		
		
		addView(calendarGridItemTextView, params);
		addView(calendarGridItemImageLayout, params2);
		resetInvisible();
		ob = new ArrayList<CalendarObject>();
		
	}
	
	private Bitmap sizer(int Res){
		
		Bitmap b = BitmapFactory.decodeResource(getResources(), Res);
		int width = b.getWidth();
		int height = b.getHeight();
		float aspect =(float) width/height;
		float scaleHeight;
		if(h<w)	scaleHeight = h/35;
		else scaleHeight = w/35;
		float scaleWidth = scaleHeight/aspect;
		
		Matrix m= new Matrix();
		m.postScale(scaleWidth/width, scaleHeight/height);
		Bitmap b2 = Bitmap.createBitmap(b,0,0,width,height,m, true);
		
		
		return b2;
	}
	
	
	//get the date of this item
	public Date getDate(){
		
		return calendarGridItemDate;
	}
	
	
	//set the date of this item
	public void setDate(Date date){
		
		calendarGridItemDate = date;

		calendarGridItemTextView.setText(String.valueOf(date.getDate()));
	}
		
	//set the image of this item
//	public void addImage(int ResID){
//		ImageView img= new ImageView(cont);
//	    img.setImageResource(ResID);
//		calendarGridItemImageLayout.addView(img);		
//	}
	
	
	public void resetInvisible(){
		
		plusSign.setVisibility(View.INVISIBLE);
		minusSign.setVisibility(View.INVISIBLE);
		star.setVisibility(View.INVISIBLE);
		
		
	}
	
//	private void setInvisible(){
//	
//	}
//	
//	private void setGone(){
//		plusSign.setVisibility(View.GONE);
//		minusSign.setVisibility(View.GONE);
//		star.setVisibility(View.GONE);
//	}
}
