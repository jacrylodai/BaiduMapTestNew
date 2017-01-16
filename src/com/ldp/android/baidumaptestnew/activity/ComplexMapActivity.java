package com.ldp.android.baidumaptestnew.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.ldp.android.baidumaptestnew.R;

public class ComplexMapActivity extends Activity{
	
	private MapView mMVCity;
	
	private BaiduMap mBaiduMap;
	
	private boolean mShowCityStreet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_complex_map);  
        
        mMVCity = (MapView) findViewById(R.id.mv_city);
        
        initial();
	}
	
	private void initial() {

		mBaiduMap = mMVCity.getMap();
		mShowCityStreet = false;
		
		MapStatusUpdate mapUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(mapUpdate);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.menu_complex, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
		case R.id.menu_item_nomal_map:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			return true;

		case R.id.menu_item_satellite_map:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			return true;

		case R.id.menu_item_show_city_street:
			
			if(mShowCityStreet){
				mShowCityStreet = false;
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle(R.string.show_city_street);
			}else{
				mShowCityStreet = true;
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle(R.string.not_show_city_street);
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
}
