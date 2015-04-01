package com.xy.tickets;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.*;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import com.umeng.analytics.MobclickAgent;
import com.xy.tickets.fragment.Refreshable;
import com.xy.tickets.service.ServiceImpl;

public class BaseActivity extends FragmentActivity {

    private static final String LOGTAG = "BaseActivity";

    //Service instance for RPC or DB
    private ServiceImpl service;

    private final Object mClickLock = new Object();

    private InputMethodManager mInputMethodManager;

    private float densityMultiplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = ServiceImpl.getInstance(this);

        mInputMethodManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        MobclickAgent.updateOnlineConfig(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        densityMultiplier = metrics.density;

    }

    public void closeInputKeyboard() {
        if (getCurrentFocus() != null) {
            mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void addFragment(final Fragment f, String tag, boolean back) {
        synchronized (mClickLock) {
            final FragmentTransaction ft = getSupportFragmentManager().
                    beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (back) {
                ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
            } else {
                ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit, R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
            }
            ft.replace(R.id.pager, f, tag).addToBackStack(tag);
            ft.commitAllowingStateLoss();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (f instanceof Refreshable) {
                        ((Refreshable) f).refreshTitle(BaseActivity.this);
                    }

                }
            }, 100);

            mClickLock.notifyAll();
        }
    }

    public Fragment getActivityFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public void popupFragment(DialogFragment f) {
        synchronized (mClickLock) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(
                    "dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            f.show(ft, "dialog");

            mClickLock.notifyAll();
        }
    }

    public void clearFragment(final int count) {
        synchronized (mClickLock) {
            final FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
//		ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
            for (int i = 0; i < ((count > 0) ? count : fm.getBackStackEntryCount()); ++i) {
                fm.popBackStack();
            }
            ft.commit();
            mClickLock.notifyAll();
        }
    }

    public void backFragment(Fragment f) {
        synchronized (mClickLock) {
            final FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() < 2) {
                this.finish();
                return;
            }
                FragmentTransaction ft = fm.beginTransaction();
                fm.popBackStack();
                ft.commit();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Fragment f2 = fm.findFragmentById(R.id.pager);
                    if (f2 instanceof Refreshable) {
                        ((Refreshable) f2).refreshTitle(BaseActivity.this);
                    }

                }
            }, 100);

            mClickLock.notifyAll();
        }
    }

    public Fragment getCurrentFragment() {
        final FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentById(R.id.pager);

    }

    public Fragment getCurrentDialogFragment() {
        final FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentByTag("dialog");

    }

    public float getDensityMultiplier() {
        return densityMultiplier;
    }

    @Override
    protected void onResume() {
        super.onResume();

        service.setContext(this);

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        service.close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句

    }

    public ServiceImpl getService() {
        if (service == null) {
            service = ServiceImpl.getInstance(this);
        }
        return service;
    }

}
