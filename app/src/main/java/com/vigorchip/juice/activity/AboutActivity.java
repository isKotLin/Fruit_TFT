package com.vigorchip.juice.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.vigorchip.juice.R;
import com.vigorchip.juice.view.PickerView;
import com.vigorchip.juice.view.wheel.LoopView;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;

import butterknife.InjectView;

/**
 * Created by Administrator on 2017/1/15.
 */

public class AboutActivity extends BaseActivity {

    @InjectView(R.id.min_wpv)
    LoopView min_wpv;

    @InjectView(R.id.sec_wpv)
    PickerView sec_wpv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_aboutus;
    }

    @Override
    protected void initView() {
    }

    private int choose_min;
    private int choose_sec;

    @Override
    protected void initData(Bundle bundle) {
        ArrayList<String> data1s = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            data1s.add(i + "");
        }
        min_wpv.setItems(data1s);

        min_wpv.setOnSelectListener(new LoopView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                Logutil.e("min=" + text);
                choose_min = Integer.valueOf(text);
                if (choose_min == 0 && choose_sec == 0) {
                    sec_wpv.setCenterItem("1");
                    choose_sec = 1;
                }
                setWheelColor(0xFFFED703);
            }
        });

        min_wpv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setWheelColor(0xFFFED703);
                return false;
            }
        });

        min_wpv.setCenterItem(5);


        ArrayList<String> data2s = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            data2s.add(i + "");
        }
        sec_wpv.setItems(data2s);


        sec_wpv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                choose_sec = Integer.valueOf(text);
                if (choose_min == 0 && choose_sec == 0) {
                    sec_wpv.setCenterItem("1");
                    choose_sec = 1;
                }
                setWheelColor(0xFFFED703);
            }
        });

        sec_wpv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setWheelColor(0xFFFED703);
                return false;
            }
        });

        sec_wpv.setCenterItem(0);
    }


    public void setWheelColor(int color) {
        min_wpv.setTextColor(color);
        sec_wpv.setTextColor(color);
    }



}
