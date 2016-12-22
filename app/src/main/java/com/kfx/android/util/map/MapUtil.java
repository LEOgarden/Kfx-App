package com.kfx.android.util.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;

import com.kfx.android.util.cache.ImageFileCache;
import com.kfx.android.util.cache.ImageGetFromHttp;
import com.kfx.android.util.cache.ImageMemoryCache;
import com.kfx.android.util.common.Properties;
import com.kfx.android.util.gps.Distance;
import com.kfx.android.util.time.TimeUtil;

public class MapUtil {
	public static List<Map<String, Object>> getListMapAddForMatTime(List<Map<String, Object>> newsList){
		List<Map<String, Object>> newsListForTimeFormat = new ArrayList<Map<String, Object>>();
		if (newsList.size() > 0) {
			for (Map<String, Object> map : newsList) {
				map.put("time", TimeUtil.formatMillisToTime(map.get(
						"createTime").toString()));
				//Date date=new Date(Long.parseLong(map.get("createTime").toString()));
				
				newsListForTimeFormat.add(map);
			}
		}
		return newsListForTimeFormat;
	}
	
	public static List<Map<String, Object>> getListMapAddImageUrl(List<Map<String, Object>> newsList){
		List<Map<String, Object>> newsListForTimeFormat = new ArrayList<Map<String, Object>>();
		String url=getImageUrlImager();
		if (newsList.size() > 0) {
			for (Map<String, Object> map : newsList) {
				map.put("photoPath", url+map.get(
						"photoPath").toString());
				//Date date=new Date(Long.parseLong(map.get("createTime").toString()));
				
				newsListForTimeFormat.add(map);
			}
		}
		return newsListForTimeFormat;
	}
	
	
	public static List<Map<String, Object>> getListMapAddImage(
			List<Map<String, Object>> newsList, ImageMemoryCache memoryCache,
			ImageFileCache fileCache,int id) {
		List<Map<String, Object>> newsListAddImage = new ArrayList<Map<String, Object>>();
		if (newsList.size() > 0) {
			for (Map<String, Object> map : newsList) {
				Set<String> set = map.keySet();
				if (set.contains("photoPath")) {
					if (map.get("photoPath") != null
							&& map.get("photoPath").toString().length() > 0) {						
						String path = (String) map.get("photoPath");
						Bitmap result = memoryCache.getBitmapFromCache(path);
						if (result == null) {
							// 文件缓存中获取
							result = fileCache.getImage(path);
							if (result == null) {
								// 从网络获取
								result = ImageGetFromHttp.downloadBitmap(path);
								if (result != null) {
									fileCache.saveBitmap(result, path);
									memoryCache.addBitmapToCache(path, result);
								}
							} else {
								// 添加到内存缓存
								memoryCache.addBitmapToCache(path, result);
							}
						}
						map.put("image", result);
					} else {
						map.put("image", id);
					}
				}
				newsListAddImage.add(map);
			}
		}
		return newsListAddImage;
	}
	
	public static List<Map<String, Object>> getListMapAddForDistance(List<Map<String, Object>> newsList,String location){
		String distance="...";
		String x="0";
		String y="0";
		distance=distance.replaceAll(" ", "");
		if(location.length()>9){			
			String strs[]=location.split(",");
			if(strs.length==2){
				x=strs[0];
				y=strs[1];
			}else{
				x="0";
				y="0";
			}
		}
		//经度
		double x1=Double.parseDouble(x);
		//维度
		double y1=Double.parseDouble(y);
		List<Map<String, Object>> newsListForTimeFormat = new ArrayList<Map<String, Object>>();
		if (newsList.size() > 0) {
			for (Map<String, Object> map : newsList) {
				String factory_x=map.get("x").toString();
				String factory_y=map.get("y").toString();
				//经度
				double factory_x1=Double.parseDouble(factory_x);
				//维度
				double factory_y1=Double.parseDouble(factory_y);
				double d =Distance.GetDistance(x1,y1,factory_x1,factory_y1)/1000;
				if(x.equals("0")||y.equals("0")){
					distance="...";
				}else{
					distance=Double.toString(d);
					distance=distance.substring(0,5);
				}
				map.put("distance", distance);				
				newsListForTimeFormat.add(map);
			}
		}
		return newsListForTimeFormat;
	}
	
	
	public static List<Map<String, Object>> getMapToForMatTimeImage(
			List<Map<String, Object>> newsList,ImageMemoryCache memoryCache,ImageFileCache fileCache) {
		List<Map<String, Object>> newsForImageList = new ArrayList<Map<String, Object>>();
		if (newsList.size() > 0) {
			for (Map<String, Object> map : newsList) {
				if (map.get("photoPath") != null
						&& map.get("photoPath").toString().length() > 0) {
					String path = (String) map.get("photoPath");
					Bitmap result = memoryCache.getBitmapFromCache(path);
			        if (result == null) {  
			            // 文件缓存中获取  
			            result = fileCache.getImage(path);  
			            if (result == null) {  
			                // 从网络获取  
			                result = ImageGetFromHttp.downloadBitmap(path);  
			                if (result != null) {  
			                    fileCache.saveBitmap(result, path);  
			                    memoryCache.addBitmapToCache(path, result);  
			                }  
			            } else {  
			                // 添加到内存缓存  
			                memoryCache.addBitmapToCache(path, result);  
			            }  
			        }
					map.put("image", result);
				}
				map.put("time", TimeUtil.formatMillisToTime(map.get(
						"createTime").toString()));
				newsForImageList.add(map);
			}
		}
		return newsForImageList;
	}

	public static List<Map<String, Object>> getHomeForMap(
			List<Map<String, Object>> newsList, String str) {
		List<Map<String, Object>> newsForImageList = new ArrayList<Map<String, Object>>();
		if (newsList.size() > 3) {
			if (str.equals("flipper")) {
				for (int i = 0; i < 3; i++) {
					Map<String, Object> map = newsList.get(i);
					newsForImageList.add(map);
				}
			} else if (str.equals("list")) {
				for (int i = 3; i < newsList.size(); i++) {
					Map<String, Object> map = newsList.get(i);
					newsForImageList.add(map);
				}
			}
			return newsForImageList;
		} else {
			return newsList;
		}
	}

	public static Map<String, Object> getHomeForDetailMap(
			List<Map<String, Object>> newsList, String title) {
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i=0;i<newsList.size();i++){
			Map<String, Object> mapTmp=newsList.get(i);
			String titleTmp=(String) mapTmp.get("title");
			if(title.equals(titleTmp)){
				map=mapTmp;
				break;
			}
		}
		return map;
	}
	public static String getImageUrlImager(){
		//http://17.18.2.51:8081/Kfx-Azd-Net/index.jsp
		StringBuilder sb=new StringBuilder();
		sb.append("http://");
		sb.append(Properties.web_ip);
		sb.append(":");
		sb.append(Properties.web_port);
		sb.append("/");
		sb.append(Properties.web_name);
		sb.append("/");
		sb.append(Properties.img_cache_part);
		sb.append("/");
		return sb.toString();
	}
}
