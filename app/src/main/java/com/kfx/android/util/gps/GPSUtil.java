package com.kfx.android.util.gps;

import com.kfx.android.activity.service.SealServiceMainActivity;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class GPSUtil{
	public Location mLocation;
    public static LocationManager mLocationManager;
	public static boolean gpsIsOpen(Context context) {
		boolean bRet = true;
		LocationManager alm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (!alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(context, "未开启GPS", Toast.LENGTH_SHORT).show();
			bRet = false;
		} else {
			Toast.makeText(context, "GPS已开启", Toast.LENGTH_SHORT).show();
		}
		return bRet;
	}	
	public static Location getLocation(Context context) {
		// 获取位置管理服务
		mLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 查找服务信息
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 定位精度: 最高
		criteria.setAltitudeRequired(false); // 海拔信息：不需要
		criteria.setBearingRequired(false); // 方位信息: 不需要
		criteria.setCostAllowed(true); // 是否允许付费
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 耗电量: 低功耗
		String provider = mLocationManager.getBestProvider(criteria, true); // 获取GPS信息
																			// L
		Location location = mLocationManager.getLastKnownLocation(provider);
		mLocationManager.requestLocationUpdates(provider, 2000, 5,
				locationListener);
		Toast toast = Toast.makeText(context,
				"获取不到数据", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return location;
	}
	private final static LocationListener locationListener = new LocationListener() {		
		@Override
		public void onLocationChanged(Location location) {
			ListMapFileCache fileCacheForFile=new ListMapFileCache();
			String returnMsg="";
			if (location != null){
				returnMsg="维度:" + location.getLatitude() + "\n经度:"
						+ location.getLongitude();
				ListMapService service=new ListMapServiceImpl();				
				service.saveStrForFile( location.getLongitude()+","+location.getLatitude(), fileCacheForFile, "seal_record_location");
			}else{
				returnMsg= "获取不到数据" + Integer.toString(0);
			}
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	};
}
