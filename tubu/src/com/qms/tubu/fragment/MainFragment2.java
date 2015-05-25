package com.qms.tubu.fragment;

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

import com.qms.tubu.R;
import com.qms.tubu.activity.MyLocationActivity;
import com.qms.tubu.activity.ScenicMapActivity;
import com.qms.tubu.activity.ScenicNavigationActivity;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.fragment.base.ImageFragment;


public class MainFragment2 extends BaseFragment implements OnClickListener{
	private ViewPager viewPager;
	private int[] imageId = {R.drawable.image1,R.drawable.image2,R.drawable.image3};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TAG = "MainFragment";
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		
		viewPager = (ViewPager)view.findViewById(R.id.main_pager);
		viewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
		
		view.findViewById(R.id.main_scenic).setOnClickListener(this);
		view.findViewById(R.id.main_navigation).setOnClickListener(this);
		view.findViewById(R.id.main_scenicmap).setOnClickListener(this);
//		view.findViewById(R.id.main_location).setOnClickListener(this);


		return view;
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
		switch (v.getId()) {
		case R.id.main_scenic:
			showLog("景区");
			fl.skip(R.id.scenic, null,false);
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
//		case R.id.main_location:
//			showLog("定位取点");
//			fl.activity(MyLocationActivity.class);
//			break;
		}
	}
}
