package com.vigorchip.juice.activity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.vigorchip.db.entity.ActionBean;
import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.db.entity.FoodBean;
import com.vigorchip.db.entity.FoodHelpBean;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.server.ControlServer;
import com.vigorchip.juice.util.CupStatusManager;
import com.vigorchip.juice.util.WifiUtils;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.JsonUtil;
import com.vigorchip.puliblib.utils.Logutil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SplashActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
    }


    @Override
    protected void initData(Bundle bundle) {

        AlphaAnimation anim = new AlphaAnimation(0.8f, 1.0f);
        anim.setDuration(200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation)  {
                setConfig();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layoutView.setAnimation(anim);
        anim.startNow();

//        setConfig();

    }

    public void setConfig() {
        startService(new Intent(this, ControlServer.class));
//        closeWifi();
        if (MyApplication.sp.getBoolean(FIRSTFLAG, true)) {
            Logutil.e("初始化数据库");
            initDataBase();
        } else {
            Logutil.e("直接进入app");
        }

        if (CupStatusManager.isSmallCupStatus) {
            SmallMainMenuActivity.startActivity(SplashActivity.this);
        } else {
            BigMenuActivity.startActivity(SplashActivity.this);
        }
        finish();
    }

    /**
     * 为了效果关闭wifi
     */
    public void closeWifi() {
        WifiUtils localWifiUtils = new WifiUtils(this);
        if (localWifiUtils.WifiCheckState() == WifiManager.WIFI_STATE_ENABLED) {
            localWifiUtils.WifiClose();
        }
    }

    private String FIRSTFLAG = "firstCome 2.1";

    public void initDataBase() {
        categoryJson();
        cocktailJson();
        actionsJson();
        foodHelpJson();
        MyApplication.sp.edit().putBoolean(FIRSTFLAG, false).commit();
    }

    public void categoryJson() {
        try {
            InputStream is = getAssets().open("datas/category.json");
            final StringBuffer sb = new StringBuffer();
            String readLine;
            BufferedReader responseReader;
            responseReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
            ArrayList<CategoryBean> beans = JsonUtil.json2beans(sb.toString(), CategoryBean.class);
            MyApplication.dataHelper.deleteAllCategory();
            MyApplication.dataHelper.inserMulCategorys(beans);
        } catch (Exception e) {
            finish();
            return;
        }
    }

    public void foodHelpJson() {
        try {
            InputStream is = getAssets().open("datas/category_help.json");
            final StringBuffer sb = new StringBuffer();
            String readLine;
            BufferedReader responseReader;
            responseReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
            ArrayList<FoodHelpBean> beans = JsonUtil.json2beans(sb.toString(), FoodHelpBean.class);
            MyApplication.dataHelper.deleteAllFoodHelp();
            MyApplication.dataHelper.inserMultFoodHelpBeans(beans);
        } catch (Exception e) {
            finish();
            return;
        }
    }

    public void cocktailJson() {
        try {
            InputStream is = getAssets().open("datas/cocktail.json");
            final StringBuffer sb = new StringBuffer();
            String readLine;
            BufferedReader responseReader;
            responseReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
            ArrayList<FoodBean> beans = JsonUtil.json2beans(sb.toString(), FoodBean.class);
            MyApplication.dataHelper.deleteAllFoods();
            MyApplication.dataHelper.inserMultFoods(beans);
        } catch (Exception e) {
            Logutil.e("sssss我不相信！！！");
            finish();
            return;
        }
    }

    public void actionsJson() {
        try {
            InputStream is = getAssets().open("datas/actions.json");
            final StringBuffer sb = new StringBuffer();
            String readLine;
            BufferedReader responseReader;
            responseReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
            ArrayList<ActionBean> beans = JsonUtil.json2beans(sb.toString(), ActionBean.class);
            MyApplication.dataHelper.deleteAllAction();
            MyApplication.dataHelper.inserMultActions(beans);
        } catch (Exception e) {
            Logutil.e("sssss这有B   U   G！！！");
            finish();
            return;
        }
    }


}