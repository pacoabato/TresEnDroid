package com.pacoabato.tresendroid.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.pacoabato.tresendroid.R;

public class BoxDrawablesFactory {
	
	private static Context context;
	
	private static final int[] xIdBitmaps = {
			R.drawable.btn_backgr_x_1c,
			R.drawable.btn_backgr_x_2c,
			R.drawable.btn_backgr_x_3c,
			R.drawable.btn_backgr_x_4c,
			R.drawable.btn_backgr_x_5c,
			R.drawable.btn_backgr_x_6c,
			R.drawable.btn_backgr_x_7c,
			R.drawable.btn_backgr_x_8c
	};
	
	private static final int[] oIdBitmaps = {
			R.drawable.btn_backgr_o_1c,
			R.drawable.btn_backgr_o_2c,
			R.drawable.btn_backgr_o_3c,
			R.drawable.btn_backgr_o_4c,
			R.drawable.btn_backgr_o_5c,
			R.drawable.btn_backgr_o_6c,
			R.drawable.btn_backgr_o_7c,
			R.drawable.btn_backgr_o_8c
	};
	
	private static final int oxIdBitmap = R.drawable.btn_backgr_ox;
	
	private static final List<BitmapDrawable> xBitmaps = new ArrayList<BitmapDrawable>(xIdBitmaps.length);
	private static final List<BitmapDrawable> oBitmaps = new ArrayList<BitmapDrawable>(oIdBitmaps.length);
	private static BitmapDrawable oxBitmap;
	
	private static int xIndex = 0;
	private static int oIndex = 0;
	
	public static void setContext(Context context) {
		BoxDrawablesFactory.context = context;
		reorder();
		
		Bitmap ox = BitmapFactory.decodeResource(
				context.getResources(),
				oxIdBitmap);
		oxBitmap = new BitmapDrawable(ox);
	}
	
	public static void reorder() {
		Resources res = context.getResources();
		
		xBitmaps.clear();
		oBitmaps.clear();
		
		for(int id : xIdBitmaps) {
			Bitmap bitmap = BitmapFactory.decodeResource(res, id);
			BitmapDrawable drawable = new BitmapDrawable(bitmap);
			
			xBitmaps.add(drawable);
		}
		Collections.shuffle(xBitmaps);
		
		for(int id : oIdBitmaps) {
			Bitmap bitmap = BitmapFactory.decodeResource(res, id);
			BitmapDrawable drawable = new BitmapDrawable(bitmap);
			
			oBitmaps.add(drawable);
		}
		Collections.shuffle(oBitmaps);
		
		xIndex = oIndex = 0;
	}
	
	public static BitmapDrawable createOBoxDrawable() {
		return oBitmaps.get(oIndex++);
	}

	public static BitmapDrawable createXBoxDrawable() {
		return xBitmaps.get(xIndex++);
	}
	
	public static BitmapDrawable createOXBoxDrawable() {
		return oxBitmap;
	}
}
