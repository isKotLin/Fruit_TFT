package com.vigorchip.puliblib.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/1/14.简单的log
 */

public class Logutil {
    public static boolean isDEBUG = true;

    public static void e(Object message) {
        if (isDEBUG) {
            Log.e("ME", message == null ? "空" : message.toString());
        }
    }

    public static void e(String tag, Object message) {
        if (isDEBUG) {
            Log.e("" + tag, message == null ? "空" : message.toString());
        }
    }
    public static void i(Object message) {
        if (isDEBUG) {
            Log.i("ME", message == null ? "空" : message.toString());
        }
    }

    public static void i(String tag, Object message) {
        if (isDEBUG) {
            Log.i("" + tag, message == null ? "空" : message.toString());
        }
    }

}
