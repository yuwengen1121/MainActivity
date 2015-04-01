package com.xy.tickets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_page);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startMainActivity();
			}
		}, 1500);

	}
	
	public void startMainActivity(){
		Intent in=new Intent(this,MainActivity.class);
		startActivity(in);
		finish();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
