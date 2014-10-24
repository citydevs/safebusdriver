package com.bm.safebusdriver.instrucciones.adaptadores;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.safebusdriver.R;
import com.bm.safebusdriver.instrucciones.paginas.PaginaUnoGuia;

/**
 * 
 * @author amatellanes
 * 
 */
public class ScreenSlidePageFragmentDialog extends Fragment   {

	/**
	 * Key to insert the background color into the mapping of a Bundle.
	 */
	private static final String BACKGROUND_COLOR = "color";

	/**
	 * Key to insert the index page into the mapping of a Bundle.
	 */
	private static final String INDEX = "index";

	private int color;
	private int index;
	static Activity actividad;
	/**
	 * Instances a new fragment with a background color and an index page.
	 * 
	 * @param color
	 *            background color
	 * @param index
	 *            index page
	 * @return a new page
	 */
	public static ScreenSlidePageFragmentDialog newInstance(int color, int index,Activity act) {

		// Instantiate a new fragment
		ScreenSlidePageFragmentDialog fragment = new ScreenSlidePageFragmentDialog();
		actividad=act;
		// Save the parameters
		Bundle bundle = new Bundle();
		bundle.putInt(BACKGROUND_COLOR, color);
		bundle.putInt(INDEX, index);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);

		return fragment;

	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Load parameters when the initial creation of the fragment is done
		this.color = (getArguments() != null) ? getArguments().getInt(BACKGROUND_COLOR) : Color.WHITE;
		this.index = (getArguments() != null) ? getArguments().getInt(INDEX): -1;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		if(index==1){
			PaginaUnoGuia paginaPrueba=new PaginaUnoGuia(actividad);
			paginaPrueba.init();
			return paginaPrueba.getView();
		}else{
			ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
			TextView tvIndex = (TextView) rootView.findViewById(R.id.tvIndex);
			tvIndex.setText(getResources().getString(R.string.app_name));
			rootView.setBackgroundColor(this.color);
			return rootView;
		}

	}






	
}
