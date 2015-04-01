package com.xy.tickets.fragment;

import android.content.Intent;

/**
 * Created by chenyu on 14-3-13.
 */
public interface ActivityResultable {

    public void onActivityResult(int requestCode, int resultCode, Intent data);

}
