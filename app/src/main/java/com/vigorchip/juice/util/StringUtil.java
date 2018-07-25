package com.vigorchip.juice.util;

/**
 * Created by Administrator on 2017/1/17.
 */

public class StringUtil {
    /**
     * 装换时间
     *
     * @param all_time
     * @return
     */
    public static String changeTime(String all_time) {
        try {
            int time_int = Integer.valueOf(all_time);
            if (time_int <= 0) {
                return "0\"";
            }
            int min = time_int / 60;
            int sec = time_int % 60;
            if (min == 0) {
                return sec + "\"";
            } else {
                return min + "\'" + sec + "\"";
            }

        } catch (Exception e) {
            return all_time;
        }

    }

    /**
     * 装换时间
     *
     * @param time1 ,time2
     * @return
     */
    public static String changeTime(String time1, String time2) {
        try {
            int time_int = Integer.valueOf(time1) + Integer.valueOf(time2);
            if (time_int <= 0) {
                return "0\"";
            }
            int min = time_int / 60;
            int sec = time_int % 60;
            if (min == 0) {
                return sec + "\"";
            } else {
                return min + "\'" + sec + "\"";
            }

        } catch (Exception e) {
            return time1;
        }

    }
}
