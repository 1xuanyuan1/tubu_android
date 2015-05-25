package com.qms.tubu.bean;

import java.io.Serializable;

public class AccountBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2615345412545693578L;
	
	private String id;
	private String nickname;
	private String phone;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
