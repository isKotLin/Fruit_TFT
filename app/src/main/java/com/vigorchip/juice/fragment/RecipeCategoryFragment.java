package com.vigorchip.juice.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tmall.ultraviewpager.UltraViewPager;
import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.adapter.UltraPagerAdapter;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.FavouriteUtil;
import com.vigorchip.juice.util.PublicUtil;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.juice.util.event.CollectEvent;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.puliblib.base.BaseFragment;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/4/2.
 */

public class RecipeCategoryFragment extends BaseFragment {

    @InjectView(R.id.ultra_viewpager)
    UltraViewPager ultra_viewpager;

    @InjectView(R.id.category_title_ll)
    LinearLayout category_title_ll;

    @InjectView(R.id.downcontent_tv)
    TextView downcontent_tv;

    @InjectView(R.id.upcontent_tv)
    TextView upcontent_tv;

    @InjectView(R.id.isCollectShow_iv)
    ImageView isCollectShow_iv;

    @InjectView(R.id.select_bt)
    Button select_bt;

    @InjectView(R.id.indicator_ll)
    LinearLayout indicator_ll;

    @InjectView(R.id.indicator_view)
    View indicator_view;

    private int curPosition = 0;

    private UltraPagerAdapter adapter;

    private String category_ID;
    private String category_Name;
    private int categoryCount;

    private ArrayList<CategoryBean> categoryBeans;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recipecategory;
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        category_ID = arguments.getString("category_ID");
        category_Name = arguments.getString("category_Name");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ViewGroup.LayoutParams lp = ultra_viewpager.getLayoutParams();
        lp.width = MyApplication.sWidthPix + 2;
        ultra_viewpager.setLayoutParams(lp);

        EventBus.getDefault().register(this);
        category_Name = category_Name.substring(0, 1).toUpperCase() + category_Name.substring(1).toLowerCase();
        upcontent_tv.setText(category_Name + "");
        TextFontUtil.changeRobotoRegular(myActivity, upcontent_tv);
        TextFontUtil.changeRobotoBold(myActivity, downcontent_tv);
        TextFontUtil.changeRobotoBold(myActivity, select_bt);
        setCategoryTypeDrawable();
        getDataFromDataBase();
        Constant.hintRecipeDetailInfo(category_ID);
    }


    public void setCategoryTypeDrawable() {
        Drawable drawable = null;
        if (category_ID.equals("A")) {
            drawable = getResources().getDrawable(R.drawable.btn_icon_01_1);
            categoryBeans = Constant.categoryBeens_A;
        } else if (category_ID.equals("B")) {
            drawable = getResources().getDrawable(R.drawable.btn_icon_02_1);
            categoryBeans = Constant.categoryBeens_B;
        } else if (category_ID.equals("C")) {
            drawable = getResources().getDrawable(R.drawable.btn_icon_03_1);
            categoryBeans = Constant.categoryBeens_C;
        } else if (category_ID.equals("D")) {
            drawable = getResources().getDrawable(R.drawable.btn_icon_04_1);
            categoryBeans = Constant.categoryBeens_D;
        } else if (category_ID.equals("E")) {
            drawable = getResources().getDrawable(R.drawable.btn_icon_05_1);
            categoryBeans = Constant.categoryBeens_E;
        } else if (category_ID.equals("F")) {
            drawable = getResources().getDrawable(R.drawable.btn_icon_06_1);
            categoryBeans = Constant.categoryBeens_F;
        } else if (category_ID.equals("G")) {
            drawable = getResources().getDrawable(R.drawable.btn_icon_07_1);
            categoryBeans = Constant.categoryBeens_G;
        }
        categoryCount = categoryBeans.size();
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            downcontent_tv.setCompoundDrawables(drawable, null, null, null);
        }

    }

    private void getDataFromDataBase() {
        if (categoryBeans == null) {
            Logutil.e("进来null");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    categoryBeans = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans(category_ID);
                    categoryCount = categoryBeans.size();
                    if (categoryBeans == null || categoryBeans.size() == 0) {
                        return;
                    }
                    mHandler.sendEmptyMessage(Constant.HANDLER_SUCCESS);
                }
            }).start();
        } else {
            Logutil.e("进来不null");
            mHandler.sendEmptyMessage(Constant.HANDLER_SUCCESS);
        }

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.HANDLER_SUCCESS) {
                setData();
            }
        }
    };

    public void onEventMainThread(CollectEvent event) {
        String foodId = event.getFoodId();
        try {
            if (categoryBeans.get(ultra_viewpager.getCurrentItem()).getFid().equals(foodId)) {
                if (event.isCollect()) {
                    isCollectShow_iv.setImageResource(R.drawable.btn_favorite_2);
                } else {
                    isCollectShow_iv.setImageResource(R.drawable.btn_favorite_1);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * get asset json data
     */

    private ArrayList<View> scrollViews;

    public void setData() {
        scrollViews = PublicUtil.addIndicator(myActivity, indicator_ll, categoryBeans.size());
        indicator_width = ((MyApplication.sWidthPix) / categoryBeans.size() + 1);
        FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(indicator_width, ViewGroup.LayoutParams.MATCH_PARENT);
        par.setMargins(0, 0, 0, 0);
        indicator_view.setLayoutParams(par);

        ultra_viewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultra_viewpager.setInfiniteLoop(true);

        ArrayList<String> bgPics = new ArrayList<>();
        for (int i = 0; i < categoryCount; i++) {
            bgPics.add(categoryBeans.get(i).getBg_pic());
        }
        adapter = new UltraPagerAdapter(myActivity, bgPics);
        ultra_viewpager.setAdapter(adapter);
        ultra_viewpager.setMultiScreen(1.0f);
        ultra_viewpager.setItemRatio(1.0f);
        ultra_viewpager.setAutoMeasureHeight(true);
        ultra_viewpager.setOffscreenPageLimit(11);


        ultra_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logutil.e("onPageScrolled  position=" + position);
                if (categoryBeans.size() < 1) {
                    return;
                }

                //"页面:" + position + "
                // offset偏移百分比" + positionOffset
                // pix像素" + positionOffsetPixels
                //设置下划线的属性
                //设置下划线View的长度
                Logutil.e("Position=" + position + "<positionOffset=" + positionOffset + "<positionOffsetPixels>=" + positionOffsetPixels);
                FrameLayout.LayoutParams par = (FrameLayout.LayoutParams) indicator_view.getLayoutParams();
                //设置下划线距离左边的位置长度
                int left = (int) ((positionOffset + ((position) % categoryBeans.size())) * MyApplication.sWidthPix / categoryBeans.size());
                if (MyApplication.sWidthPix - left < 10) {//上一行代码精度丢失,故加一行
                    par.setMargins(0, 0, 0, 0);
                } else {
                    par.setMargins(left, 0, 0, 0);
                }
                indicator_view.setLayoutParams(par);
            }

            @Override
            public void onPageSelected(int position) {
                Logutil.e("onPageSelected  position=" + position);
                curPosition = position;
                PublicUtil.ChangeScollViewColor(myActivity, scrollViews, ((position) % categoryBeans.size()));
                boolean isCollect = FavouriteUtil.isCollect(categoryBeans.get(ultra_viewpager.getCurrentItem()).getFid(), null);
                if (isCollect) {
                    isCollectShow_iv.setImageResource(R.drawable.btn_favorite_2);
                } else {
                    isCollectShow_iv.setImageResource(R.drawable.btn_favorite_1);
                }
                downcontent_tv.setText(categoryBeans.get(ultra_viewpager.getCurrentItem()).getName().toUpperCase() + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        boolean isCollect = FavouriteUtil.isCollect(categoryBeans.get(ultra_viewpager.getCurrentItem()).getFid(), null);
        if (isCollect) {
            isCollectShow_iv.setImageResource(R.drawable.btn_favorite_2);
        } else {
            isCollectShow_iv.setImageResource(R.drawable.btn_favorite_1);
        }
        downcontent_tv.setText(categoryBeans.get(ultra_viewpager.getCurrentItem()).getName().toUpperCase() + "");
    }

    int indicator_width;

    /**
     * left click
     */
    @OnClick(R.id.left_click_tv)
    public void leftModeClick() {
        if (categoryBeans.size() == 0) {
            return;
        }
        curPosition--;
        if (curPosition == 0) {
            curPosition = categoryBeans.size();
        }
        ultra_viewpager.setCurrentItem(curPosition);
    }

    /**
     * right click
     */
    @OnClick(R.id.right_click_tv)
    public void rightModeClick() {
        if (categoryBeans.size() == 0) {
            return;
        }
        curPosition++;
        if (curPosition == categoryBeans.size()) {
            curPosition = 0;
        }
        ultra_viewpager.setCurrentItem(curPosition);
    }

    @OnClick(R.id.backhome_iv)
    public void backHomeClick() {
        upToDown(category_title_ll, 1);
    }

    /**
     * 选择进入下一步
     */
    @OnClick(R.id.select_bt)
    public void selectClick() {
        upToDown(category_title_ll, 2);
    }

    @OnClick(R.id.isCollectShow_iv)
    public void collectClick() {
        if (categoryBeans == null || categoryBeans.size() == 0) {
            return;
        }
        FavouriteUtil.setCollect(categoryBeans.get(ultra_viewpager.getCurrentItem()).getFid(), new FavouriteUtil.CollectCallBack() {
            @Override
            public void YesBack() {
                isCollectShow_iv.setImageResource(R.drawable.btn_favorite_2);
                FavouriteUtil.showCollectPop(myActivity, layoutView, true);
            }

            @Override
            public void NoBack() {
                isCollectShow_iv.setImageResource(R.drawable.btn_favorite_1);
                FavouriteUtil.showCollectPop(myActivity, layoutView, false);
            }
        });
    }


    public void startBeginAnim() {
        if (category_title_ll != null) {
            downToUp(category_title_ll);
        }
    }

    public void upToDown(final View v, final int tag) {
        Logutil.e("RecipeCategory=upToDown" + v.getMeasuredHeight());
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(0, PublicUtil.dip2px(myActivity, 600));
        animator.setTarget(v);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(800).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (tag == 1) {
                    EventBus.getDefault().post(new MainChangeEvent(1, 0, 0));
                } else if (tag == 2) {

                    int position1 = 0;
                    if (category_ID.contains("A")) {
                        position1 = 0;
                    } else if (category_ID.contains("B")) {
                        position1 = 1;
                    } else if (category_ID.contains("C")) {
                        position1 = 2;
                    } else if (category_ID.contains("D")) {
                        position1 = 3;
                    } else if (category_ID.contains("E")) {
                        position1 = 4;
                    } else if (category_ID.contains("F")) {
                        position1 = 5;
                    } else if (category_ID.contains("G")) {
                        position1 = 6;
                    }
                    EventBus.getDefault().post(new MainChangeEvent(3, position1, ultra_viewpager.getCurrentItem()));
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    public void downToUp(final View v) {
        Logutil.e("RecipeCategory=downToUp" + v.getMeasuredHeight());
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(PublicUtil.dip2px(myActivity, 600), 0);
        animator.setTarget(v);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(800).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }


    public static RecipeCategoryFragment newInstance(String category_ID, String category_Name) {
        RecipeCategoryFragment fragment = new RecipeCategoryFragment();
        Bundle args = new Bundle();
        args.putString("category_ID", category_ID);
        args.putString("category_Name", category_Name);
        fragment.setArguments(args);
        return fragment;
    }

}
