package com.music.ui.fragment;

public class BusEvent<T> {
	private T msg;
	public BusEvent(T t){
		this.setMsg(t);
	}
	public T getMsg() {
		return msg;
	}
	public void setMsg(T msg) {
		this.msg = msg;
	}
}
