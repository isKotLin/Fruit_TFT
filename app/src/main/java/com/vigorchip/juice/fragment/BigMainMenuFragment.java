package com.vigorchip.juice.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tmall.ultraviewpager.UltraViewPager;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.activity.ManualActivity;
import com.vigorchip.juice.activity.MyRecipeActivity;
import com.vigorchip.juice.activity.SettingActivity;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.DataContant;
import com.vigorchip.juice.util.ImageUtil;
import com.vigorchip.juice.util.PublicUtil;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.juice.view.SpacingTextView;
import com.vigorchip.puliblib.base.BaseFragment;
import com.vigorchip.puliblib.utils.DoubleClickUtil;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/4/2.
 */

public class BigMainMenuFragment extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.ultra_viewpager)
    UltraViewPager ultra_viewpager;

    @InjectView(R.id.iv_position_01)
    ImageView iv_position_01;

    @InjectView(R.id.iv_position_02)
    ImageView iv_position_02;

    @InjectView(R.id.iv_position_03)
    ImageView iv_position_03;

    @InjectView(R.id.iv_position_04)
    ImageView iv_position_04;

    @InjectView(R.id.iv_position_05)
    ImageView iv_position_05;

    @InjectView(R.id.iv_position_06)
    ImageView iv_position_06;

    @InjectView(R.id.iv_position_07)
    ImageView iv_position_07;

    @InjectView(R.id.iv_position_08)
    ImageView iv_position_08;

    @InjectView(R.id.iv_position_09)
    ImageView iv_position_09;

    @InjectView(R.id.iv_position_10)
    ImageView iv_position_10;

    @InjectView(R.id.upcontent_tv)
    TextView upcontent_tv;

    @InjectView(R.id.downcontent_tv)
    SpacingTextView downcontent_tv;

    @InjectView(R.id.content_ll)
    LinearLayout content_ll;

    @InjectView(R.id.commitclick_bt)
    Button commitclick_bt;

    private int curPosition = 0;

    private PagerAdapter adapter;


    private String[] bg_pics = DataContant.mainBgPics;
    private String[] downTexts = DataContant.mainModeNames;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bigmainmenu;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        TextFontUtil.changeRobotoRegular(myActivity, upcontent_tv);
        TextFontUtil.changeRobotoBold(myActivity, downcontent_tv);
        TextFontUtil.changeRobotoBold(myActivity, commitclick_bt);

        ultra_viewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultra_viewpager.setInfiniteLoop(true);
        adapter = new UltraPagerAdapter(bg_pics);
        ultra_viewpager.setAdapter(adapter);
        ultra_viewpager.setMultiScreen(0.6875f);
        ultra_viewpager.setItemRatio(1.0f);
        ultra_viewpager.setAutoMeasureHeight(true);
        ultra_viewpager.setOffscreenPageLimit(11);
        Logutil.e("缓存个数=" + ultra_viewpager.getOffscreenPageLimit());
//        ultra_viewpager.initIndicator();
//        ultra_viewpager.getIndicator().setOrientation(gravity_indicator);
        ultra_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logutil.e("position=" + position);
                curPosition = position;
                switch (ultra_viewpager.getCurrentItem()) {
                    case 0:
                        showChooseHeight(0);
                        downcontent_tv.setText(downTexts[0]);
                        break;
                    case 1:
                        showChooseHeight(1);
                        downcontent_tv.setText(downTexts[1]);
                        break;
                    case 2:
                        showChooseHeight(2);
                        downcontent_tv.setText(downTexts[2]);
                        break;
                    case 3:
                        showChooseHeight(3);
                        downcontent_tv.setText(downTexts[3]);
                        break;
                    case 4:
                        showChooseHeight(4);
                        downcontent_tv.setText(downTexts[4]);
                        break;
                    case 5:
                        showChooseHeight(5);
                        downcontent_tv.setText(downTexts[5]);
                        break;
                    case 6:
                        showChooseHeight(6);
                        downcontent_tv.setText(downTexts[6]);
                        break;
                    case 7:
                        showChooseHeight(7);
                        downcontent_tv.setText(downTexts[7]);
                        break;
                    case 8:
                        showChooseHeight(8);
                        downcontent_tv.setText(downTexts[8]);
                        break;
                    case 9:
                        showChooseHeight(9);
                        downcontent_tv.setText(downTexts[9]);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        downcontent_tv.setText(downTexts[0]);
        iv_position_10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = System.currentTimeMillis();
                        mHandler.sendEmptyMessageDelayed(Constant.HANDLER_SUCCESS, 300);
                        break;
                    case MotionEvent.ACTION_UP:
                        mHandler.removeMessages(Constant.HANDLER_SUCCESS);
                        break;
                }
                return false;
            }
        });
        iv_position_01.setOnClickListener(this);
        iv_position_02.setOnClickListener(this);
        iv_position_03.setOnClickListener(this);
        iv_position_04.setOnClickListener(this);
        iv_position_05.setOnClickListener(this);
        iv_position_06.setOnClickListener(this);
        iv_position_07.setOnClickListener(this);
        iv_position_08.setOnClickListener(this);
        iv_position_09.setOnClickListener(this);
        iv_position_10.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_position_01:
                showChooseHeight(0);
                ultra_viewpager.setCurrentItem(0);
                break;
            case R.id.iv_position_02:
                showChooseHeight(1);
                ultra_viewpager.setCurrentItem(1);
                break;
            case R.id.iv_position_03:
                showChooseHeight(2);
                ultra_viewpager.setCurrentItem(2);
                break;
            case R.id.iv_position_04:
                showChooseHeight(3);
                ultra_viewpager.setCurrentItem(3);
                break;
            case R.id.iv_position_05:
                showChooseHeight(4);
                ultra_viewpager.setCurrentItem(4);
                break;
            case R.id.iv_position_06:
                showChooseHeight(5);
                ultra_viewpager.setCurrentItem(5);
                break;
            case R.id.iv_position_07:
                showChooseHeight(6);
                ultra_viewpager.setCurrentItem(6);
                break;
            case R.id.iv_position_08:
                showChooseHeight(7);
                ultra_viewpager.setCurrentItem(7);
                break;
            case R.id.iv_position_09:
                showChooseHeight(8);
                ultra_viewpager.setCurrentItem(8);
                break;
            case R.id.iv_position_10:
                showChooseHeight(9);
                ultra_viewpager.setCurrentItem(9);
                break;
        }
    }


    public void showChooseHeight(int position) {
        curPosition = position;
        iv_position_01.setImageResource(R.drawable.btn_icon_09_1);
        iv_position_02.setImageResource(R.drawable.btn_icon_01_1);
        iv_position_03.setImageResource(R.drawable.btn_icon_02_1);
        iv_position_04.setImageResource(R.drawable.btn_icon_03_1);
        iv_position_05.setImageResource(R.drawable.btn_icon_04_1);
        iv_position_06.setImageResource(R.drawable.btn_icon_05_1);
        iv_position_07.setImageResource(R.drawable.btn_icon_06_1);
        iv_position_08.setImageResource(R.drawable.btn_icon_07_1);
        iv_position_09.setImageResource(R.drawable.btn_icon_10_1);
        iv_position_10.setImageResource(R.drawable.btn_icon_08_1);
        if (position == 0) {
            iv_position_01.setImageResource(R.drawable.btn_icon_09_2);
        } else if (position == 1) {
            iv_position_02.setImageResource(R.drawable.btn_icon_01_2);
        } else if (position == 2) {
            iv_position_03.setImageResource(R.drawable.btn_icon_02_2);
        } else if (position == 3) {
            iv_position_04.setImageResource(R.drawable.btn_icon_03_2);
        } else if (position == 4) {
            iv_position_05.setImageResource(R.drawable.btn_icon_04_2);
        } else if (position == 5) {
            iv_position_06.setImageResource(R.drawable.btn_icon_05_2);
        } else if (position == 6) {
            iv_position_07.setImageResource(R.drawable.btn_icon_06_2);
        } else if (position == 7) {
            iv_position_08.setImageResource(R.drawable.btn_icon_07_2);
        } else if (position == 8) {
            iv_position_09.setImageResource(R.drawable.btn_icon_10_2);
        } else if (position == 9) {
            iv_position_10.setImageResource(R.drawable.btn_icon_08_2);
        }
    }

    private long startClickTime;
    /**
     * 为了那个长按10秒弹出总时间的傻逼需求
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.HANDLER_SUCCESS) {
                if (System.currentTimeMillis() - startClickTime >= 10000) {
                    long time = MyApplication.runAllTime % (9999 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000);
                    Logutil.e("总时间=" + time);
                    long hour = time / (1000 * 3600);
                    long min = (time % (1000 * 3600)) / (60 * 1000);
                    long sec = (time - hour * 3600 * 1000 - min * 60 * 1000) / 1000;
                    Toast toast = Toast.makeText(myActivity, Constant.PROJECT_ID + "--运行总时间:" + hour + "时 " + min + "分 " + sec + "秒", Toast.LENGTH_LONG);
                    showMyToast(toast, 5000);
                } else {
                    mHandler.sendEmptyMessageDelayed(Constant.HANDLER_SUCCESS, 300);
                }
            }
        }
    };

    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }

    /**
     * 进入子模块
     */
    @OnClick(R.id.commitclick_bt)
    public void commitClick() {
        if (DoubleClickUtil.isCanClick(commitclick_bt.getId())) {
            switch (ultra_viewpager.getCurrentItem()) {
                case 0:
                    downToUp(0, content_ll);
                    break;
                case 1:
                    downToUp(1, content_ll);
                    break;
                case 2:
                    downToUp(2, content_ll);
                    break;
                case 3:
                    downToUp(3, content_ll);
                    break;
                case 4:
                    downToUp(4, content_ll);
                    break;
                case 5:
                    downToUp(5, content_ll);
                    break;
                case 6:
                    downToUp(6, content_ll);
                    break;
                case 7:
                    downToUp(7, content_ll);
                    break;
                case 8:
                    downToUp(8, content_ll);
                    break;
                case 9:
                    downToUp(9, content_ll);
                    break;
            }
        }
    }


    public void startBeginAnim() {
        if (content_ll != null) {
            upToDown(content_ll);
        }
    }


    public void upToDown(final View v) {
        Logutil.e("bigmainmenufragment=upToDown" + v.getMeasuredHeight());
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(-PublicUtil.dip2px(myActivity, content_ll.getMeasuredHeight() + 130), 0);
        animator.setTarget(v);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }


    public void downToUp(final int position, final View v) {
        Logutil.e("bigmainmenufragment=downToUp" + v.getMeasuredHeight());
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(0, -PublicUtil.dip2px(myActivity, content_ll.getMeasuredHeight() + 130));
        animator.setTarget(v);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

        String str = "";
        if (position < 8 && position > 0) {
            if (position == 1) {
                str = "A";
            } else if (position == 2) {
                str = "B";
            } else if (position == 3) {
                str = "C";
            } else if (position == 4) {
                str = "D";
            } else if (position == 5) {
                str = "E";
            } else if (position == 6) {
                str = "F";
            } else if (position == 7) {
                str = "G";
            } else {
                str = "A";
            }
            Constant.setCategoryBeans(str);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switch (position) {
                    case 0:
                        ManualActivity.startActivity(myActivity);
                        break;
                    case 1:
                        EventBus.getDefault().post(new MainChangeEvent(2, 0, 0));
                        break;
                    case 2:
                        EventBus.getDefault().post(new MainChangeEvent(2, 1, 0));
                        break;
                    case 3:
                        EventBus.getDefault().post(new MainChangeEvent(2, 2, 0));
                        break;
                    case 4:
                        EventBus.getDefault().post(new MainChangeEvent(2, 3, 0));
                        break;
                    case 5:
                        EventBus.getDefault().post(new MainChangeEvent(2, 4, 0));
                        break;
                    case 6:
                        EventBus.getDefault().post(new MainChangeEvent(2, 5, 0));
                        break;
                    case 7:
                        EventBus.getDefault().post(new MainChangeEvent(2, 6, 0));
                        break;
                    case 8:
                        MyRecipeActivity.startActivity(myActivity);
                        break;
                    case 9:
                        SettingActivity.startActivity(myActivity);
                        break;
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


    class UltraPagerAdapter extends PagerAdapter {
        private String[] beans;

        public UltraPagerAdapter(String[] beans) {
            this.beans = beans;
        }

        @Override
        public int getCount() {
            return beans.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrameLayout linearLayout = (FrameLayout) LayoutInflater.from(myActivity).inflate(R.layout.item_main_vp_child, null);
            ImageView image_iv = (ImageView) linearLayout.findViewById(R.id.image_iv);
            linearLayout.setId(R.id.item_id);

            FrameLayout.LayoutParams iv_params = new FrameLayout.LayoutParams((int) (MyApplication.sWidthPix * 5.5 / 8), MyApplication.sHeightPix);
            image_iv.setLayoutParams(iv_params);
            image_iv.setScaleType(ImageView.ScaleType.FIT_XY);//ImageView.ScaleType.FIT_XY
            ImageUtil.showImageByName(myActivity, image_iv, beans[position]);
            container.addView(linearLayout);
            image_iv.setOnTouchListener(new View.OnTouchListener() {
                private int downX = 0;
                private long downTime = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //获取按下的x坐标
                            downX = (int) v.getX();
                            downTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            int moveX = (int) v.getX();
                            long moveTime = System.currentTimeMillis();
                            if (downX == moveX && (moveTime - downTime < 500)) {//点击的条件
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                    }
                    return true;
                }
            });
            return linearLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            FrameLayout view = (FrameLayout) object;
            container.removeView(view);
        }
    }


    public static BigMainMenuFragment newInstance() {
        BigMainMenuFragment fragment = new BigMainMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}
