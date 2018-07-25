package com.vigorchip.juice.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vigorchip.juice.R;
import com.vigorchip.puliblib.utils.Logutil;

/**
 * Created by Administrator on 2017.05.23.
 */

public class CupStatusManager {
    // ChangeJarCupEvent  int mode;//0没有杯子 1小杯 2大杯
    public static boolean isBigCupStatus = true;
    public static boolean isSmallCupStatus = false;
    public static boolean isNoCupStatus = false;

    public static String MODESHOWTAG = "2";//记录是否展示的标志
    public static String CURCUP = "BIG";//当前杯子  BIG为包括跳转到大杯判断及以后的页面  SMALL为包括跳转到小杯判断及以后的页面

    public static void setBigCupStatus() {
        isBigCupStatus = true;
        isSmallCupStatus = false;
        isNoCupStatus = false;
    }

    public static void setSmallCupStatus() {
        isBigCupStatus = false;
        isSmallCupStatus = true;
        isNoCupStatus = false;
    }

    public static void setNoCupStatus() {
        isBigCupStatus = false;
        isSmallCupStatus = false;
        isNoCupStatus = true;
    }


    public static boolean isJarPutOk() {
        return isBigCupStatus || isSmallCupStatus;
    }

    /**
     * 显示无杯的弹框
     *
     * @param activity
     * @param isBigCup
     * @param listener
     */
    public static void showNOCupNoticeDialog(final Activity activity, boolean isBigCup, final OnFinishListener listener) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = null;
        if (isBigCup) {
            layout = inflater.inflate(R.layout.dialog_public_bigcup_notice, null);
        } else {
            layout = inflater.inflate(R.layout.dialog_public_smallcup_notice, null);
        }
        ImageView close_iv = (ImageView) layout.findViewById(R.id.close_iv);
        ImageView iv_cup_anim = (ImageView) layout.findViewById(R.id.iv_cup_anim);
        final Dialog showdialog = new Dialog(activity, R.style.DefaultDialog);
        showdialog.setContentView(layout);
        close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.isCloseLister(true);
                showdialog.dismiss();
            }
        });

        if (isBigCup) {
            iv_cup_anim.setImageResource(R.drawable.big_jar_anim);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_cup_anim.getDrawable();
            animationDrawable.start();
        } else {
            iv_cup_anim.setImageResource(R.drawable.small_jar_anim);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_cup_anim.getDrawable();
            animationDrawable.start();
        }

        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = showdialog.getWindow().getAttributes();
        showdialog.getWindow().setAttributes(lp);
        showdialog.setCanceledOnTouchOutside(false);
        showdialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        showdialog.setCancelable(false);
        showdialog.show();
    }

    public interface OnFinishListener {
        void isCloseLister(boolean isClose);
    }

}
