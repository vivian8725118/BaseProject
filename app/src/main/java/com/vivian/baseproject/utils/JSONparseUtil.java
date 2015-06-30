package com.vivian.baseproject.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JSONparseUtil {

	// {"result":"no_ok","data":{},"cursor":{},"success":"","errors":[{"field":"invalid_request","message":"The \"username\" or \"password\" is incorrect"}]}
	/**
	 * 登录信息错误返回值解析
	 * 
	 * @param content
	 * @return
	 */
	public static String getErrorMessage(String content) {
		if (content != null) {

			try {
				JSONObject obj = new JSONObject(content);
				JSONArray resultArr = obj.optJSONArray("errors");
				if (resultArr.length() >= 0) {
					JSONObject o = resultArr.getJSONObject(0);
					String errorMessage = o.getString("message");
					return errorMessage;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 登录信息错误返回值解析
	 * 
	 * @param content
	 * @return
	 */
	public static String getErrorField(String content) {
		if (content != null) {

			try {
				JSONObject obj = new JSONObject(content);
				JSONArray resultArr = obj.optJSONArray("errors");
				if (resultArr.length() >= 0) {
					JSONObject o = resultArr.getJSONObject(0);
					String errorMessage = o.getString("field");
					return errorMessage;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	// {"result":"ok","data":{},"cursor":{},"success":"\u9a8c\u8bc1\u94fe\u63a5\u5df2\u7ecf\u53d1\u9001\u5230\u60a8\u7684\u7535\u5b50\u90ae\u7bb1\uff0c\u8bf7\u67e5\u6536","errors":[]}
	/**
	 * 解析成功信息返回值
	 * 
	 * @param content
	 * @return
	 */
	public static String getSucessMessage(String content) {
		try {
			JSONObject obj = new JSONObject(content);
			String success = obj.getString("success");
			return success;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "未知原因";
	}

	public static String getKey(JSONObject obj) {
		Iterator<String> iterator = obj.keys();
		String key = null;
		if (iterator.hasNext()) {
			key = iterator.next();
		}
		return key;
	}
}
