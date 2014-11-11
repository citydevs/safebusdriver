package com.bm.safebusdriver.servicio;

import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bm.safebusdriver.R;
import com.bm.safebusdriver.utils.Utils;

/**
 * Servicio que traquea por medio de GPS
 * @author mikesaurio
 *
 */
public class ServicioLocalizacion extends Service implements Runnable {
	
	/*
	 * Declaración de variables
	 */
	
	public final String TAG = this.getClass().getSimpleName();

	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;
	public static double latitud_inicial = 19.0f;
	public static double longitud_inicial = -99.0f;
	public static double latitud =0;
	public static double longitud=0;
	public static String horaInicio;
	public static String horaFin;
	private Location currentLocation = null;
	private Thread thread;
	ArrayList<String> pointsLat = new ArrayList<String>();
	ArrayList<String> pointsLon = new ArrayList<String>();
	public static boolean countTimer = true;
	public static  boolean panicoActivado = false;
	public boolean isSendMesagge= false;
    private final int INTERVALO_LOCALIZACION =10000;
    private final int DISTANCIA_LOCALIZACION =0;
 
  
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("*****************", "creado");
		 
		 //escucha para la location 
		mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}
	


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		obtenerSenalGPS();
		return super.onStartCommand(intent, flags, startId);
	}



	@Override
	public void onDestroy() {
		  super.onDestroy();
		if (mLocationManager != null)
			if (mLocationListener != null){
				mLocationManager.removeUpdates(mLocationListener);
				mLocationManager=null;
			}
	
		Log.d("*****************", "destruido");
	}

	@Override
	public IBinder onBind(Intent intencion) {
		return null;
	}


	/**
	 * handler
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// mLocationManager.removeUpdates(mLocationListener);
			updateLocation(currentLocation);
		}
	};

	/**
	 * metodo para actualizar la localizaci�n
	 * 
	 * @param currentLocation
	 * @return void
	 */
	public void updateLocation(Location currentLocation) {
		if (currentLocation != null) {
			latitud = Double.parseDouble(currentLocation.getLatitude() + "");
			longitud = Double.parseDouble(currentLocation.getLongitude() + "");

			pointsLat.add(latitud + "");
			pointsLon.add(longitud + "");
			Log.d("*****************", "enviando ubicacion");
			
			
			
			Utils.doHttpPostCoordenadasChofer(ServicioLocalizacion.this, "https://cryptic-peak-2139.herokuapp.com/locations",latitud,longitud);
			
			
			
			//mandamos la ubicacion del servicio a una actividad
			Intent intent = new Intent("key");
			intent.putExtra("latitud", pointsLat);
			intent.putExtra("longitud", pointsLon);
			getApplicationContext().sendBroadcast(intent);

		}
	}


	/**
	 * Hilo de la aplicacion para cargar las cordenadas del usuario
	 */
	public void run() {
		
		if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Looper.prepare();
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVALO_LOCALIZACION, DISTANCIA_LOCALIZACION, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();
		} else {
			Toast.makeText(ServicioLocalizacion.this,getResources().getString(R.string.servicio_de_localizacion_gps_off),Toast.LENGTH_SHORT).show();
			
		}
	}

	
	
	/**
	 * Metodo para Obtener la señal del GPS
	 */
	private void obtenerSenalGPS() {
		thread = new Thread(this);
		thread.start();
	}
	

	/**
	 *  SET Metodo para asignar las cordenadas del usuario
	 * */
	private void setCurrentLocation(Location loc) {
		currentLocation = loc;
	}

	/**
	 * Metodo para obtener las cordenadas del GPS
	 */
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			if (loc != null) {
				setCurrentLocation(loc);
				handler.sendEmptyMessage(0);
			}
		}

		/**
		 * metodo que revisa si el GPS esta apagado
		 */
		public void onProviderDisabled(String provider) {
			Toast.makeText(ServicioLocalizacion.this,getResources().getString(R.string.servicio_de_localizacion_gps_off),Toast.LENGTH_SHORT).show();
		}

		// @Override
		public void onProviderEnabled(String provider) {
		}

		// @Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
  
   

		
}
    
