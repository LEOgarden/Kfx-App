package com.kfx.android.util.cache;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.StrictMode;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.kfx.android.util.common.Properties;
import com.kfx.android.util.conn.ConCla;


public class ListMapGetFromInternet {
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> downloadListMap(String cla,String fileName) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_action_map)));
			client.setConnectionTimeoutMillis(30*1000);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map<String, Object>> newsList=new ArrayList<Map<String, Object>>();
		try {
			newsList = (List<Map<String, Object>>)client.invoke("getMaps", new Object[]{cla}, List.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return newsList;
	}
}
