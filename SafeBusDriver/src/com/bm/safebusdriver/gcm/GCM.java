package com.bm.safebusdriver.gcm;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.bm.safebusdriver.SafeBusChoferMainActivity;
import com.bm.safebusdriver.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCM {

	private Activity activity;
	    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	    static final String TAG = "GCMDemo";
	    private String regid;
	    String SENDER_ID = "849687871210";
	    String URL = "MY_SERVER_URL";
	 	public static final String EXTRA_MESSAGE = "message";
	    public static final String PROPERTY_REG_ID = "registration_id";


	    AtomicInteger msgId = new AtomicInteger();
	    SharedPreferences prefs;
	    Context context;
	
	public GCM(Activity activity){
		this.activity=activity;
		
	}
	
	
	/********************************************GCM****************************************/
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	public boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            activity.finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	public void registerInBackground(final GoogleCloudMessaging gcm) {
		

	    
		  new AsyncTask<String, String, String>() {
			  GoogleCloudMessaging gcm_;
			  ProgressDialog pd;
			  
	    	@Override
			protected void onPreExecute() {
	    	pd=	Utils.anillo(activity, pd);
	    	pd.show();   
	    		 if (gcm == null) {
	    			 gcm_ = GoogleCloudMessaging.getInstance(activity);
	                }else{
	                 gcm_=gcm;
	                }
				super.onPreExecute();
			}
	    	

			@Override
	        protected String doInBackground(String... params) {
	            String msg = "";
	            try {
	               
	                regid = gcm_.register(SENDER_ID);
	                msg = regid;
              
	               
	            } catch (IOException ex) {
	                msg = "Error";
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	        	if(!msg.equals("Error")){
	        		 new   Utils(activity).setPreferenciasGCM(msg);
	        		 if(true){
	        			 if(pd!=null)
	     	        		pd.dismiss();
	        			 
	        			 activity.startActivity(new Intent(activity,SafeBusChoferMainActivity.class));
				         activity.finish();
	        		 }
			         

	        	}
	        	if(pd!=null)
	        		pd.dismiss();
	         
	        }
	        
	    }.execute(null, null, null);
	    
	}
	

	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	public static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	
	
	
	
	
	
	
}
