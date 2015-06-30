package com.vivian.baseproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class FitWidthRelativeLayout extends RelativeLayout {

	public FitWidthRelativeLayout(Context context) {
		this(context, null);
	}

	public FitWidthRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FitWidthRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		if (height != width) {

			height = width;

			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));

			this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
