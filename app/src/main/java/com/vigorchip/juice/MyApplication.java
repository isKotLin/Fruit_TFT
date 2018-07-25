package com.vigorchip.juice;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tencent.bugly.crashreport.CrashReport;
import com.vigorchip.juice.util.db.DataUtils;
import com.vigorchip.puliblib.utils.Logutil;


/**
 * Created by Administrator on 2017/2/3.
 */

public class MyApplication extends Application {
    public static SharedPreferences sp;
    public static float sScale;
    public static int sWidthPix;
    public static int sHeightPix;
    public static DataUtils dataHelper;
    public static long runAllTime;
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sScale = getResources().getDisplayMetrics().density;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
        sHeightPix = getResources().getDisplayMetrics().heightPixels;
        Logutil.e("scale=" + sScale);
        Logutil.e("sWidthPix=" + sWidthPix);
        Logutil.e("sHeightPix=" + sHeightPix);
        CrashReport.initCrashReport(getApplicationContext(), "06acfd6eaa", false);
        dataHelper = new DataUtils(this);
        getRunAllTime();
    }

    public static void setRunAllTime() {
        MyApplication.sp.edit().putLong("RUNALLTIME", runAllTime).commit();
    }

    public static void getRunAllTime() {
        runAllTime = MyApplication.sp.getLong("RUNALLTIME", 0);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
