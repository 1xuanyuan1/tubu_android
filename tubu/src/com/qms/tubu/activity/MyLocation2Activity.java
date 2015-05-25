package com.qms.tubu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.qms.tubu.R;
import com.qms.tubu.application.MyApplication;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.config.Config;

public class MyLocation2Activity extends Activity{
	private LocationClient mLocationClient;
	private EditText et_name;
	private TextView latitude,longitude,belong;
	private Button startLocation;
	private Button addScenic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylocation);
		mLocationClient = ((MyApplication)getApplication()).mLocationClient;
		
		et_name = (EditText)findViewById(R.id.myLocation_name);
		latitude = (TextView)findViewById(R.id.myLocation_latitude);
		longitude = (TextView)findViewById(R.id.myLocation_longitude);
		belong = (TextView)findViewById(R.id.myLocation_belong);
		
		 ((MyApplication)getApplication()).latitude = latitude;
		 ((MyApplication)getApplication()).longitude = longitude;
		 
		startLocation = (Button)findViewById(R.id.addfence);
		startLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitLocation();
				if(startLocation.getText().equals(getString(R.string.startlocation))){
					mLocationClient.start();
					startLocation.setText(getString(R.string.stoplocation));
				}else{
					mLocationClient.stop();
					latitude.setText("");
					longitude.setText("");
					startLocation.setText(getString(R.string.startlocation));
				}
			}
		});
		addScenic = (Button)findViewById(R.id.add_scenic);
		addScenic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuffer sb = new StringBuffer(256);
				sb.append("type=Android&&name=");
				sb.append(et_name.getText().toString().trim());
				sb.append("&&latitude=");
				sb.append(latitude.getText().toString());
				sb.append("&&longitude=");
				sb.append(longitude.getText().toString());
				sb.append("&&belong=");
				sb.append(belong.getText().toString());
				new MyAsyncTask().execute(Config.ADD_POINT_URL,sb.toString());
			}
		});
	}
	class MyAsyncTask extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			activity = MyLocation2Activity.this;
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(isSucceed){
				et_name.setText("");
			}
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mLocationClient.stop();
		et_name.setText("");
		latitude.setText("");
		longitude.setText("");
		startLocation.setText(getString(R.string.startlocation));
		super.onStop();
	}

	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("gcj02");
		int span=1000;
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
}
