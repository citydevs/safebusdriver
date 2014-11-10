package com.bm.safebusdriver.instrucciones;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.safebusdriver.R;
import com.bm.safebusdriver.gcm.GCM;
import com.bm.safebusdriver.instrucciones.adaptadores.FragmentPagerAdapterDialog;
import com.bm.safebusdriver.instrucciones.adaptadores.ScreenSlidePageFragmentDialog;
import com.bm.safebusdriver.utils.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * Paginador que muestra el instructivo de la app
 * @author mikesaurio
 *
 */
public class PaginadorInstrucciones extends FragmentActivity  implements  OnClickListener,OnPageChangeListener{
	
	private ViewPager pager = null;
	private ImageView btn_siguiente;
	//GCM
	private GoogleCloudMessaging gcm;
	private GCM mGCM;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.setContentView(R.layout.paginador_activity);

	
			
		pager = (ViewPager)findViewById(R.id.pager_dialog);
		pager.setOffscreenPageLimit(4);
		
		/*Creamos las paginas*/
		FragmentPagerAdapterDialog adapter = new FragmentPagerAdapterDialog(getSupportFragmentManager());
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(com.mikesaurio.mensajesydialogos.R.color.color_vivos), 1,PaginadorInstrucciones.this));
		
		
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		
		
		btn_siguiente =(ImageView)findViewById(R.id.instrucciones_btn_siguiente); 
		Point p = Utils.getTamanoPantalla(PaginadorInstrucciones.this); //tama�o de pantalla
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(p.x / 2, p.y / 3);
		btn_siguiente.setLayoutParams(lp);
		btn_siguiente.setOnClickListener(this);
		
	
		
	}
	
       
	  
	
	@Override
	protected void onDestroy() {
		pager=null;
		super.onDestroy();
	}






	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.instrucciones_btn_siguiente:
				
			//activar GSM 
			 mGCM= new GCM(PaginadorInstrucciones.this);
			  if (mGCM.checkPlayServices()) {
		            gcm = GoogleCloudMessaging.getInstance(this);
		            mGCM.registerInBackground(gcm);       
		        }
			break;
		default:
			break;
		}
		
	}




	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	@Override
	public void onPageSelected(int index) {

		if(index==0){
			btn_siguiente.setImageResource(R.drawable.boton_siguiente_selector);
		}else if(index==1){
			btn_siguiente.setImageResource(R.drawable.boton_entiendo_selector);
		}
	}

	// You need to do the Play Services APK check here too.
			@Override
			protected void onResume() {
			    super.onResume();
			    if(mGCM!=null)
			    	mGCM.checkPlayServices();
			}
			
			
			


	
}