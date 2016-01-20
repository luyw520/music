package com.music.bean;
public class UserBean {
	private String username;
	private String headPath="/sdcard/lu/img/head.jpg";
	public UserBean(String username, String passwrod) {
		super();
		this.username = username;
		this.passwrod = passwrod;
	}
	public UserBean(String username, String passwrod,String headPath) {
		this(username, passwrod);
		if(headPath!=null){
			this.headPath=headPath;
		}
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswrod() {
		return passwrod;
	}
	public void setPasswrod(String passwrod) {
		this.passwrod = passwrod;
	}
	public String getHeadPath() {
		return headPath;
	}
	public void setHeadPath(String headPath) {
		this.headPath = headPath;
	}
	private String passwrod;
}