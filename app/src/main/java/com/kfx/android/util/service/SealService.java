package com.kfx.android.util.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapMemoryCache;

public interface SealService {
	public Boolean valSealMsg(String code, String md5);
	public List<Map<String,Object>> getSealRecords(String cla, ListMapMemoryCache memoryCache, ListMapFileCache fileCache, String catchName, Date date);
	public List<Map<String,Object>> getChips(String where);

}
