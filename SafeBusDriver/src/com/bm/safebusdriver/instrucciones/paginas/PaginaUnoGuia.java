package com.bm.safebusdriver.instrucciones.paginas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.bm.safebusdriver.R;

/**
 * pagina que muestra en una lista los adeudos de un carro con las secretarias
 * 
 * @author mikesaurio
 *
 */
@SuppressLint("ViewConstructor")
public class PaginaUnoGuia extends View {

	
	private View view;
	private Activity context;

	
	
	public PaginaUnoGuia(Activity context) {
		super(context);
		this.context = context;
		init();
	}

	public PaginaUnoGuia(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public PaginaUnoGuia(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}


	
	/**
	 * init de la pagina
	 */
	public void init() {


		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.instrucciones_pag_uno, null);
		
		
		
	
		

	}


	/**
	 * GET view
	 * @return (view) vista inflada
	 */
	public View getView() {
		return view;
	}
	
	
	
	

	


}
