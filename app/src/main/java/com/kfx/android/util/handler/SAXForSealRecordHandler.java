package com.kfx.android.util.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXForSealRecordHandler extends DefaultHandler {
	private List<Map<String,Object>> provinces;
	private Map<String,Object> province;	
	private List<Map<String,Object>> cities;
	private Map<String,Object> city;
	private List<Map<String,Object>> companies;
	private Map<String,Object> company;
	String nowWitch;
	public List<Map<String, Object>> getProvinces() {
		return provinces;
	}
	public List<Map<String, Object>> getCities() {
		return cities;
	}
	@Override
	public void startDocument() throws SAXException {
		provinces=new ArrayList<Map<String,Object>>();
		
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if("province".equals(localName)){
			cities=new ArrayList<Map<String,Object>>();			
			for(int i=0;i<attributes.getLength();i++){
				province=new HashMap<String,Object>();
				province.put("id", attributes.getValue(i));
				province.put("name", attributes.getValue("name"));
			}
		}
		if("city".equals(localName)){
			companies=new ArrayList<Map<String,Object>>();
			for(int i=0;i<attributes.getLength();i++){
				city=new HashMap<String,Object>();
				city.put("id", attributes.getValue(i));
				city.put("name", attributes.getValue("name"));
			}
		}
		if("company".equals(localName)){
			for(int i=0;i<attributes.getLength();i++){
				company=new HashMap<String,Object>();
				company.put("id", attributes.getValue(i));
				company.put("name", attributes.getValue("name"));
			}			
		}
		nowWitch=localName;
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String data=new String(ch,start,length).trim();	
		if("address".equals(nowWitch))
		{
			company.put("address", data.trim());
		}else if("telp".equals(nowWitch))
		{
			company.put("telp", data.trim());
		}else if("area".equals(nowWitch))
		{
			company.put("area", data.trim());
		}else if("postion".equals(nowWitch))
		{
			company.put("postion", data.trim());
		}		
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if("company".equals(localName)){
			companies.add(company);
			company=null;
		}
		if("city".equals(localName)){
			city.put("companies", companies);
			cities.add(city);
			city=null;
		}
		if("province".equals(localName)){
			province.put("cities", cities);
			provinces.add(province);
			province=null;
		}
		nowWitch=null;
	}	
}
