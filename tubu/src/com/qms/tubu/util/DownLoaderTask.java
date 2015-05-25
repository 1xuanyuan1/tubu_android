package com.qms.tubu.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qms.tubu.R;
public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {
	private final String TAG = "DownLoaderTask";
	private URL mUrl;
	private File mFile;
	private int mProgress = 0;
	private ProgressReportingOutputStream mOutputStream;
	private Context mContext;
	private ProgressBar pb;
	private TextView tv;
	private View view;
	public DownLoaderTask(String url,String out,View view){
		super();
		this.view = view;
		pb = (ProgressBar)view.findViewById(R.id.my_map_pb);
		tv = (TextView)view.findViewById(R.id.my_map_pb_tv);
		try {
			mUrl = new URL(url);
			String fileName = new File(mUrl.getFile()).getName();
			mFile = new File(out, fileName);
			Log.d(TAG, "out="+out+", name="+fileName+",mUrl.getFile()="+mUrl.getFile());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	protected Long doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return download();
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		//super.onProgressUpdate(values);
		if(values.length>1){
			int contentLength = values[1];
			if(contentLength==-1){
				pb.setIndeterminate(true);
			}
			else{
				tv.setText("开始下载");
				pb.setMax(contentLength);
			}
		}
		else{
			pb.setProgress(values[0].intValue());
			tv.setText((values[0].intValue()*100/pb.getMax())+"%");
		}
	}
	@Override
	protected void onPostExecute(Long result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		if(isCancelled())
			return;
		tv.setText("下载完成");
		new ZipExtractorTask(mFile.getAbsolutePath(), Environment.getExternalStorageDirectory().toString(), view, true).execute();
//		((MainActivity)mContext).showUnzipDialog();
	}
	private long download(){
		URLConnection connection = null;
		int bytesCopied = 0;
		try {
			connection = mUrl.openConnection();
			int length = connection.getContentLength();
			if(mFile.exists()&&length == mFile.length()){
				Log.d(TAG, "file "+mFile.getName()+" already exits!!");
				return 0l;
			}
			mOutputStream = new ProgressReportingOutputStream(mFile);
			publishProgress(0,length);
			bytesCopied =copy(connection.getInputStream(),mOutputStream);
			if(bytesCopied!=length&&length!=-1){
				Log.e(TAG, "Download incomplete bytesCopied="+bytesCopied+", length"+length);
			}
			mOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytesCopied;
	}
	private int copy(InputStream input, OutputStream output){
		byte[] buffer = new byte[1024*8];
		BufferedInputStream in = new BufferedInputStream(input, 1024*8);
		BufferedOutputStream out  = new BufferedOutputStream(output, 1024*8);
		int count =0,n=0;
		try {
			while((n=in.read(buffer, 0, 1024*8))!=-1){
				out.write(buffer, 0, n);
				count+=n;
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}
	private final class ProgressReportingOutputStream extends FileOutputStream{
		public ProgressReportingOutputStream(File file)
				throws FileNotFoundException {
			super(file);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void write(byte[] buffer, int byteOffset, int byteCount)
				throws IOException {
			// TODO Auto-generated method stub
			super.write(buffer, byteOffset, byteCount);
			mProgress += byteCount;
			publishProgress(mProgress);
		}

	}
}