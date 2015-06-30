package com.vivian.baseproject.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtil {
	private static DisplayMetrics sDisplayMetrics;
	private static final float ROUND_DIFFERENCE = 0.5f;

	public DisplayUtil(Context context) {
		sDisplayMetrics = context.getResources().getDisplayMetrics();
	}

	// // 初始化
	// public static void init(Context context) {
	//
	// }

	// 获取屏幕的高度，单位：像素
	public static int getHeightPixels() {
		return sDisplayMetrics.heightPixels;
	}

	// 获取屏幕的宽度，单位：像素
	public static float getDensity() {
		return sDisplayMetrics.density;
	}

	// dp转px
	public static int dp2px(int dp) {
		return (int) (dp * sDisplayMetrics.density + ROUND_DIFFERENCE);
	}

	// px转dp
	public static int px2dp(int px) {
		return (int) (px / sDisplayMetrics.density + ROUND_DIFFERENCE);
	}
}
