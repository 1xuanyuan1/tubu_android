package com.qms.tubu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RemoteImagView {

	private static HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	private static ImageHelper imageHelper;
	private static File m_ImagesPath = null;

	public RemoteImagView() {
//		imageCache = new HashMap<String, SoftReference<Drawable>>();
		imageHelper = new ImageHelper();
	}

	@SuppressLint("HandlerLeak")
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		Log.i("TAG", "imageCache.size:"+imageCache.size());;
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public static Drawable loadImageFromUrl(String url) {
		Drawable d = Drawable.createFromPath(imageHelper.downImage(url));
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable);
	}

	public static File getM_ImagesPath() {
		return m_ImagesPath;
	}

	public static void setM_ImagesPath(File m_ImagesPath) {
		RemoteImagView.m_ImagesPath = m_ImagesPath;
	}

	private class ImageHelper {
		private String makeFileName(String url) {
			int index = url.lastIndexOf('.');
			if (index == -1) {
				index = url.length() - 3;
			}
			String Md5Str = null;
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.update(url.getBytes());
				byte bytes[] = digest.digest();
				StringBuilder hexString = new StringBuilder();
				for (byte b : bytes) {
					hexString.append(String.format("%02x", b));
				}
				Md5Str = hexString.toString();
			} catch (Exception e) {
				e.printStackTrace();
				Md5Str = null;
			}

			return Md5Str + url.substring(index);
		}

		public String downImage(String strUrl) {
//			Log.i("TAG", "strUrl   "+strUrl);
			InputStream is = null;
			FileOutputStream fos = null;
			File file = null;
			String result = null;
			try {
				file = new File(getM_ImagesPath(), makeFileName(strUrl));
				if (!file.exists()) {
					is = GetInputStream.getInstance().getInputStream(strUrl,
							null);
					fos = new FileOutputStream(file);
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						if (numread <= 0) {
							break;
						}
						fos.write(buf, 0, numread);
					} while (true);
				}
				result = file.getAbsolutePath();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fos)
						fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (null != is)
						is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
//			Log.i("TAG", "result   "+result);
			return result;
		}
	}

}
