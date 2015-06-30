package com.vivian.baseproject.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class BitmapUtil {
	/**
	 * drawable to bitmap
	 * 
	 * @param drawable
	 * @param widthPixels
	 * @param heightPixels
	 * @return
	 */
	public static Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
		Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutableBitmap);
		drawable.setBounds(0, 0, widthPixels, heightPixels);
		drawable.draw(canvas);

		return mutableBitmap;
	}
}
