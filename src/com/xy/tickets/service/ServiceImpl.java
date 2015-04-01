package com.xy.tickets.service;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.com.xy.sms.util.ParseManager;
import com.xy.tickets.ApplicationController;
import com.xy.tickets.db.SMSMessageModel;
import com.xy.tickets.dto.Msginfo;
import com.xy.tickets.util.DateUtils;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceImpl {

	private static final String LOG_TAG = "ServiceImpl";

	private Uri SMS_INBOX = Uri.parse("content://sms");

	private IDBService dbService;
	private IRemoteService remoteService;
	private FragmentActivity context;

    private String location = "";

    private boolean autoRegister = true;

	private static ServiceImpl instance;

	public static ServiceImpl getInstance(FragmentActivity context) {
		if (instance == null) {
			instance = new ServiceImpl(context);
		}
		if (instance.remoteService == null) {
			instance.remoteService = RemoteServiceImpl.getInstance();
		}
		return instance;
	}

	private ServiceImpl(FragmentActivity context) {
		this.context = context;
		this.dbService = DBServiceImpl.getInstance(context);
		this.remoteService = RemoteServiceImpl.getInstance();

		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String sid = manager.getDeviceId();

		this.remoteService.setSid(sid);

	}

	public void loadSMSFromInbox(final Handler handler) {
		final ContentResolver cr = context.getContentResolver();
		final String[] projection = new String[] { "body", "_id", "address", "person", "date", "type"};
		final String where = " date >  "
				+ (DateUtils.addDateDays(new Date(), -100)).getTime();

		PopupReceiver.registerPopupReceiver(context, handler);

		new Thread(new Runnable() {
			@Override
			public void run() {
				Cursor cur = cr.query(SMS_INBOX, new String[]{"count(*) as count"}, null, null, null);
				if(cur.moveToNext()){
					String total = cur.getString(cur.getColumnIndex("count"));
					Message messsage = handler.obtainMessage();
					messsage.what = 1;

					messsage.arg1 = Integer.valueOf(total);

					handler.sendMessage(messsage);
				}
				cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
				if (null == cur)
					return;
				int i = 0;
					while (cur.moveToNext()) {
						i++;
						String number = cur.getString(cur.getColumnIndex("address"));//手机号
						String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
						String body = cur.getString(cur.getColumnIndex("body"));
						String strDate = cur.getString(cur.getColumnIndex("date"));
						Date date = new Date(Long.parseLong(strDate));

						if (number == null) {
							String[] tmpStr = body.split("!!");
							if(tmpStr.length == 2){
								number = tmpStr[0];
								body = tmpStr[1];
							}
						}

						Map<String, Object> result = callApiToDistinguish(number, null, body, null);
						if (result != null) {
							SMSMessageModel sms = SMSMessageModel.getSMSMessageModel(number, body, date, result);

							if (sms != null) {
								Message messsage = handler.obtainMessage();
								messsage.what = 2;

								messsage.arg1 = i;
								messsage.obj = sms;
								// save apps into local db
								getDbService().updateSMSMessageModel(sms);

								handler.sendMessage(messsage);
							}
						}
						Message messsage = handler.obtainMessage();
						messsage.what = 3;

						messsage.arg1 = i;
						handler.sendMessage(messsage);
					}
					cur.close();
			}
		}).start();

	}

	/**
	 * 调用情景弹窗的api
	 * @param phoneNum 短信接入码 如:10086,10010，即你收到的短信的短信号码
	 * @param smsCenterNum 接收短信的短信中心号码
	 * @param msg 短消息内容
	 * @param extendMap 扩展参数
	 */
	private Map<String, Object> callApiToDistinguish(String phoneNum,String smsCenterNum,String msg,Map<String,String> extendMap){
		try {
			return ParseManager.parseMsgToMap(ApplicationController.getInstance().getApplicationContext(), phoneNum, smsCenterNum, msg, getMap());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 扩展参数
	 * @return
	 */
	public static Map<String, String> getMap() {
		Map<String ,String> map = new HashMap<String, String>();
		map.put("simIndex", "1");//接收短信的卡位,双卡时可用,用于后续回调使用
		map.put("msgId", "1");//短信的消息id,用于后续回调使用
		map.put("opensms_enable", "true");   //是否启用打开短信原文
		map.put("version", "20140601");

		return map;
	}

	public IDBService getDbService() {
		if (dbService == null) {
			dbService = DBServiceImpl.getInstance(context);
		}
		return dbService;
	}

	public void close() {
		if (dbService != null) {
			dbService.close();
			dbService = null;
		}
	}

	public void setContext(FragmentActivity context) {
		this.context = context;
	}

}
