package com.xy.tickets.service;

public class MessageModel<T> {

	private boolean flag; // true - successful ; false - failed
	private int errorCode;
	private String message;
	private T data;
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
		if(data == null){
			message = "Sorry, no data yet.";
		}
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
