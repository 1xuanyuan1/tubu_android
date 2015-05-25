package com.qms.tubu.bean;

import java.io.Serializable;
import java.util.List;

public class ScenicBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5036712329013079286L;
	/**
	 * name:景区名字
	 * latitude:经度
	 * longitude:纬度
	 * distance:距离
	 * sensor:角度
	 * subtitle:小标题
	 * phone:电话
	 * price:人均消费
	 * address:地址
	 */
	private String id;
	private String name;
	private double latitude;
	private double longitude;
	private double distance;
	private double sensor;
	private String image_url;
	
	private String subtitle;
	private String phone;
	private double price;
	private String address;
	private List<TicketBean> tickets;
	
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	private int image;
	private String intro;
	public ScenicBean(){
		
	}
	public ScenicBean(double latitude,double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
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
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getSensor() {
		return sensor;
	}
	public void setSensor(double sensor) {
		this.sensor = sensor;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public List<TicketBean> getTickets() {
		return tickets;
	}
	public void setTickets(List<TicketBean> tickets) {
		this.tickets = tickets;
	}
	
}
