package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.util.event.ChangeJarCupEvent;
import com.vigorchip.juice.view.MyWheerView;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/19.
 */

public class SmallMainMenuActivity extends BaseActivity {

    @InjectView(R.id.speed_wpv)
    MyWheerView speed_wpv;

    @InjectView(R.id.sec_tv)
    TextView sec_tv;

    private int mode = 1;//1为小杯 2为大杯

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smallmainmenu;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(ChangeJarCupEvent event) {
        Logutil.e("SmallMainMenuActivity___ChangeJarCupEvent=Mode=" + event.getMode());
        if (event.getMode() == 2) {
            BigJarNoticeActivity.startActivity(this);
        }
    }

    @Override
    protected void initData(Bundle bundle) {
        ArrayList<String> datas = new ArrayList<>();
        datas.add("1");
        datas.add("2");
        speed_wpv.setItems(datas);
        speed_wpv.setOnSelectListener(new MyWheerView.onSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                Logutil.e(position + "<><><>" + text);
                if (text.equals("1")) {
                    mode = 1;
                    sec_tv.setText("20");
                } else if (text.equals("2")) {
                    mode = 2;
                    sec_tv.setText("25");
                }
            }
        });
        speed_wpv.setCenterItem(0);
    }

    @Override
    public void onBackPressed() {

    }


    public void smalljudgeClick(View view) {
        SmallJudgeWorkActivity.startActivity(this, mode);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SmallMainMenuActivity.class));
    }
}
