package com.qms.tubu.fragment.account;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.qms.tubu.R;
import com.qms.tubu.fragment.base.BaseFragment;
import com.qms.tubu.util.ToastUtils;

public class PerfectionFragment extends BaseFragment implements OnFocusChangeListener,OnClickListener{
	private EditText et_birthday;
	private TextView constellation;
	private EditText et_affective_state;
	private EditText et_hobbies;

	private boolean[] check_hobbies;
	private String[] hobbies;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_perfection, container, false);

		et_birthday = (EditText)view.findViewById(R.id.perfection_birthday);
		constellation = (TextView)view.findViewById(R.id.perfection_constellation);
		et_affective_state = (EditText)view.findViewById(R.id.perfection_affective_state);
		et_hobbies = (EditText)view.findViewById(R.id.perfection_hobbies);

		et_birthday.setOnFocusChangeListener(this);
		et_birthday.setOnClickListener(this);

		et_affective_state.setOnFocusChangeListener(this);
		et_affective_state.setOnClickListener(this);
		
		et_hobbies.setOnFocusChangeListener(this);
		et_hobbies.setOnClickListener(this);
		
		hobbies = getResources().getStringArray(R.array.exercise);
		check_hobbies = new boolean[hobbies.length];
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ViewListener(v.getId());
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			ViewListener(v.getId());
		}
	}
	public void ViewListener(int id){
		switch (id) {
		case R.id.perfection_birthday:
			Time time = new Time("GMT+8");
			time.setToNow();
			DatePickerDialog datePicker=new DatePickerDialog(getActivity(), new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					et_birthday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
					constellation.setText(getAstro(monthOfYear+1, dayOfMonth));
					ToastUtils.showToast(getActivity(), year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day");
				}
			}, time.year, time.month, time.monthDay);
			datePicker.show();
			break;
		case R.id.perfection_affective_state:
			new AlertDialog.Builder(getActivity())   
			.setTitle("情感状态")  
			.setItems(R.array.affective_state, new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) {  
					//点击后弹出窗口选择了第几项  
					et_affective_state.setText(getResources().getStringArray(R.array.affective_state)[which]);
//					showDialog("你选择的id为" + which + " , " +getResources().getStringArray(R.array.affective_state)[which]);  
				}  
			}).create().show();  
			break;
		case R.id.perfection_hobbies:
			new AlertDialog.Builder(getActivity()).setTitle("兴趣爱好")  
			.setMultiChoiceItems(hobbies,  
					check_hobbies,  
					new DialogInterface.OnMultiChoiceClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton,  
						boolean isChecked) {  
					check_hobbies[whichButton] = isChecked;
				}  
			})
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) { 
					int count = check_hobbies.length;
					String str = "";
					for(int i = 0;i < count;i++){
						if(check_hobbies[i]){
							str += hobbies[i]+",";
						}
					}
//					String str = "";  
//					int size = MultiChoiceID.size();  
//					for (int i = 0 ;i < size; i++) {  
//						str+= mItems[MultiChoiceID.get(i)] + ", ";  
//					}  
					if(str.length() > 1){
						str = str.substring(0, str.length()-1);
						et_hobbies.setText(str);
//						showDialog("你选择的是" + str);  
					}
				}  
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {  

				}  
			}).create().show(); 
			break;
		default:
			break;
		}
	}
	public String getAstro(int month, int day) {
		String[] astro = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
				"双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
		int[] arr = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };// 两个星座分割日
		int index = month;
		// 所查询日期在分割日之前，索引-1，否则不变
		if (day < arr[month - 1]) {
			index = index - 1;
		}
		// 返回索引指向的星座string
		return astro[index];
	}

}
