package com.vivian.baseproject.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.vivian.baseproject.base.BaseApplication;
import com.vivian.baseproject.tools.SharedPMananger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ApplicationUtil {

	public static final BaseApplication getApplication() {
		return BaseApplication.getInstance();
	}

	public static final String getResource(int resId) {
		return BaseApplication.getInstance().getResources().getString(resId);
	}

	public static final String getVersionName() {
		try {
			return getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	/**
	 * 获取设备唯一标识码
	 * 
	 * @return
	 */
	public static String getDeviceId() {
		String imei = SharedPMananger.getInstance().getString("device_id");
		if (TextUtils.isEmpty(imei)) {
			TelephonyManager telephonyManager = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
			if (!TextUtils.isEmpty(imei) && !imei.contains("000000")) {
				SharedPMananger.getInstance().putString("device_id", imei);
				return imei;
			}

			if (TextUtils.isEmpty(imei) || imei.contains("000000")) {
				imei = Secure.getString(getApplication().getContentResolver(), Secure.ANDROID_ID);
				SharedPMananger.getInstance().putString("device_id", imei);
				return imei;
			}

			if (TextUtils.isEmpty(imei) || imei.contains("000000")) {
				// mac+user_unique_key的md5值
				WifiManager wifiMan = (WifiManager) getApplication().getSystemService(Context.WIFI_SERVICE);
				if (wifiMan != null) {
					WifiInfo wifiInf = wifiMan.getConnectionInfo();
					if (wifiInf != null && wifiInf.getMacAddress() != null) {// 48位，如FA:34:7C:6D:E4:D7
						imei = wifiInf.getMacAddress().replaceAll(":", "");
						imei = MD5.MD5(imei);
						SharedPMananger.getInstance().putString("device_id", imei);
						return imei;
					}
				}
			}
		}
		return imei;
	}

	public static void writeBitmap(Bitmap photo, String fPath) {
		try {
			File sdCard = new File(fPath);
			FileOutputStream outStreamz = new FileOutputStream(sdCard);
			photo.compress(Bitmap.CompressFormat.PNG, 100, outStreamz);
			outStreamz.flush();
			outStreamz.close();
			if (photo.isRecycled()) {
				photo.recycle();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前应用的版本号：
	 * 
	 * @return
	 * @throws Exception
	 */

	private String getVersionName(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}

}
