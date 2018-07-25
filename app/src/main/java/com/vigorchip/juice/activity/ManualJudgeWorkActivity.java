package com.vigorchip.juice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import com.vigorchip.puliblib.base.BaseActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/15.选择看流程还是直接制作
 */

public class ManualJudgeWorkActivity extends BaseActivity {

    private int mode;
    private int manualTime;
    private int manualSpeed;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigjudgework;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        mode = getIntent().getIntExtra("mode", 0);
        manualTime = getIntent().getIntExtra("manualTime", 0);
        manualSpeed = getIntent().getIntExtra("manualSpeed", 0);
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
            View layout = inflater.inflate(R.layout.dialog_bigjudgetap, null);
            final Dialog showdialog = new Dialog(this, R.style.DefaultDialog);
            showdialog.setContentView(layout);
            final ImageView close_iv = (ImageView) layout
                    .findViewById(R.id.close_iv);
            final ImageView judge_iv = (ImageView) layout
                    .findViewById(R.id.judge_iv);
            ImageView iv_tap_anim = (ImageView) layout.findViewById(R.id.iv_tap_anim);
            iv_tap_anim.setImageResource(R.drawable.big_tap_anim);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_tap_anim.getDrawable();
            animationDrawable.start();
            judge_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CupStatusManager.isBigCupStatus) {
                        showdialog.dismiss();
                        if (mode == 0) {
                            ManualShowWorkActivity.startActivity(ManualJudgeWorkActivity.this, manualTime, manualSpeed);
                        } else {
                            ManualShowWorkCustomActivity.startActivity(ManualJudgeWorkActivity.this, mode);
                        }
                        finish();
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
            View layout = inflater.inflate(R.layout.dialog_bigjudgejar, null);
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
            ImageView iv_big_jar = (ImageView) layout.findViewById(R.id.iv_big_jar);
            iv_big_jar.setImageResource(R.drawable.big_jar_anim);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_big_jar.getDrawable();
            animationDrawable.start();

            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = showdialog.getWindow().getAttributes();
//            lp.height = display.getHeight() / 128 * 36;//45
//            lp.width = display.getWidth() / 6 * 5;
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


    public static void startActivity(Context context, int mode, int manualTime, int manualSpeed) {
        context.startActivity(new Intent(context, ManualJudgeWorkActivity.class).putExtra("mode", mode).putExtra("manualTime", manualTime).putExtra("manualSpeed", manualSpeed));
    }

}
