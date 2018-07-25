package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.fragment.FavouriteFragment;
import com.vigorchip.juice.fragment.RecentFragment;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.puliblib.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/3.
 */

public class MyRecipeActivity extends BaseActivity {

    FavouriteFragment favouriteFragment;
    RecentFragment recentFragment;
    // fragment的管理者
    private FragmentManager fragmentManager;

    @InjectView(R.id.favourite_iv)
    ImageView favourite_iv;

    @InjectView(R.id.recent_iv)
    ImageView recent_iv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myrecipe;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        fragmentManager = getSupportFragmentManager();
        showFavouriteFragment();
    }

    @OnClick(R.id.favourite_iv)
    public void showFavouriteFragment() {
        favourite_iv.setImageResource(R.drawable.btn_myrecipes_favourites_2);
        recent_iv.setImageResource(R.drawable.btn_myrecipes_recent_1);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        if (favouriteFragment == null) {
            favouriteFragment = FavouriteFragment.newInstance();
            transaction.add(R.id.content_frame, favouriteFragment);
        }
        transaction.show(favouriteFragment).commitAllowingStateLoss();
    }

    @OnClick(R.id.recent_iv)
    public void showRecentFragment() {
        favourite_iv.setImageResource(R.drawable.btn_myrecipes_favourites_1);
        recent_iv.setImageResource(R.drawable.btn_myrecipes_recent_2);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        if (recentFragment == null) {
            recentFragment = RecentFragment.newInstance();
            transaction.add(R.id.content_frame, recentFragment);
        }
        transaction.show(recentFragment).commitAllowingStateLoss();
    }


    /**
     * 隐藏所有的fragment
     *
     * @param transaction
     */
    public void hideAllFragment(FragmentTransaction transaction) {
        if (favouriteFragment != null) {
            transaction.hide(favouriteFragment);
        }
        if (recentFragment != null) {
            transaction.hide(recentFragment);
        }
    }

    public void onEventMainThread(BackMenuEvent event) {
        finish();
    }

    public void backHomeClick(View view) {
        finish();
        //DDD
        EventBus.getDefault().post(new MainChangeEvent(1, 0, 0));
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MyRecipeActivity.class));
    }
}
