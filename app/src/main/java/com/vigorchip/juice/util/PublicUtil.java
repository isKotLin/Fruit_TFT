package com.vigorchip.juice.util;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;

import static com.vigorchip.juice.MyApplication.sWidthPix;

/**
 * Created by Administrator on 2017/2/5.
 */

public class PublicUtil {
    /**
     * 根据count来生成白线和指示器空格
     *
     * @param context
     * @param indicator_ll
     * @param count
     */
    public static ArrayList<View> addIndicator(Context context, LinearLayout indicator_ll, int count) {
        if (count < 1) {

            return null;
        }
        indicator_ll.removeAllViews();
        LinearLayout.LayoutParams lp_white = new LinearLayout.LayoutParams((int) (1 * MyApplication.sScale), (int) (10 * MyApplication.sScale));
        LinearLayout.LayoutParams lp_empty = new LinearLayout.LayoutParams(0, (int) (10 * MyApplication.sScale), 1);
        View view0 = new View(context);
        view0.setLayoutParams(lp_white);
        view0.setBackgroundColor(context.getResources().getColor(R.color.white));
        indicator_ll.addView(view0);
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View view_empty = new View(context);
            view_empty.setLayoutParams(lp_empty);
            View view = new View(context);
            view.setLayoutParams(lp_white);
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
            indicator_ll.addView(view_empty);
            indicator_ll.addView(view);
            views.add(view_empty);
        }
        return views;
    }

    /**
     * 根据count来生成白线和指示器空格
     *
     * @param context
     */
    public static void ChangeScollViewColor(Context context, ArrayList<View> scollviews, int curPosition) {
        if (scollviews == null) {
            return;
        }
        for (int i = 0; i < scollviews.size(); i++) {
            View view = scollviews.get(i);
            if (curPosition == i) {
                view.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            } else {
                view.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            }
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 动态隐藏软键盘
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 设置屏幕常亮和关闭屏幕常亮
     *
     * @param activity
     * @param flag
     */
    public static void keepScreenOn(Activity activity, boolean flag) {
        if (flag) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕常亮
        }
    }


    /**
     * 设置休眠时间 毫秒 180000
     */
    public static void setScreenOffTime(Context context, int paramInt) {
        Logutil.e("设置的休眠时间" + paramInt);
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
            Logutil.e("设置休眠时间出异常了啦啦啦啦");
        }
    }
}
