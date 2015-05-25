package com.qms.tubu.fragment.scenic;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.CommonParams.Const.ModelName;
import com.baidu.navisdk.CommonParams.NL_Net_Mode;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.mapcontrol.MapParams.Const.LayerMode;
import com.baidu.navisdk.comapi.routeguide.RouteGuideParams.RGLocationMode;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.routeplan.IRouteResultObserver;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.baidu.navisdk.comapi.setting.SettingParams;
import com.baidu.navisdk.model.NaviDataEngine;
import com.baidu.navisdk.model.RoutePlanModel;
import com.baidu.navisdk.model.datastruct.RoutePlanNode;
import com.baidu.navisdk.ui.routeguide.BNavConfig;
import com.baidu.navisdk.ui.routeguide.BNavigator;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.navisdk.util.common.PreferenceHelper;
import com.baidu.navisdk.util.common.ScreenUtil;
import com.baidu.nplatform.comapi.map.MapGLSurfaceView;
import com.qms.tubu.R;
import com.qms.tubu.activity.BNavigatorActivity;
import com.qms.tubu.bean.ScenicBean;
/**
 * 百度地图模块
 * @author dwj
 * @time 2013-11-22 上午10:05:16
 */
@SuppressLint("InflateParams")
public class MapFragment extends Fragment implements OnClickListener{
	private RoutePlanModel mRoutePlanModel = null;
	private MapGLSurfaceView mMapView = null;
	private View view;
	private ScenicBean stScenic;
	private ScenicBean endScenic;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_map, null);
		Bundle bundle = getArguments();
		stScenic =(ScenicBean)bundle.getSerializable("stScenic");
		endScenic =(ScenicBean)bundle.getSerializable("endScenic");

		view.findViewById(R.id.map_bt_simulate).setOnClickListener(this);
		view.findViewById(R.id.map_bt_real).setOnClickListener(this);
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
		BNRoutePlaner.getInstance().setRouteResultObserver(null);
		((ViewGroup) (view.findViewById(R.id.mapview_layout))).removeAllViews();
		BNMapController.getInstance().onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		initMapView();
		((ViewGroup) (view.findViewById(R.id.mapview_layout))).addView(mMapView);
		BNMapController.getInstance().onResume();
		//开始路线规划
		startCalcRoute(NL_Net_Mode.NL_Net_Mode_OnLine);
	}

	private void initMapView() {
		if (Build.VERSION.SDK_INT < 14) {
			BaiduNaviManager.getInstance().destroyNMapView();
		}

		mMapView = BaiduNaviManager.getInstance().createNMapView(getActivity());
		BNMapController.getInstance().setLevel(14);
		BNMapController.getInstance().setLayerMode(
				LayerMode.MAP_LAYER_MODE_BROWSE_MAP);
		updateCompassPosition();

		BNMapController.getInstance().locateWithAnimation(
				(int) (stScenic.getLongitude() * 1e5), (int) (stScenic.getLatitude() * 1e5));
	}

	/**
	 * 更新指南针位置
	 */
	private void updateCompassPosition(){
		int screenW = getActivity().getResources().getDisplayMetrics().widthPixels;
		BNMapController.getInstance().resetCompassPosition(
				screenW - ScreenUtil.dip2px(getActivity(), 30),
				ScreenUtil.dip2px(getActivity(), 126), -1);
	}

	private void startCalcRoute(int netmode) {

		int sX = (int)(stScenic.getLatitude()*1e5);
		int sY = (int)(stScenic.getLongitude()*1e5);
		int eX = (int)(endScenic.getLatitude()*1e5);
		int eY = (int)(endScenic.getLongitude()*1e5);

		//起点
		RoutePlanNode startNode = new RoutePlanNode(sX, sY,
				RoutePlanNode.FROM_MAP_POINT, "当前位置", "当前位置");
		//终点
		RoutePlanNode endNode = new RoutePlanNode(eX, eY,
				RoutePlanNode.FROM_MAP_POINT, stScenic.getName(),stScenic.getName());
		//将起终点添加到nodeList
		ArrayList<RoutePlanNode> nodeList = new ArrayList<RoutePlanNode>(2);
		nodeList.add(startNode);
		nodeList.add(endNode);
		BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(getActivity(), null));
		//设置算路方式
		BNRoutePlaner.getInstance().setCalcMode(NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME);
		// 设置算路结果回调
		BNRoutePlaner.getInstance().setRouteResultObserver(mRouteResultObserver);
		// 设置起终点并算路
		boolean ret = BNRoutePlaner.getInstance().setPointsToCalcRoute(
				nodeList,NL_Net_Mode.NL_Net_Mode_OnLine);
		if(!ret){
			Toast.makeText(getActivity(), "规划失败", Toast.LENGTH_SHORT).show();
		}
	}

	private void startNavi(boolean isReal) {
		if (mRoutePlanModel == null) {
			Toast.makeText(getActivity(), "请先算路！", Toast.LENGTH_LONG).show();
			return;
		}
		// 获取路线规划结果起点
		RoutePlanNode startNode = mRoutePlanModel.getStartNode();
		// 获取路线规划结果终点
		RoutePlanNode endNode = mRoutePlanModel.getEndNode();
		if (null == startNode || null == endNode) {
			return;
		}
		// 获取路线规划算路模式
		int calcMode = BNRoutePlaner.getInstance().getCalcMode();
		Bundle bundle = new Bundle();
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_VIEW_MODE,
				BNavigator.CONFIG_VIEW_MODE_INFLATE_MAP);
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_CALCROUTE_DONE,
				BNavigator.CONFIG_CLACROUTE_DONE);
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_START_X,
				startNode.getLongitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_START_Y,
				startNode.getLatitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_END_X, endNode.getLongitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_END_Y, endNode.getLatitudeE6());
		bundle.putString(BNavConfig.KEY_ROUTEGUIDE_START_NAME,
				mRoutePlanModel.getStartName(getActivity(), false));
		bundle.putString(BNavConfig.KEY_ROUTEGUIDE_END_NAME,
				mRoutePlanModel.getEndName(getActivity(), false));
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_CALCROUTE_MODE, calcMode);
		if (!isReal) {
			// 模拟导航
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_LOCATE_MODE,
					RGLocationMode.NE_Locate_Mode_RouteDemoGPS);
		} else {
			// GPS 导航
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_LOCATE_MODE,
					RGLocationMode.NE_Locate_Mode_GPS);
		}

		bundle.putSerializable("endScenic", endScenic);//终点信息
		
		Intent intent = new Intent(getActivity(), BNavigatorActivity.class);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);
		getActivity().finish();
	}

	private IRouteResultObserver mRouteResultObserver = new IRouteResultObserver() {

		@Override
		public void onRoutePlanYawingSuccess() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRoutePlanYawingFail() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRoutePlanSuccess() {
			// TODO Auto-generated method stub
			BNMapController.getInstance().setLayerMode(
					LayerMode.MAP_LAYER_MODE_ROUTE_DETAIL);
			mRoutePlanModel = (RoutePlanModel) NaviDataEngine.getInstance()
					.getModel(ModelName.ROUTE_PLAN);
		}

		@Override
		public void onRoutePlanFail() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onRoutePlanCanceled() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onRoutePlanStart() {
			// TODO Auto-generated method stub

		}

	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.map_bt_simulate:
			startNavi(false);
			break;
		case R.id.map_bt_real:
			PreferenceHelper.getInstance(getActivity().getApplicationContext())
			.putBoolean(SettingParams.Key.SP_TRACK_LOCATE_GUIDE,
					false);
			startNavi(true);
			break;
		default:
			break;
		}
	}
}
