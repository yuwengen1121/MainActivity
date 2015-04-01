package com.xy.tickets.dto;

public class Msginfo {
	public String phone;  //短信接入码
	public String content;//短信内容
	public String smsCenterNum;//接收短信的短信中心号码
	public String scendId;
	public Msginfo(String phone, String content, String smsCenterNum, String scendId){
		this.phone = phone;
		this.content = content;
		this.smsCenterNum=smsCenterNum;
		this.scendId=scendId;
	}
	@Override
	public String toString() {
		return "Msginfo [phone=" + phone + ", content=" + content
				+ ", smsCenterNum=" + smsCenterNum + ", scendId=" + scendId
				+ "]";
	}

	 
}
