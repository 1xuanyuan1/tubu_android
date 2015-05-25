package com.qms.tubu.fragment.order;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.bean.OrderBean;
import com.qms.tubu.config.Config;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.util.CreateQRImage;
import com.qms.tubu.util.RemoteImagView;
import com.qms.tubu.util.RemoteImagView.ImageCallback;

public class OrderListFragment extends BaseFragment {
	private List<OrderBean> orders = new ArrayList<OrderBean>();
	private ListView listView;
	private SparseArray<View> cache;//缓存
	private MyAdapter adapter;
	private RemoteImagView remoteImagView = new RemoteImagView();
	//构造函数 
	public static OrderListFragment newInstance(String data){
		OrderListFragment fragment = new OrderListFragment();
		Bundle args = new Bundle();
		args.putString("data", data);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TAG = "OrderListFragment";
		View view = inflater.inflate(R.layout.fragment_scenic_order_list, container , false);
		cache = new SparseArray<View>();
		listView = (ListView)view.findViewById(R.id.fragment_order_list);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		new MyAsyncTask().execute(Config.QUERY_ORDER_URL,getArguments().getString("data"));
		return view;
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orders.size();
		}

		@Override
		public OrderBean getItem(int position) {
			// TODO Auto-generated method stub
			return orders.get(position);
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
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_scenic_order_item, parent,false);
				RelativeLayout rl = (RelativeLayout)convertView.findViewById(R.id.scenic_ticket_rl);
				TextView tv_name = (TextView)convertView.findViewById(R.id.scenic_ticket_name);
				TextView tv_price = (TextView)convertView.findViewById(R.id.scenic_ticket_price);
				TextView tv_num = (TextView)convertView.findViewById(R.id.scenic_ticket_num);
				
				final ImageView image = (ImageView)convertView.findViewById(R.id.scenic_ticket_image);

				ImageView code = (ImageView)convertView.findViewById(R.id.scenic_ticket_code);
				OrderBean bean = getItem(position);
				
				code.setImageBitmap(CreateQRImage.createQRImage(bean.getId()));
				Drawable drawable = remoteImagView.loadDrawable(bean.getTicket_image(), new ImageCallback() {
					
					@Override
					public void imageLoaded(Drawable imageDrawable) {
						// TODO Auto-generated method stub
						image.setImageDrawable(imageDrawable);
					}
				});
				if(null != drawable){
					image.setImageDrawable(drawable);
				}
				switch(position%2){
				case 0:
					rl.setBackgroundColor(getResources().getColor(R.color.white));
					break;
				case 1:
					rl.setBackgroundColor(getResources().getColor(R.color.gray1));
					break;
				}
				tv_num.setText("数量"+bean.getNum());
				tv_name.setText(bean.getTicket_name());
				tv_price.setText(String.valueOf(bean.getTicket_price()));
				cache.put(position, convertView);
			}
			return convertView;
		}

	}
	class MyAsyncTask extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.activity = getActivity();
			orders = new ArrayList<OrderBean>();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(isSucceed){
				try {
					JSONArray json_orders = jb.getJSONArray("orders");
					int count = json_orders.length();
					showLog("count:"+count);
					for(int i = 0;i < count;i++){
						JSONObject json_order = json_orders.getJSONObject(i);
						OrderBean order = new OrderBean();
						order.setId(json_order.getString("id"));
						order.setNum(Integer.parseInt(json_order.getString("num")));
						order.setCreateTime(json_order.getString("createtime"));
						order.setTicket_image(Config.APP_URL+json_order.getString("ticket_image"));
						order.setTicket_name(json_order.getString("ticket_name"));
						order.setTicket_price(Double.parseDouble(json_order.getString("ticket_price")));
						orders.add(order);
					}
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
}
