package com.jeannius.tallycap;

import java.util.HashMap;

import com.jeannius.tallycap.Calculator.Calculator_401k_Activity;
import com.jeannius.tallycap.Calculator.Calculator_CreditCard_Activity;
import com.jeannius.tallycap.Calculator.Calculator_Loan_Activity;
import com.jeannius.tallycap.Calculator.Calculator_Loan_Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TabHost;

public class CalculatorActivity2 extends FragmentActivity{
TabHost mTabhost;
TabManager mTabManager;
	
@Override
protected void onCreate(Bundle arg0) {
	
	super.onCreate(arg0);
	setContentView(R.layout.calculator_tab_main2);
	mTabhost = (TabHost) findViewById(android.R.id.tabhost);
	mTabhost.setup();
	
	mTabManager = new TabManager(this, mTabhost, R.id.realtabcontent2);
	
	mTabManager.addTab(mTabhost.newTabSpec("Loan").setIndicator("Loan"), Calculator_Loan_Fragment.class	,null);
	
}







public static class TabManager implements TabHost.OnTabChangeListener{
	
	private final FragmentActivity mActivity;
	private final TabHost mTabhost;
	private final int mContainerId;
	private final HashMap<String, tabInfo> mTabs = new HashMap<String, CalculatorActivity2.TabManager.tabInfo>();
	tabInfo mlastTab;
	
	
	static final class tabInfo{
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;
		private android.support.v4.app.Fragment fragment;
		
		public tabInfo(String _tag, Class<?> _class, Bundle _args){
			
			tag = _tag;
			clss = _class;
			args = _args;
		}
		
	}
	
	
	static class dummyTabFactory implements TabHost.TabContentFactory{
		private final Context mContext;
		
		public dummyTabFactory(Context context){
			mContext = context;
		}
		
		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumHeight(0);
			v.setMinimumWidth(0);
			return v;
		}
		
	}
	
	
	public TabManager(FragmentActivity activity, TabHost tabHost, int containerId){
		
		mActivity = activity;
		mTabhost = tabHost;
		mContainerId = containerId;
		mTabhost.setOnTabChangedListener(this);
	}
	
	
	public void addTab(TabHost.TabSpec tabspec, Class<?> clss, Bundle args){
		tabspec.setContent(new dummyTabFactory(mActivity));
		String tag = tabspec.getTag();
		
		tabInfo info = new tabInfo(tag, clss, args);
//		// CHECK TO SEE IF WE ALREADY HAVE A FRAGMENT FOR THIS TAB, PROBABLY
//      // FROM A PREVIOUSLY SAVED STATE.  IF SO, DEACTIVATE IT, BECAUSE OUR
//      // INITIAL STATE IS THAT A TAB ISN'T SHOWN.
		
		info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
		if(info.fragment != null && !info.fragment.isDetached()){
			android.support.v4.app.FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
			ft.detach(info.fragment);
			ft.commit();
		}
		
		mTabs.put(tag, info);
		mTabhost.addTab(tabspec);
	}
	
	
	
	@Override
	public void onTabChanged(String tabId) {
		tabInfo newTab = mTabs.get(tabId);
		if(mlastTab!=newTab){
			android.support.v4.app.FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
			if(mlastTab!=null) if(mlastTab.fragment!=null) ft.detach(mlastTab.fragment);
			if(newTab!=null){
				if(newTab.fragment ==null){
					newTab.fragment = android.support.v4.app.Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
					ft.add(mContainerId, newTab.fragment, newTab.tag);
				}
				else ft.attach(newTab.fragment);
			}
			
			mlastTab = newTab;
			ft.commit();
			mActivity.getSupportFragmentManager().executePendingTransactions();
		}
		
	}
	
	
}


}
