package com.kfx.android.util.net;

import java.util.concurrent.ExecutionException;

import com.kfx.android.util.common.Properties;
import com.kfx.android.util.conn.ConCla;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetLinkWork {
	public static boolean isLinkedNetwork(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	/**
	 * 获取连接的信息
	 * @param context 执行的Activity
	 * @return NetworkInfo数组
	 */
	public static String getNetWorkInfo(Context context){
		String netType="";
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
		 if (networkInfo.getTypeName().equals("WIFI")){
			 netType="WIFI网络";
         }else if(networkInfo.getTypeName().equals("mobile")){
        	 netType="GPRS数据网络";
         }else{
        	 netType=networkInfo.getTypeName();
         }		
		return netType;		
	}
	public static String isLinkedNetWeb(){
    	String url=ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,"");
    	String str="error";
    	try {
    		str=(new ConnServer()).execute(url).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}  
		return str;
	}

}
