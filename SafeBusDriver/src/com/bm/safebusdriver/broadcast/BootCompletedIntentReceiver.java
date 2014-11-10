package com.bm.safebusdriver.broadcast;
import com.mikesaurio.modulolocalizacion.ServicioLocalizacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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