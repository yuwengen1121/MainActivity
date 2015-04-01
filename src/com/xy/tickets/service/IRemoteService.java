package com.xy.tickets.service;

public interface IRemoteService {
	
	public void setBaseUrl(String url);
	public String getBaseUrl();
	
	/**********User Module****************/
	
	public void setSid(String sid) ;
	public String getSid();
	
}
