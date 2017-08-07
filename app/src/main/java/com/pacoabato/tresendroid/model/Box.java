package com.pacoabato.tresendroid.model;

import com.pacoabato.tresendroid.view.BoxDrawablesFactory;

import android.graphics.drawable.BitmapDrawable;


/** Represents a box of the board*/
public class Box {
	private static final int STATE_FREE = 0;
	private static final int STATE_X = 1; 
	private static final int STATE_O = 2;
	
	private int state;
	
	private BitmapDrawable drawable;
	
	public Box() {
		setFree();
	}
	
	public boolean isFree(){
		return state == STATE_FREE;
	}
	
	public boolean isX(){
		return state == STATE_X;
	}
	
	public boolean isO(){
		return state == STATE_O;
	}
	
	public void setFree(){
		state = STATE_FREE;
		drawable = null;
	}
	
	public void setX(){
		setX(false);
	}
	
	public void setO(){
		setO(false);
	}
	
	public void setX(boolean ignoreGui){
		state = STATE_X;
		if(!ignoreGui) {
			drawable = BoxDrawablesFactory.createXBoxDrawable();
		}
	}
	
	public void setO(boolean ignoreGui){
		state = STATE_O;
		if(!ignoreGui) {
			drawable = BoxDrawablesFactory.createOBoxDrawable();
		}
	}
	
	public int getState(){
		return state;
	}
	
	public BitmapDrawable getDrawable() {
		return drawable;
	}
}