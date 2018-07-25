package com.vigorchip.juice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.util.CupStatusManager;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.juice.util.event.ChangeJarCupEvent;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.Logutil;
import com.vigorchip.puliblib.utils.event.ErrorEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/15.选择看流程还是直接制作
 */

public class SmallJudgeWorkActivity extends BaseActivity {

    private int mode;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_smalljudgework;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        mode = getIntent().getIntExtra("mode", 1);
        showJudgeJarDialog();
    }

    public void onEventMainThread(BackMenuEvent event) {
        finish();
    }


    /**
     * 判断容器放好没
     */
    public void showJudgeJarDialog() {
        if (CupStatusManager.isJarPutOk()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.dialog_smalljudgetap, null);
            final Dialog showdialog = new Dialog(this, R.style.DefaultDialog);
            showdialog.setContentView(layout);
            final ImageView close_iv = (ImageView) layout
                    .findViewById(R.id.close_iv);
            final ImageView judge_iv = (ImageView) layout
                    .findViewById(R.id.judge_iv);
            judge_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CupStatusManager.isSmallCupStatus) {
                        SmallShowWorkActivity.startActivity(SmallJudgeWorkActivity.this, mode);
                        finish();
                        showdialog.dismiss();
                    }
                }
            });
            close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showdialog.dismiss();
                    finish();
                }
            });
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
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
        } else {
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.dialog_smalljudgejar, null);
            final Dialog showdialog = new Dialog(this, R.style.DefaultDialog);
            showdialog.setContentView(layout);
            final ImageView close_iv = (ImageView) layout
                    .findViewById(R.id.close_iv);
            final ImageView judge_iv = (ImageView) layout
                    .findViewById(R.id.judge_iv);
            judge_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showdialog.dismiss();
                    showJudgeJarDialog();
                }
            });
            close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showdialog.dismiss();
                    finish();
                }
            });

            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
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

    }

    public void onEventMainThread(ChangeJarCupEvent event) {
        if (this.hasWindowFocus()) {

        }
    }


    public static void startActivity(Context context, int mode) {
        context.startActivity(new Intent(context, SmallJudgeWorkActivity.class).putExtra("mode", mode));
        Logutil.e("mode===========================" + mode);
    }

}
