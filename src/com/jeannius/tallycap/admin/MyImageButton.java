package com.jeannius.tallycap.admin;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeannius.tallycap.R;

public class MyImageButton extends LinearLayout {
	
	
	public ImageButton imageButton;
	public TextView text;
	Context cont;
	
	public MyImageButton(Context context) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);

		cont = context;
		
		setter();
		

//		imageButton.setImageResource(Res);
		imageButton.setScaleType(ScaleType.CENTER_INSIDE);
		addView(imageButton);
		addView(text);

	}
	
	public MyImageButton(Context context, AttributeSet attrs){
		super(context, attrs);
		TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MyImageButton);
		cont = context;
		setOrientation(LinearLayout.VERTICAL);
		setter();
		imageButton.setScaleType(ScaleType.FIT_CENTER);
		try{
			
			CharSequence s = arr.getString(R.styleable.MyImageButton_textBelow);
			text.setText(s);
//			Bitmap z = scaling(arr.getResourceId(R.styleable.MyImageButton_imagesource, 0));
			imageButton.setImageDrawable(arr.getDrawable(R.styleable.MyImageButton_imagesource));
//			imageButton.setImageBitmap(z);

			
		}finally{
			
			arr.recycle();
		}
		
			
		
		addView(imageButton);
		addView(text);
	}
	
	


	private void setter() {
		imageButton = new ImageButton(cont);
		imageButton.setScaleType(ScaleType.FIT_CENTER);
		imageButton.setBackgroundResource(0);
		//imageButton.setAdjustViewBounds(true);
		
		text = new TextView(cont);
		text.setGravity(Gravity.CENTER);
		text.setTextColor(0xFFE8E595);
		text.setTextSize(10);
		
//		scaling();
	}
	
	public String getText(){
		return (String) text.getText();
	}
	
//	public void setImage(int r){
//		StateListDrawable s = (StateListDrawable) getResources().getDrawable(r);
//		Drawable d =s.getCurrent();
////		imageButton.setImageBitmap(scaling(d));
//		imageButton.setImageDrawable(d);
//	}


	

}



