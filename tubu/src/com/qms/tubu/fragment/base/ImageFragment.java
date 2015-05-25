package com.qms.tubu.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qms.tubu.R;

public class ImageFragment extends BaseFragment {
	//构造函数 
	public static ImageFragment newInstance(int imageId){
		ImageFragment fragment = new ImageFragment();
		Bundle args = new Bundle();
		args.putInt("imageId", imageId);
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_image, container,false);
		ImageView image = (ImageView)view.findViewById(R.id.main_image);
		image.setImageResource(getArguments().getInt("imageId"));
		return view;
	}
}
