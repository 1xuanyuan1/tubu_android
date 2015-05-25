package com.qms.tubu.bean;

import java.io.Serializable;

public class OrderBean implements Serializable{

	/**
	 * 订单
	 */
	private static final long serialVersionUID = 8431656797568928831L;
	private String id;
	private int num;
	private String ticket_name;
	private double ticket_price;
	private String ticket_image;
	private int type;
	private String createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getTicket_name() {
		return ticket_name;
	}
	public void setTicket_name(String ticket_name) {
		this.ticket_name = ticket_name;
	}
	public double getTicket_price() {
		return ticket_price;
	}
	public void setTicket_price(double ticket_price) {
		this.ticket_price = ticket_price;
	}
	public String getTicket_image() {
		return ticket_image;
	}
	public void setTicket_image(String ticket_image) {
		this.ticket_image = ticket_image;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
}
