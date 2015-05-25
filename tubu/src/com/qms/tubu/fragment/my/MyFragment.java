package com.qms.tubu.fragment.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.qms.tubu.R;
import com.qms.tubu.fragment.base.BaseFragment;

public class MyFragment extends BaseFragment implements OnClickListener{
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_my, container,false);
		view.findViewById(R.id.my_login).setOnClickListener(this);
		view.findViewById(R.id.my_map).setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_login:
			fl.skip(R.id.login, null, false);
			break;
		case R.id.my_map:
			fl.skip(R.id.my_map, null, false);
			break;
		}
	}
}
