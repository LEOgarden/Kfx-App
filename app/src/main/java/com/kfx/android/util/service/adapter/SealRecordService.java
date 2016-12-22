package com.kfx.android.util.service.adapter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.kfx.android.util.handler.SAXForSealRecordHandler;

public class SealRecordService {
	public List<Map<String,Object>> getProvinces(String titlePath) throws Exception{
		InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream(titlePath);
		SAXForSealRecordHandler sfth=new SAXForSealRecordHandler();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser;
		saxParser = spf.newSAXParser();
		saxParser.parse(inputStream, sfth);
		List<Map<String,Object>> provinces=sfth.getProvinces();
		return provinces;
	}
	public String[] getProvinceName(List<Map<String,Object>> provinces){
		String[] str=new String[provinces.size()];
		for(int i=0;i<provinces.size();i++){
			Map<String,Object> map=provinces.get(i);
			String name=(String)map.get("name");		
			str[i]=name;
			System.out.println("name:"+name);
		}				
		return str;
	}

	public String[][] getCityName(List<Map<String,Object>> provinces)
			throws Exception {
		System.out.println("titlesprovinces:" + provinces);		
		String[][] cityStrs = new String[provinces.size()][];
		for (int i = 0; i < provinces.size(); i++) {
			Map<String, Object> provinceMap = provinces.get(i);
			String provinceName = (String) provinceMap.get("name");
			System.out.println("provinceName:" + provinceName);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> cities = (List<Map<String, Object>>) provinceMap
					.get("cities");	
			cityStrs[i]=new String[cities.size()];
			for (int j = 0; j < cities.size(); j++) {
				Map<String, Object> cityMap = cities.get(j);
				String cityName = (String) cityMap.get("name");
				System.out.println("city name:" + cityName);
				cityStrs[i][j] = cityName;
			}
		}
		return cityStrs;
	}
	@SuppressWarnings("unchecked")
	public String[][][] getCompanyName(List<Map<String,Object>> provinces)
			throws Exception {	
		String[][][] companyStrs=new String[provinces.size()][][];			
		for (int i = 0; i < provinces.size(); i++) {
			Map<String, Object> provinceMap = provinces.get(i);
			List<Map<String, Object>> cities = (List<Map<String, Object>>) provinceMap
					.get("cities");				
			companyStrs[i]=new String[cities.size()][];
			for (int j = 0; j < cities.size(); j++) {
				Map<String, Object> cityMap = cities.get(j);				
				List<Map<String, Object>> companies = (List<Map<String, Object>>) cityMap
						.get("companies");
				companyStrs[i][j]=new String[companies.size()];
				for(int k=0;k<companies.size();k++){
					Map<String, Object> companyMap=companies.get(k);
					String companyName = (String) companyMap.get("name");
					companyStrs[i][j][k] = companyName;
				}			
			}
		}
		return companyStrs;
	}
	@SuppressWarnings("unchecked")
	public Map<String,Object> getCompanyMap(List<Map<String,Object>> provinces,String companyName){
		
		Map<String,Object> company=null;
		for (int i = 0; i < provinces.size(); i++) {
			Map<String, Object> provinceMap = provinces.get(i);
			List<Map<String, Object>> cities = (List<Map<String, Object>>) provinceMap
					.get("cities");			
			for (int j = 0; j < cities.size(); j++) {
				Map<String, Object> cityMap = cities.get(j);				
				List<Map<String, Object>> companies = (List<Map<String, Object>>) cityMap
						.get("companies");				
				for(int k=0;k<companies.size();k++){
					Map<String, Object> companyMap=companies.get(k);
					String companyNameStr = (String) companyMap.get("name");
					if(companyNameStr.equals(companyName)){
						company=companyMap;
						break;
					}
					
				}			
			}
		}
		return company;		
	}
}
