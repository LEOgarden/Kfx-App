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
import com.kfx.android.util.service.SealService;

public class SealServiceImpl implements SealService {

	@Override
	public Boolean valSealMsg(String code, String md5) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_action_map)));
			client.setConnectionTimeoutMillis(30*1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Boolean flg=false;
		try {
			flg=client.invoke("getSealIsTrue", new Object[]{code,md5}, Boolean.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}	
		return flg;
	}

	@Override
	public List<Map<String, Object>> getSealRecords(String cla,
			ListMapMemoryCache memoryCache, ListMapFileCache fileCache,
			String fileName, Date date) {
		ListMapService service=new ListMapServiceImpl();
		return service.getListMapForCla(cla, memoryCache, fileCache, fileName, date);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getChips(String where) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_action_chip)));
			client.setConnectionTimeoutMillis(30*1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
			list=client.invoke("getChipsWhere", new Object[]{where}, List.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}
}
