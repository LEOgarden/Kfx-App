package com.kfx.android.util.version;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.kfx.android.util.common.Properties;
import com.kfx.android.util.conn.ConCla;
import com.kfx.android.util.str.StringUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.TextView;

public class AppVersionUtil {
	public static Map<String, Object> getVersionForLoaction(Context context){ 
		Map<String, Object> map=new HashMap<String, Object>();
	    //获取packagemanager的实例   
	    PackageManager packageManager = context.getPackageManager();  
	    //getPackageName()是你当前类的包名，0代表是获取版本信息  
	    PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(packInfo!=null){
		    map.put("code", packInfo.versionCode);
		    map.put("name", packInfo.versionName);
		}
	    return map;   
	}  
	public static Map<String,Object> getVersionForServer(String path) throws Exception{
		InputStream iStream =StringUtil.getInputStream(path);
		XmlPullParser  parser = Xml.newPullParser();  
		parser.setInput(iStream, "utf-8");//设置解析的数据源 
		int type = parser.getEventType();  
		Map<String, Object> map=new HashMap<String, Object>();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:  
	            if("version".equals(parser.getName())){ 
	            	map.put("version", parser.nextText());//获取版本号  
	            }else if ("url".equals(parser.getName())){  
	            	map.put("url", parser.nextText()); //获取要升级的APK文件  
	            }else if("content".equals(parser.getName())){
	            	map.put("content", parser.nextText()); //修改内容
	            }
	            else if ("description".equals(parser.getName())){  
	            	map.put("description", parser.nextText()); //获取该文件的信息  
	            }  
	            break;  
	        }  
	        type = parser.next();  
		}
		return map;
	}
	public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{  
	    //如果相等的话表示当前的sdcard挂载在手机上并且是可用的  
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
	    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ 
	    	System.out.println("Environment.getExternalStorageState():"+Environment.getExternalStorageState());
	        URL url = new URL(path);  
	        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(5000);	        
	        //获取到文件的大小   
	        pd.setMax(conn.getContentLength()/1024);
	        pd.setProgressNumberFormat("%1d kb/%2d kb");  
	        InputStream is = conn.getInputStream();  
	        File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");  
	        FileOutputStream fos = new FileOutputStream(file);  
	        BufferedInputStream bis = new BufferedInputStream(is);  
	        byte[] buffer = new byte[1024];  
	        int len ;  
	        int total=0;  
	        long startTime = System.currentTimeMillis();
	        while((len =bis.read(buffer))!=-1){  
	            fos.write(buffer, 0, len);  
	            total+= len; 
	            //获取当前下载量  
	            long endTime = System.currentTimeMillis();
	            int usedTime = (int) ((endTime-startTime)/1000);
	            //速度："+total/usedTime/1024+"kb/s"
	            pd.setProgress(total/1024);
	        }  
	        fos.close();  
	        bis.close();  
	        is.close(); 
	        System.out.println("-------------------------------时间 下载完毕 结束时间"+System.currentTimeMillis());
	        return file;  
	    }else{  
	        return null;  
	    }  
	} 
	public static boolean isNewstVersion(Context context){		
		Map<String,Object> map=AppVersionUtil.getVersionForLoaction(context);    			
		String path=ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_update_xml);
		Map<String,Object> map1=new HashMap<String, Object>();
		try {
			map1=AppVersionUtil.getVersionForServer(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!map1.get("version").equals(map.get("name"))){
			return false;
		}else{
			return true;
		}
	}
	
}
