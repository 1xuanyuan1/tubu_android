package com.qms.tubu.bean;

import java.io.Serializable;

public class PointBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6350180544972819140L;
	private String id;
	private String name;
	private double latitude;
	private double longitude;
	
	private int pic_x;
	private int pic_y;
	
	
	public int getPic_x() {
		return pic_x;
	}
	public void setPic_x(int pic_x) {
		this.pic_x = pic_x;
	}
	public int getPic_y() {
		return pic_y;
	}
	public void setPic_y(int pic_y) {
		this.pic_y = pic_y;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String toString(){
		return name+" x:"+pic_x+"   y:"+pic_y;
	}
}
