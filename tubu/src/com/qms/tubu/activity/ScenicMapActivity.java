package com.qms.tubu.activity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.GPSConfig;
import com.ls.widgets.map.config.MapGraphicsConfig;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapScrolledEvent;
import com.ls.widgets.map.events.MapTouchedEvent;
import com.ls.widgets.map.events.ObjectTouchEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.MapEventsListener;
import com.ls.widgets.map.interfaces.OnLocationChangedListener;
import com.ls.widgets.map.interfaces.OnMapScrollListener;
import com.ls.widgets.map.interfaces.OnMapTouchListener;
import com.ls.widgets.map.model.MapObject;
import com.ls.widgets.map.utils.PivotFactory;
import com.ls.widgets.map.utils.PivotFactory.PivotPosition;
import com.qms.tubu.R;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.bean.PointBean;
import com.qms.tubu.config.Config;
import com.qms.tubu.model.MapObjectContainer;
import com.qms.tubu.model.MapObjectModel;
import com.qms.tubu.popup.Popup;
import com.qms.tubu.util.CalculateDistance;
import com.qms.tubu.util.JWD;
import com.qms.tubu.util.ToastUtils;

@SuppressLint("InflateParams")
public class ScenicMapActivity extends Activity implements OnMapTouchListener,MapEventsListener,OnClickListener{
	private static final String TAG = "ScenicMapActivity";

	private List<PointBean> points;
	//地图上的点
	private MapObjectContainer model = new MapObjectContainer();
	//图层的ID
	private static final Integer LAYER1_ID = 0;
	private static final Integer LAYER2_ID = 1;
	//地图的ID
	private static final int MAP_ID = 23;

	private int pinHeight;
	private int nextObjectId;

	private int maxLevel=12;
	private int minLevel=11;

	private MapWidget map;

	//冒泡弹窗信息
	private Popup mapObjectInfoPopup;
	//是否在地图上显示我的位置
	private boolean isShowMyPosition = false;

	private Bundle savedInstanceState;

	private PointBean nowPoint;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.savedInstanceState = savedInstanceState;
		//		//初始化地图
		//		initMap(savedInstanceState);
		//		//初始化模型定点
		//		initModel();
		//		//初始化冒泡层
		//		initMapObjects();
		//		//初始化地图监听
		//		initMapListeners();
		//		//是否在地图上显示我的位置
		//		map.setShowMyPosition(isShowMyPosition);
		//		//将地图放置在屏幕中间
		//		map.centerMap();

		findViewById(R.id.map_filter_all).setOnClickListener(this);
		findViewById(R.id.map_filter_scenic).setOnClickListener(this);
		findViewById(R.id.map_filter_room).setOnClickListener(this);
		findViewById(R.id.map_filter_food).setOnClickListener(this);
		findViewById(R.id.map_self_location).setOnClickListener(this);
		new MyAsyncTask().execute(Config.QUERY_POINT_URL,"type=Android");

	}
	class MyAsyncTask2 extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			activity = ScenicMapActivity.this;
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return super.doInBackground(params);
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//初始化模型定点
			initModel();
			//初始化冒泡层
			initMapObjects();
			//初始化地图监听
			initMapListeners();
			//是否在地图上显示我的位置
			map.setShowMyPosition(isShowMyPosition);
			//将地图放置在屏幕中间
			map.centerMap();
		}
	}
	class MyAsyncTask extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			activity = ScenicMapActivity.this;
			points = new ArrayList<PointBean>();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(isSucceed){
				try {
					JSONArray json_points = jb.getJSONArray("points");
					int length = json_points.length();
					int position = 0;
					for(int i = 0;i < length;i++){
						JSONObject json_point = json_points.getJSONObject(i);
						if(json_point.getString("name").equals("水上舞台")){
							nowPoint = new PointBean();
							nowPoint.setId(json_point.getString("id"));
							nowPoint.setName(json_point.getString("name"));
							nowPoint.setLatitude(Double.parseDouble(json_point.getString("latitude")));
							nowPoint.setLongitude(Double.parseDouble(json_point.getString("longitude")));
							nowPoint.setPic_x(Integer.parseInt(json_point.getString("pic_x")));
							nowPoint.setPic_y(Integer.parseInt(json_point.getString("pic_y")));

						}else if(!json_point.getString("pic_x").equals("0")){
							PointBean bean = new PointBean();
							bean.setId(json_point.getString("id"));
							bean.setName(json_point.getString("name"));
							bean.setLatitude(Double.parseDouble(json_point.getString("latitude")));
							bean.setLongitude(Double.parseDouble(json_point.getString("longitude")));
							bean.setPic_x(Integer.parseInt(json_point.getString("pic_x")));
							bean.setPic_y(Integer.parseInt(json_point.getString("pic_y")));
							points.add(bean);
							MapObjectModel objectModel = new MapObjectModel(position, Integer.parseInt(json_point.getString("pic_x"))*3/10,Integer.parseInt(json_point.getString("pic_y"))*3/10,json_point.getString("name"),R.raw.jxl);
							model.addObject(objectModel);
							position++;
						}
					}
					File file = new File((Environment.getExternalStorageDirectory().toString()+"/college"));
					if(!file.exists()){
						ToastUtils.showToast(activity, "文件不存在！");
						return;
					}
					//初始化地图
					initMap(savedInstanceState,file);
					//初始化冒泡层
					initMapObjects();
					//初始化地图监听
					initMapListeners();
					//是否在地图上显示我的位置
					//					map.setShowMyPosition(isShowMyPosition);
					//将地图放置在屏幕中间
					map.centerMap();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	//保存地图信息
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		map.saveState(outState);
	}
	//初始化地图
	private void initMap(Bundle savedInstanceState,File file) 
	{
		//去掉水印
		Class<?> c = null;
		try {
			//反射找到Resources类
			c = Class.forName("com.ls.widgets.map.utils.Resources");
			Object obj = c.newInstance();
			//找到Logo属性，是一个数组
			Field field = c.getDeclaredField("LOGO");
			field.setAccessible(true);
			//将LOGO字段设置为null
			field.set(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		map = new MapWidget(savedInstanceState, this, file, maxLevel); 
		map.setId(MAP_ID);

		OfflineMapConfig config = map.getConfig();
		config.setZoomBtnsVisible(false); 
		config.setPinchZoomEnabled(true); 
		config.setFlingEnabled(true);    
		config.setMapCenteringEnabled(false);
		config.setMaxZoomLevelLimit(maxLevel);
		config.setMinZoomLevelLimit(minLevel);


		GPSConfig gpsConfig = config.getGpsConfig();
		gpsConfig.setPassiveMode(false);
		gpsConfig.setGPSUpdateInterval(500, 5);


		configureLocationPointer();


		FrameLayout layout = (FrameLayout) findViewById(R.id.rootLayout);
		layout.addView(map, 0);
		layout.setBackgroundColor(Color.parseColor("#4CB263"));
		map.createLayer(LAYER1_ID); 
		map.createLayer(LAYER2_ID); 
	}
	/**
	 * 初始化模型定点
	 */
	private void initModel()
	{

		//		MapObjectModel objectModel = new MapObjectModel(0, 701, 1529, "水心塔",R.raw.sxt);
		//		model.addObject(objectModel);
		//		objectModel = new MapObjectModel(1, 1150, 1630, "办公楼",R.raw.bgl);
		//		model.addObject(objectModel);
		//		objectModel = new MapObjectModel(2, 903, 1212, "聚贤楼",R.raw.jxl);
		//		model.addObject(objectModel);
	}
	/**
	 * 初始化地图弹窗内容
	 */
	private void initMapObjects() 
	{	
		View view = getLayoutInflater().inflate(R.layout.map_point_detail, null);

		mapObjectInfoPopup = new Popup(this,view, (FrameLayout)findViewById(R.id.rootLayout));


		Layer layer1 = map.getLayerById(LAYER1_ID);


		for (int i=0; i<model.size(); ++i) {
			addScalableMapObject(model.getObject(i), layer1);
			//			addNotScalableMapObject(model.getObject(i), layer1);
		}


	}
	/**
	 * 添加不随地图变化而变化的图标
	 */
	//图标不可变化
	private void addNotScalableMapObject(int x, int y,  Layer layer) 
	{
		// 得到比较的图片
		Drawable drawable = getResources().getDrawable(R.drawable.map_object);
		pinHeight = drawable.getIntrinsicHeight();
		// 创建图标
		MapObject object1 = new MapObject(Integer.valueOf(nextObjectId),drawable,new Point(x, y), PivotFactory.createPivotPoint(drawable, PivotPosition.PIVOT_CENTER),true, false); 
		layer.addMapObject(object1);
		nextObjectId += 1;
	}
	private void addNotScalableMapObject(MapObjectModel objectModel,  Layer layer) 
	{
		if (objectModel.getLocation() != null) {
			addNotScalableMapObject(objectModel.getLocation(), layer);
		} else {
			addNotScalableMapObject(objectModel.getX(), objectModel.getY(),  layer);
		}
	}
	private void addNotScalableMapObject(Location location, Layer layer) {
		if (location == null)
			return;
		Drawable drawable = getResources().getDrawable(R.drawable.map_object);
		MapObject object1 = new MapObject(Integer.valueOf(nextObjectId), 
				drawable,  
				new Point(0, 0), 
				PivotFactory.createPivotPoint(drawable, PivotPosition.PIVOT_CENTER),
				true,
				true);
		layer.addMapObject(object1);

		object1.moveTo(location);
		nextObjectId += 1;
	}
	/**
	 * 添加随地图变化而变化的图标
	 */
	private void addScalableMapObject(int x, int y, Layer layer) 
	{
		// 得到比较的图片
		Drawable drawable = getResources().getDrawable(R.drawable.map_object);
		pinHeight = drawable.getIntrinsicHeight();
		// 创建图标
		MapObject object1 = new MapObject(Integer.valueOf(nextObjectId),drawable,new Point(x, y), PivotFactory.createPivotPoint(drawable, PivotPosition.PIVOT_CENTER),true, true); 
		layer.addMapObject(object1);

		nextObjectId += 1;
	}
	private void addScalableMapObject(MapObjectModel objectModel,  Layer layer) 
	{
		if (objectModel.getLocation() != null) {
			addScalableMapObject(objectModel.getLocation(), layer);
		} else {
			addScalableMapObject(objectModel.getX(), objectModel.getY(),  layer);
		}
	}
	private void addScalableMapObject(Location location, Layer layer) {
		if (location == null)
			return;
		Drawable drawable = getResources().getDrawable(R.drawable.map_object);
		MapObject object1 = new MapObject(Integer.valueOf(nextObjectId), 
				drawable,  
				new Point(0, 0), 
				PivotFactory.createPivotPoint(drawable, PivotPosition.PIVOT_CENTER),
				true,
				true);
		layer.addMapObject(object1);

		object1.moveTo(location);
		nextObjectId += 1;
	}

	//地图点
	private void configureLocationPointer() 

	{ 
		MapGraphicsConfig graphicsConfig = map.getMapGraphicsConfig(); 

		//      graphicsConfig.setAccuracyAreaColor(0x55FF0000); 
		//      graphicsConfig.setAccuracyAreaBorderColor(Color.RED); 
		graphicsConfig.setDotPointerDrawableId(R.drawable.round_pointer); 
		graphicsConfig.setArrowPointerDrawableId(R.drawable.arrow_pointer); 

	}

	private void initMapListeners() 
	{
		// 触摸地图时间
		map.setOnMapTouchListener(this);

		//为了在放大前后执行一些操作
		map.addMapEventsListener(this); 


		//滚动地图事件
		map.setOnMapScrolledListener(new OnMapScrollListener()
		{
			public void onScrolledEvent(MapWidget v, MapScrolledEvent event)
			{
				handleOnMapScroll(v, event);
			}
		});


		map.setOnLocationChangedListener(new OnLocationChangedListener() {
			@Override
			public void onLocationChanged(MapWidget v, Location location) {
				// You can handle location change here.
				// For example you can scroll to new location by using v.scrollMapTo(location)
				Log.i(TAG, "LocationChanged");
				String msg = "Latitude:"+location.getLatitude()+"\nLongitude:"+location.getLongitude();
				Log.i(TAG, msg);
				Toast.makeText(ScenicMapActivity.this, msg, Toast.LENGTH_SHORT).show();

				//实时获取经纬度
				//				double latitude = location.getLatitude();
				//				double longitude =  location.getLongitude();
				double latitude = nowPoint.getLatitude();
				double longitude =nowPoint.getLongitude();
				//					System.out.println("latitude:"+latitude+"\n longitude:"+longitude);
				double minDistance = -1;
				int minPosition = -1;
				double minSensor = -1;
				String name = "";
				for(int i = 0;i < points.size();i++){
					PointBean bean = points.get(i);
					//距离
					double distance = CalculateDistance.getDistance(latitude,longitude,bean.getLatitude(),bean.getLongitude());
					if(distance < minDistance || minDistance == -1){
						minDistance = distance;
						//角度
						minSensor = JWD.GetSensor(latitude, longitude, bean.getLatitude(), bean.getLongitude());
						minPosition = i;
						name = bean.getName();
					}

				}

				Log.i(TAG, "my:  "+nowPoint.toString());
				Log.i(TAG, "min:  "+points.get(minPosition).toString());
				Log.i(TAG, "minSensor:"+minSensor);
				Log.i(TAG, "minDistance:"+minDistance);
				double y = Math.sin(minSensor)*minDistance;
				double x = Math.cos(minSensor)*minDistance;
				Log.i(TAG, "x:"+x+"     y:"+y);


			}
		});
	}

	private void handleOnMapScroll(MapWidget v, MapScrolledEvent event) 
	{	

		//沿x轴滚动的距离
		int dx = event.getDX(); 
		//沿y轴滚动的距离
		int dy = event.getDY(); 
		//气泡跟着移动
		if (mapObjectInfoPopup.isVisible()) {
			mapObjectInfoPopup.moveBy(dx, dy);
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onTouch(MapWidget v, MapTouchedEvent event) {
		// TODO Auto-generated method stub
		// 得到触摸的对象
		ArrayList<ObjectTouchEvent> touchedObjs = event.getTouchedObjectIds();
		//判断是否同时点击多个地图对象
		if (touchedObjs.size() > 0) {

			int xInMapCoords = event.getMapX();
			int yInMapCoords = event.getMapY();
			int xInScreenCoords = event.getScreenX();
			int yInScreenCoords = event.getScreenY();

			ObjectTouchEvent objectTouchEvent = event.getTouchedObjectIds().get(0);
			//的到图层对象
			long layerId = objectTouchEvent.getLayerId();
			//得到地图对象id
			Integer objectId = (Integer)objectTouchEvent.getObjectId();

			String message = "You touched the object with id: " + objectId + " on layer: " + layerId + 
					" mapX: " + xInMapCoords + " mapY: " + yInMapCoords + " screenX: " + xInScreenCoords + " screenY: " + 
					yInScreenCoords;

			Log.d(TAG, message);
			//根据id找到地图对象
			MapObjectModel objectModel = model.getObjectById(objectId.intValue());

			if (objectModel != null) {
				//的到屏幕密度
				float density = getResources().getDisplayMetrics().density;
				int imgHeight = (int) (pinHeight / density / 2);

				int x = xToScreenCoords(objectModel.getX());
				int y = yToScreenCoords(objectModel.getY()) - imgHeight;

				//显示气泡
				showLocationsPopup(objectModel,x, y, objectModel.getCaption());
			} else {
				// 如果同时触摸的是多个 判断用户点的是哪个
				showLocationsPopup(objectModel,xInScreenCoords, yInScreenCoords, "Shows where user touched");
			}

		} else {
			if (mapObjectInfoPopup != null) {
				mapObjectInfoPopup.hide();
			}
		}
	}
	//显示冒泡层
	private void showLocationsPopup(MapObjectModel objectModel,int x, int y, String text)
	{
		FrameLayout mapLayout = (FrameLayout) findViewById(R.id.rootLayout);

		if (mapObjectInfoPopup != null)
		{
			mapObjectInfoPopup.hide();
		}
		mapObjectInfoPopup.setInfo(objectModel,text);
		((Popup) mapObjectInfoPopup).show(mapLayout, x, y);
	}
	//地图放大后的事件
	@Override
	public void onPostZoomIn() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPostZoomIn()");
	}
	//地图缩小后的事件
	@Override
	public void onPostZoomOut() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPostZoomIn()");
	}
	//地图在放大前调用的事件
	@Override
	public void onPreZoomIn() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPreZoomIn()");
		if (mapObjectInfoPopup != null) {
			mapObjectInfoPopup.hide();
		}
		if (map.getZoomLevel()+1==maxLevel) {
			Toast.makeText(this, "已放大到最大级别", 3000).show();
		}
	}
	//地图在放大前调用的事件
	@Override
	public void onPreZoomOut() {
		// TODO Auto-generated method stub
		if (mapObjectInfoPopup != null) {
			mapObjectInfoPopup.hide();
		}
		Log.i(TAG, "onPreZoomIn()");
	}

	/***
	 * Transforms coordinate in map coordinate system to screen coordinate system
	 * @param mapCoord - X in map coordinate in pixels. 
	 * @return X coordinate in screen coordinates. You can use this value to display any object on the screen.
	 */
	private int xToScreenCoords(int mapCoord)
	{
		return (int)(mapCoord *  map.getScale() - map.getScrollX());
	}

	private int yToScreenCoords(int mapCoord)
	{
		return (int)(mapCoord *  map.getScale() - map.getScrollY());
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.map_self_location:
			Log.i(TAG, "isShowMyPosition:"+isShowMyPosition);
			isShowMyPosition = !isShowMyPosition;
			map.setShowMyPosition(isShowMyPosition);
			break;

		default:
			break;
		}
	}
}
