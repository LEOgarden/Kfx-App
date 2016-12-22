package com.kfx.android.util.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kfx.android.util.cache.ImageFileCache;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapGetFromInternet;
import com.kfx.android.util.cache.ListMapMemoryCache;
import com.kfx.android.util.service.ListMapService;

public class ListMapServiceImpl implements ListMapService {

	@Override
	public List<Map<String, Object>> getListMapForCla(String cla,ListMapMemoryCache memoryCache,ListMapFileCache fileCache,String fileName,Date date)  {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		//list=memoryCache.getBitmapFromCache(fileName,date);
		if(list==null||list.size()==0){
			//list=fileCache.getListMap(fileName,date);
			if(list==null||list.size()==0) {  
				list=ListMapGetFromInternet.downloadListMap(cla,fileName);
				if(list!=null&&list.size()>0){
					fileCache.saveListMapToFile(list, fileName);
					memoryCache.addListMapToCache(list, fileName);
				 }
			}			
		}
		return list;
	}

	@Override
	public void saveStrForFile(String newDate, ListMapFileCache fileCache,
			String fileName) {
		fileCache.saveStrToFile(newDate, fileName);
	}

	@Override
	public String getStrForFile(ListMapFileCache fileCache, String fileName) {
		return fileCache.getStrs(fileName);
	}

	@Override
	public void saveListMapForFile(List<Map<String, Object>> list,
			ListMapMemoryCache memoryCache, ListMapFileCache fileCache,
			String fileName, Date date) {
		fileCache.saveListMapToFile(list, fileName);
		memoryCache.addListMapToCache(list, fileName);		
	}

	@Override
	public void clearCacheForFile(ListMapFileCache listMapFileCache,ImageFileCache imageFileCache) {
		if(listMapFileCache!=null){			
			listMapFileCache.clearListMapChacheToFile();
		}
		if(imageFileCache!=null){			
			imageFileCache.clearImgChacheToFile();		
		}		
	}
}
