package com.vivian.baseproject.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.vivian.baseproject.base.BaseApplication;
import com.vivian.baseproject.net.multipart.MultipartEntity;
import com.vivian.baseproject.net.multipart.MultipartRequest;
import com.vivian.baseproject.tools.BitmapLruCache;
import com.vivian.baseproject.tools.SharedPMananger;
import com.vivian.baseproject.utils.DialogUtil;
import com.vivian.baseproject.utils.LogUtil;
import com.vivian.baseproject.utils.NetWorkCheckUtil;
import com.vivian.baseproject.utils.SDCardUtil;
import com.vivian.baseproject.utils.ScreenSizeUtil;
import com.vivian.baseproject.utils.UrlUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class VolleyUtil {
	private static final String TAG = "VolleyUtil";
	private static VolleyUtil instance;
	private final Context mContext;
	private final RequestQueue mQueue;
	private final RequestQueue mImageQueue;
	private final ImageLoader mImageLoader;
	private ImageListener mImageListener;
	private final BitmapLruCache mImageCache;
	private static final int MAX_LRU_IMAGE_CACHE = (int) ((BaseApplication.getInstance().getRuntimeMemory() / 10));

	public void cancelRequest() {
		mQueue.cancelAll("get");
		mQueue.cancelAll("post");
		mQueue.cancelAll("put");
		mQueue.cancelAll("delete");
	}

	public void showDialog(int strId) {
		try {
			DialogUtil.getDialog().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showDialog() {
		try {
			DialogUtil.getDialog().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showDialog(Context context) {
		try {
			DialogUtil.getDialog().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private VolleyUtil(Context context) {
		mContext = context;
		mQueue = Volley.newRequestQueue(mContext);
		mImageQueue = Volley.newRequestQueue(mContext);
		//image路径可以改
		mImageCache = new BitmapLruCache(MAX_LRU_IMAGE_CACHE, 50 * 1024 * 1024, new File("/image", "volley"));// 50M硬件缓存
		mImageLoader = new ImageLoader(mImageQueue, mImageCache);
	}

	public static VolleyUtil getInstance(Context context) {
		if (instance == null) {
			instance = new VolleyUtil(context);
		}
		return instance;
	}

	/**
	 * 文章内页可以缓存
	 * 
	 * @param url
	 */
	public void get(String url, final Map<String, String> map, BaseHandle handle, boolean showDialog, boolean isCached) {
		StringBuilder builder = new StringBuilder(url);
		builder.append("?");
		Set<String> keys = map.keySet();
		Iterator<String> iterator = keys.iterator();
		int j = 0;
		for (int i = 0; i < map.size(); i++) {
			String key = iterator.next();
			String value = map.get(key);
			if (!TextUtils.isEmpty(value)) {
				builder.append(key);
				builder.append("=");
				builder.append(map.get(key));
			}
			if (!TextUtils.isEmpty(value) && i < map.size() - 1) {
				builder.append("&");
			}
		}
		Log.i("tag", builder.toString());

		HttpRequest getRequest = new HttpRequest(Method.GET, builder.toString(), handle.listener, handle.errorListener);
		getRequest.setShouldCache(isCached);
		getRequest.shouldCache();
		getRequest.setTag("get");
		if (mQueue.getCache().get(getRequest.getCacheKey()) == null || !isCached) {
			if (NetWorkCheckUtil.checkNet(mContext)) {
				if (showDialog) {
					showDialog();// 显示等待框
				}
				mQueue.add(getRequest);
			} else {
				Toast.makeText(mContext,"网络连接失败",Toast.LENGTH_SHORT).show();
			}
		} else if (mQueue.getCache().get(getRequest.getCacheKey()) != null) {// 有缓存
			handle.listener.onResponse(new String(mQueue.getCache().get(getRequest.getCacheKey()).data));
		}
	}

	// 检查文章是否缓存过
	public boolean isCached(String url) {
		if (mQueue.getCache() == null) {
			return false;
		} else {
			try {
				return mQueue.getCache().get(url).data == null ? false : true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * get请求:默认显示对话框
	 *
	 * @param url
	 */
	public void get(boolean showDialog, String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			if (showDialog) {
				showDialog(mContext);// 显示等待框
			}
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");
			Set<String> keys = map.keySet();
			Iterator<String> iterator = keys.iterator();
			int j = 0;
			for (int i = 0; i < map.size(); i++) {
				String key = iterator.next();
				String value = map.get(key);
				if (!TextUtils.isEmpty(value)) {
					builder.append(key);
					builder.append("=");
					builder.append(map.get(key));
				}
				if (!TextUtils.isEmpty(value) && i < map.size() - 1) {
					builder.append("&");
				}
			}
			Log.i("tag", builder.toString());
			HttpRequest getRequest = new HttpRequest(Method.GET, builder.toString(), handle.listener, handle.errorListener);
			getRequest.setShouldCache(false);
			getRequest.shouldCache();
			getRequest.setTag("get");
			mQueue.add(getRequest);

		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	public void get(String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			showDialog();// 显示等待框
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");
			Set<String> keys = map.keySet();
			Iterator<String> iterator = keys.iterator();
			int j = 0;
			for (int i = 0; i < map.size(); i++) {
				String key = iterator.next();
				String value = map.get(key);
				if (!TextUtils.isEmpty(value)) {
					builder.append(key);
					builder.append("=");
					builder.append(map.get(key));
				}
				if (!TextUtils.isEmpty(value) && i < map.size() - 1) {
					builder.append("&");
				}
			}
			Log.i("tag", builder.toString());
			HttpRequest getRequest = new HttpRequest(Method.GET, builder.toString(), handle.listener, handle.errorListener);
			getRequest.setShouldCache(true);
			getRequest.shouldCache();
			getRequest.setTag("get");
			mQueue.add(getRequest);

		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	// 专门为兑吧写的，不做缓存的get请求
	public void get(boolean isCache, boolean showDialog, String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			if (showDialog) {
				showDialog();// 显示等待框
			}
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");
			Set<String> keys = map.keySet();
			Iterator<String> iterator = keys.iterator();
			int j = 0;
			for (int i = 0; i < map.size(); i++) {
				String key = iterator.next();
				String value = map.get(key);
				if (!TextUtils.isEmpty(value)) {
					builder.append(key);
					builder.append("=");
					builder.append(map.get(key));
				}
				if (!TextUtils.isEmpty(value) && i < map.size() - 1) {
					builder.append("&");
				}
			}
			Log.i("tag", builder.toString());
			HttpRequest getRequest = new HttpRequest(Method.GET, builder.toString(), handle.listener, handle.errorListener);
			if (isCache) {
				getRequest.setShouldCache(true);
				getRequest.shouldCache();
			}
			getRequest.setTag("get");
			mQueue.add(getRequest);

		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param map
	 */
	public void post(String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			showDialog();// 显示等待框
			HttpRequest postRequest = new HttpRequest(Method.POST, url, handle.listener, handle.errorListener) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					map.putAll(super.getParams());
					return map;

				}
			};
			postRequest.setTag("post");
			mQueue.add(postRequest);
		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	public void post(boolean isShowingDialog, String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			if (isShowingDialog) {
				showDialog();// 显示等待框
			}
			HttpRequest postRequest = new HttpRequest(Method.POST, url, handle.listener, handle.errorListener) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					map.putAll(super.getParams());
					return map;
				}
			};
			postRequest.setTag("post");
			mQueue.add(postRequest);
		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * put请求
	 * 
	 * @param url
	 */
	public void put(String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			showDialog();// 显示等待框
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");
			Set<String> keys = map.keySet();
			Iterator<String> iterator = keys.iterator();
			int j = 0;
			for (int i = 0; i < map.size(); i++) {
				String key = iterator.next();
				String value = map.get(key);
				if (!TextUtils.isEmpty(value)) {
					builder.append(key);
					builder.append("=");
					builder.append(map.get(key));
				}
				if (!TextUtils.isEmpty(value) && i < map.size() - 1) {
					builder.append("&");
				}
			}
			Log.i("tag", builder.toString());

			HttpRequest putRequest = new HttpRequest(Method.PUT, builder.toString(), handle.listener, handle.errorListener);
			putRequest.setShouldCache(true);
			putRequest.shouldCache();
			putRequest.setTag("put");
			mQueue.add(putRequest);

		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	public void put(boolean isShow, String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			if (isShow) {
				showDialog();// 显示等待框
			}
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");
			Set<String> keys = map.keySet();
			Iterator<String> iterator = keys.iterator();
			int j = 0;
			for (int i = 0; i < map.size(); i++) {
				String key = iterator.next();
				String value = map.get(key);
				if (!TextUtils.isEmpty(value)) {
					builder.append(key);
					builder.append("=");
					builder.append(map.get(key));
				}
				if (!TextUtils.isEmpty(value) && i < map.size() - 1) {
					builder.append("&");
				}
			}
			Log.i("tag", builder.toString());
			HttpRequest putRequest = new HttpRequest(Method.PUT, builder.toString(), handle.listener, handle.errorListener);
			putRequest.setShouldCache(true);
			putRequest.shouldCache();
			putRequest.setTag("put");
			mQueue.add(putRequest);

		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * delete请求
	 * 
	 * @param url
	 */
	public void delete(String url, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			showDialog();// 显示等待框
			HttpRequest deleteRequest = new HttpRequest(Method.DELETE, url, handle.listener, handle.errorListener) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					return super.getParams();
				}
			};
			deleteRequest.setTag("delete");
			mQueue.add(deleteRequest);
		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	public void deleteOfPost(String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			showDialog();// 显示等待框
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");
			Set<String> keys = map.keySet();
			Iterator<String> iterator = keys.iterator();
			int j = 0;
			for (int i = 0; i < map.size(); i++) {
				String key = iterator.next();
				String value = map.get(key);
				if (!TextUtils.isEmpty(value)) {
					builder.append(key);
					builder.append("=");
					builder.append(map.get(key));
				}
				if (!TextUtils.isEmpty(value) && i < map.size() - 1) {
					builder.append("&");
				}
			}
			Log.i("tag", builder.toString());
			HttpRequest putRequest = new HttpRequest(Method.DELETE, builder.toString(), handle.listener, handle.errorListener);
			putRequest.setShouldCache(true);
			putRequest.shouldCache();
			putRequest.setTag("put");
			mQueue.add(putRequest);

		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	public void delete(String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			showDialog();// 显示等待框
			HttpRequest deleteRequest = new HttpRequest(Method.DELETE, url, handle.listener, handle.errorListener) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					map.putAll(super.getParams());
					return map;
				}
			};
			deleteRequest.setTag("delete");
			mQueue.add(deleteRequest);
		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	public void deleteAppend(boolean isShow, String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			if (isShow) {
				showDialog();// 显示等待框
			}
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");
			Set<String> keys = map.keySet();
			Iterator<String> iterator = keys.iterator();
			int j = 0;
			for (int i = 0; i < map.size(); i++) {
				String key = iterator.next();
				String value = map.get(key);
				if (!TextUtils.isEmpty(value)) {
					builder.append(key);
					builder.append("=");
					builder.append(map.get(key));
				}
				if (!TextUtils.isEmpty(value) && i < map.size() - 1) {
					builder.append("&");
				}
			}
			Log.i("tag", builder.toString());
			HttpRequest putRequest = new HttpRequest(Method.DELETE, builder.toString(), handle.listener, handle.errorListener);
			putRequest.setShouldCache(true);
			putRequest.shouldCache();
			putRequest.setTag("put");
			mQueue.add(putRequest);

		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	public void delete(boolean isShowingDialog, String url, final Map<String, String> map, BaseHandle handle) {
		if (NetWorkCheckUtil.checkNet(mContext)) {
			if (isShowingDialog) {
				showDialog();// 显示等待框
			}
			HttpRequest deleteRequest = new HttpRequest(Method.DELETE, url, handle.listener, handle.errorListener) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					map.putAll(super.getParams());
					return map;
				}
			};
			deleteRequest.setTag("delete");
			mQueue.add(deleteRequest);
		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
		}
	}

	public ImageListener getImageListener(ImageView imageView) {
		// mImageListener = ImageLoader.getImageListener(imageView, R.drawable.ic_launcher, R.drawable.ic_launcher);
		mImageListener = ImageLoader.getImageListener(imageView, 0, 0);
		return mImageListener;
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param imageView
	 */
	public void getImage(String url, ImageView imageView) {
		// ImageListener listener = getImageListener(imageView);
		// mImageLoader.get(url, listener);
		try {
			imageRequest(url, imageView, 0, 0, null, ScreenSizeUtil.getScreenWidth(mContext), ScreenSizeUtil.getScreenHeight(mContext));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void getImage(String url, ImageView imageView, int width, int height) {
		ImageListener listener = getImageListener(imageView);
		mImageLoader.get(url, listener, width, height);
	}

	// 离线下载：下载图片
	public void downImage(final String url, final Boolean flag) {
		try {
			ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
				@Override
				public void onResponse(Bitmap bitmap) {
					String filename = SDCardUtil.getInstance(mContext).parseUrl(url);
					if (flag) {
						SDCardUtil.getInstance(mContext).SaveToSdCard(filename, bitmap);
					} else {
						SDCardUtil.getInstance(mContext).SaveToSdCards(filename, bitmap);
					}

				}
			}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					// Toast.makeText(context, "保存失败",
					// Toast.LENGTH_SHORT).show();
				}
			});
			mImageQueue.add(imageRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imageRequest(final String url, final ImageView imageView, int preLoadRes, final int errorLoadRes, final ImageListener listener, final int maxWidth, final int maxHeight) {

		int rWidth = maxWidth == -1 ? ScreenSizeUtil.getScreenWidth(mContext) : maxWidth;
		int rHeight = maxHeight == -1 ? ScreenSizeUtil.getScreenHeight(mContext) : maxHeight;

		long size = rWidth * rHeight * 4;
		if (Runtime.getRuntime().freeMemory() <= size * 2) {
			Log.d("msize", Runtime.getRuntime().freeMemory() + ":" + size * 2);
			System.gc();
			mImageCache.trimToSize((int) (mImageCache.size() - size));
		}
		if (preLoadRes != 0) {
			if (imageView.getDrawingCache() != null) {
				Bitmap b = imageView.getDrawingCache();
				imageView.setImageBitmap(null);
				b.recycle();
				b = null;
			}
			imageView.setImageResource(preLoadRes);
		}

		final String encodedUrl = UrlUtil.encodeCH(url);

		if (imageView.getTag() != null) {
			ImageRequest oldRequest = (ImageRequest) imageView.getTag();
			oldRequest.cancel();
			imageView.setTag(null);
			oldRequest = null;
		}

		Bitmap bitmap = mImageCache.getBitmap(encodedUrl);
		if (bitmap != null) {
			if (imageView.getDrawingCache() != null) {
				Bitmap b = imageView.getDrawingCache();
				imageView.setImageBitmap(null);
				b.recycle();
				b = null;
			}
			imageView.setImageBitmap(bitmap);
			return;
		}

		ImageRequest imageRequest = new ImageRequest(encodedUrl, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap bitmap) {
				if (imageView.getDrawingCache() != null) {
					Bitmap b = imageView.getDrawingCache();
					imageView.setImageBitmap(null);
					b.recycle();
					b = null;
				}
				imageView.setImageBitmap(bitmap);
				mImageCache.putBitmap(encodedUrl, bitmap);
				if (listener != null) {
					ImageLoader.ImageContainer imageContainer = mImageLoader.new ImageContainer(bitmap, encodedUrl, null, null);
					listener.onResponse(imageContainer, true);
				}
			}
		}, maxWidth == -1 ? ScreenSizeUtil.getScreenWidth(mContext) : maxWidth, maxHeight == -1 ? 0 : maxHeight, Config.RGB_565, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (imageView.getDrawingCache() != null) {
					Bitmap b = imageView.getDrawingCache();
					imageView.setImageBitmap(null);
					b.recycle();
					b = null;
				}

				System.gc();

				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inJustDecodeBounds = true;
				opt.inPreferredConfig = Config.RGB_565;
				Bitmap errorBitmap = BitmapFactory.decodeResource(mContext.getResources(), errorLoadRes, opt);
				int bitmapWidth = opt.outWidth;
				int bitmapHeight = opt.outHeight;

				int widgetWidth = maxWidth == -1 ? ScreenSizeUtil.getScreenWidth(mContext) : maxWidth;
				int widgetHeight = maxHeight == -1 ? 0 : maxHeight;

				opt.inJustDecodeBounds = false;
				opt.inSampleSize = widgetWidth < bitmapWidth ? (int) Math.ceil(bitmapWidth / widgetWidth) : 1;
				errorBitmap = BitmapFactory.decodeResource(mContext.getResources(), errorLoadRes, opt);

				imageView.setImageBitmap(errorBitmap);
				if (listener != null)
					listener.onErrorResponse(error);
			}

		});
		imageRequest.setShouldCache(true);

		try {
			mImageQueue.add(imageRequest);

			imageView.setTag(imageRequest);
		} catch (OutOfMemoryError error) {
			Log.d("msize", Runtime.getRuntime().freeMemory() + "");
			System.gc();
			wipeImageCache(false);
			Toast.makeText(mContext,"亲，手机内存不够用了T_T",Toast.LENGTH_SHORT).show();

		}

	}

	public void wipeImageCache(boolean clearDiskCache) {
		mImageQueue.getCache().clear();
		mImageCache.clear(clearDiskCache);
	}







	private String readStream(InputStream is) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i = is.read();
			while (i != -1) {
				bo.write(i);
				i = is.read();
			}
			return bo.toString();
		} catch (IOException e) {
			return "";
		}
	}



	/**
	 * 上传头像
	 * 
	 * @param data
	 * @param listener
	 * @param errorListener
	 */
	public void uploadPhoto(byte[] data, Listener<String> listener, ErrorListener errorListener) {
		String user_unique_key = "";
		String path="";//上传图片的path
		if (NetWorkCheckUtil.checkNet(mContext)) {
			MultipartRequest request = new MultipartRequest(path, listener, errorListener);

			// 添加header
			// request.addHeader("Content-Type", "multipart/form-data");
			request.addHeader("device", "android");
			request.addHeader("Accept", "application/json");

			// 通过MultipartEntity来设置参数
			MultipartEntity multi = request.getMultiPartEntity();
			// 直接从上传Bitmap
			multi.addBinaryPart("avatar", data);
			LogUtil.e(TAG, data.toString());
			// 将请求添加到队列中
			mQueue.add(request);
			LogUtil.d("mQueue", "did add request");
		} else {
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT);
		}
	}



}
