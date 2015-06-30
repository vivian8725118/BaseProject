package com.vivian.baseproject.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.vivian.baseproject.base.BaseApplication;
import com.vivian.baseproject.utils.LogUtil;
import com.vivian.baseproject.utils.StringUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 异常崩溃处理类 当程序发生未捕获异常时，由该类来接管程序并记录发送错误报告。
 */
public class CrashHandler implements UncaughtExceptionHandler {

	private static final String TAG = "CrashHandler";
	/** 错误日志文件名称 */
	public static final String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/btmedia/fileCache/crash/";
	static final String LOG_NAME = basePath + "crash" + System.currentTimeMillis() + ".txt";

	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;

	BaseApplication application;

	/** 用来存储设备信息和异常信息 */
	private Map<String, String> infos = new HashMap<String, String>();

	/**
	 * @brief 构造函数
	 * @details 获取系统默认的UncaughtException处理器，设置该CrashHandler为程序的默认处理器 。
	 * @param application
	 *            上下文
	 */
	public CrashHandler(BaseApplication application) {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.application = application;
	}

	/**
	 * @brief 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 如果用户没有处理则让系统默认的异常处理器来处理
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// 等待会后结束程序
			try {
				LogUtil.i(LOG_NAME, "exit start");
				Thread.sleep(3000);
				application.finishActivity();
				LogUtil.i(LOG_NAME, "exit end");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @brief 自定义错误处理，收集错误信息
	 * @details 发送错误报告等操作均在此完成
	 * @param ex
	 *            异常
	 * @return true：如果处理了该异常信息；否则返回false。
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return true;
		}
		ex.printStackTrace();
		// 提示错误消息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(application.getApplicationContext(), "应用发生异常，即将退出！", Toast.LENGTH_LONG).show();
				// Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(application);
		// 保存错误报告文件
		saveCrashInfoToFile(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				String deviceNumber = manager.getDeviceId() == null ? "null" : manager.getDeviceId();
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
				infos.put("padNumber", deviceNumber);
				// infos.put("crashTime", MathUtils.getStrTimeByLong(
				// System.currentTimeMillis(), "yyyy-MM-dd HH:mm"));
				// User user = UserManager.getUserManager().getDefaultUser();
				// infos.put("user_name", user.getUserName());
			}
		} catch (NameNotFoundException e) {
			LogUtil.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				LogUtil.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				LogUtil.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * @brief 保存错误信息到文件中
	 * @param ex
	 *            异常
	 */
	private void saveCrashInfoToFile(Throwable ex) {
		final StackTraceElement[] stack = ex.getStackTrace();
		final String message = ex.getMessage();
		/* 准备错误日志文件 */
		File logFile = new File(LOG_NAME);
		if (!logFile.getParentFile().exists()) {
			logFile.getParentFile().mkdirs();
		}
		/* 写入错误日志 */
		FileWriter fw = null;
		final String lineFeed = "\r\n";
		try {
			fw = new FileWriter(logFile, true);
			fw.write(StringUtil.currentTime(StringUtil.FORMAT_YMDHMS).toString() + lineFeed + lineFeed);
			fw.write(message + lineFeed);
			for (int i = 0; i < stack.length; i++) {
				fw.write(stack[i].toString() + lineFeed);
			}
			fw.write(lineFeed);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fw)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}