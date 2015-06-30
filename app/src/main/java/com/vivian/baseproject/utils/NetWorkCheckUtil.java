package com.vivian.baseproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.vivian.baseproject.tools.ServiceManager;


public class NetWorkCheckUtil {
	
	/**
	 * 检查当前网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {

		ConnectivityManager manager = ServiceManager.getConnectivityManager(context);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isAvailable() && info.isConnected()) {
			return true;
		}else {
			return false;
		}
	}

    /**
     * 判断是不是wifi连接
     * @param context
     * @return
     */
	public static boolean isWifi(Context context) {
        ConnectivityManager manager = ServiceManager.getConnectivityManager(context);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
        	int type = networkInfo.getType();
        	if (networkInfo.isAvailable() && networkInfo.isConnected() && type == ConnectivityManager.TYPE_WIFI ) {
        		return true;
        	}
        }
		return false;
	}
    /**
     * 判断当前网络是否是移动流量连接
     * @param context
     * @return
     */
    public static  boolean  isMobile(Context context) {
        ConnectivityManager manager = ServiceManager.getConnectivityManager(context);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
        	int type = networkInfo.getType();
        	if (networkInfo.isAvailable() && networkInfo.isConnected() && type == ConnectivityManager.TYPE_MOBILE) {
        		return true;
        	}
        }
        return false;
    }

    /**
     * 判断wifi是否已经打开
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = ServiceManager.getConnectivityManager(context);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }
}
