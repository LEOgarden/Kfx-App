package com.kfx.android.util.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapMemoryCache;

public interface NewsService {
	/**
	 * 
	 * @param cla 传递的类名
	 * @param memoryCache 缓存离的数据
	 * @param fileCache 文件形式保存的数据
	 * @param catchName //存储的name
	 * @param date //存储的时间
	 * @return 返回listMap类型
	 */	
	public List<Map<String,Object>> getNewsForListMap(String cla, ListMapMemoryCache memoryCache, ListMapFileCache fileCache, String catchName, Date date);
	public List<Map<String,Object>> getNewsForLatest(String where);
	public List<Map<String,Object>> getMailsForLatest(String where);
	public void saveNewsForLatest(List<Map<String, Object>> list, ListMapMemoryCache memoryCache, ListMapFileCache fileCache, String catchName, Date date);

}
