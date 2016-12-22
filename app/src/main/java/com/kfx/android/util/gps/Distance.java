package com.kfx.android.util.gps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Distance {
	private static final double EARTH_RADIUS = 6378137;
	private static double rad(double d){
		return d * Math.PI / 180.0;
	}
	public static double GetDistance(double lng1, double lat1, double lng2, double lat2){
		 double radLat1 = rad(lat1);
		 double radLat2 = rad(lat2);
		 double radLng1 = rad(lng1);
		 double radLng2 = rad(lng2);
		 double a = radLat1 - radLat2;
		 double b = radLng1 - radLng2;
		 double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		 s = s * EARTH_RADIUS;
		 s = Math.round(s * 10000) / 10000;
		 return s;
	}
	public static List<Map<String,Object>> getNearByDistanceFactory(List<Map<String,Object>> list,double lat,double lon,long distance){
		List<Map<String,Object>> listNearByFactory=new ArrayList<Map<String,Object>>();
		if(list!=null&&list.size()>0){
			for(Map<String,Object> map:list){
				String factory_x=map.get("x").toString();
				String factory_y=map.get("y").toString();
				//经度
				double factory_x1=Double.parseDouble(factory_x);
				//维度
				double factory_y1=Double.parseDouble(factory_y);
				double d =Distance.GetDistance(lat,lon,factory_y1,factory_x1)/1000;
				if(d<distance){
					listNearByFactory.add(map);
				}
			}
		}
		return  listNearByFactory;
	}
	public static void main(String[] args) {
		double distance = GetDistance(121.491909,31.233234,121.411994,31.206134);
		 System.out.println("Distance is:"+distance);
	}

}
