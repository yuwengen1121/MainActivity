package com.xy.tickets.service;


import com.xy.tickets.db.SMSMessageModel;

import java.util.List;

public interface IDBService {

	public void close();
	
    public void updateSMSMessageModel(SMSMessageModel sms);

    public List<SMSMessageModel> getSMSMessageModel();


}
