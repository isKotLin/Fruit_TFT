package com.vigorchip.juice.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tmall.ultraviewpager.UltraViewPager;
import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.activity.BigRecipeDetailActivity;
import com.vigorchip.juice.adapter.UltraPagerAdapter;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.FavouriteUtil;
import com.vigorchip.juice.util.PublicUtil;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.puliblib.base.BaseFragment;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/4.
 */

public class FavouriteFragment extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.nocontent_fl)
    FrameLayout nocontent_fl;

    @InjectView(R.id.content_fl)
    FrameLayout content_fl;

    @InjectView(R.id.ultra_viewpager)
    UltraViewPager ultra_viewpager;

    @InjectView(R.id.isCollectShow_iv)
    ImageView isCollectShow_iv;

    @InjectView(R.id.count_tv)
    TextView count_tv;

    @InjectView(R.id.recipe_name_tv)
    TextView recipe_name_tv;

    @InjectView(R.id.indicator_ll)
    LinearLayout indicator_ll;

    @InjectView(R.id.indicator_view)
    View indicator_view;

    @InjectView(R.id.recipe_00_iv)
    ImageView recipe_00_iv;

    @InjectView(R.id.recipe_01_iv)
    ImageView recipe_01_iv;

    @InjectView(R.id.recipe_02_iv)
    ImageView recipe_02_iv;

    @InjectView(R.id.recipe_03_iv)
    ImageView recipe_03_iv;

    @InjectView(R.id.recipe_04_iv)
    ImageView recipe_04_iv;

    @InjectView(R.id.recipe_05_iv)
    ImageView recipe_05_iv;

    @InjectView(R.id.recipe_06_iv)
    ImageView recipe_06_iv;

    @InjectView(R.id.recipe_07_iv)
    ImageView recipe_07_iv;

    @InjectView(R.id.changefont1_tv)
    TextView changefont1_tv;

    @InjectView(R.id.changefont2_tv)
    TextView changefont2_tv;

    @InjectView(R.id.changefont3_tv)
    TextView changefont3_tv;

    @InjectView(R.id.left_recipe_tv)
    TextView left_recipe_tv;

    @InjectView(R.id.right_recipe_tv)
    TextView right_recipe_tv;

    @InjectView(R.id.selectrecipe_bt)
    Button selectrecipe_bt;

    private int curCategoryPosition = 0;
    private boolean[] isHaveContent = new boolean[8];

    private UltraPagerAdapter mAdapter;

    ArrayList<CategoryBean> allRecipeBeans = new ArrayList<>();
    ArrayList<CategoryBean> curRecipeBeans = new ArrayList<>();

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favourite;
    }

    @Override
    protected void initView(View view) {
        recipe_00_iv.setOnClickListener(this);
        recipe_01_iv.setOnClickListener(this);
        recipe_02_iv.setOnClickListener(this);
        recipe_03_iv.setOnClickListener(this);
        recipe_04_iv.setOnClickListener(this);
        recipe_05_iv.setOnClickListener(this);
        recipe_06_iv.setOnClickListener(this);
        recipe_07_iv.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        TextFontUtil.changeRobotoRegular(myActivity, changefont1_tv);
        TextFontUtil.changeRobotoRegular(myActivity, changefont2_tv);
        TextFontUtil.changeRobotoRegular(myActivity, changefont3_tv);
        TextFontUtil.changeRobotoBold(myActivity, selectrecipe_bt);

        String collects = MyApplication.sp.getString("collects", "");
        Logutil.e("收藏=" + collects);
        if (TextUtils.isEmpty(collects)) {
            nocontent_fl.setVisibility(View.VISIBLE);
        }
        String[] splits = collects.split(":");
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
                    allRecipeBeans.add(categoryBeens.get(m));
                }
            }
        }
        if (allRecipeBeans.size() > 0) {
            mHandler.sendEmptyMessage(Constant.HANDLER_SUCCESS);
        } else {
            nocontent_fl.setVisibility(View.VISIBLE);
            content_fl.setVisibility(View.GONE);
        }

    }

    /**
     * 清空数据
     */
    public void emptyHaveCategory() {
        isHaveContent[0] = false;
        isHaveContent[1] = false;
        isHaveContent[2] = false;
        isHaveContent[3] = false;
        isHaveContent[4] = false;
        isHaveContent[5] = false;
        isHaveContent[6] = false;
        isHaveContent[7] = false;
    }

    /**
     * 判断这个种类有没有
     *
     * @param category
     */
    public void judeIsHaveCategory(String category) {
        if (category.contains("A")) {
            isHaveContent[0] = true;
            isHaveContent[1] = true;
        } else if (category.contains("B")) {
            isHaveContent[0] = true;
            isHaveContent[2] = true;
        } else if (category.contains("C")) {
            isHaveContent[0] = true;
            isHaveContent[3] = true;
        } else if (category.contains("D")) {
            isHaveContent[0] = true;
            isHaveContent[4] = true;
        } else if (category.contains("E")) {
            isHaveContent[0] = true;
            isHaveContent[5] = true;
        } else if (category.contains("F")) {
            isHaveContent[0] = true;
            isHaveContent[6] = true;
        } else if (category.contains("G")) {
            isHaveContent[0] = true;
            isHaveContent[7] = true;
        }
    }


    @Override
    public void onClick(View view) {
        Logutil.e("点击了=" + allRecipeBeans.size());
        if (allRecipeBeans.size() == 0) {
            nocontent_fl.setVisibility(View.VISIBLE);
            content_fl.setVisibility(View.GONE);
            return;
        }
        emptyHaveCategory();
        for (int i = 0; i < allRecipeBeans.size(); i++) {
            CategoryBean bean = allRecipeBeans.get(i);
            judeIsHaveCategory(bean.getFid());
        }
        //指示器设置
        ArrayList<CategoryBean> recipeBeen1s = new ArrayList<CategoryBean>();

        switch (view.getId()) {
            case R.id.recipe_00_iv:
                recipeBeen1s.addAll(allRecipeBeans);
                curCategoryPosition = 0;
                break;
            case R.id.recipe_01_iv:
                for (int i = 0; i < allRecipeBeans.size(); i++) {
                    if (allRecipeBeans.get(i).getFid().contains("A")) {
                        recipeBeen1s.add(allRecipeBeans.get(i));
                        curCategoryPosition = 1;
                    }
                }
                break;
            case R.id.recipe_02_iv:
                for (int i = 0; i < allRecipeBeans.size(); i++) {
                    if (allRecipeBeans.get(i).getFid().contains("B")) {
                        recipeBeen1s.add(allRecipeBeans.get(i));
                        curCategoryPosition = 2;
                    }
                }
                break;
            case R.id.recipe_03_iv:
                for (int i = 0; i < allRecipeBeans.size(); i++) {
                    if (allRecipeBeans.get(i).getFid().contains("C")) {
                        recipeBeen1s.add(allRecipeBeans.get(i));
                        curCategoryPosition = 3;
                    }
                }
                break;
            case R.id.recipe_04_iv:
                for (int i = 0; i < allRecipeBeans.size(); i++) {
                    if (allRecipeBeans.get(i).getFid().contains("D")) {
                        recipeBeen1s.add(allRecipeBeans.get(i));
                        curCategoryPosition = 4;
                    }
                }
                break;
            case R.id.recipe_05_iv:
                for (int i = 0; i < allRecipeBeans.size(); i++) {
                    if (allRecipeBeans.get(i).getFid().contains("E")) {
                        recipeBeen1s.add(allRecipeBeans.get(i));
                        curCategoryPosition = 5;
                    }
                }
                break;
            case R.id.recipe_06_iv:
                for (int i = 0; i < allRecipeBeans.size(); i++) {
                    if (allRecipeBeans.get(i).getFid().contains("F")) {
                        recipeBeen1s.add(allRecipeBeans.get(i));
                        curCategoryPosition = 6;
                    }
                }
                break;
            case R.id.recipe_07_iv:
                for (int i = 0; i < allRecipeBeans.size(); i++) {
                    if (allRecipeBeans.get(i).getFid().contains("G")) {
                        recipeBeen1s.add(allRecipeBeans.get(i));
                        curCategoryPosition = 7;
                    }
                }
                break;
        }
        if (recipeBeen1s.size() == 0) {
            return;
        }

        curRecipeBeans.clear();
        curRecipeBeans.addAll(recipeBeen1s);
        setViewPageData();
        setHeighShow(curCategoryPosition);
    }

    int indicator_width;

    /**
     * 设置高亮显示
     */
    public void setHeighShow(int position) {
        if (isHaveContent[0]) {
            recipe_00_iv.setImageResource(R.drawable.btn_myrecipes_made_1);
        } else {
            recipe_00_iv.setImageResource(R.drawable.btn_myrecipes_made_3);
        }
        if (isHaveContent[1]) {
            recipe_01_iv.setImageResource(R.drawable.btn_icon_01_1);
        } else {
            recipe_01_iv.setImageResource(R.drawable.btn_icon_01_4);
        }
        if (isHaveContent[2]) {
            recipe_02_iv.setImageResource(R.drawable.btn_icon_02_1);
        } else {
            recipe_02_iv.setImageResource(R.drawable.btn_icon_02_4);
        }
        if (isHaveContent[3]) {
            recipe_03_iv.setImageResource(R.drawable.btn_icon_03_1);
        } else {
            recipe_03_iv.setImageResource(R.drawable.btn_icon_03_4);
        }
        if (isHaveContent[4]) {
            recipe_04_iv.setImageResource(R.drawable.btn_icon_04_1);
        } else {
            recipe_04_iv.setImageResource(R.drawable.btn_icon_04_4);
        }
        if (isHaveContent[5]) {
            recipe_05_iv.setImageResource(R.drawable.btn_icon_05_1);
        } else {
            recipe_05_iv.setImageResource(R.drawable.btn_icon_05_4);
        }
        if (isHaveContent[6]) {
            recipe_06_iv.setImageResource(R.drawable.btn_icon_06_1);
        } else {
            recipe_06_iv.setImageResource(R.drawable.btn_icon_06_4);
        }
        if (isHaveContent[7]) {
            recipe_07_iv.setImageResource(R.drawable.btn_icon_07_1);
        } else {
            recipe_07_iv.setImageResource(R.drawable.btn_icon_07_4);
        }

        switch (position) {
            case 0:
                recipe_00_iv.setImageResource(R.drawable.btn_myrecipes_made_2);
                break;
            case 1:
                recipe_01_iv.setImageResource(R.drawable.btn_icon_01_3);
                break;
            case 2:
                recipe_02_iv.setImageResource(R.drawable.btn_icon_02_3);
                break;
            case 3:
                recipe_03_iv.setImageResource(R.drawable.btn_icon_03_3);
                break;
            case 4:
                recipe_04_iv.setImageResource(R.drawable.btn_icon_04_3);
                break;
            case 5:
                recipe_05_iv.setImageResource(R.drawable.btn_icon_05_3);
                break;
            case 6:
                recipe_06_iv.setImageResource(R.drawable.btn_icon_06_3);
                break;
            case 7:
                recipe_07_iv.setImageResource(R.drawable.btn_icon_07_3);
                break;
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showProgressBar(false);
            if (msg.what == Constant.HANDLER_SUCCESS) {
                recipe_00_iv.performClick();
            } else {
                myActivity.finish();
                //DDD
                EventBus.getDefault().post(new MainChangeEvent(1, 0, 0));
            }
        }
    };
    private ArrayList<View> scrollViews;

    /**
     * 根据选择的类别不同，对viewpage显示内容分别设置
     *
     * @param
     */
    public void setViewPageData() {
        if (curRecipeBeans.size() == 0) {
            return;
        }
        if (curRecipeBeans.size() == 1) {
            left_recipe_tv.setBackgroundResource(R.drawable.btn_arrow_left_2);
            right_recipe_tv.setBackgroundResource(R.drawable.btn_arrow_right_2);
        } else {
            left_recipe_tv.setBackgroundResource(R.drawable.btn_arrow_left_1);
            right_recipe_tv.setBackgroundResource(R.drawable.btn_arrow_right_1);
        }
        Logutil.e("当前菜单个数" + curRecipeBeans.size());
        scrollViews = PublicUtil.addIndicator(myActivity, indicator_ll, curRecipeBeans.size());
        indicator_width = (int) ((MyApplication.sWidthPix) / curRecipeBeans.size() + 1);
        FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(indicator_width, ViewGroup.LayoutParams.MATCH_PARENT);
        par.setMargins(0, 0, 0, 0);
        indicator_view.setLayoutParams(par);

        ArrayList<String> bgPics = new ArrayList<>();
        for (int i = 0; i < curRecipeBeans.size(); i++) {
            bgPics.add(curRecipeBeans.get(i).getBg_pic());
        }

        if (curRecipeBeans.size() == 1) {
            Logutil.e("不能左右滑动");
            ultra_viewpager.setInfiniteLoop(false);
        } else {
            Logutil.e("能左右滑动");
            ultra_viewpager.setInfiniteLoop(true);
        }

        mAdapter = new UltraPagerAdapter(myActivity, bgPics);
        ultra_viewpager.setAdapter(mAdapter);

        ultra_viewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

        ultra_viewpager.setMultiScreen(1.0f);
        ultra_viewpager.setItemRatio(1.0f);
        ultra_viewpager.setAutoMeasureHeight(true);


        ultra_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (curRecipeBeans.size() <= 1) {
                    return;
                }
                //"页面:" + position + "
                // offset偏移百分比" + positionOffset
                // pix像素" + positionOffsetPixels
                //设置下划线的属性
                //设置下划线View的长度
                FrameLayout.LayoutParams par = (FrameLayout.LayoutParams) indicator_view.getLayoutParams();
                //设置下划线距离左边的位置长度
                int left = (int) ((positionOffset + ((position) % curRecipeBeans.size())) * MyApplication.sWidthPix / curRecipeBeans.size());
                if (MyApplication.sWidthPix - left < 10) {//上一行代码精度丢失,故加一行
                    par.setMargins(0, 0, 0, 0);
                } else {
                    par.setMargins(left, 0, 0, 0);

                }
                indicator_view.setLayoutParams(par);
            }

            @Override
            public void onPageSelected(int position) {
                if (ultra_viewpager.getCurrentItem() >= curRecipeBeans.size()) {
                    return;
                }
                PublicUtil.ChangeScollViewColor(myActivity, scrollViews, ((position) % curRecipeBeans.size()));
                boolean isCollect = FavouriteUtil.isCollect(curRecipeBeans.get(ultra_viewpager.getCurrentItem()).getFid(), null);
                if (isCollect) {
                    isCollectShow_iv.setImageResource(R.drawable.btn_favorite_2);
                } else {
                    isCollectShow_iv.setImageResource(R.drawable.btn_favorite_1);
                }
                recipe_name_tv.setText(curRecipeBeans.get(ultra_viewpager.getCurrentItem()).getName().toUpperCase() + "");
                count_tv.setText((ultra_viewpager.getCurrentItem() + 1) + "/" + curRecipeBeans.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        boolean isCollect = FavouriteUtil.isCollect(curRecipeBeans.get(ultra_viewpager.getCurrentItem()).getFid(), null);
        if (isCollect) {
            isCollectShow_iv.setImageResource(R.drawable.btn_favorite_2);
        } else {
            isCollectShow_iv.setImageResource(R.drawable.btn_favorite_1);
        }

        count_tv.setText((ultra_viewpager.getCurrentItem() + 1) + "/" + curRecipeBeans.size());
        recipe_name_tv.setText(curRecipeBeans.get(ultra_viewpager.getCurrentItem()).getName().toUpperCase() + "");
    }

    @OnClick(R.id.isCollectShow_iv)
    public void collectClick() {
        if (curRecipeBeans == null || curRecipeBeans.size() == 0) {
            return;
        }
        FavouriteUtil.setCollect(curRecipeBeans.get(ultra_viewpager.getCurrentItem()).getFid(), new FavouriteUtil.CollectCallBack() {
            @Override
            public void YesBack() {
                isCollectShow_iv.setImageResource(R.drawable.btn_favorite_2);
                FavouriteUtil.showCollectPop(myActivity, layoutView, true);
            }

            @Override
            public void NoBack() {
                isCollectShow_iv.setImageResource(R.drawable.btn_favorite_1);
                FavouriteUtil.showCollectPop(myActivity, layoutView, false);
                CategoryBean bean = curRecipeBeans.get(ultra_viewpager.getCurrentItem());
                for (int i = 0; i < allRecipeBeans.size(); i++) {
                    CategoryBean bean1 = allRecipeBeans.get(i);
                    if (bean1.getFid().equals(bean.getFid())) {
                        allRecipeBeans.remove(i);
                        if (curRecipeBeans.size() == 1) {
                            recipe_00_iv.performClick();
                        } else {
                            if (curCategoryPosition == 0) {
                                recipe_00_iv.performClick();
                            } else if (curCategoryPosition == 1) {
                                recipe_01_iv.performClick();
                            } else if (curCategoryPosition == 2) {
                                recipe_02_iv.performClick();
                            } else if (curCategoryPosition == 3) {
                                recipe_03_iv.performClick();
                            } else if (curCategoryPosition == 4) {
                                recipe_04_iv.performClick();
                            } else if (curCategoryPosition == 5) {
                                recipe_05_iv.performClick();
                            } else if (curCategoryPosition == 6) {
                                recipe_06_iv.performClick();
                            } else if (curCategoryPosition == 7) {
                                recipe_07_iv.performClick();
                            }
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.left_recipe_tv)
    public void leftRecipe() {
        if (curRecipeBeans.size() == 0 || curRecipeBeans.size() == 1) {
            return;
        }
        ultra_viewpager.setCurrentItem(ultra_viewpager.getCurrentItem() - 1);
    }

    @OnClick(R.id.right_recipe_tv)
    public void rightRecipe() {
        if (curRecipeBeans.size() == 0 || curRecipeBeans.size() == 1) {
            return;
        }
        ultra_viewpager.setCurrentItem(ultra_viewpager.getCurrentItem() + 1);
    }

    @OnClick(R.id.selectrecipe_bt)
    public void selectRecipeClick() {
        if (curRecipeBeans.size() == 0) {
            return;
        }
        CategoryBean bean = curRecipeBeans.get(ultra_viewpager.getCurrentItem());
        BigRecipeDetailActivity.startActivity(myActivity, bean.getFid(), bean.getName(), bean.getBg_pic());
    }


}
