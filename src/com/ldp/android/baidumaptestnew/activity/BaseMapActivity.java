package com.ldp.android.baidumaptestnew.activity;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.ldp.android.baidumaptestnew.R;

public class BaseMapActivity extends Activity{
	
	private MapView mMVCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_base_map);  
        
        mMVCity = (MapView) findViewById(R.id.mv_city);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMVCity.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mMVCity.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mMVCity.onPause();
	}
	
}
