package com.xy.tickets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import com.umeng.update.UmengUpdateAgent;
import com.xy.tickets.fragment.ActivityResultable;
import com.xy.tickets.fragment.TicketListFragment;
import com.xy.tickets.util.Utils;

public class MainActivity extends BaseActivity {

	private final static String LOGTAG = "MainActivity";

	private TextView titleTxt;

	private ImageView watchBtn;

	private ImageView listBtn;

	private boolean isTwiceQuit;
	
	private boolean isRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);

		titleTxt = (TextView) findViewById(R.id.main_title_txt);

        watchBtn = (ImageView) findViewById(R.id.main_watch_btn);
        listBtn = (ImageView) findViewById(R.id.main_list_btn);

		loadMainScreen();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //auto update
//                UmengUpdateAgent.setUpdateOnlyWifi(false);
//                UmengUpdateAgent.setUpdateCheckConfig(false);
//                UmengUpdateAgent.update(MainActivity.this);

            }
        }, 500);

	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句

        //TODO:deal with push notification parameters for jump into other screen
    }

    public void loadMainScreen(){
        addFragment(new TicketListFragment(), TicketListFragment.LOGTAG, false);
    }


    public void setTitleTxt(int resId){
            titleTxt.setText(resId);
	}

	public void setTitleTxt(String title){
		titleTxt.setText(title);
	}

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public void setTitleBtn(boolean isShowBack, boolean isShowCity, Integer icon, OnClickListener listener){
        if(isShowBack){
            titleTxt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(
                    R.drawable.back_btn_bg),null,null,null);
            titleTxt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isRoot) {
                        backFragment(null);
                    }
                }
            });
        }else {
            titleTxt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(
                    R.drawable.icon),null,null,null);
            titleTxt.setOnClickListener(null);
        }

        if(icon != null){
            listBtn.setImageResource(icon);
            listBtn.setVisibility(View.VISIBLE);
        }else {
            listBtn.setVisibility(View.GONE);
        }

        if(listener != null){
            listBtn.setOnClickListener(listener);
        }else {
        }

	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment f = getCurrentFragment();
        Fragment f2 = getCurrentDialogFragment();
        if(f2 != null && f2 instanceof ActivityResultable){
            ((ActivityResultable)f2).onActivityResult(requestCode,resultCode,data);
        } else if(f != null && f instanceof ActivityResultable){
            ((ActivityResultable)f).onActivityResult(requestCode,resultCode,data);
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy(){
//        if(mBMapMan!=null){
//            mBMapMan.destroy();
//            mBMapMan=null;
//        }
        super.onDestroy();
    }
    @Override
    public void onPause(){
//        if(mBMapMan!=null){
//            mBMapMan.stop();
//        }
        super.onPause();


    }
    @Override
    public void onResume(){
//        if(mBMapMan!=null){
//            mBMapMan.start();
//        }
        super.onResume();


    }

    @Override
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
            if(isRoot) {
                if (isTwiceQuit) {


                    this.finish();
                } else {

                    Utils.toastMsg(this, R.string.sure_quit_app);
                    isTwiceQuit = true;

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            isTwiceQuit = false;

                        }
                    }, 2000);
                }
            }else{
                    backFragment(null);
                }
			return true;
		default:
			return false;
		}
	}

    private void installShortCut(String name) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean flag = prefs.getBoolean("shortcut", false);
        if (!flag) {
            // install shortcut
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

            // 显示的名字
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            // 显示的图标
            Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.icon);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

            // 不允许重复创建
            shortcut.putExtra("duplicate", false);

			/*
			 * 这个Intent是快捷方式所实现的功能 这里实现的是打10010电话的功能
			 */
            Intent intent = new Intent(this, StartActivity.class);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

            // 发送广播用以创建shortcut
            this.sendBroadcast(shortcut);

            // save flag

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("shortcut", true);
            editor.commit();

        }
    }
	

}
