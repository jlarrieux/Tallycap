package com.jeannius.tallycap.Calendar;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.jeannius.tallycap.HomeScreenActivity;

public class calendarGridMonth extends LinearLayout {
	
	
	private Context context;
	private Boolean selectable;
	private boolean popable;
	
	
	public calendarGridMonth(Context context2,Boolean sel, Boolean pop) {
		super(context2);
		context = context2;
		
		setOrientation(LinearLayout.VERTICAL);
		selectable = sel;
		popable = pop;
		setup();
		createHeader();
		
	}
	
	private void createHeader() {
		
		for (int i =0; i <7; i++){
			
			
			((calendarGridItem)((calendarGridWeek)this.getChildAt(0)).getChildAt(i)).removeViewAt(1);
			((calendarGridItem)((calendarGridWeek)this.getChildAt(0)).getChildAt(i)).calendarGridItemTextView
			.setText(HomeScreenActivity.daysOfTheWeek[i]);
			((calendarGridItem)((calendarGridWeek)this.getChildAt(0)).getChildAt(i)).calendarGridItemTextView
			.setGravity(Gravity.CENTER);
			((calendarGridItem)((calendarGridWeek)this.getChildAt(0)).getChildAt(i)).setBackgroundColor(0x60000000);
			((calendarGridItem)((calendarGridWeek)this.getChildAt(0)).getChildAt(i)).setMinimumHeight(0);
			
		}
		
		
		
	}

	private void setup() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		
		for(int i=0; i<7; i++) {
			calendarGridWeek gridw = new calendarGridWeek(context,  selectable, popable);
			
			addView(gridw, params);
			
		}
		
	}
	
	
	public void reset(){
		
		calendarGridWeek w;
//		Toast.makeText(getContext(), "Reset!!\nnu= "+ String.valueOf(CalendarPopUp.nu), Toast.LENGTH_LONG).show();
		for(int t=1; t<7; t++){
			w = (calendarGridWeek) getChildAt(t);
			w.reset();
		}
	}
	

	

}
