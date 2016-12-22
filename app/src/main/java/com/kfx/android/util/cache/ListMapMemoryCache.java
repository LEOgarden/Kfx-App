package com.kfx.android.util.cache;

import java.lang.ref.SoftReference;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.util.LruCache;

public class ListMapMemoryCache {
    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。
     * 硬引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    private static final int SOFT_CACHE_SIZE = 15;  //软引用缓存容量
    private static LruCache<String, List<Map<String,Object>>> mLruCache;  //硬引用缓存
    private static LinkedHashMap<String, SoftReference<List<Map<String,Object>>>> mSoftCache;  //软引用缓存
    public ListMapMemoryCache(Context context){
    	int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    	int cacheSize = 1024 * 1024 * memClass / 4;  //硬引用缓存容量，为系统可用内存的1/4
        mLruCache = new LruCache<String, List<Map<String,Object>>>(cacheSize) {
            @Override
            protected int sizeOf(String key, List<Map<String,Object>> value) {
                if (value != null){
                	 return 32*value.size();
                }else{
                	return 0;
                }                    
            }
                                                                                          
            @Override
            protected void entryRemoved(boolean evicted, String key, List<Map<String,Object>> oldValue, List<Map<String,Object>> newValue) {
                if (oldValue != null)
                    // 硬引用缓存容量满的时候，会根据LRU算法把最近没有被使用的图片转入此软引用缓存
                    mSoftCache.put(key, new SoftReference<List<Map<String,Object>>>(oldValue));
            }
        };
        mSoftCache = new LinkedHashMap<String, SoftReference<List<Map<String,Object>>>>(SOFT_CACHE_SIZE, 0.75f, true) {
            private static final long serialVersionUID = 6040103833179403725L;
            @Override
            protected boolean removeEldestEntry(Entry<String, SoftReference<List<Map<String,Object>>>> eldest) {
                if (size() > SOFT_CACHE_SIZE){    
                    return true;  
                }  
                return false; 
            }
        };
    }
    /**
     * 从缓存中获取图片
     */
    public List<Map<String,Object>> getBitmapFromCache(String fileName,Date date) {
    	List<Map<String,Object>> list;
        //先从硬引用缓存中获取
        synchronized (mLruCache) {
        	list = mLruCache.get(fileName);
            if (list != null) {
                //如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是最后被删除
                mLruCache.remove(fileName);
                mLruCache.put(fileName, list);
                return list;
            }
        }
        //如果硬引用缓存中找不到，到软引用缓存中找
        synchronized (mSoftCache) { 
            SoftReference<List<Map<String,Object>>> bitmapReference = mSoftCache.get(fileName);
            if (bitmapReference != null) {
            	list = bitmapReference.get();
                if (list != null) {
                    //将图片移回硬缓存
                    mLruCache.put(fileName, list);
                    mSoftCache.remove(fileName);
                    return list;
                } else {
                    mSoftCache.remove(fileName);
                }
            }
        }
        return null;
    } 
                                                                                  
    /**
     * 添加图片到缓存
     */
    public void addListMapToCache(List<Map<String,Object>> list,String fileName) {
        if (list!= null) {
            synchronized (mLruCache) {
                mLruCache.put(fileName, list);
            }
        }
    }                                                                                  
    public void clearCache() {
        mSoftCache.clear();
    }
}