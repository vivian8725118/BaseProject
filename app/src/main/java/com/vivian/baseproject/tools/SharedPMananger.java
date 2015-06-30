package com.vivian.baseproject.tools;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPMananger {

	private static SharedPMananger instance;
	private SharedPreferences sp;
	private static Context mContext;
	public static boolean IsCommitVisible = true;
	// public static boolean IsPeopellingFirst = true;

	/** sharedPreference文件名 */
	private static final String PREFERENCE_NAME = "businessvalue.sh";

	/** key名 */
	// private static final String IS_NIGHT_MODE="is_night_mode";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String USER_UNIQUE_KEY = "user_unique_key";
	public static final String EXPIRATION = "expiration";

	public static final String SUBMIT_EMAIL = "submit_email";// 找回密码的邮箱或者手机
	public static final String SIGN_UP_EMAIL = "sign_up_email";// 注册邮箱
	public static final String SIGN_UP_PASSWORD = "sign_up_password";// 注册密码
	public static final String MESSAGE_CODE = "message_code";// 短信验证码

	// 用户信息常量
	public static final String LAST_USER_GUID = "last_user_guid";
	public static final String LAST_USER_NAME = "last_user_name";
	public static final String BV_TEXTSIZE = "text_size";
	public static final String FONT_SIZE_NORMAL = "normal";
	public static final String FONT_SIZE_SMALL = "small";
	public static final String FONT_SIZE_BIG = "big";


	public static void init(Context context) {
		mContext = context;
	}

	public SharedPMananger(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		}
	}

	public static SharedPMananger getInstance() {
		if (instance == null) {
			instance = new SharedPMananger(mContext);
		}
		return instance;
	}

	public boolean putString(String key, String value) {
		sp.edit().putString(key, value).commit();
		return true;
	}

	public String getString(String key) {
		return sp.getString(key, "");
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public void putBoolean(String key, boolean bool) {
		sp.edit().putBoolean(key, bool).commit();
	}

	public void putInt(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	public int getInt(String key) {
		return sp.getInt(key, 0);
	}



}
