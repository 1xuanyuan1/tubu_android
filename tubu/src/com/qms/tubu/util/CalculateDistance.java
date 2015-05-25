package com.qms.tubu.util;

import android.location.Location;

/**
 * 计算距离
 * @author dwj
 * @time 2013-11-14 下午12:43:48
 */
public class CalculateDistance {
	public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
		float[] results=new float[1];
		Location.distanceBetween(lat1, lon1, lat2, lon2, results);
		return results[0];
	}
}
