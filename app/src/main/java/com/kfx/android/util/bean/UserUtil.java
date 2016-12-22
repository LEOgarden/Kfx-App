package com.kfx.android.util.bean;

import java.util.HashMap;
import java.util.Map;

import com.kfx.android.bean.User;

public class UserUtil {
	public static Map<String,Object> getUserToMap(User user){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", user.getId());
		map.put("name", user.getName());
		map.put("password", user.getPassword());
		map.put("role", user.getRole());
		map.put("certificateid", user.getCertificateid());
		map.put("telp", user.getTelp());
		return map;
	}

}
