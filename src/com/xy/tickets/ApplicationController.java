package com.xy.tickets;

import android.app.Application;
import cn.com.xy.sms.util.ParseManager;
import com.xy.tickets.service.PopupReceiver;

import java.util.HashMap;

public class ApplicationController extends Application {

    /**
     * Log or request TAG
     */
    public static final String TAG = "ApplicationController";

    private String location;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static ApplicationController sInstance;
    
    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;

        try {

            //extend初始化的扩展参数
            HashMap<String,String> extend=new HashMap<String,String>();
            extend.put("ONLINE_UPDATE_SDK", "0");//是否支持 在线升级sdk 0:不支持 1:支持,默认是支持
            extend.put("SUPPORT_NETWORK_TYPE", "0");//支持网络  0:不支持 1:wifi 2:3g及wifi
            //密钥
            extend.put("SECRETKEY", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANu74jdyz5gYUT0DIfEBY9dDcpAM1h5mqMp4k/+676nhG1OciqHih5t6exjJk9ij9o3+RyTD/pM3zGEcmGRgKerhbKQuiqiuK9PZzwvx0DdxGJMTrtluuoGweQINWW9forBJ77y6sFTnyGMvLIu31FZTdepY9bVpZhcj6MTm9K/lAgMBAAECgYBZ1eTU2qG5Aygjk6ZKzFCvb6nk7vqTj1EvBkEr6peVkq0DcBrVuLHMhTTSm4dQqShiQTWA+IiaU9PBWFmNoCtCVB3aQhS5NwQa+Kk2MxumrpEVKkRyZbS1jgn79kP9XI1tb2JbUtIr3V9ge2j0MeYte3XSi8+CsUjNFP5Ob8PlgQJBAPlbfF3jv+pLhRRSua5wXfjVaARGafHLVw34nAH6se6D7j/7hUOzlLAtbzFPLss6zwjRQ0oFPxJL9OuSbot4lXUCQQDhlmDRLXrVv9eQTeD7wZfAr9K6A76/60GdjooBNGYIguNRjnStkRXnvOkmRBhuJ01gW3wui23xo7GwyRwmwrKxAkB3lAu76q/p53VWHaW1pWUrIwvSoQHFVkHVDVejC1mwi8wBGtj5cnDnW6Jq/xHtm7IVBGVEZeetGofFQF+KurTRAkEAjMGDz4ENL+nv2v587xkwUu5iEebcUmu8GU8jFPVY5N6mb/DvJI1umZXp9BJxG+oyAXmT1obxJuD4slIJ2rWJQQJASB4g3Z/BSTARasNhg+d52SuYPokuQ8MXd5DWSBwMIjtbskGCZanU9Jr/M+44kccXrTGa460Dln8KfE/vk40YPw==");

            String channel="jE5vSv5QPIAO";//渠道
            String simIccid = "89860113859018840854";//默认的Iccid
            ParseManager.initSdk(this, channel, simIccid, true, true, extend);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {
        return sInstance;
    }

}