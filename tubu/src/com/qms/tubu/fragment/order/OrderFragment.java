package com.qms.tubu.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.qms.tubu.R;
import com.qms.tubu.fragment.base.BaseFragment;

public class OrderFragment extends BaseFragment {
	private int[] rbId = {R.id.scenic_order_all,R.id.scenic_order_no,R.id.scenic_order_done};
	private MyPagerAdapter myPagerAdapter;
	private ViewPager viewPager;
	private RadioGroup rg;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_scenic_order, container , false);
		rg = (RadioGroup)view.findViewById(R.id.scenic_order_rg);
		viewPager = (ViewPager)view.findViewById(R.id.scenic_order_pager);
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
		initViewPager();
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		activity.changeMenu(R.id.main_rb_foot_order);
		super.onResume();
	}
	public void initViewPager(){
		myPagerAdapter = new MyPagerAdapter(getFragmentManager());
		viewPager.setAdapter(myPagerAdapter);
		viewPager.setOffscreenPageLimit(3);//设置预加载页面个数
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				rg.check(rbId[position]);
			}
		});
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
			StringBuffer sb = new StringBuffer();
			sb.append("account_id=");
			sb.append(myApplication.accountBean.getId());
			Log.i("Fragment","Fragment:"+ position);
			if(null == fragment){
				switch (rbId[position]) {
				case R.id.scenic_order_all:
					
					break;
				case R.id.scenic_order_no:
					sb.append("&&type=1");
					break;
				case R.id.scenic_order_done:
					sb.append("&&type=2");
					break;
				}
				fragment = OrderListFragment.newInstance(sb.toString());
				sbf.append(position, fragment);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rbId.length;
		}

	}
}
