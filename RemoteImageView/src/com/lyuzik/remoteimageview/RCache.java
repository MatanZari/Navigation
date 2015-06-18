package com.lyuzik.remoteimageview;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class RCache {
	private LruCache<String, Bitmap> mMemoryCache;
	protected final static int MB = 1024 * 1024;

	public RCache(int cacheSize) {
		if (RImageView.INFO) {
			Log.i(RImageView.TAG, "CacheSize:" + cacheSize / MB + "Mb");
		}
		mMemoryCache = new LruCache<String, Bitmap>((int) cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return (int) Utils.sizeOf(bitmap);
			}
		};
	}

	public void clear() {
		mMemoryCache.evictAll();
	}

	public Bitmap get(String key) {
		return mMemoryCache.get(key);
	}

	public int maxSize() {
		return mMemoryCache.maxSize();
	}

	public void set(String key, Bitmap value) {
		mMemoryCache.put(key, value);
	}

	public int size() {
		return mMemoryCache.size();
	}
}
