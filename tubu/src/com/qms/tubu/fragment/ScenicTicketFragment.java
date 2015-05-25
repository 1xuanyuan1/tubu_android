package com.qms.tubu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.bean.ScenicBean;
import com.qms.tubu.bean.TicketBean;
import com.qms.tubu.config.Config;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.fragment.ticket.ScenicIntroFragment;
import com.qms.tubu.fragment.ticket.TicketListFragment;
import com.qms.tubu.util.RemoteImagView;
import com.qms.tubu.util.RemoteImagView.ImageCallback;

public class ScenicTicketFragment extends BaseFragment{
	private ScenicBean bean;
	private RelativeLayout scenic_ticket_rl;
	private TextView tv_name,tv_subtitle,tv_address,tv_phone;
	private ImageView image;
	private RemoteImagView remoteImagView = new RemoteImagView();
	
	
	private ViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	private RadioGroup rg;
	private int[] rbId = {R.id.scenic_ticket_intro,R.id.scenic_ticket_list};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_scenic_details, container, false);
		scenic_ticket_rl = (RelativeLayout)view.findViewById(R.id.scenic_ticket_rl);
		
		tv_name = (TextView)view.findViewById(R.id.scenic_ticket_name);
		tv_subtitle = (TextView)view.findViewById(R.id.scenic_ticket_subtitle);
//		tv_address = (TextView)view.findViewById(R.id.scenic_ticket_address);
//		tv_phone = (TextView)view.findViewById(R.id.scenic_ticket_phone);
		
		image = (ImageView)view.findViewById(R.id.scenic_ticket_image);
		
		rg = (RadioGroup)view.findViewById(R.id.scenic_ticket_rg);
		viewPager = (ViewPager)view.findViewById(R.id.scenic_ticket_pager);
		
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
		
		TextView title = (TextView)view.findViewById(R.id.title);
		title.setText(getArguments().getString("name"));
		StringBuffer data = new StringBuffer();
		data.append("id=");
		data.append(getArguments().getString("id"));
		new MyAsyncTask().execute(Config.GET_SCENICDETAILS_URL,data.toString());
		return view;
	}
	
	public void initViewPager(){
		myPagerAdapter = new MyPagerAdapter(getFragmentManager());
		viewPager.setAdapter(myPagerAdapter);
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
			Log.i("Fragment","Fragment:"+ position);
			if(null == fragment){
				switch (rbId[position]) {
				case R.id.scenic_ticket_intro:
					fragment = ScenicIntroFragment.newInstance(bean.getIntro());
					break;
				case R.id.scenic_ticket_list:
					fragment = TicketListFragment.newInstance(bean);
					break;
				}
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
	class MyAsyncTask extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			activity = getActivity();
			bean = new ScenicBean();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (isSucceed) {
				try {
					JSONObject json_scenic = jb.getJSONObject("scenic");
					bean.setId(json_scenic.getString("id"));
					bean.setName(json_scenic.getString("name"));
					bean.setSubtitle(json_scenic.getString("subtitle"));
					bean.setAddress(json_scenic.getString("address"));
					bean.setPhone(json_scenic.getString("phone"));
					bean.setImage_url(Config.APP_URL+json_scenic.getString("image"));
					bean.setPrice(Double.parseDouble(json_scenic.getString("price")));
					bean.setIntro(json_scenic.getString("intro"));
					bean.setLatitude(Double.parseDouble(json_scenic.getString("latitude")));
					bean.setLongitude(Double.parseDouble(json_scenic.getString("longitude")));
					
					List<TicketBean> ticketBeans = new ArrayList<TicketBean>();
					JSONArray json_tickets = jb.getJSONArray("tickets");
					int length = json_tickets.length();
					for(int i = 0;i < length;i++){
						JSONObject json_ticket = json_tickets.getJSONObject(i);
						TicketBean bean = new TicketBean();
						bean.setId(json_ticket.getString("id"));
						bean.setName(json_ticket.getString("name"));
						bean.setImage(Config.APP_URL+json_ticket.getString("image"));
						bean.setPrice(Double.parseDouble(json_ticket.getString("price")));
						ticketBeans.add(bean);
					}
					bean.setTickets(ticketBeans);
					
					tv_name.setText(bean.getName());
					tv_subtitle.setText(bean.getSubtitle());
//					tv_address.setText(bean.getAddress());
//					tv_phone.setText(bean.getPhone());
					Drawable drawable = remoteImagView.loadDrawable(bean.getImage_url(), new ImageCallback() {
						
						@Override
						public void imageLoaded(Drawable imageDrawable) {
							// TODO Auto-generated method stub
							image.setImageDrawable(imageDrawable);
						}
					});
					if(null != drawable){
						image.setImageDrawable(drawable);
					}
					scenic_ticket_rl.setVisibility(View.VISIBLE);
					
					
					initViewPager();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
