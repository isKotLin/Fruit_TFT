package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import com.vigorchip.juice.R;
import com.vigorchip.juice.fragment.LanguageSettingFragment;
import com.vigorchip.juice.fragment.NewWifiSettingFragment;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.puliblib.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2017/1/15.
 */

public class SettingActivity extends BaseActivity {

    @InjectView(R.id.wifi_iv)
    ImageView wifi_iv;

    @InjectView(R.id.language_iv)
    ImageView language_iv;


    NewWifiSettingFragment wifiSettingFragment;
    LanguageSettingFragment languageSettingFragment;
    // fragment的管理者
    private FragmentManager fragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle bundle) {
        fragmentManager = getSupportFragmentManager();
        setWifi();
    }

    @OnClick(R.id.wifi_iv)
    public void setWifi() {
        wifi_iv.setImageResource(R.drawable.btn_setting_wifi_2);
        language_iv.setImageResource(R.drawable.btn_setting_language_1);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        if (wifiSettingFragment == null) {
            wifiSettingFragment = NewWifiSettingFragment.newInstance();
            transaction.add(R.id.content_fl, wifiSettingFragment);
        }
        transaction.show(wifiSettingFragment).commitAllowingStateLoss();
    }

    @OnClick(R.id.language_iv)
    public void setLanguage() {
        wifi_iv.setImageResource(R.drawable.btn_setting_wifi_1);
        language_iv.setImageResource(R.drawable.btn_setting_language_2);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        if (languageSettingFragment == null) {
            languageSettingFragment = LanguageSettingFragment.newInstance();
            transaction.add(R.id.content_fl, languageSettingFragment);
        }
        transaction.show(languageSettingFragment).commitAllowingStateLoss();
    }

    /**
     * 隐藏所有的fragment
     *
     * @param transaction
     */
    public void hideAllFragment(FragmentTransaction transaction) {
        if (wifiSettingFragment != null) {
            transaction.hide(wifiSettingFragment);
        }
        if (languageSettingFragment != null) {
            transaction.hide(languageSettingFragment);
        }
    }

    /**
     * back home  clicklistenner
     *
     * @param view
     */
    public void backHomeClick(View view) {
        finish();
        //DDD
        EventBus.getDefault().post(new MainChangeEvent(1, 0, 0));
    }


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        wifiSettingFragment.onActivityResult(requestCode, resultCode, data);
    }
}
