package com.qms.tubu.fragment.account;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.qms.tubu.R;
import com.qms.tubu.application.MyApplication;
import com.qms.tubu.asynctask.BaseAsyncTask;
import com.qms.tubu.bean.AccountBean;
import com.qms.tubu.config.Config;
import com.qms.tubu.fragment.base.BaseFragment;

public class LoginFragment extends BaseFragment implements OnClickListener{
	private EditText et_password,et_phone;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_login, container,false);
		
		et_phone = (EditText)view.findViewById(R.id.login_et_phone);
		et_password = (EditText)view.findViewById(R.id.login_et_password);
		
		Button login = (Button)view.findViewById(R.id.login_login);
		Button register = (Button)view.findViewById(R.id.login_register);
		
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_login:
			StringBuffer sb = new StringBuffer();
			sb.append("phone=");
			sb.append(et_phone.getText().toString().trim());
			sb.append("&&password=");
			sb.append(et_password.getText().toString().trim());
			new MyAsyncTask().execute(Config.LOGIN_URL,sb.toString());
			break;
		case R.id.login_register:
			fl.skip(R.id.register, null,false);
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
				try {
					AccountBean accountBean = new AccountBean();
					JSONObject jb_account = jb.getJSONObject("account");
					accountBean.setId(jb_account.getString("id"));
					accountBean.setNickname(jb_account.getString("nickname"));
					accountBean.setPhone(jb_account.getString("phone"));
					((MyApplication)getActivity().getApplication()).accountBean = accountBean;
					showLog("Next:"+getArguments().getInt("Next"));
					fl.skip(getArguments().getInt("Next"), null, false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
}
