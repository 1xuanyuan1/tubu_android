package com.qms.tubu.fragment.ticket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.fragment.base.BaseFragment;

public class ScenicIntroFragment extends BaseFragment{
	//构造函数 
	public static ScenicIntroFragment newInstance(String intro){
		ScenicIntroFragment fragment = new ScenicIntroFragment();
		Bundle args = new Bundle();
		args.putString("intro", intro);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_scenic_intro, container,false);
		TextView textView = (TextView)view.findViewById(R.id.scenic_intro);
		textView.setText(getArguments().getString("intro"));
		return view;
	}
}
