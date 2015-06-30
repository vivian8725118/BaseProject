package com.vivian.baseproject.tools;

import java.io.File;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.vivian.baseproject.utils.LogUtil;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {

	private DiskBasedCache diskCache;
	private File cacheDirectory;
	private int maxSize;
	private int maxDiskSize;

	private boolean usingDiskBasedCache;

	public BitmapLruCache(int maxSize, int maxDiskSize, File cacheDirectory) {
		super(maxSize);

		if (maxSize == 0)
			throw new IllegalArgumentException();

		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}

		diskCache = new DiskBasedCache(cacheDirectory, maxDiskSize);

		this.cacheDirectory = cacheDirectory;
		this.maxSize = maxSize;
		this.maxDiskSize = maxDiskSize;

		usingDiskBasedCache = maxDiskSize > 0 ? true : false;
	}

	@Override
	public Bitmap getBitmap(String url) {
		if (url != null) {
			Bitmap bitmap = get(url);
			if (bitmap == null && usingDiskBasedCache) {
				if (usingDiskBasedCache) {
					final File volleyFileName = diskCache.getFileForKey(url);
					if (volleyFileName != null && volleyFileName.exists()) {
						bitmap = BitmapFactory.decodeFile(volleyFileName.getAbsolutePath());
						LogUtil.e("BitmapLruCache", "getBitmap:" + url);
						LogUtil.e("BitmapLruCache", volleyFileName.getName());
					}
				}
			}

			if (bitmap != null && !bitmap.isRecycled())
				return bitmap;
		}

		return null;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if (size() + sizeOf(url, bitmap) >= maxSize) {
			trimToSize(maxSize - sizeOf(url, bitmap));
		}

		if (usingDiskBasedCache) {
			final Entry entry = new Entry();

			ByteBuffer buffer = ByteBuffer.allocate(sizeOf(url, bitmap));
			bitmap.copyPixelsToBuffer(buffer);
			entry.data = buffer.array();
			diskCache.put(url, entry);
			LogUtil.e("BitmapLruCache", "putBitmap:" + url);
		}

		put(url, bitmap);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {

	}

	public void clear(boolean clearDiskCache) {
		evictAll();
		if (clearDiskCache && usingDiskBasedCache)
			diskCache.clear();
	}

}
