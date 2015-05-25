package com.qms.tubu.fragment.scenic;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.qms.tubu.R;
/**
 * 指南针模块
 * @author dwj
 * @time 2013-11-22 上午10:04:51
 */
@SuppressLint("InflateParams")
public class CompassFragment extends Fragment{
	private ImageView needle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_compass, null);
		needle = (ImageView)view.findViewById(R.id.needle);
		return view;
	}
	/**
	 * 指南针指针转达
	 * @param predegree 开始时指针方向
	 * @param degree 结束时指针方向
	 */
	public void changeNeedle(float predegree,float degree){
		RotateAnimation animation = new RotateAnimation(predegree, degree,
				Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		animation.setFillAfter(true);
		animation.setDuration(200);
		needle.startAnimation(animation);
	}
}
