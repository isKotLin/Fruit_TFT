package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigorchip.db.entity.ActionBean;
import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.CupStatusManager;
import com.vigorchip.juice.util.ImageUtil;
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

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.vigorchip.juice.util.Constant.ISLONGCLICKRUNNING;

/**
 * Created by Administrator on 2017/1/15.
 */

public class BigShowWorkActivity extends BaseActivity {
    @InjectView(R.id.bg_iv)
    ImageView bg_iv;

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
    private String cocktailName;
    private String picID;
    private String cocktailID;
    private int person;
    private int step;
    private int allStep;
    private List<ActionBean> allActionBeans;

    private MachineControl control;
    private long curTime = 0;

    private int step1AllTime;
    private int step2AllTime;

    private String touchMinTime;
    private String touchSecTime;
    private String touchSeek;
    private long touchTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigshowwork;
    }

    @Override
    protected void initView() {
        circle_anim = AnimationUtils.loadAnimation(this, R.anim.right_top_left_circle);
    }


    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        picID = getIntent().getStringExtra("picID");
        cocktailName = getIntent().getStringExtra("cocktailName");
        cocktailID = getIntent().getStringExtra("cocktailID");
        person = getIntent().getIntExtra("person", 2);
        step = getIntent().getIntExtra("step", 1);
        allStep = getIntent().getIntExtra("allStep", 1);

        if (TextUtils.isEmpty(cocktailID)) {
            finish();
            return;
        }
        ImageUtil.showImageByName(this, bg_iv, picID);
        allActionBeans = MyApplication.dataHelper.queryBuilderActions(cocktailID, "p" + person, step + "");
        if (allActionBeans.size() == 0) {
            finish();
            return;
        }
        List<CategoryBean> categoryBeens = MyApplication.dataHelper.queryBuilderCategoryByFid(cocktailID);
        if (categoryBeens.size() != 0) {
            CategoryBean bean = categoryBeens.get(0);
            String time1;
            String time2;
            if (person == 4) {
                time1 = bean.getP4totletime1();
                time2 = bean.getP4totletime2();
            } else if (person == 6) {
                time1 = bean.getP6totletime1();
                time2 = bean.getP6totletime2();
            } else {
                time1 = bean.getP2totletime1();
                time2 = bean.getP2totletime2();
            }
            try {
                step1AllTime = Integer.valueOf(time1);
                step2AllTime = Integer.valueOf(time2);
            } catch (Exception e) {

            }
        }
        String old_cocktailID = MyApplication.sp.getString("cocktailID", "");
        int old_person = MyApplication.sp.getInt("person", -1);
        int old_step = MyApplication.sp.getInt("step", -1);
        int old_allStep = MyApplication.sp.getInt("allStep", -1);
        long old_time = MyApplication.sp.getLong("cur_time", 0);
        if (cocktailID.equals(old_cocktailID) && person == old_person && old_step == step && old_allStep == allStep) {
            curTime = old_time;
        }
        MyApplication.sp.edit().putString("cocktailID", "").putInt("person", 0).putInt("step", 0).putInt("allStep", 0).putLong("cur_time", 0).commit();

        control = new MachineControl(allActionBeans, curTime, new MachineControl.ControlListener() {
            @Override
            public void curMachineStatu(boolean isWorking, long alltime, long curtime, int curSpeed, int oldSpeed) {
                Logutil.e("isWorking=" + isWorking + "     curSpeed=" + curSpeed + "            oldSpeed=" + oldSpeed);
                curTime = curtime;
                if (step2AllTime != 0) {
                    progress_view.setMax((step2AllTime + step1AllTime) * 1000);
                    if (step == 1) {
                        progress_view.setProgress((int) curtime);
                    } else {
                        progress_view.setProgress((int) (curtime + step1AllTime * 1000));
                    }
                } else {
                    progress_view.setMax((int) alltime);
                    progress_view.setProgress((int) curtime);
                }

                if (isWorking) {
                    setTimeAndSpeed(curtime, curSpeed);
                    play_iv.setVisibility(View.GONE);
                    pause_iv.setVisibility(View.VISIBLE);
                    speed_tag_iv.clearAnimation();
                    if (curSpeed != 0) {
                        speed_tag_iv.startAnimation(circle_anim);
                    }
                } else {
                    setTimeAndSpeed(curtime, oldSpeed);
                    play_iv.setVisibility(View.VISIBLE);
                    pause_iv.setVisibility(View.GONE);
                    speed_tag_iv.clearAnimation();
                }
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
            public void curSpeedBack(int speed1) {
                speed_tv.setText((speed1 < 10 ? "0" + speed1 : "H") + "  speed");
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
                            mhandler.removeMessages(Constant.HANDLER_SUCCESS);
                            speed_tag_iv.startAnimation(circle_anim);

                            ISLONGCLICKRUNNING = true;

                            mhandler.sendEmptyMessageDelayed(Constant.HANDLER_SUCCESS, 500);
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

    public void onEventMainThread(ScreenCloseEvent event) {
        if (control.isWorking()) {
            Logutil.e("关闭屏幕的通知");
            stopPlay();
        }
    }

    public void onEventMainThread(ErrorEvent event) {
        super.onEventMainThread(event);
        if (!event.getTag().equals("E0")) {
            mhandler.removeMessages(Constant.HANDLER_SUCCESS);
        }
        if (control.isWorking() && (event.getTag().equals("E2") || event.getTag().equals("E3"))) {
            Logutil.e("ErrorEvent的通知" + event.getTag());
            stopPlay();
        }
    }

    /**
     * 点击就停止了
     */
    @OnClick(R.id.pause_iv)
    public void stopPlay() {
        Logutil.e("手点的");
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
        Logutil.e("closeBack");
        if (control != null && control.isWorking()) {
            control.stopPlay();
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
                            BigShowWorkActivity.this.finish();
                        }
                    }
                });
            }

        } else {
            CupStatusManager.MODESHOWTAG = "2";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.sp.edit().putString("cocktailID", cocktailID).putInt("person", person).putInt("step", step).putInt("allStep", allStep).putLong("cur_time", curTime).commit();
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

    public static void startActivity(Context context, String cocktailID, String cocktailName, String picID, int person, int step, int allStep) {
        context.startActivity(new Intent(context, BigShowWorkActivity.class).putExtra("picID", picID)
                .putExtra("cocktailID", cocktailID).putExtra("person", person).putExtra("step", step).putExtra("allStep", allStep).putExtra("cocktailName", cocktailName));
    }

    /**
     * 设置时间
     */
    public void setTimeAndSpeed(long time, int speed) {
        if (step2AllTime != 0) {
            if (step == 2) {
                time += step1AllTime * 1000;
            }
        }
        long leftTime = (step1AllTime + step2AllTime) * 1000 - time;

        int min = (int) (leftTime / (1000 * 60));
        int sec = (int) Math.round((leftTime / 1000) % 60 + 0.5);
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
        if (step == allStep) {
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BigWorkFinishActivity.startActivity(BigShowWorkActivity.this, cocktailID, picID, cocktailName);
                    finish();
                }
            }, 100);
        } else {
            BigJudgeWorkActivity.startActivity(this, cocktailID, cocktailName, picID, person, step + 1, allStep);
            finish();
        }
    }


}
