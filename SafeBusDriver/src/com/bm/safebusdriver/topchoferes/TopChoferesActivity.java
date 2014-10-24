package com.bm.safebusdriver.topchoferes;

import com.bm.safebusdriver.R;
import com.bm.safebusdriver.R.layout;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class TopChoferesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_top_choferes);
		
		
		
	}
}
