package com.vigorchip.juice.util;

import android.text.TextUtils;

import com.vigorchip.juice.MyApplication;
import com.vigorchip.puliblib.utils.Logutil;

/**
 * Created by Administrator on 2017/2/3.
 */

public class RecentUtil {
    /**
     * 判断是否已保存
     *
     * @param recipeID
     * @return
     */
    public static boolean isCollect(String recipeID, String recents) {
        if (TextUtils.isEmpty(recents)) {
            recents = MyApplication.sp.getString("recents", "");
        }
        if (TextUtils.isEmpty(recents)) {
            return false;
        }
        String[] splits = recents.split(":");
        for (int i = 0; i < splits.length; i++) {
            if (!TextUtils.isEmpty(splits[i]) && splits[i].equals(recipeID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置收藏或取消收藏
     *
     * @param recipeID
     * @param back
     */

    public static void setCollect(String recipeID, CollectCallBack back) {
        if (TextUtils.isEmpty(recipeID)) {
            return;
        }
        String recents = MyApplication.sp.getString("recents", "");
        if (TextUtils.isEmpty(recents)) {
            MyApplication.sp.edit().putString("recents", recipeID).commit();
            Logutil.e("近期列表=" + recipeID);
            back.YesBack();
        } else {
            if (!isCollect(recipeID, recents)) {//不存在就保存
                String newCollects = recents + ":" + recipeID;
                MyApplication.sp.edit().putString("recents", newCollects).commit();
                Logutil.e("近期列表=" + newCollects);
                back.YesBack();
            }
        }
    }

    /**
     * 回調接口
     */
    public interface CollectCallBack {
        void YesBack();
    }
}
