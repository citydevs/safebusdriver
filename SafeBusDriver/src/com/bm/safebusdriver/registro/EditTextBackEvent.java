package com.bm.safebusdriver.registro;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.bm.safebusdriver.R;
import com.mikesaurio.mensajesydialogos.Mensajes;

public class EditTextBackEvent extends EditText {

	private EditTextImeBackListener mOnImeBack;
	private Context context;
	private boolean countTimer= true;
	private Handler handler_time = new Handler();
	private int countStart=0;

	public EditTextBackEvent(Context context) {
		super(context);
		this.context=context;
	}

	public EditTextBackEvent(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	public EditTextBackEvent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
	  if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
		 
		  if (countStart >= 2) {
				countStart = 0;
				Activity act=(Activity)context;
				act.onBackPressed();
				
				
			} else {
				countStart += 1;
				if (countTimer) {
					Mensajes.Toast((Activity)context, getResources().getString(R.string.back_salir), Toast.LENGTH_SHORT);
					countTimer = false;
					handler_time.postDelayed(runnable, 10000);
				}
			}
	    return true;  
	  }
	  return super.dispatchKeyEvent(event);
	}

	public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
		mOnImeBack = listener;
	}

	public interface EditTextImeBackListener {
		public abstract void onImeBack(EditTextBackEvent ctrl, String text);
	}
	
	/**
	 * hilo que al pasar el tiempo reeinicia los valores
	 */
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			countTimer = true;
			countStart = 0;
		
		}
	};
}