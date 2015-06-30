package com.vivian.baseproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewEx extends GridView {

	public GridViewEx(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public GridViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
