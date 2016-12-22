package com.kfx.android.util.service;


import java.util.List;
import java.util.Map;

import com.kfx.android.bean.User;
import com.kfx.android.util.cache.ListMapFileCache;

public interface UserService {
	
	public boolean clearSealUser(List<Map<String, Object>> map, ListMapFileCache fileCache, String catchName);
	public boolean valSealUser(String where);
	public List<Map<String,Object>> searchSealUser(String where);
	public boolean saveSealRegisterUser(User user);
	public boolean updateSealUser(Map<String, Object> map);

}
