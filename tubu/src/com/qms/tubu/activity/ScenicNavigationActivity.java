package com.qms.tubu.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.qms.tubu.R;
import com.qms.tubu.application.MyApplication;
import com.qms.tubu.bean.ScenicBean;
import com.qms.tubu.fragment.scenic.CameraFragment;
import com.qms.tubu.fragment.scenic.CompassFragment;
import com.qms.tubu.fragment.scenic.HintFragment;
import com.qms.tubu.fragment.scenic.MapFragment;
import com.qms.tubu.fragment.scenic.ScenicListFragment;
import com.qms.tubu.util.CalculateDistance;
import com.qms.tubu.util.JWD;
import com.qms.tubu.util.ToastUtils;

@SuppressLint("InflateParams")
public class ScenicNavigationActivity extends FragmentActivity {
	private DisplayMetrics metrics;
	private MyApplication myApplication;
	private FragmentManager fmg;
	private FragmentTransaction transaction;

	private MapFragment mapFragment;//百度地图模块
	private ScenicListFragment scenicListFragment;//景点列表模块
	private HintFragment hintFragment;//提示模块
	private CameraFragment cameraFragment;//摄像头模块
	private CompassFragment compassFragment;//指南针模块

	private boolean isShowMap = false;//是否显示地图
	/**
	 * 导航
	 */
	private boolean mIsEngineInitSuccess = false;//是否初始化导航成功
	/**
	 * 定位
	 */
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;//高精度
	private String tempcoor="gcj02";//gcj02(国测局加密经纬度坐标)
	private int span = 1000;
	/**
	 * 方向
	 */
	private Sensor gyroSensor = null;
	private SensorManager sensorManager = null;
	private double lastSensor;//		    方位，返回水平时磁北极和Y轴的夹角，范围为0°至360°。 0°=北，90°=东，180°=南，270°=西。  
	private MySensorEventListener lsn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scenicnavigation);
		fmg = this.getSupportFragmentManager();
		transaction = fmg.beginTransaction();

		myApplication = (MyApplication) getApplication();
		mLocationClient = myApplication.mLocationClient;

		cameraFragment = new CameraFragment();
		compassFragment = new CompassFragment();
		mapFragment = new MapFragment();
		scenicListFragment = new ScenicListFragment();
		hintFragment = new HintFragment();;

		transaction.add(R.id.boot_main, cameraFragment, "camera");
		transaction.add(R.id.boot_sceniclist, scenicListFragment, "sceniclist");
		transaction.add(R.id.boot_compass, compassFragment, "compass");
		transaction.add(R.id.boot_hint, hintFragment,"hint");
		transaction.commit();

		metrics = getResources().getDisplayMetrics();
		//定位初始化
		initLocation();
		//导航初始化
		initRoutePlan();
		//方向初始化
		initSensor();
	}
	/**
	 * 导航初始化
	 */
	private void initRoutePlan(){
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
				mNaviEngineInitListener, new LBSAuthManagerListener() {
			@Override
			public void onAuthResult(int status, String msg) {
				String str = null;
				if (0 == status) {
					str = "key校验成功!";
				} else {
					str = "key校验失败, " + msg;
				}
				ToastUtils.showToast(ScenicNavigationActivity.this, str);
			}
		});
	}
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			mIsEngineInitSuccess = true;
		}

		public void engineInitStart() {
		}

		public void engineInitFail() {
			mIsEngineInitSuccess = false;
		}
	};
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		sensorManager.registerListener(lsn, gyroSensor,  
				SensorManager.SENSOR_DELAY_GAME);  //为<strong>传感器</strong>注册监听器  
		super.onStart();
		if(!mLocationClient.isStarted()){
			mLocationClient.start();
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		sensorManager.unregisterListener(lsn, gyroSensor);
		super.onStop();
		mLocationClient.stop();
	}
	/**
	 * 定位初始化
	 */
	private void initLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(span);//设置发起定位请求的间隔时间为1000ms
		option.setIsNeedAddress(false);//是否显示地址（若显示地址则不换地方不更新经纬度,不显示地址则随时更新经纬度）
		mLocationClient.setLocOption(option);
	}
	/**
	 * 方向传感器初始化
	 */
	@SuppressWarnings("deprecation")
	private void initSensor(){
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
		gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); 
		lsn = new MySensorEventListener();
	}
	/**
	 * 方向传感器监听
	 * @author dwj
	 * @time 2013-11-14 下午3:25:01
	 */
	class MySensorEventListener implements SensorEventListener{
		private float predegree = 0;
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			//		    方位，返回水平时磁北极和Y轴的夹角，范围为0°至360°。  
			//		    0°=北，90°=东，180°=南，270°=西。  
			CompassFragment compassFragment = (CompassFragment)fmg.findFragmentByTag("compass");
			ScenicListFragment scenicListFragment = (ScenicListFragment)fmg.findFragmentByTag("sceniclist");
			HintFragment hintFragment = (HintFragment)fmg.findFragmentByTag("hint");

			//如果未加载完数据 不进行下面操作
			if(!scenicListFragment.isData()){
				return;
			}
			/**
			 * 方向指针操作
			 */
			float degree = (event.values[0]+event.values[1]+90)%360;
			if(Math.abs(event.values[1]) > 90){
				degree = (degree + 180)%360;
			}
			compassFragment.changeNeedle(predegree, degree);
			predegree=-degree;

			//已经显示导航图时 不进行实景导航操作
			if(isShowMap){
				return;
			}
			List<ScenicBean> scenicBeans = scenicListFragment.getScenicBeans();
			if(hintFragment.getFl().getChildCount() < scenicBeans.size()){
				System.out.println("scenicBeans:"+scenicBeans.size());
				for(int i = 0;i <scenicBeans.size();i++){
					View view = getLayoutInflater().inflate(R.layout.fragment_hint_item, null);
					hintFragment.getFl().addView(view);
					FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams(); 
					params.width = (int)(150*metrics.density);
					params.height = (int)(150*metrics.density);
					view.setLayoutParams(params);
					view.setOnClickListener(new HintOnClickListener(scenicBeans.get(i)));
				}
			}
			if(Math.abs(degree - lastSensor) >1){
				lastSensor = degree;
				//				System.out.println("ChildCount:"+hintFragment.getFl().getChildCount());
				//				System.out.println("degree:"+degree);
				for(int i = 0;i <scenicBeans.size();i++){
					View view = hintFragment.getFl().getChildAt(i);
					ScenicBean bean = scenicBeans.get(i);
					FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams(); 

					//实时获取经纬度
					double latitude = myApplication.mLocation.getLatitude();
					double longitude =  myApplication.mLocation.getLongitude();
					//					System.out.println("latitude:"+latitude+"\n longitude:"+longitude);
					//距离
					double distance = CalculateDistance.getDistance(latitude,longitude,bean.getLatitude(),bean.getLongitude());
					//角度
					double sensor = JWD.GetSensor(latitude, longitude, bean.getLatitude(), bean.getLongitude());

					//角度差
					double variation = sensor -degree;
					//大于180° 则去反方向
					if(Math.abs(variation) > 180){
						variation = variation-360;
					}
					if(Math.abs(variation) > 45){
						view.setVisibility(View.INVISIBLE);
						continue;
					}
					view.setVisibility(View.VISIBLE);

					java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  
					//					distance = Double.parseDouble(df.format(distance/1000));//换成公里
					distance = Double.parseDouble(df.format(distance));//换成米 精确到小数点后两位

					TextView title = (TextView)view.findViewById(R.id.hint_name);
					StringBuilder sb = new StringBuilder();
					sb.append("距");
					sb.append(bean.getName());
					sb.append("还有");
					sb.append(distance);
					sb.append("米");
					title.setText(sb.toString());

					if(distance < 100){
						ToastUtils.showToast(ScenicNavigationActivity.this, "您已经到达"+bean.getName()+"区域");
					}
					//					System.out.println("bean.getSensor()------"+i+"------["+bean.getSensor()+"]");
					//					System.out.println("bean.getName()------"+i+"------["+bean.getName()+"]");
					//					System.out.println("variation:------"+i+"------["+variation+"]");
					//					System.out.println("variation---"+i+"---:"+variation);
					//					System.out.println("Math.tan(variation)---"+i+"---:"+Math.tan(variation*Math.PI/180));
					params.leftMargin =metrics.widthPixels/2 + (int)(metrics.widthPixels / 2 * Math.tan(variation*Math.PI/180)); 
					//					System.out.println("leftMargin---"+i+"---:"+params.leftMargin);
					params.topMargin = metrics.heightPixels/4; 
					view.setLayoutParams(params);
				}
				//				System.out.println("-------------------------------------------------");
			}

		}
	}
	//提示冒泡监听
	class HintOnClickListener implements OnClickListener{
		private ScenicBean endScenic;
		public HintOnClickListener(ScenicBean endScenic){
			this.endScenic = endScenic;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showMap(endScenic);
		}

	}
	/**
	 * 显示地图
	 */
	public void showMap(ScenicBean endScenic){
		mIsEngineInitSuccess = BaiduNaviManager.getInstance().checkEngineStatus(getApplicationContext());
		if(!mIsEngineInitSuccess){
			return ;
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("stScenic", new ScenicBean(myApplication.mLocation.getLatitude(), myApplication.mLocation.getLongitude()));
		bundle.putSerializable("endScenic",endScenic);
		mapFragment.setArguments(bundle);
		isShowMap = true;
		transaction = fmg.beginTransaction();
		transaction.remove(fmg.findFragmentByTag("hint"));
		transaction.remove(fmg.findFragmentByTag("sceniclist"));
		transaction.replace(R.id.boot_main, mapFragment,"map");
		transaction.addToBackStack("");
		transaction.commit();
	}
}
