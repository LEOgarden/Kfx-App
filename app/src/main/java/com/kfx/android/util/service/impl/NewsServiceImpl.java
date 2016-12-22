package com.kfx.android.util.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.os.StrictMode;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapMemoryCache;
import com.kfx.android.util.common.Properties;
import com.kfx.android.util.conn.ConCla;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.NewsService;


public class NewsServiceImpl implements NewsService {
	@Override
	public List<Map<String, Object>> getNewsForListMap(String cla,ListMapMemoryCache memoryCache,ListMapFileCache fileCache,String fileName,Date date) {
		ListMapService service=new ListMapServiceImpl();
		return service.getListMapForCla(cla, memoryCache, fileCache, fileName, date);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getNewsForLatest(String where) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(
					Properties.web_ip, Properties.web_port,
					Properties.web_name, Properties.web_action_news)));
			client.setConnectionTimeoutMillis(30*1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		try {
			newList = client.invoke("getNewsWhere", new Object[] { where },
					List.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return newList;
	}

	@Override
	public void saveNewsForLatest(List<Map<String, Object>> list,
			ListMapMemoryCache memoryCache, ListMapFileCache fileCache,
			String catchName, Date date) {
		ListMapService service=new ListMapServiceImpl();
		service.saveListMapForFile(list, memoryCache, fileCache, catchName, date);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getMailsForLatest(String where) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(
					Properties.web_ip, Properties.web_port,
					Properties.web_name, Properties.web_action_mails)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> mailList = new ArrayList<Map<String, Object>>();
		try {
			mailList = client.invoke("getMailsWhere", new Object[] { where },
					List.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return mailList;
	}
}
