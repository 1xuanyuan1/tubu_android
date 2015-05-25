package com.qms.tubu.asynctask;

import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

import com.qms.tubu.R;
import com.qms.tubu.util.GetInputStream;
import com.qms.tubu.util.ReadInStream;
import com.qms.tubu.util.ToastUtils;

public class BaseAsyncTask extends AsyncTask<String, Integer, String>{
	private Dialog dialog;
	protected Activity activity;
	protected boolean isSucceed=false;
	protected JSONObject jb;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if(null == dialog){
			dialog = new Dialog(activity,R.style.dialog_style);
			dialog.setContentView(R.layout.progress);
		}
		dialog.show();
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.cancel();
		try {
			Log.i("TAG", "result:"+result);
			JSONTokener jsonParser = new JSONTokener(result);  
			// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。  
			// 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
			jb = (JSONObject) jsonParser.nextValue();
			isSucceed = jb.getBoolean("result");
			if(isSucceed){
				ToastUtils.showToast(activity, "成功");
			}else{
				ToastUtils.showToast(activity, "失败:"+jb.getString("err"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ToastUtils.showToast(activity, result);
			e.printStackTrace();
		}  
		
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		InputStream is;
		try {
			if(params.length == 1){
				is = GetInputStream.getInstance().getInputStream(params[0], null);
			}else{
				is = GetInputStream.getInstance().getInputStream(params[0], params[1]);
			}
			String result = ReadInStream.readInStream(is);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
	}
}
