package com.ldp.android.baidumaptestnew.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.ldp.android.baidumaptestnew.R;

public class ComplexMapActivity extends Activity{
	
	private static final String TAG = ComplexMapActivity.class.getSimpleName();
	
	private MapView mMVCity;
	
	private BaiduMap mBaiduMap;
	
	private boolean mShowCityStreet;
	
	private LocationClient mLocationClient;
	
	private MyLocationListener mMyLocationListener;
	
	private boolean firstLocate = true;
	
	private LatLng currentLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_complex_map);  
        
        mMVCity = (MapView) findViewById(R.id.mv_city);
        
        initialMap();
        
        initialMyLocation();
	}
	
	private void initialMyLocation() {
		
		mLocationClient = new LocationClient(this);
		mMyLocationListener = new MyLocationListener();
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(5*1000);
		
		mLocationClient.setLocOption(option);
	}

	private void initialMap() {

		mBaiduMap = mMVCity.getMap();
		mShowCityStreet = false;
		
		MapStatusUpdate mapUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(mapUpdate);
	}

	@Override
	protected void onDestroy() {

		Log.i(TAG, "onDestroy");
		
		mMVCity.onDestroy();
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		
		Log.i(TAG, "onResume");
		
		super.onResume();
		mMVCity.onResume();
	}
	
	@Override
	protected void onPause() {

		Log.i(TAG, "onPause");
		
		mMVCity.onPause();
		super.onPause();
	}
	
	@Override
	protected void onStart() {
		
		Log.i(TAG, "onStart");
		super.onStart();
		mBaiduMap.setMyLocationEnabled(true);
		mLocationClient.registerLocationListener(mMyLocationListener);
		mLocationClient.start();
	}
	
	@Override
	protected void onStop() {
		
		Log.i(TAG, "onStop");

		mLocationClient.stop();
		mLocationClient.unRegisterLocationListener(mMyLocationListener);
		mBaiduMap.setMyLocationEnabled(false);
		super.onStop();
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

		case R.id.menu_item_show_my_location:
			
			if(currentLocation != null){
				locationToPosition(currentLocation);
			}
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	private void locationToPosition(LatLng latLng){
		
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(update);
	}
	
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {

			if(firstLocate){
				firstLocate = false;
				LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
				locationToPosition(latLng);
			}

			currentLocation = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
			
			MyLocationData myLocationData = 
					new MyLocationData.Builder()
						.accuracy(bdLocation.getRadius())
						.latitude(bdLocation.getLatitude())
						.longitude(bdLocation.getLongitude())
						.build();
			
			mBaiduMap.setMyLocationData(myLocationData);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
