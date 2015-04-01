package com.xy.tickets.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.umeng.analytics.MobclickAgent;
import com.xy.tickets.MainActivity;
import com.xy.tickets.R;


/**
 * Created by chenyu on 14-5-17.
 */
public class WebFragment extends Fragment implements Refreshable {

    public final static String LOGTAG = "WebFragment";

    private String title;
    private String url;

    public static WebFragment newInstance(String title, String url){
        WebFragment frag = new WebFragment();
        frag.url = url;
        frag.title = title;

        return frag;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(LOGTAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_web, container,
                false);

        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.loadUrl(url);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(LOGTAG);
    }

    @Override
    public void refreshTitle(Activity activity) {
        ((MainActivity)activity).setRoot(false);
        ((MainActivity)activity).setTitleTxt(title);
        ((MainActivity)activity).setTitleBtn(true, false, null, null);

    }

}
