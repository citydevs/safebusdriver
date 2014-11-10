package com.bm.safebusdriver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;

import com.bm.safebusdriver.R;
import com.bm.safebusdriver.gcm.UserInfo;
import com.bm.safebusdriver.servicio.ServicioLocalizacion;

public class Utils {

	Context activity;

	public Utils(Context context) {
		this.activity = context;

	}

	public void setPreferenciasGCM(String gcm) {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBusChofer", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("gcm", gcm);
		editor.commit();
	}

	public String getPreferenciasGCM() {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBusChofer", Context.MODE_PRIVATE);
		return prefs.getString("gcm", null);
	}
	
	public void setPreferenciasChofer(String[] info) {
		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBusChofer", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("placa", info[0]);
		editor.putString("ruta", info[1]);
		editor.putString("nombre", info[2]);
		editor.putString("fecha_sesion", info[3]);
		editor.commit();
	}

	public  String[] getPreferenciasChofer() {

		SharedPreferences prefs = activity.getSharedPreferences("PreferenciasSafeBusChofer", Context.MODE_PRIVATE);
		String[] info = new String[4];
		info[0]=prefs.getString("placa", null);
		info[1]=prefs.getString("ruta", null);
		info[2]=prefs.getString("nombre", null);
		info[3]=prefs.getString("fecha_sesion", "0");
		return info;
	}
	
	/**
	 * obtener los milisegundos de una fecha
	 * @return
	 */
	public static long getFechaHoy(){
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	String fechaCel= now.get(Calendar.DAY_OF_MONTH)+"/"+((now.get(Calendar.MONTH))+1)+"/"+now.get(Calendar.YEAR)+
				" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
    	try {
			return  (formatter.parse(fechaCel)).getTime();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	/**
	 * metodo que vaida que el telefono tenga internet
	 * 
	 * @param a
	 * @return
	 */
	public static boolean hasInternet(Activity a) {
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("wifi"))
				if (ni.isConnected())
					hasConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("mobile"))
				if (ni.isConnected())
					hasConnectedMobile = true;
		}
		return hasConnectedWifi || hasConnectedMobile;
	}
	
	/**
	 * metodo que vaida que el telefono tenga GPS encendido
	 * @param a
	 * @return
	 */
	public static boolean  hasGPS(Activity a){
		LocationManager mLocationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		}
		return false;
	}
	
	/**
	 * metodo que hace la conexion al servidor con una url especifica
	 * 
	 * @param url
	 *            (String) ruta del web service
	 * @return (String) resultado del service
	 */
	public static String doHttpConnection(String url) {
		HttpClient Client = new DefaultHttpClient();
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpGet httpget = new HttpGet(url);
			HttpResponse hhrpResponse = Client.execute(httpget);
			HttpEntity httpentiti = hhrpResponse.getEntity();
			// Log.d("RETURN HTTPCLIENT", EntityUtils.toString(httpentiti));
			return EntityUtils.toString(httpentiti);
		} catch (ParseException e) {
			
			return null;
		} catch (IOException e) {
			
			return null;
		}
	}

	public static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }   
	
	
	public static boolean doHttpPostAltaChofer(Activity act,String url){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		 HttpParams myParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		    HttpConnectionParams.setSoTimeout(myParams, 10000);
		    HttpClient httpclient = new DefaultHttpClient(myParams );
		    try { 
		    	
		    	String s[]= new Utils(act).getPreferenciasChofer();
		    	
		    JSONObject json = new JSONObject();
		    JSONObject manJson = new JSONObject();
		    manJson.put("email", UserInfo.getEmail(act));
		    manJson.put("reg_id", new Utils(act).getPreferenciasGCM());
		    manJson.put("placa", s[0]);
		    manJson.put("route_id", Integer.parseInt(s[1]));
		    manJson.put("name", s[2]);
		    manJson.put("device", "android");
		    json.put("bus",manJson);
		    
		        HttpPost httppost = new HttpPost(url.toString());
		        httppost.setHeader("Content-type", "application/json");

		        StringEntity se = new StringEntity(json.toString()); 
		        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		        httppost.setEntity(se); 

		        HttpResponse response = httpclient.execute(httppost);
		        String temp = EntityUtils.toString(response.getEntity());

		return true;
		} catch (ClientProtocolException e) {
		e.printStackTrace();
		return false;
		} catch (IOException e) {
		e.printStackTrace();
		return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public static boolean doHttpPostCoordenadasChofer(ServicioLocalizacion servicioLocalizacion,String url, double latitud, double longitud){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		 HttpParams myParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		    HttpConnectionParams.setSoTimeout(myParams, 10000);
		    HttpClient httpclient = new DefaultHttpClient(myParams );
		    try { 
		    	
		    	String s[]= new Utils(servicioLocalizacion).getPreferenciasChofer();
		    	
		    JSONObject json = new JSONObject();
		    JSONObject manJson = new JSONObject();
		    manJson.put("lat", latitud+"");
		    manJson.put("lng", longitud+"");
		    manJson.put("placa", s[0]);
		    json.put("location",manJson);
		    
		        HttpPost httppost = new HttpPost(url.toString());
		        httppost.setHeader("Content-type", "application/json");

		        StringEntity se = new StringEntity(json.toString()); 
		        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		        httppost.setEntity(se); 

		        HttpResponse response = httpclient.execute(httppost);
		        String temp = EntityUtils.toString(response.getEntity());

		return true;
		} catch (ClientProtocolException e) {
		e.printStackTrace();
		return false;
		} catch (IOException e) {
		e.printStackTrace();
		return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	
	/**
	 * obtienes el tama�o de pantalla
	 * @param (activity) Activity
	 * @return (Point) .x = width
	 * 					.y = height 
	 */
		public static Point getTamanoPantalla(Activity activity){
			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			 display.getSize(size);
			int width = size.x;
			int height = size.y;
			return (new Point (width,height));
		}
		/**
		 * dialogo de espera
		 */
		public static ProgressDialog anillo(Activity activity, ProgressDialog pDialog){
			pDialog = new ProgressDialog(activity);
	 		pDialog.setCanceledOnTouchOutside(false);
	 		pDialog.setMessage(activity.getString(R.string.espere));
	 		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	 		pDialog.setCancelable(false);
	 		return pDialog;

		}
		
		

}
