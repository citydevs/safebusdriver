package com.bm.safebusdriver.mapa;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.safebusdriver.R;
import com.bm.safebusdriver.SafeBusChoferMainActivity;
import com.bm.safebusdriver.mapa.bean.MapaBean;
import com.bm.safebusdriver.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikesaurio.mensajesydialogos.Mensajes;
import com.mikesaurio.modulolocalizacion.ServicioLocalizacion;

public class MapaTrackingActivity extends Activity {
    private GoogleMap map;
  	private ProgressDialog pDialog;
//	private MarkerOptions marker_;
	private ArrayList<MapaBean>	mapaBeanArray= new ArrayList<MapaBean>();
	private boolean isFirstTime=true;
	private Menu menu;
	private String id_ubicacion= null;
	private ArrayList<String> pointsLat;
	private ArrayList<String> pointsLon;
	private  String[] info;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	setContentView(R.layout.activity_mapa_tracking);
	
	/*ActionBar*/
	ActionBar mActionBar = getActionBar();
	mActionBar.setDisplayShowHomeEnabled(false);
	mActionBar.setDisplayShowTitleEnabled(false);//new ColorDrawable(Color.WHITE)
	mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.marco));
	LayoutInflater mInflater = LayoutInflater.from(this);
	View mCustomView = mInflater.inflate(R.layout.action_bar_custome, null);
	mActionBar.setCustomView(mCustomView);
	mActionBar.setDisplayShowCustomEnabled(true);
	/**/
	
	
	
	//cargamos el mapa
	setUpMapIfNeeded();
	
	//iniciamos el servicio de localizacio
	startService(new Intent(MapaTrackingActivity.this,ServicioLocalizacion.class));
	
	ImageView	mapa_iv_back =(ImageView)findViewById(R.id.mapa_iv_back);
	mapa_iv_back.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			onBackPressed();
			
		}
	});
	
	ImageView iv_gps =(ImageView)findViewById(R.id._iv_gps);
	iv_gps.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			CameraPosition cameraPosition;
			cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(pointsLat.get(pointsLat.size()-1)),
					 Double.parseDouble(pointsLon.get(pointsLon.size()-1)))).zoom(16).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
		}
	});

	
	 	
}


public void setUpMapIfNeeded() {
	if (map == null) {
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		if (map != null) {
			if(setUpMap()) {
				initMap();
			}
		}
	}
}

public void initMap() {
	//iniciamos un anillo de espera
	pDialog=Mensajes.ringDialog(MapaTrackingActivity.this, getResources().getString(R.string.mapa_espera));
	pDialog.show();
	
	map.setMyLocationEnabled(false);//quitar circulo azul;
	map.setBuildingsEnabled(true);
	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	map.getUiSettings().setZoomControlsEnabled(true); //ZOOM
	map.getUiSettings().setCompassEnabled(true); //COMPASS
	map.getUiSettings().setZoomGesturesEnabled(true); //GESTURES ZOOM
	map.getUiSettings().setRotateGesturesEnabled(true); //ROTATE GESTURES
	map.getUiSettings().setScrollGesturesEnabled(true); //SCROLL GESTURES
	map.getUiSettings().setTiltGesturesEnabled(true); //TILT GESTURES
	map.getUiSettings().setZoomControlsEnabled(false);
}



public void actualizarMapa(LatLng latLng){
	if(Utils.hasInternet(MapaTrackingActivity.this)){
		buscarBuses();
		
		map.clear();
		MarkerOptions	marker_ = new MarkerOptions().position(latLng).title(getString(R.string.mapa_mi_ubicacion));
		marker_.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_bus));
		
		if(isFirstTime){
			CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			isFirstTime=false;
		}/*else{
			CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(map.getCameraPosition().zoom).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
		*/
		 

		Marker m = map.addMarker(marker_);
		id_ubicacion=m.getId();
		
		
		
		MarkerOptions[] markers= new MarkerOptions[mapaBeanArray.size()];
		
		for(int i=0;i<mapaBeanArray.size();i++){
			markers[i]=new MarkerOptions().position(mapaBeanArray.get(i).getPunto()).title(mapaBeanArray.get(i).getPlaca()+"@@"+mapaBeanArray.get(i).getRuta_id());
			markers[i].icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_bus_verde));
			map.addMarker(markers[i]);
		}
		
		
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {}});

		
		map.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) { 
            	
            	if(!marker.getId().toString().equals(id_ubicacion)){
            		ContextThemeWrapper cw = new ContextThemeWrapper(
                            getApplicationContext(), R.style.Transparent);
                    LayoutInflater inflater = (LayoutInflater) cw.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View v = inflater.inflate(R.layout.mapa_pupop_layout_compuesto, null);
                    String s[] = marker.getTitle().split("@@");
	                TextView   pupop_nombre = (TextView) v.findViewById(R.id.pupop_nombre);
	                pupop_nombre.setText("Ruta: "+s[1] );
	                TextView pupop_lugar = (TextView) v.findViewById(R.id.pupop_lugar);
	                pupop_lugar.setText("Placa: "+s[0]);
                    return v;
            	
        	}else{
        		ContextThemeWrapper cw = new ContextThemeWrapper( getApplicationContext(), R.style.Transparent);
                LayoutInflater inflater = (LayoutInflater) cw.getSystemService(LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.mapa_pupop_layout_simple, null);
                TextView   pupop_nombre = (TextView) v.findViewById(R.id.pupop_simple_nombre);
	              pupop_nombre.setText(getResources().getString(R.string.mapa_mi_ubicacion));
                return v;
        	}
            	 
            }           
            @Override
            public View getInfoContents(Marker marker) {
            	return null; 
            }
        });
		
		
		
					
		
	}
	
	
	
	if(pDialog!=null){
    	pDialog.dismiss();
    	}
}



private void buscarBuses() {
	mapaBeanArray.clear();
	
	String url= "http://cryptic-peak-2139.herokuapp.com/buses.json";
	 final String strigJson = new  Utils(MapaTrackingActivity.this).doHttpConnection(url);
	JSONArray jObj;
	try {
		jObj = new JSONArray(strigJson);
		for (int i=0; i < jObj.length(); i++) {
			MapaBean mapaBean = new MapaBean();
			    JSONObject obj = jObj.getJSONObject(i);
			    String placa = obj.getString("placa");
			    mapaBean.setPlaca(placa);
			    
			    	JSONObject subObj = obj.getJSONObject("route");
			    	String id = subObj.getString("id");
			    	mapaBean.setRuta_id(id);
			    	
			    	JSONArray jArr = obj.getJSONArray("locations");
			    //	for (int j=0; j < jArr.length(); j++) {
			    	    	JSONObject objs = jArr.getJSONObject(jArr.length()-1);
					    	 String Slat=   objs.getString("lat");
					    	 String Slng=   objs.getString("lng");
					    	 mapaBean.setPunto(new LatLng(Double.parseDouble(Slat), Double.parseDouble(Slng)));
			    	 
					    	 mapaBeanArray.add(mapaBean);

			    //	}

			    	

			}


	} catch (JSONException e) {
		e.printStackTrace();
	}
	
}


/**
 * revisa si el mapa esta 
 * @return (boolean) true si el mapa esta listo 
 */
public boolean setUpMap() {
	if (!checkReady()) {
        return false;
    } else {
    	return true;
    }
}

/**
 * revisa si el mapa esta listo
 * @return (boolea) si esta listo TRUE
 */
private boolean checkReady() {
    if (map == null) {
        return false;
    }
    return true;
}



/**
 * manejo de transmiciones
 */

private BroadcastReceiver onBroadcast = new BroadcastReceiver() {



	@Override
	public void onReceive(Context ctxt, Intent t) {

		 pointsLat = t.getStringArrayListExtra("latitud");
		 pointsLon = t.getStringArrayListExtra("longitud");
		 
		 actualizarMapa(new LatLng(Double.parseDouble(pointsLat.get(pointsLat.size()-1)),
				 Double.parseDouble(pointsLon.get(pointsLon.size()-1))));
		
	}
};


@Override
protected void onPause() {
	unregisterReceiver(onBroadcast);
	super.onPause();
}

@Override
protected void onResume() {
	registerReceiver(onBroadcast, new IntentFilter("key"));
	super.onResume();
}

 @Override
	protected void onDestroy() {
	if(pDialog!=null){
    	pDialog.dismiss();
    	}
	stopService(new Intent(MapaTrackingActivity.this,ServicioLocalizacion.class));
		super.onDestroy();
	}

 
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
   MenuInflater inflater = getMenuInflater();
   info= new Utils(MapaTrackingActivity.this).getPreferenciasChofer();
	if(info[0]!=null){
		inflater.inflate(R.menu.menu_main_cerrar, menu);
	}else{
		inflater.inflate(R.menu.menu_main_full, menu);
	}
 this.menu=menu;
   return true;
 } 






@Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.menuabouth:
    	Mensajes.mostrarAercaDe(MapaTrackingActivity.this).show();
    	return true;

    case R.id.menuclose:
    	info[0]= null;
    	info[1]= null;
    	new Utils(MapaTrackingActivity.this).setPreferenciasChofer(info);
    	finish();
    	return true;
    default:
    	return false;
    }
  } 
 


}
