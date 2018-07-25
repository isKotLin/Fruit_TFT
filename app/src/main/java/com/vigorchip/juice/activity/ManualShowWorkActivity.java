package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigorchip.db.entity.ActionBean;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.CupStatusManager;
import com.vigorchip.juice.util.ManualMachineControl;
import com.vigorchip.juice.util.PublicUtil;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.juice.util.event.ChangeJarCupEvent;
import com.vigorchip.juice.util.event.ScreenCloseEvent;
import com.vigorchip.juice.view.PickerView;
import com.vigorchip.juice.view.ProgressView;
import com.vigorchip.juice.view.wheel.LoopView;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.Logutil;
import com.vigorchip.puliblib.utils.MachineErrorManager;
import com.vigorchip.puliblib.utils.event.ErrorEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.vigorchip.juice.util.Constant.ISLONGCLICKRUNNING;

/**
 * Created by Administrator on 2017/2/19.//手动选择时间和速度
 */

public class ManualShowWorkActivity extends BaseActivity {

    @InjectView(R.id.speed_tag_iv)
    ImageView speed_tag_iv;

    @InjectView(R.id.progress_view)
    ProgressView progress_view;

    @InjectView(R.id.play_iv)
    ImageView play_iv;

    @InjectView(R.id.pause_iv)
    ImageView pause_iv;

    @InjectView(R.id.min_time_tv)
    TextView min_time_tv;

    @InjectView(R.id.sec_time_tv)
    TextView sec_time_tv;

    @InjectView(R.id.speed_tv)
    TextView speed_tv;

    @InjectView(R.id.min_plus_tv)
    TextView min_plus_tv;

    @InjectView(R.id.min_min_tv)
    TextView min_min_tv;

    @InjectView(R.id.sec_plus_tv)
    TextView sec_plus_tv;

    @InjectView(R.id.sec_min_tv)
    TextView sec_min_tv;

    @InjectView(R.id.speed_plus_tv)
    TextView speed_plus_tv;

    @InjectView(R.id.speed_min_tv)
    TextView speed_min_tv;

    @InjectView(R.id.longclick_iv)
    ImageView longclick_iv;

    @InjectView(R.id.min_wpv)
    LoopView min_wpv;

    @InjectView(R.id.sec_wpv)
    LoopView sec_wpv;

    @InjectView(R.id.speed_wpv)
    LoopView speed_wpv;

    private Animation circle_anim;
    private List<ActionBean> allActionBeans = new ArrayList<>();

    private ManualMachineControl control;
    private int time;
    private long allTime;
    private long curTime;
    private int speed;
    private int min;
    private int sec;
    private boolean isStart;

    private String touchMinTime;
    private String touchSecTime;
    private String touchSeek;
    private long touchTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manualshowwork;
    }

    @Override
    protected void initView() {
        circle_anim = AnimationUtils.loadAnimation(this, R.anim.right_top_left_circle);
        time = getIntent().getIntExtra("time", 0);
        speed = getIntent().getIntExtra("speed", 0);
        if (time == 0 || speed == 0) {
            finish();
        }
        allTime = time * 1000;
        curTime = 0;
        setTimeAndSpeed();
    }

    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        initActionData();
        control = new ManualMachineControl(allActionBeans, 0, new ManualMachineControl.ControlListener() {
            @Override
            public void curMachineStatu(boolean isWorking, long alltime, long curtime, int curSpeed, int oldSpeed) {
                speed = curSpeed;
                Logutil.e("alltime=" + alltime);
                Logutil.e("curtime=" + curtime);
                allTime = alltime;
                curTime = curtime;
                progress_view.setMax((int) alltime);
                progress_view.setProgress((int) curtime);
                setTimeAndSpeed();
                if (isWorking) {
                    speed_tv.setText(curSpeed < 10 ? "0" + curSpeed : " H");
                    play_iv.setVisibility(View.GONE);
                    pause_iv.setVisibility(View.VISIBLE);
                    speed_tag_iv.clearAnimation();
                    if (curSpeed != 0) {
                        speed_tag_iv.startAnimation(circle_anim);
                    }
                    //全部显示
                    min_plus_tv.setVisibility(View.VISIBLE);
                    min_min_tv.setVisibility(View.VISIBLE);
                    sec_plus_tv.setVisibility(View.VISIBLE);
                    sec_min_tv.setVisibility(View.VISIBLE);
                    speed_plus_tv.setVisibility(View.VISIBLE);
                    speed_min_tv.setVisibility(View.VISIBLE);

                    min_wpv.setVisibility(View.GONE);
                    sec_wpv.setVisibility(View.GONE);
                    speed_wpv.setVisibility(View.GONE);
                    min_time_tv.setVisibility(View.VISIBLE);
                    sec_time_tv.setVisibility(View.VISIBLE);
                    speed_tv.setVisibility(View.VISIBLE);
                } else {
                    //全部隐藏
                    min_plus_tv.setVisibility(View.INVISIBLE);
                    min_min_tv.setVisibility(View.INVISIBLE);
                    sec_plus_tv.setVisibility(View.INVISIBLE);
                    sec_min_tv.setVisibility(View.INVISIBLE);
                    speed_plus_tv.setVisibility(View.INVISIBLE);
                    speed_min_tv.setVisibility(View.INVISIBLE);

                    min_wpv.setVisibility(View.VISIBLE);
                    sec_wpv.setVisibility(View.VISIBLE);
                    speed_wpv.setVisibility(View.VISIBLE);
                    min_time_tv.setVisibility(View.GONE);
                    sec_time_tv.setVisibility(View.GONE);
                    speed_tv.setVisibility(View.GONE);

                    speed_tv.setText((oldSpeed < 10 ? "0" + oldSpeed : "H") + "  speed");
                    play_iv.setVisibility(View.VISIBLE);
                    pause_iv.setVisibility(View.GONE);
                    speed_tag_iv.clearAnimation();
                }
            }

            @Override
            public void curSpeedBack(int speed1) {
                speed_tv.setText((speed1 < 10 ? "0" + speed1 : "H") + "  speed");
            }

            @Override
            public void curControlFinish() {
                min_time_tv.setText("00");
                sec_time_tv.setText("00");
                speed_tv.setText("00");
                progress_view.setMaxProgress();
                finishCurActivity();
            }

            @Override
            public void showInfo(String str) {

            }

            @Override
            public void handlerExecpter() {

            }
        });
        longclick_iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!control.isWorking() && MachineErrorManager.isMachineOK()) {

                            min_wpv.setVisibility(View.GONE);
                            sec_wpv.setVisibility(View.GONE);
                            speed_wpv.setVisibility(View.GONE);
                            min_time_tv.setVisibility(View.VISIBLE);
                            sec_time_tv.setVisibility(View.VISIBLE);
                            speed_tv.setVisibility(View.VISIBLE);
                            speed_tag_iv.startAnimation(circle_anim);

                            touchMinTime = min_time_tv.getText().toString();
                            touchSecTime = sec_time_tv.getText().toString();
                            touchSeek = speed_tv.getText().toString();
                            touchTime = System.currentTimeMillis();
                            min_time_tv.setText("00");
                            sec_time_tv.setText("00");

                            ISLONGCLICKRUNNING = true;

                            mhandler.removeMessages(Constant.HANDLER_SUCCESS);
                            mhandler.sendEmptyMessageDelayed(Constant.HANDLER_SUCCESS, 500);
                        }
                    case MotionEvent.ACTION_MOVE:
                        if (MachineErrorManager.isMachineOK() && MachineErrorManager.isMachineOK()) {
                            control.setSpeed(10, true);
                            speed_tv.setText(" H");
                            ISLONGCLICKRUNNING = true;
                        } else {
                            if (!control.isWorking()) {
                                control.setSpeed(0, false);
                                min_time_tv.setText(touchMinTime);
                                sec_time_tv.setText(touchSecTime);
                                speed_tv.setText(touchSeek);
                            }
                            ISLONGCLICKRUNNING = false;
                            mhandler.removeMessages(Constant.HANDLER_SUCCESS);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        control.setSpeed(0, false);
                        if (!control.isWorking()) {
                            min_wpv.setVisibility(View.VISIBLE);
                            sec_wpv.setVisibility(View.VISIBLE);
                            speed_wpv.setVisibility(View.VISIBLE);
                            min_time_tv.setVisibility(View.GONE);
                            sec_time_tv.setVisibility(View.GONE);
                            speed_tv.setVisibility(View.GONE);
                            speed_tag_iv.clearAnimation();

                            min_time_tv.setText(touchMinTime);
                            sec_time_tv.setText(touchSecTime);
                            speed_tv.setText(touchSeek);
                        }
                        ISLONGCLICKRUNNING = false;
                        mhandler.removeMessages(Constant.HANDLER_SUCCESS);
                        break;
                }
                return true;
            }
        });
        ArrayList<String> datas1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas1.add(i + "");
        }
        min_wpv.setItems(datas1);
        ArrayList<String> datas2 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            datas2.add(i + "");
        }
        sec_wpv.setItems(datas2);
        ArrayList<String> datas3 = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            if (i == 10) {
                datas3.add("H");
            } else {
                datas3.add(i + "");
            }
        }
        speed_wpv.setItems(datas3);
        min_wpv.setOnSelectListener(new LoopView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (!control.isWorking() && isStart) {
                    min_time_tv.setText(text);
                    min = Integer.valueOf(text);
                    if (min == 0 && sec == 0) {
                        sec_wpv.setCenterItem("1");
                        sec = 1;
                    }
                    ActionBean actionBean = allActionBeans.get(0);
//                    int totletime_int = sec + min * 60 +(int) (Math.round((curTime / 1000) + 0.5));
                    int totletime_int = sec + min * 60;
                    Logutil.e("totletime_int=" + totletime_int);
                    actionBean.setTotletime(String.valueOf(totletime_int));
                    actionBean.setEtime(String.valueOf(totletime_int));
                    allActionBeans.set(0, actionBean);
                    control.setActionBeansAndClear(allActionBeans);
                    progress_view.setAsync();
                }
            }
        });
        sec_wpv.setOnSelectListener(new LoopView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (!control.isWorking() && isStart) {
                    sec_time_tv.setText(text);
                    sec = Integer.valueOf(text);
                    if (min == 0 && sec == 0) {
                        sec_wpv.setCenterItem("1");
                        sec = 1;
                    }
                    ActionBean actionBean = allActionBeans.get(0);
                    int totletime_int = min * 60 + sec;
                    Logutil.e("totletime_int=" + totletime_int);
                    actionBean.setTotletime(String.valueOf(totletime_int));
                    actionBean.setEtime(String.valueOf(totletime_int));
                    allActionBeans.set(0, actionBean);
                    control.setActionBeansAndClear(allActionBeans);
                    progress_view.setAsync();
                }
            }
        });
        speed_wpv.setOnSelectListener(new LoopView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (!control.isWorking() && isStart) {
                    ActionBean actionBean = allActionBeans.get(0);
                    if (text.equals("H")) {
                        actionBean.setStartspeed("10");
                        actionBean.setEndspeed("10");
                    } else {
                        actionBean.setStartspeed(text);
                        actionBean.setEndspeed(text);
                    }
                    allActionBeans.set(0, actionBean);
                    control.setActionBeans(allActionBeans);
                }
            }
        });
        startPlay();
    }

    public void onEventMainThread(ScreenCloseEvent event) {
        if (control.isWorking()) {
            stopPlay();
        }
    }

    public void onEventMainThread(ChangeJarCupEvent event) {
        if (event.getMode() == 1) {//小杯
            CupStatusManager.MODESHOWTAG = "1";
            closeBack();
            EventBus.getDefault().post(new BackMenuEvent());
            finish();
        } else if (event.getMode() == 0) {//无杯
            if (!"0".equals(CupStatusManager.MODESHOWTAG)) {
                if (control.isWorking()) {
                    stopPlay();
                }
                CupStatusManager.MODESHOWTAG = "0";
                CupStatusManager.showNOCupNoticeDialog(this, true, new CupStatusManager.OnFinishListener() {
                    @Override
                    public void isCloseLister(boolean isClose) {
                        if (!CupStatusManager.isBigCupStatus) {
                            ManualShowWorkActivity.this.finish();
                        }
                    }
                });
            }

        } else {
            CupStatusManager.MODESHOWTAG = "2";
        }
    }

    public void onEventMainThread(ErrorEvent event) {
        super.onEventMainThread(event);
        if (!event.getTag().equals("E0")) {
            mhandler.removeMessages(Constant.HANDLER_SUCCESS);
        }
        if (control.isWorking() && (event.getTag().equals("E2") || event.getTag().equals("E3"))) {
            stopPlay();
        }
    }

    public void initActionData() {
        ActionBean actionBean1 = new ActionBean((long) 1, "H1", "p2", "0", "" + time, "continue", "" + time, speed + "", speed + "", "1", "1");
        allActionBeans.add(actionBean1);
    }

    /**
     * 分+
     *
     * @param view
     */
    public void minplusClick(View view) {
        if (min == 9) {
            return;
        }
        min++;
        for (int i = 0; i < allActionBeans.size(); i++) {
            ActionBean actionBean = allActionBeans.get(i);
            String totletime = actionBean.getTotletime();
            int totletime_int = Integer.valueOf(totletime) + 60;
            actionBean.setTotletime(String.valueOf(totletime_int));
            actionBean.setEtime(String.valueOf(totletime_int));
            allActionBeans.set(i, actionBean);
        }
        control.setActionBeans(allActionBeans);
    }

    /**
     * 分-
     *
     * @param view
     */
    public void minminClick(View view) {
        if (min == 0) {
            return;
        }
        min--;
        for (int i = 0; i < allActionBeans.size(); i++) {
            ActionBean actionBean = allActionBeans.get(i);
            String totletime = actionBean.getTotletime();
            int totletime_int = Integer.valueOf(totletime) - 60;
            actionBean.setTotletime(String.valueOf(totletime_int));
            actionBean.setEtime(String.valueOf(totletime_int));
            allActionBeans.set(i, actionBean);
        }
        control.setActionBeans(allActionBeans);
    }

    /**
     * 秒+
     *
     * @param view
     */
    public void secplusClick(View view) {
        int add_sec;
        if (sec == 59) {
            return;
        } else if (sec >= 54) {
            add_sec = 59 - sec;
            sec = 59;
        } else {
            sec += 5;
            add_sec = 5;
        }
        for (int i = 0; i < allActionBeans.size(); i++) {
            ActionBean actionBean = allActionBeans.get(i);
            String totletime = actionBean.getTotletime();
            int totletime_int = Integer.valueOf(totletime) + add_sec;
            actionBean.setTotletime(String.valueOf(totletime_int));
            actionBean.setEtime(String.valueOf(totletime_int));
            allActionBeans.set(i, actionBean);
        }
        control.setActionBeans(allActionBeans);
    }

    /**
     * 秒-
     *
     * @param view
     */
    public void secminClick(View view) {
        int min_sec;
        if (sec == 0) {
            return;
        } else if (sec <= 5) {
            min_sec = sec - 0;
            sec = 0;
        } else {
            sec -= 5;
            min_sec = 5;
        }
        for (int i = 0; i < allActionBeans.size(); i++) {
            ActionBean actionBean = allActionBeans.get(i);
            String totletime = actionBean.getTotletime();
            int totletime_int = Integer.valueOf(totletime) - min_sec;
            actionBean.setTotletime(String.valueOf(totletime_int));
            actionBean.setEtime(String.valueOf(totletime_int));
            allActionBeans.set(i, actionBean);
        }
        control.setActionBeans(allActionBeans);
    }

    /**
     * 速度+
     *
     * @param view
     */
    public void speedplusClick(View view) {
        if (speed == 10) {
            return;
        }
        speed++;
        for (int i = 0; i < allActionBeans.size(); i++) {
            ActionBean actionBean = allActionBeans.get(i);
            actionBean.setStartspeed(speed + "");
            actionBean.setEndspeed(speed + "");
            allActionBeans.set(i, actionBean);
        }
        control.setActionBeans(allActionBeans);
    }

    /**
     * 速度-
     *
     * @param view
     */
    public void speedminClick(View view) {
        if (speed == 1) {
            return;
        }
        speed--;
        for (int i = 0; i < allActionBeans.size(); i++) {
            ActionBean actionBean = allActionBeans.get(i);
            actionBean.setStartspeed(speed + "");
            actionBean.setEndspeed(speed + "");
            allActionBeans.set(i, actionBean);
        }
        control.setActionBeans(allActionBeans);
    }

    /**
     * 点击就停止了
     */
    @OnClick(R.id.pause_iv)
    public void stopPlay() {
        if (ISLONGCLICKRUNNING) {
            return;
        }

        min_wpv.setCenterItem(min + "");
        sec_wpv.setCenterItem(sec + "");
        if (speed < 10) {
            speed_wpv.setCenterItem(speed + "");
        } else {
            speed_wpv.setCenterItem("H");
        }
        Logutil.e("min=" + min + "<><>" + sec + "<><>" + speed);
        PublicUtil.keepScreenOn(this, false);
        control.stopPlay();
    }


    /**
     * 点击就开始了
     */
    @OnClick(R.id.play_iv)
    public void startPlay() {
        if (MachineErrorManager.isMachineOK() && !ISLONGCLICKRUNNING) {
            PublicUtil.keepScreenOn(this, true);
            control.startPlay();
            isStart = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeBack();
    }

    public void closeBack() {
        if (control != null && control.isWorking()) {
            control.stopPlay();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeBack();
    }


    /**
     * 退出
     *
     * @param view
     */
    public void backFinishClick(View view) {
        if (ISLONGCLICKRUNNING) {
            return;
        }
        onBackPressed();
    }

    /**
     * 设置时间
     */
    public void setTimeAndSpeed() {
        min = (int) (((allTime - curTime) / 1000) / 60);
        sec = (int) Math.round(((allTime - curTime) * 1.0 / 1000) % 60 + 0.4999);
        Logutil.e("second=" + sec);
        if (sec != 0 && sec % 60 == 0) {
            sec = 0;
            min++;
        }
        min_time_tv.setText(min < 10 ? "0" + min : "" + min);
        sec_time_tv.setText(sec < 10 ? "0" + sec : "" + sec);
        speed_tv.setText(speed < 10 ? ("0" + speed) : "H");
    }


    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.HANDLER_SUCCESS) {
                long time = System.currentTimeMillis() - touchTime;
                int min = (int) (time / (1000 * 60));
                int sec = (int) ((time / 1000) % 60);
                Logutil.e("min=" + min + "<><>sec=" + sec);
                min_time_tv.setText((min < 10 ? "0" + min : "" + min));
                sec_time_tv.setText((sec < 10 ? "0" + sec : "" + sec));
                Logutil.e("收到时间更改通知-=" + time);
                mhandler.removeMessages(Constant.HANDLER_SUCCESS);
                mhandler.sendEmptyMessageDelayed(Constant.HANDLER_SUCCESS, 500);
            }
        }
    };

    public void finishCurActivity() {
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SmallWorkFinishActivity.startActivity(ManualShowWorkActivity.this);
                finish();
            }
        }, 500);

    }

    public static void startActivity(Context context, int time, int speed) {
        context.startActivity(new Intent(context, ManualShowWorkActivity.class).putExtra("time", time).putExtra("speed", speed));
    }
}
