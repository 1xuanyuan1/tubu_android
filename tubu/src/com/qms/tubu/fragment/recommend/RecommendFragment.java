package com.qms.tubu.fragment.recommend;

import java.text.DecimalFormat;
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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SearchView;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.bean.ScenicBean;
import com.qms.tubu.config.Config;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.fragment.base.ImageFragment;
import com.qms.tubu.util.RemoteImagView;
import com.qms.tubu.util.RemoteImagView.ImageCallback;

public class RecommendFragment extends BaseFragment {
	private ViewPager viewPager;
	private int[] imageId = {R.drawable.image2,R.drawable.image3,R.drawable.image1};
	private RadioGroup rg;
	private int[] rbId = {R.id.main_head_rb1,R.id.main_head_rb2,R.id.main_head_rb3};


	private SparseArray<View> cache;
	private String[] names = {"游乐园","漂流","真人cs","另类.."};
	private int[] images = {R.drawable.recommend1,R.drawable.recommend2,R.drawable.recommend3,R.drawable.recommend4};

	private List<ScenicBean> scenicBeans;
	private MyListAdapter myAdapter;
	private SparseArray<View> cache2;
	private RemoteImagView remoteImagView = new RemoteImagView();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_recreation, container,false);

//		SearchView sv = (SearchView) view.findViewById(R.id.search_view);
//		sv.setIconifiedByDefault(false);
//		sv.setSubmitButtonEnabled(true);

		cache = new SparseArray<View>();
		viewPager = (ViewPager)view.findViewById(R.id.scenic_pager);
		viewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				rg.check(rbId[position]);
			}
		});
		
		setTitle("娱乐");

		rg = (RadioGroup)view.findViewById(R.id.scenic_head_rg);
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

		GridView gridView = (GridView)view.findViewById(R.id.scenic_place_recommend_list);
		gridView.setAdapter(new MyAdapter());

		myAdapter = new MyListAdapter();
		new MyAsyncTask().execute(Config.QUERY_SCENIC_URL);
		ListView listView = (ListView)view.findViewById(R.id.scenic_place_list);
		listView.setAdapter(myAdapter);
		return view;
	}
	class MyAsyncTask extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			activity = getActivity();
			cache2 = new SparseArray<View>();
			scenicBeans = new ArrayList<ScenicBean>();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(isSucceed){
				try {
					JSONArray json_scenics = jb.getJSONArray("scenic");
					int length = json_scenics.length();
					for(int i = 0;i < length;i++){
						JSONObject json_scenic = json_scenics.getJSONObject(i);
						ScenicBean bean = new ScenicBean();
						bean.setId(json_scenic.getString("id"));
						bean.setName(json_scenic.getString("name"));
						bean.setAddress(json_scenic.getString("address"));
						bean.setImage_url(Config.APP_URL+json_scenic.getString("image"));
						bean.setPrice(Double.parseDouble(json_scenic.getString("price")));
						scenicBeans.add(bean);
					}
					myAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	class MyListAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return scenicBeans.size();
		}

		@Override
		public ScenicBean getItem(int position) {
			// TODO Auto-generated method stub
			return scenicBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = cache2.get(position);
			if(null == convertView){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_scenic_item2, parent,false);

				TextView name = (TextView)convertView.findViewById(R.id.scenic_item_name);
				TextView price = (TextView)convertView.findViewById(R.id.scenic_item_price);
				TextView discount_price = (TextView)convertView.findViewById(R.id.scenic_item_price_discount_price);
				TextView address = (TextView)convertView.findViewById(R.id.scenic_address);
				final ImageView image = (ImageView)convertView.findViewById(R.id.scenic_item_image);

				final ScenicBean bean = getItem(position);
				name.setText(bean.getName());
				DecimalFormat df = new DecimalFormat("###.00");  
				address.setText("地址："+bean.getAddress());
				price.setText(df.format(bean.getPrice()*0.85));
				discount_price.setText(df.format(bean.getPrice()));
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

				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putString("id", bean.getId());
						bundle.putString("name", bean.getName());
						fl.skip(R.id.scenic_ticket, bundle,false);
					}
				});
				cache2.put(position, convertView);
			}
			return convertView;
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
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = cache.get(position);
			if(null == convertView){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_recreation_item, parent,false);

				ImageView image = (ImageView)convertView.findViewById(R.id.recreation_item_image);
				TextView name = (TextView)convertView.findViewById(R.id.recreation_item_name);

				image.setImageResource(images[position]);
				name.setText(names[position]);
				cache.put(position, convertView);
			}
			return convertView;
		}

	}
}
