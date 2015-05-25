package com.qms.tubu.bean;

import java.io.Serializable;

public class MapBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5799653845186732493L;
	
	public String id;
	public String scenic;
	public String url;
	public double fileSize;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScenic() {
		return scenic;
	}
	public void setScenic(String scenic) {
		this.scenic = scenic;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public double getFileSize() {
		return fileSize;
	}
	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}
}
