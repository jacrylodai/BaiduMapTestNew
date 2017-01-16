package com.ldp.android.baidumaptestnew.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ldp.android.baidumaptestnew.R;
import com.ldp.android.baidumaptestnew.domain.FunctionItem;

public class MainActivity extends ActionBarActivity {
	
	private static final String TAG = MainActivity.class.getSimpleName();

	private ListView mLVFunction;
	
	private List<FunctionItem> mFunctionItemList;
	
	private ArrayAdapter<FunctionItem> mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initFunctionList();
		mLVFunction = (ListView) findViewById(R.id.lv_function);
		
		mAdapter = new ArrayAdapter<FunctionItem>(this
				, android.R.layout.simple_list_item_1, mFunctionItemList);
		mLVFunction.setAdapter(mAdapter);
		mLVFunction.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				FunctionItem funcItem = mAdapter.getItem(position);
				String desc = funcItem.getDesc();
				Log.i(TAG, "ƒ„—°‘Ò¡À:"+desc);
				
				Class activityClass = funcItem.getActivityClass();
				Intent intent = new Intent(MainActivity.this,activityClass);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	private void initFunctionList() {
		
		mFunctionItemList = new ArrayList<FunctionItem>();
		
		FunctionItem funcItem = null;
		
		funcItem = new FunctionItem();
		funcItem.setDesc("ª˘¥°µÿÕº≤‚ ‘");
		funcItem.setActivityClass(BaseMapActivity.class);
		
		mFunctionItemList.add(funcItem);
		
		funcItem = new FunctionItem();
		funcItem.setDesc("∏¥∫œ≤‚ ‘");
		funcItem.setActivityClass(ComplexMapActivity.class);
		
		mFunctionItemList.add(funcItem);
	}

}
