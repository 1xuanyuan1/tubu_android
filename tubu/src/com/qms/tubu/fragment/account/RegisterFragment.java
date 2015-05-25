package com.qms.tubu.fragment.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.qms.tubu.R;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.config.Config;
import com.qms.tubu.fragment.base.BaseFragment;

public class RegisterFragment extends BaseFragment implements OnClickListener{
	private EditText et_phone,et_password,et_repassword;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_register, container,false);
		et_phone = (EditText)view.findViewById(R.id.register_et_phone);
		et_password = (EditText)view.findViewById(R.id.register_et_password);
		et_repassword = (EditText)view.findViewById(R.id.register_et_repassword);
		
		Button register = (Button)view.findViewById(R.id.register_submit);
		Button back = (Button)view.findViewById(R.id.register_back);
		
		register.setOnClickListener(this);
		back.setOnClickListener(this);
		
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_submit:
			String password = et_password.getText().toString().trim();
			String repassword = et_repassword.getText().toString().trim();
			if(password.equals(repassword)){
				StringBuffer sb = new StringBuffer();
				sb.append("phone=");
				sb.append(et_phone.getText().toString().trim());
				sb.append("&&password=");
				sb.append(et_password.getText().toString().trim());
				new MyAsyncTask().execute(Config.REGISTER_URL,sb.toString());
			}else{
				showDialog("两次密码输入不一致");
			}
			break;
		case R.id.register_back:
			fl.back();
			break;
		}
	}
	class MyAsyncTask extends BaseAsyncTask{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.activity = getActivity();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (isSucceed) {
				fl.back();
			}
		}
	}
}
