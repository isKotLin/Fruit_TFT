package com.vigorchip.juice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vigorchip.db.entity.FoodBean;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.CupStatusManager;
import com.vigorchip.juice.util.ImageUtil;
import com.vigorchip.juice.util.TextFontUtil;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.base.BaseArrayListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/15.选择看流程还是直接制作
 */

public class BigJudgeWorkActivity extends BaseActivity {

    private String cocktailID;
    private String picID;
    private String cocktailName;
    private ArrayList<FoodBean> foodBeens = new ArrayList<>();
    private int person;//人数
    private int step;//是第几步的判断
    private int allStep;//是第几步的判断

    @InjectView(R.id.bg_iv)
    ImageView bg_iv;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigjudgework;
    }

    @Override
    protected void initView() {
        picID = getIntent().getStringExtra("picID");
        cocktailID = getIntent().getStringExtra("cocktailID");
        cocktailName = getIntent().getStringExtra("cocktailName");
        person = getIntent().getIntExtra("person", 2);
        step = getIntent().getIntExtra("step", 1);
        allStep = getIntent().getIntExtra("allStep", 1);
        if (TextUtils.isEmpty(cocktailID)) {
            finish();
            return;
        }
        ImageUtil.showImageByName(this, bg_iv, picID);
        foodBeens.clear();
        List<FoodBean> allfoodBeens = MyApplication.dataHelper.queryBuilderFoods(cocktailID, "p" + person);
        for (int i = 0; i < allfoodBeens.size(); i++) {
            FoodBean foodBean = allfoodBeens.get(i);
            if (foodBean.getCur_step().equals("" + step)) {
                foodBeens.add(foodBean);
            }
        }
    }

    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        showJudgeJarDialog();
    }

    public void onEventMainThread(BackMenuEvent event) {
        finish();
    }


    /**
     * 判断容器放好没
     */
    public void showJudgeJarDialog() {
        if (CupStatusManager.isJarPutOk()) {
            if (step == 1) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View layout = inflater.inflate(R.layout.dialog_bigjudgetap, null);
                final Dialog showdialog = new Dialog(this, R.style.DefaultDialog);
                showdialog.setContentView(layout);
                final ImageView close_iv = (ImageView) layout
                        .findViewById(R.id.close_iv);
                ImageView iv_tap_anim = (ImageView) layout.findViewById(R.id.iv_tap_anim);
                final ImageView judge_iv = (ImageView) layout
                        .findViewById(R.id.judge_iv);
                judge_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (CupStatusManager.isBigCupStatus) {
                            BigShowWorkActivity.startActivity(BigJudgeWorkActivity.this, cocktailID, cocktailName, picID, person, step, allStep);
                            finish();
                            showdialog.dismiss();
                        }
                    }
                });
                close_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showdialog.dismiss();
                        finish();
                    }
                });
                iv_tap_anim.setImageResource(R.drawable.big_tap_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) iv_tap_anim.getDrawable();
                animationDrawable.start();
                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = showdialog.getWindow().getAttributes();
                lp.height = display.getHeight();
                lp.width = display.getWidth();
                showdialog.getWindow().setAttributes(lp);
                showdialog.setCanceledOnTouchOutside(false);
                showdialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                showdialog.setCancelable(false);
                showdialog.show();
            } else if (step == 2) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View layout = inflater.inflate(R.layout.dialog_bigpreparework, null);
                final Dialog showdialog = new Dialog(this, R.style.DefaultDialog);
                showdialog.setContentView(layout);
                final ImageView close_iv = (ImageView) layout
                        .findViewById(R.id.close_iv);
                final ImageView judge_iv = (ImageView) layout
                        .findViewById(R.id.judge_iv);
                ImageView iv_tap_anim = (ImageView) layout.findViewById(R.id.iv_tap_anim);
                ListView listview = (ListView) layout.findViewById(R.id.listview);
                listview.setAdapter(new Step2FoodAdapter(BigJudgeWorkActivity.this, foodBeens));
                judge_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (CupStatusManager.isBigCupStatus) {
                            showdialog.dismiss();
                            BigShowWorkActivity.startActivity(BigJudgeWorkActivity.this, cocktailID, cocktailName, picID, person, step, allStep);
                            finish();
                        }
                    }
                });
                close_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showdialog.dismiss();
                        finish();
                    }
                });

                iv_tap_anim.setImageResource(R.drawable.big_tap_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) iv_tap_anim.getDrawable();
                animationDrawable.start();

                WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = showdialog.getWindow().getAttributes();
                showdialog.getWindow().setAttributes(lp);
                showdialog.setCanceledOnTouchOutside(false);
                showdialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                showdialog.setCancelable(false);
                showdialog.show();
            }
        } else {
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout = inflater.inflate(R.layout.dialog_bigjudgejar, null);
            final Dialog showdialog = new Dialog(this, R.style.DefaultDialog);
            showdialog.setContentView(layout);
            final ImageView close_iv = (ImageView) layout
                    .findViewById(R.id.close_iv);
            final ImageView judge_iv = (ImageView) layout
                    .findViewById(R.id.judge_iv);
            ImageView iv_big_jar = (ImageView) layout.findViewById(R.id.iv_big_jar);
            iv_big_jar.setImageResource(R.drawable.big_jar_anim);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_big_jar.getDrawable();
            animationDrawable.start();
            judge_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showdialog.dismiss();
                    showJudgeJarDialog();
                }
            });
            close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showdialog.dismiss();
                    finish();
                }
            });

            WindowManager.LayoutParams lp = showdialog.getWindow().getAttributes();
            showdialog.getWindow().setAttributes(lp);
            showdialog.setCanceledOnTouchOutside(false);
            showdialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            showdialog.setCancelable(false);
            showdialog.show();
        }
    }


    public static void startActivity(Context context, String cocktailID, String cocktailName, String picID, int person, int step, int allStep) {
        context.startActivity(new Intent(context, BigJudgeWorkActivity.class).putExtra("cocktailID", cocktailID)
                .putExtra("picID", picID).putExtra("cocktailName", cocktailName).putExtra("person", person).putExtra("step", step).putExtra("allStep", allStep));
    }

    class Step2FoodAdapter extends BaseArrayListAdapter {


        private Context context;
        private ArrayList<FoodBean> beans;

        public Step2FoodAdapter(Context context, ArrayList<FoodBean> beans) {
            super(context, beans);
            this.context = context;
            this.beans = beans;
        }

        @Override
        public int getContentView() {
            return R.layout.item_step2_food;
        }

        @Override
        public void onInitView(View view, int position) {
            FoodBean bean = beans.get(position);
            TextView count_tv = (TextView) get(view, R.id.count_tv);
            TextView unit_tv = (TextView) get(view, R.id.unit_tv);
            TextView content_tv = (TextView) get(view, R.id.content_tv);
            unit_tv.setText(bean.getUnit());
            count_tv.setText(bean.getCount());
            content_tv.setText(bean.getName());
            TextFontUtil.changeRobotoBold(context, count_tv);
            TextFontUtil.changeRobotoBold(context, unit_tv);
            TextFontUtil.changeRobotoLight(context, content_tv);
        }
    }

}
