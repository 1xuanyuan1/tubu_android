package com.qms.tubu.application;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.qms.tubu.bean.AccountBean;


public class MyApplication extends Application {
    public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public BDLocation mLocation;
	
	public TextView latitude,longitude;
	public MyLocationListener mMyLocationListener;
	public Vibrator mVibrator;
	
	public AccountBean accountBean;
	
	public TextView address;
	@Override
    public void onCreate() {
	    super.onCreate();
	    mLocation = new BDLocation();
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
	}
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mLocation = location;
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			sb.append("\naltitude : ");
			sb.append(location.getAltitude());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append(location.getDirection());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
//			logMsg(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
			showAddress(location.getDistrict());//获取区县信息
			mLocationClient.stop();
		}


	}
	//显示地址
	public void showAddress(String myAddress){
		try {
			if (address != null){
				address.setText(myAddress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void logMsg(String myLatitude,String myLongitude) {
		try {
			if (latitude != null)
				latitude.setText(myLatitude);
			if (longitude != null)
				longitude.setText(myLongitude);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}