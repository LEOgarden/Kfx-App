package com.kfx.android.util.service.adapter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import com.kfx.android.util.handler.SAXForHelpBackHandler;

public class HelpService {
	public List<Map<String,Object>> getHelps(String path){
		InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream(path);
		SAXForHelpBackHandler sfhbh=new SAXForHelpBackHandler();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = spf.newSAXParser();
			saxParser.parse(inputStream, sfhbh);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Map<String,Object>> helps=sfhbh.getHelps();
		System.out.println(helps);
		return helps;
	}
	

}
