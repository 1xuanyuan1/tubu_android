package com.qms.tubu.fragment;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.qms.tubu.R;
import com.qms.tubu.activity.ScenicMapActivity;
import com.qms.tubu.activity.ScenicNavigationActivity;
import com.qms.tubu.application.MyApplication;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.fragment.base.ImageFragment;


@SuppressLint("ViewHolder")
public class MainFragment extends BaseFragment implements OnClickListener{
	private ViewPager viewPager;
	private int[] imageId = {R.drawable.image2,R.drawable.image1,R.drawable.image3};
	private RadioGroup rg;
	private int[] rbId = {R.id.main_head_rb1,R.id.main_head_rb2,R.id.main_head_rb3};
	
	/**
	 * 定位
	 */
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;//高精度
	private String tempcoor="gcj02";//gcj02(国测局加密经纬度坐标)
	private int span = 1000;
	
	private MyApplication myApplication;
	
	
	private SparseArray<View> cache;
	private String[] names = {"楠溪江风景旅游区","十二峰","石桅岩","崖下库","温州龙瀑仙洞","龙湾潭"};
	private String[] addresses = {"浙江省温州市永嘉县楠溪江风景区","浙江温州市永嘉县楠溪江大若岩镇","浙江省温州市永嘉县鹤盛镇下岙村","浙江省温州市永嘉县大若岩镇小若口北偏西2.5公里处山谷中","浙江省温州市永嘉县岩头镇岙底村","浙江省温州市永嘉县鹤盛镇季家岙"};
	private int[] nums = {123,221,342,180,432,98};
	private int[] prices = {30,30,50,50,60,70};
	private int[] images = {R.drawable.scenic1,R.drawable.scenic2,R.drawable.scenic3,R.drawable.scenic4,R.drawable.scenic5,R.drawable.scenic6};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TAG = "MainFragment";
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		
		myApplication = (MyApplication) getActivity().getApplication();
		mLocationClient = myApplication.mLocationClient;
		
		initLocation();
		cache = new SparseArray<View>();
		TextView address = (TextView)view.findViewById(R.id.main_head_address);
		myApplication.address = address;
		
		viewPager = (ViewPager)view.findViewById(R.id.main_pager);
		viewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				rg.check(rbId[position]);
			}
		});
		
		rg = (RadioGroup)view.findViewById(R.id.main_head_rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int position = 0;
				for(int i=0;i<rbId.length;i++){
					position = i;
					if(rbId[position] == checkedId){
						break;
					}
				}
				viewPager.setCurrentItem(position);
			}
		});

		GridView gridView = (GridView)view.findViewById(R.id.main_list);
//		ListView listView = (ListView)view.findViewById(R.id.main_list);
		gridView.setAdapter(new Myadapter());
		
		view.findViewById(R.id.main_scenic).setOnClickListener(this);
		view.findViewById(R.id.main_date).setOnClickListener(this);
		view.findViewById(R.id.main_recreation).setOnClickListener(this);
		view.findViewById(R.id.main_scenic_gc).setOnClickListener(this);
		view.findViewById(R.id.main_scenic_hd).setOnClickListener(this);
		view.findViewById(R.id.main_scenic_ss).setOnClickListener(this);
		view.findViewById(R.id.main_scenic_th).setOnClickListener(this);
		

		return view;
	}
	
	class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = cache.get(position);
			if(null == convertView){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_scenic_item, parent,false);
				TextView name = (TextView)convertView.findViewById(R.id.scenic_item_name);
				TextView price = (TextView)convertView.findViewById(R.id.scenic_item_price);
				TextView discount_price = (TextView)convertView.findViewById(R.id.scenic_item_price_discount_price);
				TextView address = (TextView)convertView.findViewById(R.id.scenic_address);
				TextView num = (TextView)convertView.findViewById(R.id.scenic_purchase_num);
				ImageView image = (ImageView)convertView.findViewById(R.id.scenic_item_image);
				
				name.setText(names[position]);
				DecimalFormat df = new DecimalFormat("###.00");  
				address.setText("地址："+addresses[position]);
				price.setText(df.format(prices[position]*0.85));
				discount_price.setText(df.format(prices[position]));
				image.setImageResource(images[position]);
				num.setText(String.valueOf(nums[position]));
				
				cache.put(position, convertView);
			}
			return convertView;
		}
		
	}
	/**
	 * 定位初始化
	 */
	private void initLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(span);//设置发起定位请求的间隔时间为1000ms
		option.setIsNeedAddress(true);//是否显示地址（若显示地址则不换地方不更新经纬度,不显示地址则随时更新经纬度）
		mLocationClient.setLocOption(option);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		if(!mLocationClient.isStarted()){
			mLocationClient.start();
		}
		super.onStart();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(mLocationClient.isStarted()){
			mLocationClient.stop();
		}
	}
	class MyPagerAdapter extends FragmentStatePagerAdapter{
		private SparseArray<BaseFragment> sbf;//每个页面缓存
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			sbf = new SparseArray<BaseFragment>();
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			BaseFragment fragment = sbf.get(position);
			if(null == fragment){
				fragment = ImageFragment.newInstance(imageId[position]);
				sbf.append(position, fragment);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageId.length;
		}
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		activity.changeMenu(R.id.main_rb_foot_main);
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.main_scenic:
			showLog("景区");
			bundle.putString("title", "景区");
			fl.skip(R.id.scenic, bundle,false);
			break;
		case R.id.main_navigation:
			//			Bundle bundle = new Bundle();
			//			bundle.putSerializable("stScenic", new ScenicBean(28.004896, 120691273));
			//			bundle.putSerializable("endScenic",new ScenicBean(28.27356, 120.707296));
			//			fl.skip(R.id.map, bundle);
			showLog("景点导航");
			fl.activity(ScenicNavigationActivity.class);
			break;
		case R.id.main_scenicmap:
			showLog("景区地图");
			fl.activity(ScenicMapActivity.class);
			break;
		case R.id.main_recreation:
			fl.skip(R.id.recommend, bundle,false);
			break;
		case R.id.main_scenic_gc:
			bundle.putString("title", "古村游");
			fl.skip(R.id.scenic, bundle,false);
			break;
		case R.id.main_scenic_hd:
			bundle.putString("title", "活动游");
			fl.skip(R.id.scenic, bundle,false);
			break;
		case R.id.main_scenic_ss:
			bundle.putString("title", "山水游");
			fl.skip(R.id.scenic, bundle,false);
			break;
		case R.id.main_scenic_th:
			bundle.putString("title", "特惠游");
			fl.skip(R.id.scenic, bundle,false);
			break;
		case R.id.main_date:
			fl.skip(R.id.date, null, false);
			break;
//		case R.id.main_location:
//			showLog("定位取点");
//			fl.activity(MyLocationActivity.class);
//			break;
		}
	}
}
