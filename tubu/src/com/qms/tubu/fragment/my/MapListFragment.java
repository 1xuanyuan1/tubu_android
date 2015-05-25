package com.qms.tubu.fragment.my;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.bean.MapBean;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.util.DownLoaderTask;

public class MapListFragment extends BaseFragment {
	private MyAdapter adapter;
	private List<MapBean> maps = new ArrayList<MapBean>();
	private SparseArray<View> cache;//缓存
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_my_map_list, container, false);
		
		ListView listView = (ListView)view.findViewById(R.id.my_map_list);
		init();
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		return view;
	}
	public void init(){
		cache = new SparseArray<View>();
		for(int i = 0;i < 10;i++){
			MapBean bean = new MapBean();
			bean.setScenic("永嘉书院"+i);
			bean.setUrl("http://115.28.144.169/college.zip");
			maps.add(bean);
		}
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return maps.size();
		}

		@Override
		public MapBean getItem(int position) {
			// TODO Auto-generated method stub
			return maps.get(position);
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
				convertView  = getActivity().getLayoutInflater().inflate(R.layout.fragment_my_map_item,parent,false);
				
				TextView name = (TextView)convertView.findViewById(R.id.my_map_name);
				Button download = (Button)convertView.findViewById(R.id.my_map_bt);
				
				ProgressBar pb = (ProgressBar)convertView.findViewById(R.id.my_map_pb);
				
				MapBean bean = getItem(position);
				
				name.setText(bean.getScenic());
				
				download.setOnClickListener(new MyClickListener(convertView,bean));
				
				cache.append(position, convertView);
			}
			return convertView;
		}
		class MyClickListener implements OnClickListener{
			private View convertView;
			private MapBean bean;
			public MyClickListener(View convertView,MapBean bean){
				this.convertView = convertView;
				this.bean = bean;
			}
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				convertView.findViewById(R.id.my_map_bt).setVisibility(View.INVISIBLE);
				convertView.findViewById(R.id.my_map_pb_rl).setVisibility(View.VISIBLE);
				new DownLoaderTask(bean.getUrl(), Environment.getExternalStorageDirectory().toString(), convertView).execute();
			}
			
		}
	}
}
