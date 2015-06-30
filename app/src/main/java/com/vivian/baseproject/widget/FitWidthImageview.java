package com.vivian.baseproject.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FitWidthImageview extends ImageView {
	private int imageWidth;
	private int imageHeight;

	public FitWidthImageview(Context context) {
		super(context);
		getImageSize();
	}

	public FitWidthImageview(Context context, AttributeSet attrs) {
		super(context, attrs);
		getImageSize();
	}

	public FitWidthImageview(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getImageSize();
	}

	private void getImageSize() {
		Drawable background = this.getBackground();
		if (background == null)
			return;
		Bitmap bitmap = ((BitmapDrawable) background).getBitmap();
		imageWidth = bitmap.getWidth();
		imageHeight = bitmap.getHeight();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = width * imageHeight / imageWidth;
		this.setMeasuredDimension(width, height);
	}
}
