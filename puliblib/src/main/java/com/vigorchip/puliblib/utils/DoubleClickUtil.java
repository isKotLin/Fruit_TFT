package com.vigorchip.puliblib.utils;

/**
 * Created by Administrator on 2017/1/15.
 */

public class DoubleClickUtil {
    private static long lastClickTime = 0;
    private static long DIFF = 500;
    private static int lastButtonId = -1;


    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isCanClick(int buttonId) {
        return isCanClick(buttonId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isCanClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return false;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return true;
    }
}