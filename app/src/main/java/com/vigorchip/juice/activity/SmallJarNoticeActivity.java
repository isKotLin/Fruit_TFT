package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.util.CupStatusManager;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.juice.util.event.ChangeJarCupEvent;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.Logutil;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/19.
 */

public class SmallJarNoticeActivity extends BaseActivity {

    @InjectView(R.id.change_font_tv)
    TextView change_font_tv;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_smalljarnotice;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        TextFontUtil.changeRobotoBold(this, change_font_tv);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(COMMIT_CODE);
            }
        }, 1000);
    }


    private int COMMIT_CODE = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMMIT_CODE) {
                SmallMainMenuActivity.startActivity(SmallJarNoticeActivity.this);
                finish();
            }
        }
    };


    public void onEventMainThread(ChangeJarCupEvent event) {
        if (event.getMode() == 2) {
            mHandler.removeMessages(COMMIT_CODE);
            BigJarNoticeActivity.startActivity(this);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }


    public static void startActivity(Context context) {
        if (!"SMALL".equals(CupStatusManager.CURCUP)) {
            CupStatusManager.CURCUP = "SMALL";
            Logutil.e("BigJarNotice   Curcup=SMALL");
            context.startActivity(new Intent(context, SmallJarNoticeActivity.class));
        }
    }
}
