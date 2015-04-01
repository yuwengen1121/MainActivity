package com.xy.tickets.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xy.tickets.R;
import com.xy.tickets.db.SMSMessageModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public final static String LOGTAG = "Utils";
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
	private static String imgDir = null;
	private static String shareType=null;

    public static void toastMsg(final Context context, final int resId,
                                final Object... args) {
    	if(context != null){
	        final String msg = context.getString(resId, args);
	        toastMsg(context, msg, args);
    	}
    }
	
	public static void toastMsg(final Context context, final String msg,
			final Object... args) {
		if(context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}

	}

    public static String generateID(){
        String res = UUID.randomUUID().toString();
        return res.substring(20);
    }


    public static void addStrikeLine(TextView textview){
        if(textview != null) {
            Paint paint = textview.getPaint();
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);
        }
    }

    public static String contractName(String name,int length){
        String res = "";
        if(name != null){
            if(name.length() > length){
                res = name.substring(0, length) + "…";
            }else{
            	res = name;
            }
        }
        return res;
    }

    public static SMSMessageModel getTrainSeatNoType(SMSMessageModel sms){
        return sms;
    }

    public static String contractName(String name){
    	return contractName( name,12);
    }

    public static Double[] convertMap2Loc(String map){
        Double[] res = new Double[]{108.363349, 22.826214};
        if(map != null){
            String pos[] = map.split(",");
            if(pos.length == 2){
                double latitude = Double.valueOf(pos[1]);
                double longitude = Double.valueOf(pos[0]);

                res[1] = latitude;
                res[0] = longitude;
            }
        }
        return res;
    }

    public static int[] convertMap2LocInt(String map){
        int[] res = new int[]{108363349,22826214};
        if(map != null){
            String pos[] = map.split(",");
            if(pos.length == 2){
                int longitude = (int)(Double.valueOf(pos[0])*1e6);
                int latitude = (int)(Double.valueOf(pos[1])*1e6);

                res[0] = longitude;
                res[1] = latitude;
            }
        }
        return res;
    }

    public static String removeNull(String str){
        return str==null?"":str;
    }



    /**
     * 生成以中心点为中心的四方形经纬度
     *
     * @param lat 纬度
     * @param lon 精度
     * @param raidus 半径（以米为单位）
     * @return
     */
    public static double[] getAroundLocation(double lon, double lat, int raidus) {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        return new double[] { minLat, minLng, maxLat, maxLng };
    }

    public static ArrayList<String> convertImages2List(String images){
        ArrayList<String> res = new ArrayList<String>();

        if(images == null) return res;

        String[] imgs = images.split(",");
        for(String img : imgs){
            res.add(img);
        }

        return res;
    }

    public static void sendSms(final Context context, final String number) {
        if(context != null){
            if(number != null && number.length() > 0){
                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("sms:" + number));
                context.startActivity(callIntent);
            }
        }

    }

    public static void callPhone(final Context context, final String number) {
        if(context != null){
            if(number != null && number.length() > 0){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                context.startActivity(callIntent);
            }
        }

    }
	
	public static String encodeUrl(final String param){
        if(param == null) return "";
		String res = param;
		try {
			res = URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return res;
	}
	
	public static String formatNumber(Double number){
        if((number - number.intValue()) > 0){
            NumberFormat formatter = new DecimalFormat("#.0");
            return formatter.format(number);
        }else {
            NumberFormat formatter = new DecimalFormat("#0");
            return formatter.format(number);
        }
		
	}

    public static String formatNumber(String number){
		NumberFormat formatter = new DecimalFormat("###,###,###");
		return formatter.format(Long.valueOf(number));

	}
	
	public static String formatNumber(int number){
		NumberFormat formatter = new DecimalFormat("###,###,###");
		return formatter.format(Long.valueOf(number));
		
	}
	
	public static String formatNumber(long number){
		NumberFormat formatter = new DecimalFormat("###,###,###");
		return formatter.format(Long.valueOf(number));
		
	}

	public static void savePref(Context context, String key, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getPref(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(key, null);

	}

    public static void clearCourses(Context context, Long corpId) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.remove(Constants.PREF_COURSES + corpId);
		editor.commit();
	}


    public static void savePref4Auto(Context context, String key, String value) {
        if(value == null) return;

        value = value.trim();
        String history = getPref4Auto(context, key);
        if(!history.contains(value)) {
            history = value + "," + history;
            savePref(context, key, history);
        }
	}

	public static String getPref4Auto(Context context, String key) {
        String res = getPref(context, key);
        return res!=null?res:"";

	}


    public static void save2card(Bitmap bitmap, String path) {
        try {
            // save to sdcard
            FileOutputStream fos = new FileOutputStream(new File(path));
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);

            // release all instances
            fos.flush();
            fos.close();

        } catch (Exception e) {
            Log.e(LOGTAG, "save2card:"
                    + ((e != null) ? e.getMessage() : "exception is null"));
        }

    }

	public static String convertUrl2Path(Context context, String url) {
		String path = hashKeyForDisk(url);
		path = getImgDir(context) + File.separator + path;
		return path;
	}

	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static String getImgDir(Context context) {
		if (imgDir == null) {
			File dir = getDiskCacheDir(context, "img");
			dir.mkdirs();
			imgDir = dir.getAbsolutePath();
		}
		return imgDir;
	}

	public static File getDiskCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(
				context).getPath()
				: context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir() && context.getExternalCacheDir() != null) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        File file = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static Integer convertStr2Level(String level){
        Integer res = null;
        if(level != null && level.length() == 1){

            res = Integer.valueOf(level);
            if(res == 0){
                res = null;
            }
        }
        return res;
    }

    public static boolean isNull(String str){
        if(str==null||str.trim().length()==0||str.equals("NULL")||str.equals("null")){
            return true;
        }
        return false;
    }

    public static boolean isNull(Object str){
        if(str==null){
            return true;
        }else if(str instanceof String){
            return isNull((String)str);
        }else {
            return false;
        }
    }

    public static Bitmap generateQRCode(String content){
        QRCodeWriter writer = new QRCodeWriter();
        try {
            EnumMap<EncodeHintType, Object> hint = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 1024, 1024, hint);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getContactNameByPhoneNumber(Context context, String number) {

        String name = null;
        String contactId = null;
//        InputStream input = null;

// define the columns I want the query to return
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

// encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

// query time
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

        try {
            if (cursor.moveToFirst()) {

                // Get values from contacts database:
                contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

                // Get photo of contactId as input stream:
//            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
//            input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);

                Log.v("ffnet", "Started uploadcontactphoto: Contact Found @ " + number);
                Log.v("ffnet", "Started uploadcontactphoto: Contact name  = " + name);
                Log.v("ffnet", "Started uploadcontactphoto: Contact id    = " + contactId);

                return name;

            } else {

                Log.v("ffnet", "Started uploadcontactphoto: Contact Not Found @ " + number);
                return ""; // contact not found

            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }
    }

	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (Utils.hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static String getKBSize(long size) {
		if (size < 1024) {
			return size + "KB";
		} else if (size >= 1024 && size < 1024 * 1024) {
			return size / 1024 + "MB";
		} else {
			return size / (1024 * 1024) + "GB";
		}
	}
	
	
	public static  Drawable getDrawable(Context context,String drawableName) throws NotFoundException {
		Drawable resultDrawable =null;
		try{
			 int resid=getIdentifier(context,drawableName.trim(),"drawable");
			 if(resid!= 0){
			   resultDrawable=context.getResources().getDrawable(resid);
			 }
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultDrawable;
	}
	
	public static int getIdentifier(Context context,String name,String defType) throws NotFoundException{
		int result=0;
		try{
		  result = context.getResources().getIdentifier(name, defType, context.getPackageName()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getPhoneImei(Context context){
		TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei=telephonyManager.getDeviceId();
		return "android:"+imei;
	}
	
	
	/**
	 * 是否是手机号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		boolean isTrue = false;
		String phone=getPhoneNumberNo86(phoneNumber);		
		if (!Utils.isNull(phone)) {
			String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";

			Pattern p = Pattern.compile(regExp);
			Matcher m = p.matcher(phone);
			if (m.find()) {
				isTrue = true;
			} else {

			}
		}
		return isTrue;
	}
	
	/**
	 * 去掉手机号前面的+86
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static final String MPLUG86 = "+86";
	private static final String phoneFiled86 = "+86";
	private static final String phoneFiled = "\\+86";
	private static final String phoneFont86 = "86";
	public static String getPhoneNumberNo86(String phoneNumber) {
		if (!Utils.isNull(phoneNumber)) {
			phoneNumber = phoneNumber.replace(" ", "").replace("-", "")
					.replace("(", "").replace(")", "");
			if (phoneNumber.startsWith(phoneFiled86)) {
				phoneNumber = phoneNumber.replaceFirst(phoneFiled, "");
			} else if (phoneNumber.startsWith(phoneFont86)) {
				phoneNumber = phoneNumber.replaceFirst(phoneFont86, "");
			}

		}
		return phoneNumber;
	}
	
	public static void showInput(Context contxt,EditText ed){
		InputMethodManager m = (InputMethodManager)contxt.getSystemService(Context.INPUT_METHOD_SERVICE);
		m.showSoftInput(ed, 0);
	}

	public static void hideInput(Context contxt,EditText ed){
		if(contxt!=null){
			InputMethodManager m = (InputMethodManager)contxt.getSystemService(Context.INPUT_METHOD_SERVICE);
			m.hideSoftInputFromWindow(ed.getWindowToken(), 0);
		}
	}
	
	/**
	 * 发送短信(用于设置默认)
	 *
	 * @param context
	 */
	public static void setDefaultSendSMS(Context context,Uri uri,String body) {
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);            
		it.putExtra("sms_body", body);            
		context.startActivity(it);  
	}
	
	public static String getShareType(Context context){
		if(shareType==null){
			
			if(context!=null){
				shareType= Utils.getPref(context, "share");
			}
			if(Utils.isNull(shareType)){
				shareType="未邀请";
			}
		}
		return shareType;
		
	}
	public static void setShareType(Context context,String str){
		if(context!=null){
			shareType=str;
			Utils.savePref(context, "share", str);
		}
	}
	
	public static String getSignDate(Context context){
		if(context!=null){
//			UserModel currentUser=((BaseActivity)context).getService().getCurrentUser();
//			if(currentUser!=null){
//				String phone=currentUser.getName();
				return Utils.getPref(context, "sign");
//			}
		}
		return null;
	}
	
	public static void setSignDate(Context context){
		if(context!=null){
//			UserModel currentUser=((BaseActivity)context).getService().getCurrentUser();
//			if(currentUser!=null){
//				String phone=currentUser.getName();
				Utils.savePref(context, "sign", DateUtils.fomateDay(System.currentTimeMillis()));
//			}
			
		}
	}

    public static void popupCoursesList(Context context, final View view){
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

//            params.height = LayoutParams.WRAP_CONTENT;
//            params.width = LayoutParams.WRAP_CONTENT;
//            params.format = PixelFormat.TRANSLUCENT;

        params.gravity = Gravity.CENTER_VERTICAL;

        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // 设置悬浮窗的Touch监听
        view.setOnTouchListener(new View.OnTouchListener()
        {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        wm.updateViewLayout(v, params);
                        break;
                }
                return true;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wm.addView(view, params);
            }
        }, 2000);

    }

    public static void removeCoursesList(Context context, View view){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        wm.removeViewImmediate(view);
    }
}
