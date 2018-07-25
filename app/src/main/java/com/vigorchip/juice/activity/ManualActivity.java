package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.vigorchip.juice.R;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.juice.view.wheel.LoopView;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/8.
 */

public class ManualActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.min_wpv)
    LoopView min_wpv;

    @InjectView(R.id.sec_wpv)
    LoopView sec_wpv;

    @InjectView(R.id.speed_wpv)
    LoopView speed_wpv;

    @InjectView(R.id.left_layout_ll)
    LinearLayout left_layout_ll;

    @InjectView(R.id.middle_layout_ll)
    LinearLayout middle_layout_ll;

    @InjectView(R.id.right_layout_ll)
    LinearLayout right_layout_ll;

    private int mode;
    private int choose_min = 1;
    private int choose_sec;
    private int choose_speed = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manual;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        ArrayList<String> data1s = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data1s.add(i + "");
        }
        min_wpv.setItems(data1s);
        ArrayList<String> data2s = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            data2s.add(i + "");
        }
        sec_wpv.setItems(data2s);
        ArrayList<String> data3s = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            if (i != 10) {
                data3s.add(i + "");
            } else {
                data3s.add("H");
            }
        }
        speed_wpv.setItems(data3s);


        min_wpv.setOnSelectListener(new LoopView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                choose_min = Integer.valueOf(text);
                if (choose_min == 0 && choose_sec == 0) {
                    sec_wpv.setCenterItem("1");
                    choose_sec = 1;
                }
                left_layout_ll.setBackgroundResource(0);
                middle_layout_ll.setBackgroundResource(0);
                right_layout_ll.setBackgroundResource(0);
                setWheelColor(0xFFFED703);
                mode = 0;
            }
        });
        sec_wpv.setOnSelectListener(new LoopView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                choose_sec = Integer.valueOf(text);
                if (choose_min == 0 && choose_sec == 0) {
                    sec_wpv.setCenterItem("1");
                    choose_sec = 1;
                }
                left_layout_ll.setBackgroundResource(0);
                middle_layout_ll.setBackgroundResource(0);
                right_layout_ll.setBackgroundResource(0);
                mode = 0;
                setWheelColor(0xFFFED703);
            }
        });
        speed_wpv.setOnSelectListener(new LoopView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (text.equals("H")) {
                    choose_speed = 10;
                } else {
                    choose_speed = Integer.valueOf(text);
                }
                left_layout_ll.setBackgroundResource(0);
                middle_layout_ll.setBackgroundResource(0);
                right_layout_ll.setBackgroundResource(0);
                setWheelColor(0xFFFED703);
                mode = 0;
            }
        });
        min_wpv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                left_layout_ll.setBackgroundResource(0);
                middle_layout_ll.setBackgroundResource(0);
                right_layout_ll.setBackgroundResource(0);
                setWheelColor(0xFFFED703);
                mode = 0;
                return false;
            }
        });
        sec_wpv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                left_layout_ll.setBackgroundResource(0);
                middle_layout_ll.setBackgroundResource(0);
                right_layout_ll.setBackgroundResource(0);
                setWheelColor(0xFFFED703);
                mode = 0;
                return false;
            }
        });
        speed_wpv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                left_layout_ll.setBackgroundResource(0);
                middle_layout_ll.setBackgroundResource(0);
                right_layout_ll.setBackgroundResource(0);
                setWheelColor(0xFFFED703);
                mode = 0;
                return false;
            }
        });
        min_wpv.setCenterItem(1);
        sec_wpv.setCenterItem(0);
        speed_wpv.setCenterItem(0);
        left_layout_ll.setOnClickListener(this);
        middle_layout_ll.setOnClickListener(this);
        right_layout_ll.setOnClickListener(this);
    }

    public void setWheelColor(int color) {
        min_wpv.setTextColor(color);
        sec_wpv.setTextColor(color);
        speed_wpv.setTextColor(color);
    }

    public void onEventMainThread(BackMenuEvent event) {
        finish();
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ManualActivity.class));
    }

    @Override
    public void onClick(View view) {
        left_layout_ll.setBackgroundResource(0);
        middle_layout_ll.setBackgroundResource(0);
        right_layout_ll.setBackgroundResource(0);
        switch (view.getId()) {
            case R.id.left_layout_ll:
                left_layout_ll.setBackgroundResource(R.drawable.btn_frame_4);
                setWheelColor(0xFFFFFFFF);
                mode = 3;
                break;
            case R.id.middle_layout_ll:
                middle_layout_ll.setBackgroundResource(R.drawable.btn_frame_4);
                setWheelColor(0xFFFFFFFF);
                mode = 4;
                break;
            case R.id.right_layout_ll:
                right_layout_ll.setBackgroundResource(R.drawable.btn_frame_4);
                setWheelColor(0xFFFFFFFF);
                mode = 5;
                break;
        }
    }

    public void judgeClick(View view) {
        if (mode != 0) {
            ManualJudgeWorkActivity.startActivity(this, mode, 0, 0);
        } else {
            if (choose_speed == 0) {
                showToast("speed is 0");
                return;
            }
            if ((choose_min * 60 + choose_sec) == 0) {
                showToast("time is 0");
                return;
            }
            Logutil.e("ManualActivity=min=" + choose_min + "<><sec=>" + choose_sec);
            ManualJudgeWorkActivity.startActivity(this, 0, choose_min * 60 + choose_sec, choose_speed);
        }
    }

    public void backFinishClick(View view) {
        finish();
        //DDD
        EventBus.getDefault().post(new MainChangeEvent(1, 0, 0));
    }
}
