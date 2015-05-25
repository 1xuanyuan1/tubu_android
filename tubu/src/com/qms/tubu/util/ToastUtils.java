package com.qms.tubu.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author dwj
 *
 */

public class ToastUtils {

	private static Toast mToast = null;

	/**
	 * 
	 * @param content
	 * @param text
	 */
	public static void showToast(Context content,String text) {  
		if(mToast == null) {  
			mToast = Toast.makeText(content, text, Toast.LENGTH_SHORT);  
		} else {  
			mToast.setText(text);    
			mToast.setDuration(Toast.LENGTH_SHORT);  
		}  
		mToast.show();  
	}  

	
	
	
	private static void cancelToast() {  
		if (mToast != null) {  
			mToast.cancel();
		}  
	}  

	public void onBackPressed() {  
		cancelToast();  
	}  
}
