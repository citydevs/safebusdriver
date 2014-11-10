package com.bm.safebusdriver.broadcast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bm.safebusdriver.servicio.ServicioLocalizacion;

 /**
  * BroadcastReceiver el cual inicia el servicio si el telefono es apagado
  * @author mikesaurio
  *
  */
public class BootCompletedIntentReceiver extends BroadcastReceiver {
 @Override
 public void onReceive(Context context, Intent intent) {

      Intent myIntent = new Intent(context, ServicioLocalizacion.class);
      context.startService(myIntent);
      
      
    
 
 }
}