package com.qms.tubu.fragment.scenic;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.qms.tubu.R;
/**
 * 摄像头模块
 * @author dwj
 * @time 2013-11-22 上午10:03:30
 */
@SuppressLint("InflateParams")
public class CameraFragment extends Fragment{
	private Camera camera;//摄像头
	private SurfaceView surfaceView;//显示摄像头数据
	private boolean isPreview = false;//是否在预览
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_camera, null);
		DisplayMetrics dm  = getResources().getDisplayMetrics();
		surfaceView = (SurfaceView) view.findViewById(R.id.surfaceview);
		surfaceView.getHolder().setFixedSize(dm.widthPixels, dm.heightPixels);
		surfaceView.getHolder().addCallback(new SurfaceCallback());
		return view;
	}
	private class SurfaceCallback implements SurfaceHolder.Callback{
		@SuppressWarnings("deprecation")
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera = Camera.open();//打开硬件摄像头，这里导包得时候一定要注意是android.hardware.Camera
				WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);//得到窗口管理器
				Display display  = wm.getDefaultDisplay();//得到当前屏幕
				Camera.Parameters parameters = camera.getParameters();//得到摄像头的参数
				parameters.setPreviewSize(display.getWidth(), display.getHeight());//设置预览照片的大小
				parameters.setPreviewFrameRate(3);//设置每秒3帧
				parameters.setPictureFormat(PixelFormat.JPEG);//设置照片的格式
				parameters.setJpegQuality(85);//设置照片的质量
				parameters.setPictureSize(display.getHeight(), display.getWidth());//设置照片的大小，默认是和     屏幕一样大
				camera.setPreviewDisplay(surfaceView.getHolder());//通过SurfaceView显示取景画面
				camera.startPreview();//开始预览
				isPreview = true;//设置是否预览参数为真
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(camera!=null){
				if(isPreview){//如果正在预览 
					camera.stopPreview();
					camera.release();
				}
			}
		}

	}
}
