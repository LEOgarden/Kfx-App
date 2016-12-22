package com.kfx.android.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kfx.android.util.img.ImageUtil;
import com.kfx.android.util.str.StringUtil;

public class JsonUtil {
	public static JSONArray getListToJsonArray(List<Map<String, Object>> list) {
		JSONArray jsonArray = new JSONArray();
		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				JSONObject jo = new JSONObject();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					try {
						if(entry.getKey().equals("image")){
							JSONObject jImage = new JSONObject();							
							byte[] bis=ImageUtil.Bitmap2Bytes((Bitmap) entry.getValue());							
							String imgStr=StringUtil.bytesToHexString(bis);
							jImage.put("image",imgStr);							
							jo.put(entry.getKey(), jImage);							
						}else{
							if(entry.getKey().equals("photoPath")&&entry.getValue()==null){
								jo.put(entry.getKey(), "");
							}else{
								jo.put(entry.getKey(), entry.getValue());
							}						
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				jsonArray.put(jo);
			}
		}
		return jsonArray;
	}
	
	public static List<Map<String, Object>> getJsonArrayToList(JSONArray jsonArray) {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<jsonArray.length();i++){
			
			try {
				JSONObject jo=jsonArray.getJSONObject(i);
				@SuppressWarnings("unchecked")
				Iterator<String> keyIter= jo.keys(); 
				Map<String, Object> map = new HashMap<String, Object>();
				while (keyIter.hasNext()) {
					String key = keyIter.next();
					Object value = jo.get(key);
					if(key.equals("image")){
						JSONObject jImage = new JSONObject();						
						jImage= (JSONObject) jo.get("image");
						
						//System.out.println(jImage);
						String imgstr= jImage.get("image").toString();						
						byte[] bis=StringUtil.hexStringToBytes(imgstr);						
						Bitmap bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);
						map.put("image",bitmap);
					}else{
						map.put(key, value);
					}					
				}
				list.add(map);				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;		
	}
	public static Map<String, Object> getJsonObjectToMap(JSONObject jo){
		Map<String, Object> map=new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Iterator<String> keyIter= jo.keys(); 
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			Object value = null;
			try {
				value = jo.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}		
			map.put(key, value);				
		}
		return map;
	}
	public static JSONObject getMapToJsonObject(Map<String, Object> map){		
		JSONObject jo = new JSONObject();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			try {
				jo.put(entry.getKey(), map.get(entry.getKey()));
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}
		return jo;
	}
}
