package com.bm.safebusdriver.mapa.bean;

import com.google.android.gms.maps.model.LatLng;

public class MapaBean {
	private LatLng punto;
	private String placa;
	private String ruta_id;
	private int bus_id;
	
	public LatLng getPunto() {
		return punto;
	}
	public void setPunto(LatLng punto) {
		this.punto = punto;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getRuta_id() {
		return ruta_id;
	}
	public void setRuta_id(String id) {
		this.ruta_id = id;
	}
	public int getBus_id() {
		return bus_id;
	}
	public void setBus_id(int bus_id) {
		this.bus_id = bus_id;
	}
	
	
}
