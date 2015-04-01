
package com.xy.tickets.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.com.xy.sms.util.ParseManager;
import com.xy.tickets.ApplicationController;
import com.xy.tickets.db.SMSMessageModel;

/**
 * 此类只有在指定的手机imei下才会启用。
 * 
 * @author 
 */
public class PopupReceiver extends BroadcastReceiver {

    static boolean isregister = false;

    private Handler handler;

    public static void registerPopupReceiver(Context ctx, Handler handler) {
        try {
            if (ctx != null && isregister == false) {

                isregister = true;
           	 Log.i("PopupAction", "registerPopupReceiver isregister:" + isregister);

               
                   

       		 Log.i("PopupAction", "registerPopupReceiver  start success");

            	
                PopupReceiver popupReceiver = new PopupReceiver();
                popupReceiver.handler = handler;
                ctx.registerReceiver(popupReceiver, new IntentFilter("cn.com.xy.douqu.popup"));
           	 Log.i("PopupAction", "registerPopupReceiver  end success");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent != null && "cn.com.xy.douqu.popup@acion".equals(intent.getAction())) {
            
            	 String number = intent.getStringExtra("phone");
                 String body = intent.getStringExtra("body");

                if(handler != null){
                    Map<String, Object> result = callApiToDistinguish(number, null, body, null);
                    if (result != null) {
                        SMSMessageModel sms = SMSMessageModel.getSMSMessageModel(number, body, new Date(), result);

                        if (sms != null) {
                            Message messsage = handler.obtainMessage();
                            messsage.what = 2;

                            messsage.arg1 = -1;
                            messsage.obj = sms;

                            handler.sendMessage(messsage);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PopupAction", " PopupReceiver smsMessage " + e.getMessage());
        }
    }

    private Map<String, Object> callApiToDistinguish(String phoneNum,String smsCenterNum,String msg,Map<String,String> extendMap){
        try {
            return ParseManager.parseMsgToMap(ApplicationController.getInstance().getApplicationContext(), phoneNum, smsCenterNum, msg, getMap());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, String> getMap() {
        Map map = new HashMap<String, String>();
        map.put("province", "广西");

        map.put("version", "20140601");
        map.put("opensms_enable", "true");   //是否启用打开短信原文        
        return map;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
