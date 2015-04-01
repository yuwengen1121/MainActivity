package com.xy.tickets.service;


import java.util.Date;

public class RemoteServiceImpl implements IRemoteService {

	private static final String LOG_TAG = "ServiceImpl";
	public static final String TOKEN_TAG = "$token$";
	
	public static int RETURN_CODE_SUCCESS = 0;

	private static IRemoteService service;

	private String base_url;
	private String tokenId = "";
	private long tokenExpirationTime;
	private String sid = "";

	private RemoteServiceImpl(String url) {
		this.base_url = url;
	}

	public static IRemoteService getInstance() {
		if (service == null) {

			 service = new RemoteServiceImpl("http://emenu.xjtsoft.com");
//			 service = new RemoteServiceImpl("http://192.168.1.112:9000");
		}
		return service;
	}

	public boolean isLogin() {
		return tokenId != null && tokenId.length() > 0 && tokenExpirationTime > new Date().getTime();
	}
	
	@Override
	public void setBaseUrl(String url) {
		this.base_url = url;

	}

	@Override
	public String getBaseUrl() {
		return this.base_url;
	}

	@Override
	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return sid;
	}

}
