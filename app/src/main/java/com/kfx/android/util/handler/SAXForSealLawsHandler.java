package com.kfx.android.util.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXForSealLawsHandler extends DefaultHandler {
	private List<Map<String,Object>> titles;
	private Map<String,Object> title;
	private String nowWitch;
	public List<Map<String, Object>> getTitles() {
		return titles;
	}
	@Override
	public void startDocument() throws SAXException {
		titles=new ArrayList<Map<String,Object>>();
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if("title".equals(localName)){
			for(int i=0;i<attributes.getLength();i++){
				title=new HashMap<String,Object>();
				title.put("id", attributes.getValue(i));
			}
		}
		nowWitch=localName;
	}	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String data=new String(ch,start,length).trim();
		if("name".equals(nowWitch)){
			System.out.println(data.trim());
			title.put("name", data.trim());
		}else if("file_name".equals(nowWitch)){
			title.put("file_name", data.trim());
		}else if("publish_group".equals(nowWitch)){
			title.put("publish_group", data.trim());
		}else if("publish_time".equals(nowWitch)){
			title.put("publish_time", data.trim());
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if("title".equals(localName)){
			titles.add(title);
			title=null;
		}		
		nowWitch=null;
	}	
}
