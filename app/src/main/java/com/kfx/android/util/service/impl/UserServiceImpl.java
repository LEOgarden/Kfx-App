package com.kfx.android.util.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.StrictMode;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.kfx.android.bean.User;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.common.Properties;
import com.kfx.android.util.conn.ConCla;
import com.kfx.android.util.service.UserService;

public class UserServiceImpl implements UserService {
	@Override
	public boolean clearSealUser(List<Map<String,Object>> list,
			ListMapFileCache fileCache, String fileName) {
		fileCache.saveListMapToFile(list, fileName);
		return true;
	}

	@Override
	public boolean valSealUser(String where) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_action_user)));
			client.setConnectionTimeoutMillis(30*1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Boolean flg=false;
		try {
			flg=client.invoke("getUserWhere", new Object[]{where}, Boolean.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}	
		return flg;
	}

	@Override
	public boolean saveSealRegisterUser(User user) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_action_user)));
			client.setConnectionTimeoutMillis(30*1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Boolean flg=false;
		try {
			flg=client.invoke("addUser", new Object[]{user}, Boolean.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}	
		return flg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> searchSealUser(String where) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_action_user)));
			client.setConnectionTimeoutMillis(30*1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try {
			list=client.invoke("getUsersWhere", new Object[]{where}, List.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}	
		return list;
	}

	@Override
	public boolean updateSealUser(Map<String, Object> map) {
		JsonRpcHttpClient client = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			client = new JsonRpcHttpClient(new URL(ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_action_user)));
			client.setConnectionTimeoutMillis(30*1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Boolean flg=false;
		try {
			flg=client.invoke("updateUser", new Object[]{map}, Boolean.class);
		} catch (Throwable e) {
			e.printStackTrace();
		}	
		return flg;
	}

	
}
