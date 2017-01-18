package com.ldp.android.baidumaptestnew.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.ldp.android.baidumaptestnew.R;

public class ComplexMapActivity extends Activity{
	
	private static final String TAG = ComplexMapActivity.class.getSimpleName();
	
	//因为使用了方向传感器，因此我的位置需要频繁的更新，地图更新的频率，毫秒
	private static final int FREQ_MAP_UPDATE = 70;
	
	private MapView mMVCity;
	
	private BaiduMap mBaiduMap;
	
	private boolean mShowCityStreet;
	
	private LocationClient mLocationClient;
	
	private MyLocationListener mMyLocationListener;
	
	private boolean firstLocate = true;
	
	private LatLng currentLocation;

	private SensorManager mSensorManager;
	
	private Sensor mAcceSensor,mMagnSensor;
	
	private OrientationSensorListener mOriSensorListener;
	
	private double zDegree;
	
	private BDLocation mMyBDLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_complex_map);  
        
        mMVCity = (MapView) findViewById(R.id.mv_city);
        
        initialMap();
        initialSensor();
        initialMyLocation();
	}
	
	private void initialSensor() {
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		if(mSensorManager != null){

	        Sensor acceSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        if(acceSensor != null){
	        	mAcceSensor = acceSensor;
	        }
	        Sensor magnSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	        if(magnSensor != null){
	        	mMagnSensor = magnSensor;
	        }
	        
	        if(mAcceSensor == null || mMagnSensor == null){
	        	
	        	Toast.makeText(this, R.string.cant_get_sensor, Toast.LENGTH_LONG).show();
	        }
		}else{
			
			Toast.makeText(this, R.string.cant_get_sensor, Toast.LENGTH_LONG).show();
		}
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
		
		BitmapDescriptor customMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		MyLocationConfiguration configuration = 
				new MyLocationConfiguration(LocationMode.NORMAL, true, customMarker);
		mBaiduMap.setMyLocationConfigeration(configuration);
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

		if(mSensorManager != null && mAcceSensor != null && mMagnSensor != null){
			
			mOriSensorListener = new OrientationSensorListener();
			
	        mSensorManager.registerListener(mOriSensorListener, mAcceSensor
	        		, SensorManager.SENSOR_DELAY_UI);
	        mSensorManager.registerListener(mOriSensorListener, mMagnSensor
	        		, SensorManager.SENSOR_DELAY_UI);
		}
	}
	
	@Override
	protected void onStop() {
		
		Log.i(TAG, "onStop");

		if(mSensorManager != null && mAcceSensor != null && mMagnSensor != null){
			mSensorManager.unregisterListener(mOriSensorListener);
		}
		
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
	
	/**
	 * 更新我的位置
	 * 因为使用了方向传感器，需要频繁更新我的位置
	 */
	private void updateMyLocation(){
		
		if(mMyBDLocation != null){
			MyLocationData myLocationData = 
					new MyLocationData.Builder()
						.accuracy(mMyBDLocation.getRadius())
						.direction((float)zDegree)
						.latitude(mMyBDLocation.getLatitude())
						.longitude(mMyBDLocation.getLongitude())
						.build();
			
			mBaiduMap.setMyLocationData(myLocationData);
		}
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
			mMyBDLocation = bdLocation;
			
			updateMyLocation();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			
		}
		
	}
	
	private class OrientationSensorListener implements SensorEventListener{

		private float[] acceValues,magnValues;
		
		private long lastRecordTime = System.currentTimeMillis();
		
		private long lastMagnTime = System.currentTimeMillis();
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				acceValues = event.values.clone();
			}else
				if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
					magnValues = event.values.clone();		
					
					long currentMagnTime = System.currentTimeMillis();
					long intervalMagnTime = currentMagnTime-lastMagnTime;
					Log.i(TAG, "magn sensor freq:(ms)"+intervalMagnTime);
					
					lastMagnTime = currentMagnTime;
				}
					
			if(acceValues != null && magnValues != null){
	    		
	    		float[] rValues = new float[9];
	    		SensorManager.getRotationMatrix(rValues, null, acceValues, magnValues);
	    		
	    		float[] orieValues = new float[3];
	    		SensorManager.getOrientation(rValues, orieValues);
	    			    		
	    		zDegree = Math.toDegrees(orieValues[0]);
	    	}
			
			long currentTime = System.currentTimeMillis();
			long intervalTime = currentTime - lastRecordTime;
			if(intervalTime > FREQ_MAP_UPDATE){
				lastRecordTime = currentTime;
				updateMyLocation();
			}
	    }

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
		
	}
	
}
