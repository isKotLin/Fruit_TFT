package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.puliblib.base.BaseActivity;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/15.
 */

public class SmallWorkFinishActivity extends BaseActivity {
    @InjectView(R.id.changefont1_tv)
    TextView changefont1_tv;

    @InjectView(R.id.changefont2_tv)
    TextView changefont2_tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smallworkfinish;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle bundle) {
        TextFontUtil.changeRobotoBold(this, changefont1_tv);
        TextFontUtil.changeRobotoBold(this, changefont2_tv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SmallWorkFinishActivity.class));
    }

    public void backFinishClick(View view) {
        EventBus.getDefault().post(new BackMenuEvent());
        finish();
    }

}
