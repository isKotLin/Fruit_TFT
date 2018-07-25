package com.vigorchip.juice.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tmall.ultraviewpager.UltraViewPager;
import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.activity.BigRecipeDetailActivity;
import com.vigorchip.juice.adapter.UltraPagerAdapter;
import com.vigorchip.juice.util.PublicUtil;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.puliblib.base.BaseFragment;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/4.
 */

public class RecentFragment extends BaseFragment {

    @InjectView(R.id.nocontent_fl)
    FrameLayout nocontent_fl;

    @InjectView(R.id.content_fl)
    FrameLayout content_fl;

    @InjectView(R.id.ultra_viewpager)
    UltraViewPager ultra_viewpager;

    @InjectView(R.id.left_recipe_tv)
    TextView left_recipe_tv;

    @InjectView(R.id.right_recipe_tv)
    TextView right_recipe_tv;

    @InjectView(R.id.recipe_name_tv)
    TextView recipe_name_tv;

    @InjectView(R.id.count_tv)
    TextView count_tv;

    @InjectView(R.id.indicator_ll)
    LinearLayout indicator_ll;

    @InjectView(R.id.indicator_view)
    View indicator_view;

    @InjectView(R.id.changefont1_tv)
    TextView changefont1_tv;

    @InjectView(R.id.changefont2_tv)
    TextView changefont2_tv;

    @InjectView(R.id.selectrecipe_bt)
    Button selectrecipe_bt;

    private PagerAdapter mAdapter;

    ArrayList<CategoryBean> recipeBeans = new ArrayList<>();

    public static RecentFragment newInstance() {
        return new RecentFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recent;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        TextFontUtil.changeRobotoRegular(myActivity, changefont1_tv);
        TextFontUtil.changeRobotoRegular(myActivity, changefont2_tv);
        TextFontUtil.changeRobotoBold(myActivity, selectrecipe_bt);

        String recents = MyApplication.sp.getString("recents", "");
        if (TextUtils.isEmpty(recents)) {
            nocontent_fl.setVisibility(View.VISIBLE);
            content_fl.setVisibility(View.GONE);
        }
        String[] splits = recents.split(":");
        if (splits.length == 0) {
            nocontent_fl.setVisibility(View.VISIBLE);
            content_fl.setVisibility(View.GONE);
        } else {
            content_fl.setVisibility(View.VISIBLE);
            nocontent_fl.setVisibility(View.GONE);
            getCategoryData(splits);
        }
    }


    public void getCategoryData(String[] splits) {
        for (int i = 0; i < splits.length; i++) {
            if (!TextUtils.isEmpty(splits[i])) {
                List<CategoryBean> categoryBeens = MyApplication.dataHelper.queryBuilderCategoryByFid(splits[i]);
                for (int m = 0; m < categoryBeens.size(); m++) {
                    recipeBeans.add(categoryBeens.get(m));
                }
            }
        }
        if (recipeBeans.size() > 0) {
            nocontent_fl.setVisibility(View.GONE);
            content_fl.setVisibility(View.VISIBLE);
            setData();
        } else {
            nocontent_fl.setVisibility(View.VISIBLE);
            content_fl.setVisibility(View.GONE);
        }

    }

    private ArrayList<View> scrollViews;

    public void setData() {
        if (recipeBeans.size() == 1) {
            left_recipe_tv.setBackgroundResource(R.drawable.btn_arrow_left_2);
            right_recipe_tv.setBackgroundResource(R.drawable.btn_arrow_right_2);
        } else {
            left_recipe_tv.setBackgroundResource(R.drawable.btn_arrow_left_1);
            right_recipe_tv.setBackgroundResource(R.drawable.btn_arrow_right_1);
        }
        Logutil.e("最近菜单个数" + recipeBeans.size());
        scrollViews = PublicUtil.addIndicator(myActivity, indicator_ll, recipeBeans.size());
        indicator_width = (int) ((MyApplication.sWidthPix) / recipeBeans.size() + 1);
        FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(indicator_width, ViewGroup.LayoutParams.MATCH_PARENT);
        par.setMargins(0, 0, 0, 0);
        indicator_view.setLayoutParams(par);

        ArrayList<String> bgPics = new ArrayList<>();
        for (int i = 0; i < recipeBeans.size(); i++) {
            bgPics.add(recipeBeans.get(i).getBg_pic());
        }
        mAdapter = new UltraPagerAdapter(myActivity, bgPics);
        ultra_viewpager.setAdapter(mAdapter);
        if (recipeBeans.size() == 1) {
            Logutil.e("不能左右滑动");
            ultra_viewpager.setInfiniteLoop(false);
        } else {
            Logutil.e("能左右滑动");
            ultra_viewpager.setInfiniteLoop(true);
        }

        ultra_viewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

        ultra_viewpager.setMultiScreen(1.0f);
        ultra_viewpager.setItemRatio(1.0f);
        ultra_viewpager.setAutoMeasureHeight(true);
        ultra_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (recipeBeans.size() <= 1) {
                    return;
                }
                //"页面:" + position + "
                // offset偏移百分比" + positionOffset
                // pix像素" + positionOffsetPixels
                //设置下划线的属性
                //设置下划线View的长度
                FrameLayout.LayoutParams par = (FrameLayout.LayoutParams) indicator_view.getLayoutParams();
                //设置下划线距离左边的位置长度
                int left = (int) ((positionOffset + ((position) % recipeBeans.size())) * MyApplication.sWidthPix / recipeBeans.size());
                if (MyApplication.sWidthPix - left < 10) {//上一行代码精度丢失,故加一行
                    par.setMargins(0, 0, 0, 0);
                } else {
                    par.setMargins(left, 0, 0, 0);

                }
                indicator_view.setLayoutParams(par);
            }

            @Override
            public void onPageSelected(int position) {
                if (ultra_viewpager.getCurrentItem() >= recipeBeans.size()) {
                    return;
                }
                PublicUtil.ChangeScollViewColor(myActivity, scrollViews, ((position) % recipeBeans.size()));
                recipe_name_tv.setText(recipeBeans.get(ultra_viewpager.getCurrentItem()).getName() + "");
                count_tv.setText((ultra_viewpager.getCurrentItem() + 1) + "/" + recipeBeans.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        count_tv.setText((ultra_viewpager.getCurrentItem() + 1) + "/" + recipeBeans.size());
        recipe_name_tv.setText(recipeBeans.get(ultra_viewpager.getCurrentItem()).getName() + "");
    }

    int indicator_width;


    @OnClick(R.id.left_recipe_tv)
    public void leftRecipe() {
        if (recipeBeans.size() == 0) {
            return;
        }
        ultra_viewpager.setCurrentItem(ultra_viewpager.getCurrentItem() - 1);

    }

    @OnClick(R.id.right_recipe_tv)
    public void rightRecipe() {
        if (recipeBeans.size() == 0) {
            return;
        }
        ultra_viewpager.setCurrentItem(ultra_viewpager.getCurrentItem() + 1);
    }

    @OnClick(R.id.selectrecipe_bt)
    public void selectRecipeClick() {
        if (recipeBeans.size() == 0) {
            return;
        }
        if (ultra_viewpager.getCurrentItem() >= recipeBeans.size()) {
            return;
        }
        CategoryBean bean = recipeBeans.get(ultra_viewpager.getCurrentItem());
        BigRecipeDetailActivity.startActivity(myActivity, bean.getFid(), bean.getName(), bean.getBg_pic());
    }
}
