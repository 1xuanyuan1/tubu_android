package com.qms.tubu.fragment.scenic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.application.MyApplication;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.bean.ScenicBean;
import com.qms.tubu.config.Config;
/**
 * 景点列表模块
 * @author dwj
 * @time 2013-11-22 上午10:05:43
 */
@SuppressLint("InflateParams")
public class ScenicListFragment extends Fragment{
	//	private ScenicActivity scenicActivity;
	private MyApplication myApplication;
	/**
	 * 测试数据
	 */
	private List<ScenicBean> scenicBeans = new ArrayList<ScenicBean>();
	private String[] names = {"永嘉书院","江心屿","温州国际会展中心","香城寺","西郊公园","西湖"};
	private double[] lats = {28.27356,28.034432,28.022411,27.923368,27.979911,30.251772};
	private double[] lngs = {120.707296,120.651242,120.724642,120.698295,120.580015,120.139058};
	//	private int[] images = {R.drawable.scenic1,R.drawable.scenic2,R.drawable.scenic3,R.drawable.scenic4,R.drawable.scenic5};
	private String[] intros = 
		{"江心屿:\n　　江心屿，位于温州市区北面瓯江中游，东西长，南北狭，列中国四大名胜孤屿之首。该屿风景秀丽，历史古迹，人文景观丰富，是瓯江上的一颗璀璨明珠，历来被称为“瓯江蓬莱”。东西双塔凌空，映衬江心寺，别具匠心。",
			"温州国际会议展览中心:\n　　温州国际会议展览中心是温州十大标志性建筑之一。地处环境幽雅、风光旖旎的瓯江之滨，水运交通发达；以其宏伟的气势、完美的设计和一流的设施，不但在中国国内享有盛名，在国际上也有很高的声誉，成为温州的标志之一！",
			"香城寺:\n　　香城寺是梅岭八大名刹之一，建立最早，寺庙宏伟，古迹遍布寺内外。 它由庐山大林寺高僧昙显法师始建于东晋隆安（396-401）年间。传说建大殿时焚香祷于山，山忽生香木无数，以木屑燃之，香闻数里，故名香城寺。",
			"西郊公园:\n　　西郊公园亦称儿童公园，位于城区西关，是出土国宝级文物西夏木缘塔的地方。逢年过节，园内举办各种题材的大型灯展及群众性联谊活动，增添了公园的热闹气氛。",
		"西湖:\n　　西湖被孤山、白堤、苏堤、杨公堤分隔，按面积大小分别为外西湖、西里湖、北里湖、小南湖及岳湖等五片水面，苏堤、白堤越过湖面，小瀛洲、湖心亭、阮公墩三个小岛鼎立于外西湖湖心，夕照山的雷峰塔与宝石山的保俶塔隔湖相映，由此形成了“一山、二塔、三岛、三堤、五湖”的基本格局。"};

	/**
	 * 测试数据2
	 */
	private String[] name2s = {"江心屿","东塔","西塔","江心屿景区管理处","温州市江心屿东园游乐园","温州江心屿旅游区","江心屿(南门)","江心寺","花柳古亭","温州江心西园","江心海景酒店","江心汽车影院","英国驻温州领事馆旧址","海景KTV","江心西园水上世界"};
	private double[] lat2s = {28.034432,28.033276,28.032511,28.033053,28.034878,28.032048,28.028461,28.033013,28.033372,28.032670,28.032351,28.031052,28.032479,28.032543,28.032327};
	private double[] lng2s = {120.651242,120.655185,120.651916,120.649796,120.650523,120.640372,120.654098,120.649858,120.650101,120.640462,120.635099,120.633599,120.654386,120.639339,120.643301};
	
	/**
	 * 测试数据3
	 */
	private String[] name3s = {"望海楼景区","洞头风景名胜区","仙叠岩景区","半屏山风景区","南炮台风景区","中普陀寺","海霞军事主题公园","马岙潭","竹屿","海滨浴场"};
	private double[] lat3s = {27.842985,27.832173,27.82429,27.808411,27.821854,27.831957,27.865167,27.862444,27.826654,27.824601};
	private double[] lng3s = {121.136059,121.148295,121.171743,121.155526,121.182496,121.156051,121.191148,121.169171,121.227413,121.178852};

	/**
	 * 景点列表
	 */
	private ListView listView;
	private Button showlist;
	private boolean isShowList = false;//是否显示景点列表
	private boolean isFindScenic = false;//是否在找景点
	private boolean isData = false;//数据加载是否完成
	private ScenicBean endScenic= new ScenicBean();//目的景点
	private MyAdapter myAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_sceniclist, null);
		myApplication = (MyApplication) getActivity().getApplication();
		//		scenicActivity = (ScenicActivity) getActivity();
		showlist = (Button)view.findViewById(R.id.showlist);
		listView =(ListView)view.findViewById(R.id.scenic_list); 
		//初始化景点数据
		initScenic();
		//初始化景点列表
		initList();
		return view;
	}
	/**
	 * 初始化景点数据
	 */

	private void initScenic(){
		new MyAsyncTask().execute(Config.GETLOCATION_URL);
		scenicBeans = new ArrayList<ScenicBean>();
//		ScenicBean bean = new ScenicBean();
//		bean.setName("永嘉书院");
//		bean.setLatitude(28.27356);
//		bean.setLongitude(120.707296);
//		scenicBeans.add(bean);
//		for(int i = 0;i < name3s.length ;i++){
//			ScenicBean bean = new ScenicBean();
//			bean.setName(name3s[i]);
//			bean.setLatitude(lat3s[i]);
//			bean.setLongitude(lng3s[i]);
////			bean.setImage(images[i]);
////			bean.setIntro(intros[i]);
//			scenicBeans.add(bean);
//		}
	}
	/**
	 * 初始化景点列表
	 */
	private void initList(){
		myAdapter = new MyAdapter();
//		new MyAsyncTask().execute(Config.QUERY_SCENIC_URL);
		listView.setAdapter(myAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				endScenic = scenicBeans.get(arg2);
				//				String lastScenicName = scenicActivity.getLastScenicName();
				//				listView.setVisibility(View.GONE);
				//				if(!lastScenicName.equals(endScenic.getName())){
				//					isFindScenic = true;
				//					double sensor = JWD.GetSensor(myApplication.mLocation.getLatitude(), myApplication.mLocation.getLongitude(), endScenic.getLatitude(), endScenic.getLongitude());
				//					scenicActivity.showLeftOrRight(liftOrRight(sensor-scenicActivity.getSensor()));
				//				}else{
				//					ToastUtils.showToast(scenicActivity, "您已经在"+lastScenicName+"所在方向了！");
				//				}
			}
		});
		showlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isShowList = !isShowList;
				if(isShowList){
					listView.setVisibility(View.VISIBLE);
				}else{
					listView.setVisibility(View.GONE);
				}
			}
		});
	}
	/**
	 * 景点列表适配器
	 * @author dwj
	 * @time 2013-11-20 下午2:30:22
	 */
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return scenicBeans.size();
		}

		@Override
		public ScenicBean getItem(int arg0) {
			// TODO Auto-generated method stub
			return scenicBeans.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(null == view){
				view = getActivity().getLayoutInflater().inflate(R.layout.scenic_list_item, null);
				TextView tv = (TextView)view.findViewById(R.id.scenic_list_item_tv);
				tv.setText(getItem(arg0).getName());
			}
			return view;
		}

	}
	public boolean isData() {
		return isData;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isData = false;
		super.onDestroy();
	}
	/**
	 * 隐藏列表
	 */
	public void hiddenList(){
		isShowList = false;
		listView.setVisibility(View.GONE);
	}
	public List<ScenicBean> getScenicBeans() {
		return scenicBeans;
	}
	public void setScenicBeans(List<ScenicBean> scenicBeans) {
		this.scenicBeans = scenicBeans;
	}
	public boolean isFindScenic() {
		return isFindScenic;
	}
	public void setFindScenic(boolean isFindScenic) {
		this.isFindScenic = isFindScenic;
	}
	public ScenicBean getEndScenic() {
		return endScenic;
	}
	public void setEndScenic(ScenicBean endScenic) {
		this.endScenic = endScenic;
	}
	public boolean isShowList() {
		return isShowList;
	}
	class MyAsyncTask extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			activity = getActivity();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if(isSucceed){
					isData = true;
					scenicBeans = new ArrayList<ScenicBean>();
					JSONArray scenices =  jb.getJSONArray("scenic");
					int size = scenices.length();
					for(int i = 0;i < size;i++){
						JSONObject scenice = scenices.getJSONObject(i);
						ScenicBean bean = new ScenicBean();
						bean.setId(scenice.getString("id"));
						bean.setName(scenice.getString("name"));
						bean.setLatitude(scenice.getDouble("latitude"));
						bean.setLongitude(scenice.getDouble("longitude"));
//						bean.setImage_url(scenice.getString("image"));
//						bean.setIntro(scenice.getString("intro"));
						scenicBeans.add(bean);
					}
					myAdapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
				// TODO: handle exception

			}
		}
	}
}
