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
import com.vigorchip.juice.util.MachineControl;
import com.vigorchip.juice.util.PublicUtil;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.juice.util.event.ChangeJarCupEvent;
import com.vigorchip.juice.util.event.ScreenCloseEvent;
import com.vigorchip.juice.view.ProgressView;
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
 * Created by Administrator on 2017/3/14.ice crushin Smooth soup
 */

public class ManualShowWorkCustomActivity extends BaseActivity {
    int mode;

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

    @InjectView(R.id.longclick_iv)
    ImageView longclick_iv;


    private Animation circle_anim;
    private List<ActionBean> allActionBeans = new ArrayList<>();

    private MachineControl control;

    private String touchMinTime;
    private String touchSecTime;
    private String touchSeek;
    private long touchTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manualshowworkcustom;
    }

    @Override
    protected void initView() {
        circle_anim = AnimationUtils.loadAnimation(this, R.anim.right_top_left_circle);
    }

    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        mode = getIntent().getIntExtra("mode", 1);
        initActionData();
        control = new MachineControl(allActionBeans, 0, new MachineControl.ControlListener() {
            @Override
            public void curMachineStatu(boolean isWorking, long alltime, long curtime, int curSpeed, int oldSpeed) {
                progress_view.setMax((int) alltime);
                progress_view.setProgress((int) curtime);
                setTimeAndSpeed(alltime - curtime, curSpeed);
                if (isWorking) {
                    play_iv.setVisibility(View.GONE);
                    pause_iv.setVisibility(View.VISIBLE);
                    speed_tag_iv.clearAnimation();
                    if (curSpeed != 0) {
                        speed_tag_iv.startAnimation(circle_anim);
                    }
                } else {
                    play_iv.setVisibility(View.VISIBLE);
                    pause_iv.setVisibility(View.GONE);
                    speed_tv.setText((oldSpeed < 10 ? "0" + oldSpeed : "H") + "  speed");
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
                            touchMinTime = min_time_tv.getText().toString();
                            touchSecTime = sec_time_tv.getText().toString();
                            touchSeek = speed_tv.getText().toString();
                            touchTime = System.currentTimeMillis();
                            speed_tag_iv.startAnimation(circle_anim);
                            mhandler.removeMessages(Constant.HANDLER_SUCCESS);
                            mhandler.sendEmptyMessageDelayed(Constant.HANDLER_SUCCESS, 500);
                            ISLONGCLICKRUNNING = true;
                        }
                    case MotionEvent.ACTION_MOVE:
                        if (MachineErrorManager.isMachineOK() && MachineErrorManager.isMachineOK()) {
                            control.setSpeed(10, true);
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
                            min_time_tv.setText(touchMinTime);
                            sec_time_tv.setText(touchSecTime);
                            speed_tv.setText(touchSeek);
                            speed_tag_iv.clearAnimation();
                        }
                        ISLONGCLICKRUNNING = false;
                        mhandler.removeMessages(Constant.HANDLER_SUCCESS);
                        break;
                }
                return true;
            }
        });
        startPlay();

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
                            ManualShowWorkCustomActivity.this.finish();
                        }
                    }
                });
            }

        } else {
            CupStatusManager.MODESHOWTAG = "2";
        }
    }

    public void onEventMainThread(ScreenCloseEvent event) {
        if (control.isWorking()) {
            stopPlay();
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
        if (mode == 1) {
            ActionBean actionBean1 = new ActionBean((long) 1, "H1", "p2", "0", "3", "continue", "80", "6", "6", "1", "1");
            ActionBean actionBean2 = new ActionBean((long) 2, "H1", "p2", "3", "5", "continue", "80", "0", "0", "1", "1");
            ActionBean actionBean3 = new ActionBean((long) 3, "H1", "p2", "5", "8", "continue", "80", "6", "6", "1", "1");
            ActionBean actionBean4 = new ActionBean((long) 4, "H1", "p2", "8", "10", "continue", "80", "0", "0", "1", "1");
            ActionBean actionBean5 = new ActionBean((long) 5, "H1", "p2", "1", "13", "continue", "80", "6", "6", "1", "1");
            ActionBean actionBean6 = new ActionBean((long) 6, "H1", "p2", "13", "15", "continue", "80", "0", "0", "1", "1");
            ActionBean actionBean7 = new ActionBean((long) 7, "H1", "p2", "15", "18", "continue", "80", "6", "6", "1", "1");
            ActionBean actionBean8 = new ActionBean((long) 8, "H1", "p2", "18", "20", "continue", "80", "0", "0", "1", "1");
            ActionBean actionBean9 = new ActionBean((long) 9, "H1", "p2", "20", "80", "continue", "80", "2", "2", "1", "1");
            allActionBeans.add(actionBean1);
            allActionBeans.add(actionBean2);
            allActionBeans.add(actionBean3);
            allActionBeans.add(actionBean4);
            allActionBeans.add(actionBean5);
            allActionBeans.add(actionBean6);
            allActionBeans.add(actionBean7);
            allActionBeans.add(actionBean8);
            allActionBeans.add(actionBean9);
        } else if (mode == 2) {
            ActionBean actionBean1 = new ActionBean((long) 1, "H1", "p2", "0", "3", "continue", "85", "8", "8", "1", "1");
            ActionBean actionBean2 = new ActionBean((long) 2, "H1", "p2", "3", "5", "continue", "85", "0", "0", "1", "1");
            ActionBean actionBean3 = new ActionBean((long) 3, "H1", "p2", "5", "8", "continue", "85", "8", "8", "1", "1");
            ActionBean actionBean4 = new ActionBean((long) 4, "H1", "p2", "8", "10", "continue", "85", "0", "0", "1", "1");
            ActionBean actionBean5 = new ActionBean((long) 5, "H1", "p2", "10", "13", "continue", "85", "8", "8", "1", "1");
            ActionBean actionBean6 = new ActionBean((long) 6, "H1", "p2", "13", "15", "continue", "85", "0", "0", "1", "1");
            ActionBean actionBean7 = new ActionBean((long) 7, "H1", "p2", "15", "18", "continue", "85", "8", "8", "1", "1");
            ActionBean actionBean8 = new ActionBean((long) 8, "H1", "p2", "18", "20", "continue", "85", "0", "0", "1", "1");
            ActionBean actionBean9 = new ActionBean((long) 9, "H1", "p2", "20", "23", "continue", "85", "8", "8", "1", "1");
            ActionBean actionBean10 = new ActionBean((long) 10, "H1", "p2", "23", "25", "continue", "85", "0", "0", "1", "1");
            ActionBean actionBean11 = new ActionBean((long) 11, "H1", "p2", "25", "85", "continue", "85", "2", "2", "1", "1");
            allActionBeans.add(actionBean1);
            allActionBeans.add(actionBean2);
            allActionBeans.add(actionBean3);
            allActionBeans.add(actionBean4);
            allActionBeans.add(actionBean5);
            allActionBeans.add(actionBean6);
            allActionBeans.add(actionBean7);
            allActionBeans.add(actionBean8);
            allActionBeans.add(actionBean9);
            allActionBeans.add(actionBean10);
            allActionBeans.add(actionBean11);
        } else if (mode == 3) {
            ActionBean actionBean1 = new ActionBean((long) 1, "H1", "p2", "0", "8", "continue", "30", "10", "10", "1", "1");
            ActionBean actionBean2 = new ActionBean((long) 2, "H1", "p2", "8", "10", "continue", "30", "0", "0", "1", "1");
            ActionBean actionBean3 = new ActionBean((long) 3, "H1", "p2", "10", "18", "continue", "30", "10", "10", "1", "1");
            ActionBean actionBean4 = new ActionBean((long) 4, "H1", "p2", "18", "20", "continue", "30", "0", "0", "1", "1");
            ActionBean actionBean5 = new ActionBean((long) 5, "H1", "p2", "20", "28", "continue", "30", "10", "10", "1", "1");
            ActionBean actionBean6 = new ActionBean((long) 6, "H1", "p2", "28", "30", "continue", "30", "0", "0", "1", "1");
            allActionBeans.add(actionBean1);
            allActionBeans.add(actionBean2);
            allActionBeans.add(actionBean3);
            allActionBeans.add(actionBean4);
            allActionBeans.add(actionBean5);
            allActionBeans.add(actionBean6);
        } else if (mode == 4) {
            ActionBean actionBean1 = new ActionBean((long) 1, "H1", "p2", "0", "30", "continue", "116", "10", "10", "1", "1");
            ActionBean actionBean2 = new ActionBean((long) 2, "H1", "p2", "30", "32", "continue", "116", "0", "0", "1", "1");
            ActionBean actionBean3 = new ActionBean((long) 3, "H1", "p2", "32", "42", "continue", "116", "6", "6", "1", "1");
            ActionBean actionBean4 = new ActionBean((long) 4, "H1", "p2", "42", "44", "continue", "116", "0", "0", "1", "1");
            ActionBean actionBean5 = new ActionBean((long) 5, "H1", "p2", "44", "104", "continue", "116", "10", "10", "1", "1");
            ActionBean actionBean6 = new ActionBean((long) 6, "H1", "p2", "104", "106", "continue", "116", "0", "0", "1", "1");
            ActionBean actionBean7 = new ActionBean((long) 6, "H1", "p2", "106", "116", "continue", "116", "6", "6", "1", "1");
            allActionBeans.add(actionBean1);
            allActionBeans.add(actionBean2);
            allActionBeans.add(actionBean3);
            allActionBeans.add(actionBean4);
            allActionBeans.add(actionBean5);
            allActionBeans.add(actionBean6);
            allActionBeans.add(actionBean7);
        } else if (mode == 5) {
            ActionBean actionBean1 = new ActionBean((long) 1, "H1", "p2", "0", "5", "continue", "480", "1", "7", "1", "1");
            ActionBean actionBean2 = new ActionBean((long) 2, "H1", "p2", "5", "10", "continue", "480", "7", "10", "1", "1");
            ActionBean actionBean3 = new ActionBean((long) 3, "H1", "p2", "10", "480", "continue", "480", "10", "10", "1", "1");
            allActionBeans.add(actionBean1);
            allActionBeans.add(actionBean2);
            allActionBeans.add(actionBean3);
        } else {
            finish();
            return;
        }
    }


    /**
     * 点击就停止了
     */
    @OnClick(R.id.pause_iv)
    public void stopPlay() {
        if (ISLONGCLICKRUNNING) {
            return;
        }
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
    public void setTimeAndSpeed(long time, int speed) {
        int min = (int) (time / (1000 * 60));
        int sec = (int) ((time / 1000) % 60) + 1;
        if (sec != 0 && sec % 60 == 0) {
            sec = 0;
            min++;
        }
        min_time_tv.setText((min < 10 ? "0" + min : "" + min) + "  min");
        sec_time_tv.setText((sec < 10 ? "0" + sec : "" + sec) + "  sec");
        speed_tv.setText((speed < 10 ? "0" + speed : "H") + "  speed");
    }


    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.HANDLER_SUCCESS) {
                long time = System.currentTimeMillis() - touchTime;
                int min = (int) (time / (1000 * 60));
                int sec = (int) ((time / 1000) % 60);
                Logutil.e("min=" + min + "<><>sec=" + sec);
                min_time_tv.setText((min < 10 ? "0" + min : "" + min) + "  min");
                sec_time_tv.setText((sec < 10 ? "0" + sec : "" + sec) + "  sec");
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
                SmallWorkFinishActivity.startActivity(ManualShowWorkCustomActivity.this);
                finish();
            }
        }, 500);

    }

    public static void startActivity(Context context, int mode) {
        context.startActivity(new Intent(context, ManualShowWorkCustomActivity.class).putExtra("mode", mode));
    }
}
