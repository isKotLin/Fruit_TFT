package com.vigorchip.juice.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.event.CollectEvent;
import com.vigorchip.puliblib.utils.Logutil;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/3.
 */

public class FavouriteUtil {
    /**
     * 判断是否已收藏
     *
     * @param recipeID
     * @return
     */
    public static boolean isCollect(String recipeID, String collects) {
        if (TextUtils.isEmpty(collects)) {
            collects = MyApplication.sp.getString("collects", "");
        }
        if (TextUtils.isEmpty(collects)) {
            return false;
        }
        String[] splits = collects.split(":");
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
        String collects = MyApplication.sp.getString("collects", "");
        if (TextUtils.isEmpty(collects)) {
            MyApplication.sp.edit().putString("collects", recipeID).commit();
            Logutil.e("收藏列表=" + recipeID);
            EventBus.getDefault().post(new CollectEvent(true, recipeID));
            back.YesBack();
        } else {
            if (isCollect(recipeID, collects)) {//去掉
                String[] splits = collects.split(":");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < splits.length; i++) {
                    if (!TextUtils.isEmpty(splits[i]) && !recipeID.equals(splits[i])) {
                        sb.append(":" + splits[i]);
                    }
                }
                MyApplication.sp.edit().putString("collects", sb.toString()).commit();
                Logutil.e("收藏列表=" + collects);
                EventBus.getDefault().post(new CollectEvent(false, recipeID));
                back.NoBack();
            } else {
                String newCollects = collects + ":" + recipeID;
                MyApplication.sp.edit().putString("collects", newCollects).commit();
                Logutil.e("收藏列表=" + newCollects);
                EventBus.getDefault().post(new CollectEvent(true, recipeID));
                back.YesBack();
            }
        }
    }

    /**
     * 回調接口
     */
    public interface CollectCallBack {
        void YesBack();

        void NoBack();
    }

    public static void showCollectPop(Activity activity, View v, final boolean iscollect) {
        View popupView = activity.getLayoutInflater().inflate(R.layout.layout_showcollect_pop, null);
        final ImageView iv_favourite = (ImageView) popupView.findViewById(R.id.iv_favourite);
        final PopupWindow mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //动画效果参数直接定义
        final Animation animation1 = new AlphaAnimation(0.5f, 1.0f);
        animation1.setDuration(500);
        final Animation animation2 = new AlphaAnimation(1, 0.0f);
        animation2.setDuration(500);

        if (iscollect) {
            iv_favourite.setImageResource(R.drawable.img_favorite_animation_01);
        } else {
            iv_favourite.setImageResource(R.drawable.img_favorite_animation_02);
        }
        iv_favourite.setAnimation(animation1);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_favourite.setAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        final Handler mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (iscollect) {
                    iv_favourite.setImageResource(R.drawable.img_favorite_animation_02);
                } else {
                    iv_favourite.setImageResource(R.drawable.img_favorite_animation_01);
                }
                iv_favourite.setAnimation(animation1);
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }
                    }
                }, 1000);
            }
        }, 1000);
    }
}
