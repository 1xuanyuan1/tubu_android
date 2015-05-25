package com.qms.tubu.fragment.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.activity.MainActivity;
import com.qms.tubu.application.MyApplication;
import com.qms.tubu.util.DialogUtil;
import com.qms.tubu.util.ToastUtils;


@SuppressLint("NewApi")
public class BaseFragment extends Fragment implements OnClickListener{
	protected MainActivity activity = MainActivity.activity;
	protected static String TAG = "BaseFragment";
	public FragmentListener fl;
	protected MyApplication myApplication;
	
	protected View view;
	public interface FragmentListener{
		public void clear();
		public void skip(int id,@Nullable Bundle bundle,boolean isFirst);
		public void pop(int id);
		public void back();
		public void activity(Class<?> cls);
		public void activity(Class<?> cls,@Nullable Bundle bundle);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			fl.back();
			break;
		}
	}
	public void setTitle(String title){
		((TextView)view.findViewById(R.id.title)).setText(title);
		view.findViewById(R.id.back).setOnClickListener(this);
	}
	public void showEdit(){
		view.findViewById(R.id.edit).setVisibility(View.VISIBLE);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		fl = (FragmentListener)activity;
		myApplication = (MyApplication)activity.getApplication();
		super.onAttach(activity);
	}
	//输出日志
	public void showLog(String msg){
		Log.i(TAG, msg);
	}
	//显示提示
	public void showToast(String msg){
		ToastUtils.showToast(activity, msg);
	}
	//弹窗
	public void showDialog(String msg){
		DialogUtil.showDialog(msg, activity);
	}
}
