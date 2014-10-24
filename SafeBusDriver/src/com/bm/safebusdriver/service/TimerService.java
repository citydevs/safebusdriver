package com.bm.safebusdriver.service;

import java.util.Timer;
import java.util.TimerTask;

import com.bm.safebusdriver.utils.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class TimerService extends Service
{
    private static Timer timer = new Timer(); 

    public IBinder onBind(Intent arg0) 
    {
          return null;
    }

    public void onCreate() 
    {
          super.onCreate();
          startService();
    }

    private void startService()
    {           
        timer.scheduleAtFixedRate(new mainTask(), 43200000, 43200000);
    }

    private class mainTask extends TimerTask
    { 
        public void run() 
        {
            toastHandler.sendEmptyMessage(0);
        }
    }    

    public void onDestroy() 
    {
    	timer.cancel();
         super.onDestroy();
    }

    public final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
        	new Utils(getApplicationContext()).setPreferenciasChofer(new String[]{null,null});
        	TimerService.this.stopSelf();
        }
    };    
}