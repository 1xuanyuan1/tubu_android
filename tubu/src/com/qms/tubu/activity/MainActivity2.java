package com.qms.tubu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.qms.tubu.R;
import com.qms.tubu.application.MyApplication;
import com.qms.tubu.fragment.MainFragment;
import com.qms.tubu.fragment.ScenicFragment;
import com.qms.tubu.fragment.ScenicTicketFragment;
import com.qms.tubu.fragment.account.LoginFragment;
import com.qms.tubu.fragment.account.RegisterFragment;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.fragment.base.BaseFragment.FragmentListener;
import com.qms.tubu.fragment.my.MapListFragment;
import com.qms.tubu.fragment.my.MyFragment;
import com.qms.tubu.fragment.order.OrderFragment;
import com.qms.tubu.fragment.order.OrderListFragment;
import com.qms.tubu.fragment.ticket.TicketOrderFragment;
import com.qms.tubu.util.DialogUtil;
import com.qms.tubu.util.RemoteImagView;

public class MainActivity2 extends FragmentActivity implements FragmentListener{
	private FragmentManager fm; 
	private FragmentTransaction transaction; 
	public static MainActivity2 activity;
	private RadioGroup menu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		activity = this;

		RemoteImagView.setM_ImagesPath(this.getDir("images",Context.MODE_PRIVATE));//设置加载图片的缓存路径

		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		transaction.add(R.id.main_fl, new MainFragment());//放入初始页
		transaction.commit();

		menu = (RadioGroup)findViewById(R.id.main_rg_foot);
		menu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.main_rb_foot_order){
					if(!isLogin()){
						Bundle bundle = new Bundle();
						switch (checkedId) {
						case R.id.main_rb_foot_my:
							bundle.putInt("Next", R.id.my);
							break;
						case R.id.main_rb_foot_order:
							bundle.putInt("Next", R.id.myorder);
							break;
						}
						skip(R.id.login, bundle, false);
						return;
					}
				}
				switch (checkedId) {
				case R.id.main_rb_foot_main:
					clear();
					break;
				case R.id.main_rb_foot_map:
//					activity(CloudSearchActivity.class);
//					DialogUtil.showDialog("正在开发中", MainActivity.this);
					break;
				case R.id.main_rb_foot_my:
					skip(R.id.my, null, false);
					break;
				case R.id.main_rb_foot_order:
					skip(R.id.myorder, null, false);
					break;
				case R.id.main_rb_foot_search:
					DialogUtil.showDialog("正在开发中", MainActivity2.this);
					break;

				default:
					break;
				}
			}
		});

	}
	/**
	 * 修改下方菜单
	 */
	public void changeMenu(int id){
		menu.check(id);
	}
	/**
	 * 判断是否已经登录
	 * @return
	 */
	public boolean isLogin(){
		return ((MyApplication)getApplication()).accountBean != null;
	}
	@Override
	public void skip(int id, @Nullable Bundle bundle,boolean isFirst) {
		// TODO Auto-generated method stub
		BaseFragment fragment = null;
		transaction = fm.beginTransaction();
		switch (id) {
		case R.id.main:
			fragment = new MainFragment();
			break;
		case R.id.scenic:
			fragment = new ScenicFragment();
			break;
		case R.id.scenic_ticket:
			fragment = new ScenicTicketFragment();
			break;
		case R.id.map:
			//			fragment = new MapFragment();
			break;
		case R.id.login:
			fragment = new LoginFragment();
			break;
		case R.id.register:
			fragment = new RegisterFragment();
			break;
		case R.id.ticket_order:
			fragment = new TicketOrderFragment();
			break;
		case R.id.order_list:
			fragment = new OrderListFragment();
			break;
		case R.id.myorder:
			fragment = new OrderFragment();
			break;
		case R.id.my:
			fragment = new MyFragment();
			break;
		case R.id.my_map:
			fragment = new MapListFragment();
			break;
		}
		fragment.setArguments(bundle);
		transaction.add(R.id.main_fl,fragment,String.valueOf(id));
		if(!isFirst){
			transaction.addToBackStack(String.valueOf(id));
		}
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);//设置动画效果
		transaction.commit();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Log.i("TAG", "count:"+fm.getBackStackEntryCount());
		if(fm.getBackStackEntryCount()>0){
			fm.popBackStack();
		}else{
			super.onBackPressed();
		}
	}
	@Override
	public void pop(int id) {
		// TODO Auto-generated method stub
		fm.popBackStack(String.valueOf(id),0);
	}
	@Override
	public void back() {
		// TODO Auto-generated method stub
		fm.popBackStack();
	}
	@Override
	public void activity(Class<?> cls) {
		// TODO Auto-generated method stub
		startActivity(new Intent(activity, cls));
	}
	@Override
	public void activity(Class<?> cls, @Nullable Bundle bundle) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(activity, cls);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		int count = fm.getBackStackEntryCount();
		for(int i = 0;i<count;i++){
			fm.popBackStack();
		}
	}
}
