package com.kfx.android.util.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kfx.android.util.time.TimeUtil;

public class ListUtil {
	/**
	 * 获取刻制社所在省份
	 * @param sealRecordList
	 * @return
	 */
	public static String[] getProvinceName(List<Map<String,Object>> sealRecordList){
		List <String> provinceList=new ArrayList<String>();
		if(sealRecordList.size()>0){
			for(Map<String,Object> map:sealRecordList){
				String provinceName=map.get("area_first_name").toString();
				if(!provinceList.contains(provinceName)){
					provinceList.add(provinceName);
				}
			}
		}
		String[] province=getListToArrray(provinceList);
		return province;
		}
	/**
	 * 所属市区
	 * @param provinces
	 * @return
	 * @throws Exception
	 */
	public static String[][] getCityName(List<Map<String,Object>> sealRecordList,String province[]){
		String[][] cityStrs = new String[province.length][];
		List<String> cityList=new ArrayList<String>();
		for (int i = 0; i < province.length; i++) {
			String provinceNameStr=province[i];
			cityList.removeAll(cityList);
			for(Map<String,Object> map:sealRecordList){
				String provinceName=map.get("area_first_name").toString();
				if(provinceName.equals(provinceNameStr)){
					String cityName=map.get("area_name").toString();
					if(!cityList.contains(cityName)){
						cityList.add(cityName);
					}
				}
			}
			cityStrs[i]=getListToArrray(cityList);
		}
		return cityStrs;
	}
	public static List<Map<String,Object>> getCompanyMap(List<Map<String,Object>> sealRecordList,String provinceName,String cityName){
		List<Map<String,Object>> companyListMap=new ArrayList<Map<String,Object>>();
		if(sealRecordList.size()>0){
			for(Map<String,Object> map:sealRecordList){
				String provinceNameTmp=map.get("area_first_name").toString();
				String cityNameTmp=map.get("area_name").toString();
				if(provinceNameTmp.equals(provinceNameTmp)&&cityNameTmp.equals(cityName)){
					companyListMap.add(map);					
				}
			}			
		}
		return companyListMap;
	}
	public static String[] getListToArrray(List<String> list){
		Object[] arr = list.toArray();
		String[] str=new String[list.size()];
		for (int i = 0; i < arr.length; i++) {
            String e = (String) arr[i];
            str[i]=e;
        }
		return str;		
	}
	public static List<Map<String,Object>> sortNewsBycreateTime(List<Map<String,Object>> list){		
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				 for (int j = i; j < list.size(); j++){
					 Map<String, Object> iMap=new HashMap<String, Object>();
					 Map<String, Object> jMap=new HashMap<String, Object>();
					 Map<String, Object> tmpMap=new HashMap<String, Object>();
					 iMap=list.get(i);
					 jMap=list.get(j);
					 Date datei=new Date();
					 Date datej=new Date();
					 datei=TimeUtil.TimeToDate(iMap.get("createTime").toString());
					 datej=TimeUtil.TimeToDate(jMap.get("createTime").toString());
					 if(datei.before(datej)){
						 tmpMap=iMap;
						 list.set(i, jMap);
						 list.set(j, tmpMap);
					 }					 
				 }				
			}
		}		
		return list;
	}

	public static List<Map<String, Object>> sortFactoryByDistace(
			List<Map<String, Object>> list) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				for (int j = i; j < list.size(); j++) {
					Map<String, Object> iMap = new HashMap<String, Object>();
					Map<String, Object> jMap = new HashMap<String, Object>();
					Map<String, Object> tmpMap = new HashMap<String, Object>();
					iMap = list.get(i);
					jMap = list.get(j);
					String datei = iMap.get("distance").toString();
					String datej = jMap.get("distance").toString();
					if (iMap.get("distance").toString().equals("...")) {
						break;
					} else {
						double datai = Double.parseDouble(datei);
						double dataj = Double.parseDouble(datej);
						if (datai > dataj) {
							tmpMap = iMap;
							list.set(i, jMap);
							list.set(j, tmpMap);
						}
					}
				}
			}
		}
		return list;
	}	
}
