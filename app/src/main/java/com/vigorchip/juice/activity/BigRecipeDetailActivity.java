package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.db.entity.FoodBean;
import com.vigorchip.db.entity.FoodHelpBean;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.ImageUtil;
import com.vigorchip.juice.util.StringUtil;
import com.vigorchip.juice.util.event.BackMenuEvent;
import com.vigorchip.juice.view.FoodView;
import com.vigorchip.juice.view.HelpView;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/15.原料详情
 */

public class BigRecipeDetailActivity extends BaseActivity {

    @InjectView(R.id.bg_iv)
    ImageView bg_iv;

    @InjectView(R.id.needtime_tv)
    TextView needtime_tv;

    @InjectView(R.id.person_tv)
    TextView person_tv;

    @InjectView(R.id.up_person_iv)
    ImageView up_person_iv;

    @InjectView(R.id.down_person_iv)
    ImageView down_person_iv;


    @InjectView(R.id.ll_contain)
    LinearLayout ll_contain;

    private int serverPerson = 0;
    private String cocktailID;
    private String picID;
    private String cocktailName;
    private CategoryBean categoryBean;
    private List<FoodBean> p2Foods = new ArrayList<>();
    private List<FoodBean> p4Foods = new ArrayList<>();
    private List<FoodBean> p6Foods = new ArrayList<>();
    private List<FoodHelpBean> foodHelpBeans = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recipedetail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        cocktailID = getIntent().getStringExtra("cocktailID");
        picID = getIntent().getStringExtra("picID");
        cocktailName = getIntent().getStringExtra("cocktailName");
        if (TextUtils.isEmpty(cocktailID)) {
            finish();
            return;
        }
        Logutil.e("cocktailID==" + cocktailID);
        ImageUtil.showImageByName(this, bg_iv, picID);
        getDataFromDataBase();
    }

    private void getDataFromDataBase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(Constant.HANDLER_SUCCESS);
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.HANDLER_SUCCESS) {
                setDataInfo();
            }
        }
    };

    public void onEventMainThread(BackMenuEvent event) {
        finish();
    }

    /**
     * get asset json data
     */

    public void setDataInfo() {
        p2Foods.addAll(Constant.detailP2Foods);
        p4Foods.addAll(Constant.detailP4Foods);
        p6Foods.addAll(Constant.detailP6Foods);
        foodHelpBeans.addAll(Constant.detailFoodHelpBeans);
        categoryBean = Constant.detailCategoryBean;
        if (p2Foods.size() == 0) {
            p2Foods.addAll(MyApplication.dataHelper.queryBuilderFoods(cocktailID, "p2"));
        }
        if (p4Foods.size() == 0) {
            p4Foods.addAll(MyApplication.dataHelper.queryBuilderFoods(cocktailID, "p4"));
        }
        if (p6Foods.size() == 0) {
            p6Foods.addAll(MyApplication.dataHelper.queryBuilderFoods(cocktailID, "p6"));
        }
        if (foodHelpBeans.size() == 0) {
            foodHelpBeans.addAll(MyApplication.dataHelper.queryBuilderFoodHelpBeansByFid(cocktailID));
        }
        if (categoryBean == null) {
            List<CategoryBean> categoryBeens = MyApplication.dataHelper.queryBuilderCategoryByFid(cocktailID);
            if (categoryBeens != null && categoryBeens.size() > 0) {
                categoryBean = categoryBeens.get(0);
            }
        }
        setData(2);
    }


    /**
     * 设置参数
     */
    public void setData(int no) {
        Logutil.e("nobbbbb=" + serverPerson + "<><>" + no);
        List<FoodBean> curFoodBeans;
        if (serverPerson + no == 2) {
            curFoodBeans = p2Foods;
        } else if (serverPerson + no == 4) {
            curFoodBeans = p4Foods;
        } else if (serverPerson + no == 6) {
            curFoodBeans = p6Foods;
        } else {
            return;
        }
        if (curFoodBeans == null || curFoodBeans.size() == 0) {
            return;
        }
        serverPerson = serverPerson + no;
        Logutil.e("serverPerson个数=" + serverPerson);
        person_tv.setText("" + serverPerson);
        if (serverPerson == 2) {
            needtime_tv.setText(StringUtil.changeTime(categoryBean.getP2totletime1(), categoryBean.getP2totletime2()));
            down_person_iv.setImageResource(R.drawable.btn_down_0);
            if (p4Foods.size() == 0) {
                up_person_iv.setImageResource(R.drawable.btn_up_0);
            } else {
                up_person_iv.setImageResource(R.drawable.btn_up_1);
            }
        } else if (serverPerson == 4) {
            needtime_tv.setText(StringUtil.changeTime(categoryBean.getP4totletime1(), categoryBean.getP4totletime2()));

            if (p2Foods.size() == 0) {
                down_person_iv.setImageResource(R.drawable.btn_down_0);
            } else {
                down_person_iv.setImageResource(R.drawable.btn_down_1);
            }

            if (p6Foods.size() == 0) {
                up_person_iv.setImageResource(R.drawable.btn_up_0);
            } else {
                up_person_iv.setImageResource(R.drawable.btn_up_1);
            }

        } else if (serverPerson == 6) {
            needtime_tv.setText(StringUtil.changeTime(categoryBean.getP6totletime1(), categoryBean.getP6totletime2()));

            up_person_iv.setImageResource(R.drawable.btn_up_0);
            if (p4Foods.size() == 0) {
                down_person_iv.setImageResource(R.drawable.btn_down_0);
            } else {
                down_person_iv.setImageResource(R.drawable.btn_down_1);
            }
        }
        Logutil.e("curFoodBeans个数=" + curFoodBeans.size());
        setContainFood(curFoodBeans, foodHelpBeans);

    }

    public void setContainFood(List<FoodBean> mFoodBeen, List<FoodHelpBean> mFoodHelpBeen) {
        ll_contain.removeAllViews();
        for (int m = 0; m < mFoodHelpBeen.size(); m++) {
            FoodHelpBean foodHelpBean = mFoodHelpBeen.get(m);
            ll_contain.addView(new HelpView(this, foodHelpBean));
            for (int n = 0; n < mFoodBeen.size(); n++) {
                FoodBean foodBean = mFoodBeen.get(n);
                if (foodBean.getCur_step().equals(foodHelpBean.getCur_step())) {
                    ll_contain.addView(new FoodView(this, foodBean));
                }
            }
        }


    }


    /**
     * finish
     *
     * @param view
     */
    public void backHomeClick(View view) {
        cleanSp();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cleanSp();
    }

    /**
     * 清除运行界面的数据
     */
    public void cleanSp() {
        MyApplication.sp.edit().putString("cocktailID", "").putInt("person", 0).putInt("step", 0).putInt("allStep", 0).putLong("cur_time", 0).commit();
    }

    /**
     * add person
     *
     * @param view
     */
    public void addPersonClick(View view) {
        setData(2);
    }

    /**
     * 跳去选择页面，判断演示还是查看制作流程
     *
     * @param view
     */
    public void continueClick(View view) {
        BigJudgeWorkActivity.startActivity(this, cocktailID, cocktailName, picID, serverPerson, 1, Integer.valueOf(categoryBean.getTotlestep()));
    }

    /**
     * add person
     *
     * @param view
     */
    public void reducePersonClick(View view) {
        setData(-2);
    }

    public static void startActivity(Context context, String cocktailID, String cocktailName, String picID) {
        context.startActivity(new Intent(context, BigRecipeDetailActivity.class)
                .putExtra("cocktailID", cocktailID).putExtra("cocktailName", cocktailName).putExtra("picID", picID));
    }

}
