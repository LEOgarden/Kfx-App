package com.kfx.android.util.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kfx.android.util.cache.ImageFileCache;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapMemoryCache;

public interface ListMapService {
	public List<Map<String,Object>> getListMapForCla(String cla, ListMapMemoryCache memoryCache, ListMapFileCache fileCache, String catchName, Date date);
	//保存字符串到文件
	public void saveStrForFile(String Date, ListMapFileCache fileCache, String fileName);
	public String getStrForFile(ListMapFileCache fileCache, String fileName);
	//保存listMap到file文件
	public void saveListMapForFile(List<Map<String, Object>> list,
								   ListMapMemoryCache memoryCache, ListMapFileCache fileCache,
								   String catchName, Date date);
	public void clearCacheForFile(ListMapFileCache listMapFileCache, ImageFileCache imageFileCache);
}
