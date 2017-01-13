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
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
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
