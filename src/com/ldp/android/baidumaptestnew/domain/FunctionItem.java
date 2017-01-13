package com.ldp.android.baidumaptestnew.domain;

public class FunctionItem {

	private String mDesc;
	
	private Class mActivityClass;

	public String getDesc() {
		return mDesc;
	}

	public void setDesc(String desc) {
		mDesc = desc;
	}

	public Class getActivityClass() {
		return mActivityClass;
	}

	public void setActivityClass(Class activityClass) {
		mActivityClass = activityClass;
	}
	
	@Override
	public String toString() {
		return mDesc;
	}
	
}
