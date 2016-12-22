package com.kfx.android.util.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXForHelpBackHandler extends DefaultHandler {
	private List<Map<String,Object>> helps;
	private Map<String,Object> help;
	private String nowWitch;
	
	public List<Map<String, Object>> getHelps() {
		return helps;
	}

	@Override
	public void startDocument() throws SAXException {
		helps=new ArrayList<Map<String,Object>>();
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if("help".equals(localName)){
			for(int i=0;i<attributes.getLength();i++){
				help=new HashMap<String,Object>();
				help.put("id", attributes.getValue(i));
			}
		}
		nowWitch=localName;
	}	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String data=new String(ch,start,length).trim();
		if("question".equals(nowWitch)){
			help.put("question", data.trim());
		}else if("answer".equals(nowWitch)){
			help.put("answer", data.trim());
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if("help".equals(localName)){
			helps.add(help);
			help=null;
		}		
		nowWitch=null;
	}	
}
