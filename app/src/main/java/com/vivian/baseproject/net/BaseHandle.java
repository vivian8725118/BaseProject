package com.vivian.baseproject.net;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.vivian.baseproject.utils.JSONparseUtil;
import com.vivian.baseproject.utils.DialogUtil;
import com.vivian.baseproject.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseHandle {
	private static final String TAG = "BaseHandle";
	public Listener<String> listener;
	public ErrorListener errorListener;

	protected static Context mContext;

	public static void init(Context context) {
		mContext = context;
	}

	public BaseHandle() {
		listener = new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				onSuccessHandle(arg0);
				dismissDialog();
			}
		};
		errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				onErrorHandle(arg0);
				dismissDialog();
			}
		};
	}

	public void dismissDialog() {
		if (DialogUtil.getDialog() != null && DialogUtil.getDialog().isShowing()) {
			DialogUtil.getDialog().dismiss();
		}
	}

	/**
	 * 处理请求成功的情况，需要重写
	 * 
	 * @param content
	 */
	public void onSuccessHandle(String content) {
		LogUtil.e("test", content);
		try {
			JSONObject obj = new JSONObject(content);
			String result = obj.getString("result");
			if (result.equals("ok")) {
				ok(content);
			} else {
				not_ok(content);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 处理请求失败的情况，不重写则默认统一处理
	 * 统一失败弹出toast提示
	 * 
	 * @param error
	 */
	public void onErrorHandle(VolleyError error) {
		// if (error instanceof com.android.volley.ServerError) {
		// BtToast.makeText("请求服务器失败");
		// } else
		error.printStackTrace();
		if (error instanceof com.android.volley.TimeoutError) {
			Toast.makeText(mContext,"网络超时",Toast.LENGTH_SHORT);
		} else if (error.networkResponse != null) {
			byte[] htmlBodyBytes = error.networkResponse.data;
			String str = new String(htmlBodyBytes);
			String errorMsg = JSONparseUtil.getErrorMessage(str);
			LogUtil.e(TAG, errorMsg);
			try {
				JSONObject object = new JSONObject(str);
				if (object.getString("result").equals("ok")) {// 201那种情况
					ok(str);
				} else {
					not_ok(str);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT);
			}
		} else {
			// if (!TextUtils.isEmpty(error.getMessage())) {
			// BtToast.makeText(error.getMessage());
			// }
			Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT);
		}
		if (DialogUtil.getDialog() != null && DialogUtil.getDialog().isShowing()) {
			DialogUtil.getDialog().dismiss();
		}

	}

	/**
	 * 返回ok的情况
	 */
	public void ok(String message) {
		// {"result":"ok","data":{},"cursor":{},"success":"\u7528\u6237\u521b\u5efa\u6210\u529f","errors":[]}
		if (DialogUtil.getDialog() != null) {
			DialogUtil.getDialog().dismiss();
		}
		String succesMessage = JSONparseUtil.getSucessMessage(message);
		if (!TextUtils.isEmpty(succesMessage)) {
			Toast.makeText(mContext, succesMessage, Toast.LENGTH_SHORT);
		}
		LogUtil.e(succesMessage);
	}

	/**
	 * 返回not_ok的情况
	 * 
	 * @param message
	 */
	public void not_ok(String message) {
		// {"result":"no_ok","data":{},"cursor":{},"success":"","errors":[{"field":"invalid_client","message":"The client credentials are invalid"}]}
		if (DialogUtil.getDialog() != null && DialogUtil.getDialog().isShowing()) {
			DialogUtil.getDialog().dismiss();
		}
		String errorMessage = JSONparseUtil.getErrorMessage(message);
		Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT);
		LogUtil.e(errorMessage);
		// 黑名单用户,退出登录
		String errorField = JSONparseUtil.getErrorField(message);

	}
}
