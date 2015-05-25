package com.qms.tubu.activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.GPSConfig;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.interfaces.OnLocationChangedListener;
import com.qms.tubu.R;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.config.Config;
import com.qms.tubu.util.ToastUtils;

public class MyLocationActivity extends Activity{
	private LocationClient mLocationClient;
	
	public static final String TAG = "MyLocationActivity";
	private EditText et_name;
	private TextView latitude,longitude,belong;
	private Button startLocation;
	private Button addScenic;
	
	private MapWidget map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylocation);
		et_name = (EditText)findViewById(R.id.myLocation_name);
		latitude = (TextView)findViewById(R.id.myLocation_latitude);
		longitude = (TextView)findViewById(R.id.myLocation_longitude);
		belong = (TextView)findViewById(R.id.myLocation_belong);
		
		 
		startLocation = (Button)findViewById(R.id.addfence);
		startLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(startLocation.getText().equals(getString(R.string.startlocation))){
					//是否在地图上显示我的位置
					initMap();
					map.setShowMyPosition(true);
					startLocation.setText(getString(R.string.stoplocation));
				}else{
					map.setShowMyPosition(false);
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
	public void initMap(){
		map = new MapWidget(this,"map"); 
		OfflineMapConfig config = map.getConfig();
		GPSConfig gpsConfig = config.getGpsConfig();
		gpsConfig.setPassiveMode(false);
		gpsConfig.setGPSUpdateInterval(500, 1);
		
		map.setOnLocationChangedListener(new OnLocationChangedListener() {
			@Override
			public void onLocationChanged(MapWidget v, Location location) {
				// You can handle location change here.
				// For example you can scroll to new location by using v.scrollMapTo(location)
				Log.i(TAG, "LocationChanged");
				String msg = "Latitude:"+location.getLatitude()+"\nLongitude:"+location.getLongitude();
				latitude.setText(String.valueOf(location.getLatitude()));
				longitude.setText(String.valueOf(location.getLongitude()));
				Log.i(TAG, msg);
				ToastUtils.showToast(MyLocationActivity.this, msg);
			}
		});
	}
	class MyAsyncTask extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			activity = MyLocationActivity.this;
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
		map.setShowMyPosition(false);
		latitude.setText("");
		longitude.setText("");
		startLocation.setText(getString(R.string.startlocation));
		super.onStop();
	}
}
