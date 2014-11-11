package com.bm.safebusdriver.splash;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bm.safebusdriver.R;
import com.bm.safebusdriver.SafeBusChoferMainActivity;
import com.bm.safebusdriver.instrucciones.PaginadorInstrucciones;
import com.bm.safebusdriver.servicio.ServicioLocalizacion;
import com.bm.safebusdriver.utils.Utils;

/**
 * Splash inicial
 * @author mikesaurio
 *
 */
public class SplashActivity extends Activity {

	private static final long SPLASH_SCREEN_DELAY = 3000; //tiempo que dura el splash
	private Utils utils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		utils = new Utils(SplashActivity.this);
		

		FrameLayout frame_splash = (FrameLayout) findViewById(R.id.frame_splash);

		Point p = Utils.getTamanoPantalla(SplashActivity.this); //tamaño de pantalla
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(p.x / 2, p.y / 3);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		frame_splash.setLayoutParams(lp);
		
		
		if (utils.getPreferenciasGCM()!=null) {//si ya se hacepto el tutorial
			if(utils.getPreferenciasChofer()[3]!=null){//si tiene una sesion iniciada
				long dia_guardado = Long.parseLong(utils.getPreferenciasChofer()[3]);//traemos el dia de la sesion 
				if(dia_guardado+86400000<=Utils.getFechaHoy()){
					utils.setPreferenciasChofer(new String[]{null,null,
							null,
							"0"});
				}
			}
			

			
			init(SafeBusChoferMainActivity.class);
			
			
			
		} else {
			init(PaginadorInstrucciones.class);

		}

	}

	/**
	 * hilo que al terminar el splash inicia una actividad
	 * @param clase
	 */
	public void init(final Class<?> clase) {

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(new Intent().setClass(SplashActivity.this, clase));
				finish();
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, SPLASH_SCREEN_DELAY);
	}

}