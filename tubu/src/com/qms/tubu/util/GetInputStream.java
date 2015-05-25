package com.qms.tubu.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetInputStream {
//	INSTANCE;
	public static GetInputStream getInstance(){
		return new GetInputStream();
	}
	private GetInputStream (){};
	
	 
	public InputStream getInputStream(String urlPath,String data) throws IOException, Exception{
		System.out.println("________________________url  "+urlPath);
		System.out.println("________________________data  "+data);
		 URL url = new URL(urlPath);
		 HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();   
		 httpurlconnection.setConnectTimeout(20*1000);
	     httpurlconnection.setReadTimeout(20*1000);
	     if(null!=data){
	    	 httpurlconnection.setDoInput(true);   
	         httpurlconnection.setDoOutput(true);   
	         httpurlconnection.setRequestMethod("POST");
	     	 httpurlconnection.getOutputStream().write(data.getBytes());
	         httpurlconnection.getOutputStream().flush();   
	         httpurlconnection.getOutputStream().close();  
	     }
         int code = httpurlconnection.getResponseCode();  
         if (code == 200) { 
        	return httpurlconnection.getInputStream();
         }
	     return null;
	}

}
