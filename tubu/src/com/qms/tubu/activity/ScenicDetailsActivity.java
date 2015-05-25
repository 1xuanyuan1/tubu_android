package com.qms.tubu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.bean.ScenicBean;

public class ScenicDetailsActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scenicdetails);
		ScenicBean bean = (ScenicBean) getIntent().getExtras().getSerializable("endScenic");
//		ImageView image = (ImageView) findViewById(R.id.details_image);
//		image.setImageResource(bean.getImage());
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(bean.getName());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*case R.id.details_showAR:
			startActivity(new Intent(this,CloudReco.class));
			break;
		case R.id.details_showQJ:
			startActivity(new Intent(this,WebActivity.class));
			break;*/
		default:
			break;
		}
	}
}
