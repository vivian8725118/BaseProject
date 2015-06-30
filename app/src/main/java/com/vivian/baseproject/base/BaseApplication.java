package com.vivian.baseproject.base;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.vivian.baseproject.tools.CrashHandler;
import com.vivian.baseproject.utils.LogUtil;

import java.util.LinkedList;

public class BaseApplication extends Application implements OnSharedPreferenceChangeListener {
    private static final String TAG = "BaseApplication";
    private static BaseApplication mApplication;
    private static LinkedList<Activity> activityList;
    private Activity activity;
    private long runtimeMemory;

    public static BaseApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        mApplication = this;
        // if (!BuildConfig.DEBUG) {
		/* 全局异常崩溃处理 */
        CrashHandler catchExcep = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
        // }
        activityList = new LinkedList<Activity>();
        super.onCreate();
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        activityList.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        activityList.add(a);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
        activityList.clear();
        // 退出程序
        System.exit(1);
        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    public long getRuntimeMemory() {
        if (runtimeMemory == -1L || runtimeMemory == 0L) {
            runtimeMemory = Runtime.getRuntime().maxMemory();
        }

        LogUtil.d("runtime_memory", runtimeMemory / 1024 / 1024 + "MB");
        return runtimeMemory;
    }
}
