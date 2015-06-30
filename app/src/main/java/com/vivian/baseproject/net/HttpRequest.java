package com.vivian.baseproject.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.vivian.baseproject.tools.SharedPMananger;
import com.vivian.baseproject.utils.ApplicationUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends StringRequest {

	Map<String, String> params;

	public HttpRequest(String url, Listener<String> listener, ErrorListener errorListener) {
		super(url, listener, errorListener);
	}

	public HttpRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

	public HttpRequest(int method, String url, Map<String, String> params, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		this.params = params;
	}

	/**
	 * 添加请求的头部 Accept
	 * app_key
	 * app_secret
	 * access_token - String
	 * device - String - 包括：“iphone“, “ipad”, “android”, “pc”, “h5”, “unknown”
	 * identifier - String 设备uuid
	 * app_version - String
	 * user_unique_key - String 如果用户登陆后，存放用户唯一识别码, 登陆接口会返回，如果未登陆，传空
	 */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();
		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		headers.put("Accept", "application/json");
		headers.put("device", "android");
		headers.put("app_version", ApplicationUtil.getVersionName());
		headers.put("identifier", ApplicationUtil.getDeviceId());
		// 新加的参数2015.5.7
		String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
		headers.put("ut", currentTime);
		return headers;
	}

	/**
	 * 添加通用的请求参数
	 */
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		Map<String, String> params = new HashMap<String, String>();
		return params;
	}
}
