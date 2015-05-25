package com.qms.tubu.fragment.scenic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qms.tubu.R;
/**
 * 提示模块
 * @author dwj
 * @time 2013-11-22 上午10:05:01
 */
@SuppressLint("InflateParams")
public class HintFragment extends Fragment{
	private FrameLayout fl;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_hint, null);
		fl = (FrameLayout)view.findViewById(R.id.hint_fl);
		return view;                                                                   
	}
	
	public FrameLayout getFl() {
		return fl;
	}
}
