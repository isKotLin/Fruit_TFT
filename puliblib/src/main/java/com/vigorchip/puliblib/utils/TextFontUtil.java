package com.vigorchip.puliblib.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/1/15.
 */

public class TextFontUtil {
    /**
     * @param context
     * @param textView
     */
    public static void changeRobotoBold(Context context, TextView textView) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
        textView.setTypeface(typeFace);
    }


    /**
     * @param context
     * @param textView
     */
    public static void changeRobotoRegular(Context context, TextView textView) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        textView.setTypeface(typeFace);
    }

    /**
     * @param context
     * @param textView
     */
    public static void changeRobotoLight(Context context, TextView textView) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        textView.setTypeface(typeFace);
    }

    /**
     * @param context
     * @param textView
     */
    public static void changeRobotoBoldItalic(Context context, TextView textView) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-BoldItalic.ttf");
        textView.setTypeface(typeFace);
    }
}
