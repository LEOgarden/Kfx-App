package com.kfx.android.util.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.kfx.android.util.json.JsonUtil;
import com.kfx.android.util.str.StringUtil;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ListMapFileCache {
    private static final String CACHDIR = "ListMapCach";
    private static final String WHOLESALE_CONV = ".data";
    private static final int MB = 1024*1024;
    private static final int CACHE_SIZE = 10;
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
    public ListMapFileCache() {
        //清理文件缓存
        removeCache(getDirectory());
    }
    private boolean removeCache(String dirPath) {
    	 File dir = new File(dirPath);
    	 File[] files = dir.listFiles();
         if (files == null) {
             return true;
         }
         if (!Environment.getExternalStorageState().equals(
                 Environment.MEDIA_MOUNTED)) {
             return false;
         }
         int dirSize = 0;
         for (int i = 0; i < files.length; i++) {
             if (files[i].getName().contains(WHOLESALE_CONV)) {
                 dirSize += files[i].length();
             }
         }
         if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
             int removeFactor = (int) ((0.4 * files.length) + 1);
             Arrays.sort(files, new FileLastModifSort());
             for (int i = 0; i < removeFactor; i++) {
                 if (files[i].getName().contains(WHOLESALE_CONV)) {
                     files[i].delete();
                 }
             }
         }
         if (freeSpaceOnSd() <= CACHE_SIZE) {
             return false;
         }         
         return true;    	
    }
    public List<Map<String,Object>> getListMap(final String url,Date date) {
    	List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    	final String path = getDirectory() + "/" + convertUrlToFileName(url);
    	File file = new File(path);
    	if (file.exists()) { 
    		String str=StringUtil.getContentPath(path);
    		if(str.length()==0||str.equals("[]")){
    			file.delete();
    		}else{
    			try {
    				JSONArray ja=new JSONArray(str);
    				list=JsonUtil.getJsonArrayToList(ja);
				} catch (JSONException e) {
					e.printStackTrace();
				}
    		}
    	}    	
    	return list;    	
    }
    //获取字符串
    public String getStrs(String url) {
    	
    	final String path = getDirectory() + "/" + convertUrlToFileName(url);
    	File file = new File(path);
    	String str="";
    	if (file.exists()) { 
    		str=StringUtil.getContentPath(path);
    		if(str.length()>2){
    			str=str.substring(0, str.length()-1);
    		}
    	}    	
    	return str;    	
    }
    //保存字符串
    public void saveStrToFile(String flg, String fileName) {
    	  String filename = convertUrlToFileName(fileName);
    	  String dir = getDirectory();
    	  File dirFile = new File(dir);
    	  if (!dirFile.exists())
              dirFile.mkdirs();
          File file = new File(dir +"/" + filename);
          try {
          	FileOutputStream out = new FileOutputStream(file);
          	out.write(flg.getBytes());
          	out.flush();
          	out.close();           
          } catch (FileNotFoundException e) {
              Log.w("ImageFileCache", "FileNotFoundException");
          } catch (IOException e) {
              Log.w("ImageFileCache", "IOException");
          }
    }
    /** 将图片存入文件缓存 **/
    public void saveListMapToFile(List<Map<String, Object>> list, String fileName) {
        if (list.size() == 0||list==null) {
            return;
        }
        //判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            //SD空间不足
            return;
        }
        JSONArray ja=new JSONArray();
        ja=JsonUtil.getListToJsonArray(list);
        String jsonStr=ja.toString();        
        
        String filename = convertUrlToFileName(fileName);
        String dir = getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();
        File file = new File(dir +"/" + filename);
        try {
        	FileOutputStream out = new FileOutputStream(file);
        	out.write(jsonStr.getBytes());
        	out.flush();
        	out.close();           
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }
    } 
    /** 计算sdcard上的剩余空间 **/
    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    } 
    /** 获得缓存目录 **/
    private String getDirectory() {
        String dir = getSDPath() + "/" + CACHDIR;
        return dir;
    }
    /** 取SD卡路径 **/
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }
    /** 将url转成文件名 **/
    private String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1] + WHOLESALE_CONV;
    }
    /**
     * 根据文件的最后修改时间进行排序
     */
	public List<String> clearListMapChacheToFile() {
		String dir = getDirectory();
		List<String> list = new ArrayList<String>();
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] subFile = file.listFiles();
		for (File tmp : subFile) {
			if (!tmp.isDirectory()) {				
				String fileName = tmp.getName();
				if (fileName.trim().toLowerCase().endsWith(".data")) {
					list.add(fileName);
				}
				tmp.delete();
			}

		}
		return list;
	}
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}