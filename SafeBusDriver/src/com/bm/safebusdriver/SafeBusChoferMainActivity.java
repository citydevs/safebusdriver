package com.bm.safebusdriver;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bm.safebusdriver.mapa.MapaTrackingActivity;
import com.bm.safebusdriver.registro.EditTextBackEvent;
import com.bm.safebusdriver.registro.bean.RegistroBean;
import com.bm.safebusdriver.servicio.ServicioLocalizacion;
import com.bm.safebusdriver.topchoferes.TopChoferesActivity;
import com.bm.safebusdriver.utils.Utils;
import com.mikesaurio.mensajesydialogos.Mensajes;

public class SafeBusChoferMainActivity extends Activity implements OnClickListener,OnItemSelectedListener {

	public Button btn_encuentra;
	public Button btn_registra;
	public Button btn_top5;
	private AlertDialog customDialog = null;
	private Menu menu;
	private Point p;
	String[] info;
	private EditTextBackEvent et_placa;
	private Spinner et_ruta;
	private EditTextBackEvent et_nombre;
	private int id_ruta_seleccionada;
	private Integer[] id_rutas;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		
		setContentView(R.layout.activity_safe_bus_chofer_main);
		
		
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
		


		validaBotones();
		
		
		 p = Utils.getTamanoPantalla(SafeBusChoferMainActivity.this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(p.x / 3,
				p.y / 4);

		btn_encuentra = (Button) findViewById(R.id.safebuschofer_btn_encuentra);
		btn_encuentra.setOnClickListener(this);
		btn_encuentra.setLayoutParams(lp);
		btn_registra = (Button) findViewById(R.id.safebuschofer_btn_registra);
		btn_registra.setOnClickListener(this);
		btn_registra.setLayoutParams(lp);
		btn_top5 = (Button) findViewById(R.id.safebuschofer_btn_top5);
		btn_top5.setOnClickListener(this);
		btn_top5.setLayoutParams(lp);
		
		
		
	

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safebuschofer_btn_encuentra:
			if (!Utils.hasInternet(SafeBusChoferMainActivity.this)) {
				Mensajes.showDialogGPSWiFi(SafeBusChoferMainActivity.this,
						Mensajes.FLAG_WIFI).show();
			} else if (!Utils.hasGPS(SafeBusChoferMainActivity.this)) {
				Mensajes.showDialogGPSWiFi(SafeBusChoferMainActivity.this,
						Mensajes.FLAG_GPS).show();
			} else {
				iniciarActividad(MapaTrackingActivity.class);
			}

			break;
		case R.id.safebuschofer_btn_registra:
			showDialogRegistroBus().show();
			
			break;
		case R.id.safebuschofer_btn_top5:
			startActivity(new Intent(SafeBusChoferMainActivity.this,TopChoferesActivity.class));

			break;

		
		default:
			break;
		}

	}

	/**
	 * 
	 * @param clase
	 */
	public void iniciarActividad(final Class<?> clase) {
		startActivity(new Intent(SafeBusChoferMainActivity.this, clase));
	}


	

	
	
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menuabouth:
	    	Mensajes.mostrarAercaDe(SafeBusChoferMainActivity.this).show();
	    	return true;
	    case R.id.menuadd:
	    	showDialogRegistroBus().show();
	    	return true;
	    case R.id.menuclose:
	    	info[0]= null;
	    	info[1]= null;
	    	new Utils(SafeBusChoferMainActivity.this).setPreferenciasChofer(info);
	    	validaBotones();
	    	invalidateOptionsMenu();
	    	return true;
	    default:
	    	return false;
	    }
	  } 
	
	
	
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 * 
	 * @param Activity
	 *            (actividad que llama al dialogo)
	 * @return Dialog (regresa el dialogo creado)
	 **/

	public Dialog showDialogRegistroBus() {

		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater()	.inflate(R.layout.activity_registro_chofer, null);
		builder.setView(view);
		builder.setCancelable(true);

		ArrayList<RegistroBean> arrayRutas= Utils.getRutasBuses();
		
		String[] rutas = new String[arrayRutas.size()];
		 id_rutas = new Integer[arrayRutas.size()];
		
		for (int i = 0; i < rutas.length; i++) {
			rutas[i]=arrayRutas.get(i).getName();
			id_rutas[i]=arrayRutas.get(i).getId();
		}
		
		
		 et_placa =(EditTextBackEvent)view.findViewById(R.id.registro_et_placa);
		 et_ruta =(Spinner)view.findViewById(R.id.spinner_rutas);
		 et_nombre =(EditTextBackEvent)view.findViewById(R.id.registro_et_nombre);
		 ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rutas);
				  adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				  et_ruta.setAdapter(adapter_state);
				  et_ruta.setOnItemSelectedListener(this);
		
		 
		 Button btn_aceptar = (Button)view.findViewById(R.id.registro_chofer_btn_aceptar);
		 btn_aceptar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(validaEditText()){
					new Utils(SafeBusChoferMainActivity.this).setPreferenciasChofer(new String[]{et_placa.getText().toString(), id_ruta_seleccionada+"",
							et_nombre.getText().toString(),
							Utils.getFechaHoy()+""});
					
					if(Utils.doHttpPostAltaChofer(SafeBusChoferMainActivity.this,"https://cryptic-peak-2139.herokuapp.com/buses")){
							validaBotones();
							invalidateOptionsMenu();
							customDialog.dismiss();
							Mensajes.Toast(SafeBusChoferMainActivity.this, "Información guardada", Toast.LENGTH_SHORT);	
							

					}
				}
			}

			
		});
		
		customDialog = builder.create();
		customDialog.setOnShowListener(new OnShowListener() {

		    @Override
		    public void onShow(DialogInterface dialog) {
		        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.showSoftInput(et_placa, InputMethodManager.SHOW_IMPLICIT);
		    }
		});
		return customDialog;
	}
	
	
	
	public boolean validaEditText() {
		if(et_placa.getText().toString().equals("")){
			et_placa.setError(getResources().getString(R.string.ruta_registro_vacio));
			return false;
		}else if(et_nombre.getText().toString().equals("")){
			et_nombre.setError(getResources().getString(R.string.ruta_registro_vacio));
			return false;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		if(customDialog!=null){
			customDialog.dismiss();
			customDialog=null;
		}else{
			super.onBackPressed();
		}
		
	}

	

	public void validaBotones() {
		info= new Utils(SafeBusChoferMainActivity.this).getPreferenciasChofer();
		if(info[0]!=null){
			((LinearLayout)findViewById(R.id.safebus_ll_encuentra)).setVisibility(LinearLayout.VISIBLE);
			((LinearLayout)findViewById(R.id.safebuschofer_ll_registra)).setVisibility(LinearLayout.GONE);
		}else{
			((LinearLayout)findViewById(R.id.safebus_ll_encuentra)).setVisibility(LinearLayout.GONE);
			((LinearLayout)findViewById(R.id.safebuschofer_ll_registra)).setVisibility(LinearLayout.VISIBLE);
		}
		
	}
	
	
	
	
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    info= new Utils(SafeBusChoferMainActivity.this).getPreferenciasChofer();
		if(info[0]!=null){
			inflater.inflate(R.menu.menu_main_cerrar, menu);
		}else{
			inflater.inflate(R.menu.menu_main_full, menu);
		}
	  this.menu=menu;
	    return true;
	  } 
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

        menu.clear(); 
        MenuInflater inflater = getMenuInflater();
	    info= new Utils(SafeBusChoferMainActivity.this).getPreferenciasChofer();
		if(info[0]!=null){
			inflater.inflate(R.menu.menu_main_cerrar, menu);
		}else{
			inflater.inflate(R.menu.menu_main_full, menu);
		}
	  this.menu=menu;
        return super.onPrepareOptionsMenu(menu);
    }

	@Override
	protected void onResume() {
		validaBotones();
		super.onResume();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		et_ruta.setSelection(position);
		id_ruta_seleccionada =id_rutas[position];
		//ruta_seleccionada = (String) et_ruta.getSelectedItem();
	
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		et_ruta.setSelection(0);
		id_ruta_seleccionada =id_rutas[0];
		//ruta_seleccionada = (String) et_ruta.getSelectedItem();
		
	}
	
	
	
	
}
	

	
