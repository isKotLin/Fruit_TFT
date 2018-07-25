package com.vigorchip.puliblib.utils;

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
import android.widget.TextView;

import com.zhongjia.puliblib.R;


/**
 * Created by Administrator on 2017/5/31.
 */

public class MachineErrorManager {

    public static String ErrorMessage = "E0";

    public static void showE2Dialog(final Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.dialog_show_e2, null);
        final Dialog showdialog = new Dialog(activity, R.style.DefaultDialog);
        showdialog.setContentView(layout);
        final ImageView close_iv = (ImageView) layout.findViewById(R.id.close_iv);
        TextView text1_tv = (TextView) layout.findViewById(R.id.text1_tv);
        TextView text2_tv = (TextView) layout.findViewById(R.id.text2_tv);
        TextFontUtil.changeRobotoBold(activity, text1_tv);
        TextFontUtil.changeRobotoRegular(activity, text2_tv);
        close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog.dismiss();
            }
        });
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = showdialog.getWindow().getAttributes();
        lp.height = display.getHeight();
        lp.width = display.getWidth();
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

    public static void showE3Dialog(final Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.dialog_show_e3, null);
        final Dialog showdialog = new Dialog(activity, R.style.DefaultDialog);
        showdialog.setContentView(layout);
        final ImageView close_iv = (ImageView) layout.findViewById(R.id.close_iv);
        final ImageView overload_iv = (ImageView) layout.findViewById(R.id.overload_iv);
        TextView text1_tv = (TextView) layout.findViewById(R.id.text1_tv);
        TextView text2_tv = (TextView) layout.findViewById(R.id.text2_tv);
        TextFontUtil.changeRobotoBold(activity, text1_tv);
        TextFontUtil.changeRobotoRegular(activity, text2_tv);
        overload_iv.setImageResource(R.drawable.overload_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) overload_iv.getDrawable();
        animationDrawable.start();
        close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog.dismiss();
            }
        });
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = showdialog.getWindow().getAttributes();
        lp.height = display.getHeight();//45
        lp.width = display.getWidth();
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

    public static boolean isMachineOK() {
        if (ErrorMessage.equals("E0")) {
            return true;
        }
        return false;
    }
}
