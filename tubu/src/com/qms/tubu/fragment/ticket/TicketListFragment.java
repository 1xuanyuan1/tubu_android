package com.qms.tubu.fragment.ticket;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.application.MyApplication;
import com.qms.tubu.bean.AccountBean;
import com.qms.tubu.bean.ScenicBean;
import com.qms.tubu.bean.TicketBean;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.util.RemoteImagView;
import com.qms.tubu.util.RemoteImagView.ImageCallback;
/**
 * 
 * @author dwj
 * @date 2015-4-24  下午4:07:13
 *
 */
public class TicketListFragment extends BaseFragment {
	private List<TicketBean> ticketBeans;
	private SparseArray<View> cache;//缓存
	private RemoteImagView remoteImagView = new RemoteImagView();
	//构造函数 
	public static TicketListFragment newInstance(ScenicBean bean){
		TicketListFragment fragment = new TicketListFragment();
		Bundle args = new Bundle();
		args.putSerializable("scenic", bean);
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_scenic_ticket, container,false);
		ListView listView = (ListView)view.findViewById(R.id.fragment_ticket_list);
		ScenicBean bean = (ScenicBean)getArguments().getSerializable("scenic");
		ticketBeans = bean.getTickets();
		cache = new SparseArray<View>();
		listView.setAdapter(new MyAdapter());
		return view;
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ticketBeans.size();
		}

		@Override
		public TicketBean getItem(int position) {
			// TODO Auto-generated method stub
			return ticketBeans.get(position);
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
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_scenic_ticket_item, parent,false);
				RelativeLayout rl = (RelativeLayout)convertView.findViewById(R.id.scenic_ticket_rl);
				TextView tv_name = (TextView)convertView.findViewById(R.id.scenic_ticket_name);
				TextView tv_price = (TextView)convertView.findViewById(R.id.scenic_ticket_price);
				
				final ImageView image = (ImageView)convertView.findViewById(R.id.scenic_ticket_image);

				
				TicketBean bean = getItem(position);
				
				Drawable drawable = remoteImagView.loadDrawable(bean.getImage(), new ImageCallback() {
					
					@Override
					public void imageLoaded(Drawable imageDrawable) {
						// TODO Auto-generated method stub
						image.setImageDrawable(imageDrawable);
					}
				});
				if(null != drawable){
					image.setImageDrawable(drawable);
				}
				
				Button bt = (Button)convertView.findViewById(R.id.scenic_ticket_bt);
				bt.setOnClickListener(new MyClickListener(bean));
				
				switch(position%2){
				case 0:
					rl.setBackgroundColor(getResources().getColor(R.color.white));
					break;
				case 1:
					rl.setBackgroundColor(getResources().getColor(R.color.gray1));
					break;
				}
				
				tv_name.setText(bean.getName());
				tv_price.setText(String.valueOf(bean.getPrice()));
				cache.put(position, convertView);
			}
			return convertView;
		}
		class MyClickListener implements OnClickListener{
			private TicketBean bean;
			public MyClickListener(TicketBean bean){
				this.bean = bean;
			}
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AccountBean accountBean = ((MyApplication)getActivity().getApplication()).accountBean;
				if(null == accountBean){
					Bundle bundle = new Bundle();
					bundle.putInt("Next", R.id.main);
					fl.skip(R.id.login, bundle,true);
				}else{
					Bundle bundle = new Bundle();
					bundle.putSerializable("ticket", bean);
					fl.skip(R.id.ticket_order, bundle,false);
				}
			}
		}
		
	}
}
