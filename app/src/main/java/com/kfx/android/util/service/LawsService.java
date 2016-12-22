package com.kfx.android.util.service;

import java.util.List;
import java.util.Map;

public interface LawsService {
	//读取法律文件list列表
	public List<Map<String,Object>> getSealLaws(String path);
	//读取法律文件
	public String getLawsFileToString(String filePath);
}
