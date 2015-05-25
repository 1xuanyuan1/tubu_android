package com.qms.tubu.fragment.date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.fragment.base.BaseFragment;

@SuppressLint("ViewHolder")
public class DateFragment extends BaseFragment {
	private SparseArray<View> cache;
	private String[] names = {"毕业旅行","拓展","探险","景区"};
	private int[] images = {R.drawable.date1,R.drawable.date2,R.drawable.date3,R.drawable.date4};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_date, container,false);
		
		cache = new SparseArray<View>();
		GridView gridView = (GridView)view.findViewById(R.id.date_list_gv);
		gridView.setAdapter(new MyAdapter());
		
		ListView listView = (ListView)view.findViewById(R.id.date_list_lv);
		listView.setAdapter(new MyListAdapter());
		
		setTitle("约吧");
		showEdit();
		return view;
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
	
	class MyListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
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
			View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_date_item, parent,false);
			return view;
		}
		
	}
}
