package com.kfx.android.util.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.kfx.android.util.handler.SAXForSealLawsHandler;
import com.kfx.android.util.service.LawsService;
public class LawsServiceImpl implements LawsService {
	@Override
	public List<Map<String, Object>> getSealLaws(String path) {
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(path);
		SAXForSealLawsHandler sfh = new SAXForSealLawsHandler();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = spf.newSAXParser();
			saxParser.parse(inputStream, sfh);			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Map<String,Object>> sealLawList=sfh.getTitles();	
		return sealLawList;
	}
	//¶ÁÈ¡ÎÄ¼þ
	@Override
	public String getLawsFileToString(String filePath) {
		InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream(filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
