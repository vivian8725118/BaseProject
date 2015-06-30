package com.vivian.baseproject.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtil {

	public static double getLatitude(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			return location.getLatitude();
		}
		return 0;
	}

	public static double getLongtitude(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			return location.getLongitude();
		}
		return 0;
	}

}
