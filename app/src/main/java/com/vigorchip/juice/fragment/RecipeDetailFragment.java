package com.vigorchip.juice.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.vigorchip.juice.activity.BigJudgeWorkActivity;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.ImageUtil;
import com.vigorchip.juice.util.StringUtil;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.juice.view.FoodView;
import com.vigorchip.juice.view.HelpView;
import com.vigorchip.puliblib.base.BaseFragment;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/4/2.
 */

public class RecipeDetailFragment extends BaseFragment {

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
        return R.layout.fragment_recipedetail;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        cocktailID = arguments.getString("cocktailID");
        picID = arguments.getString("picID");
        cocktailName = arguments.getString("cocktailName");
        Logutil.e("RecipeDetailFragment cocktailID==" + cocktailID);
        ImageUtil.showImageByName(myActivity, bg_iv, picID);
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
        if (p4Foods.size() == 0) {
            up_person_iv.setImageResource(R.drawable.btn_up_0);
            down_person_iv.setImageResource(R.drawable.btn_down_0);
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
        person_tv.setText("" + serverPerson);
        if (serverPerson == 2) {
            needtime_tv.setText(StringUtil.changeTime(categoryBean.getP2totletime1(), categoryBean.getP2totletime2()));
        } else if (serverPerson == 4) {
            needtime_tv.setText(StringUtil.changeTime(categoryBean.getP4totletime1(), categoryBean.getP4totletime2()));
        } else if (serverPerson == 6) {
            needtime_tv.setText(StringUtil.changeTime(categoryBean.getP6totletime1(), categoryBean.getP6totletime2()));
        }
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
            ll_contain.addView(new HelpView(myActivity, foodHelpBean));
            for (int n = 0; n < mFoodBeen.size(); n++) {
                FoodBean foodBean = mFoodBeen.get(n);
                if (foodBean.getCur_step().equals(foodHelpBean.getCur_step())) {
                    ll_contain.addView(new FoodView(myActivity, foodBean));
                }
            }
        }
    }


    /**
     * finish
     */
    @OnClick(R.id.back_tv)
    public void backHomeClick() {
        cleanSp();
        int position1 = 0;
        int position2 = 0;
        if (cocktailID.contains("A")) {
            position1 = 0;
        } else if (cocktailID.contains("B")) {
            position1 = 1;
        } else if (cocktailID.contains("C")) {
            position1 = 2;
        } else if (cocktailID.contains("D")) {
            position1 = 3;
        } else if (cocktailID.contains("E")) {
            position1 = 4;
        } else if (cocktailID.contains("F")) {
            position1 = 5;
        } else if (cocktailID.contains("G")) {
            position1 = 6;
        }
        try {
            Logutil.e("RecipeDetailFragment=cocktailID" + cocktailID);
            position2 = Integer.valueOf(cocktailID.substring(1)) - 1;
            Logutil.e("substring=" + cocktailID.substring(1));
        } catch (Exception e) {
        }
        EventBus.getDefault().post(new MainChangeEvent(4, position1, position2));
    }


    /**
     * 清除运行界面的数据
     */
    public void cleanSp() {
        MyApplication.sp.edit().putString("cocktailID", "").putInt("person", 0).putInt("step", 0).putInt("allStep", 0).putLong("cur_time", 0).commit();
    }

    /**
     * add person
     */
    @OnClick(R.id.up_person_iv)
    public void addPersonClick() {
        setData(2);
    }

    /**
     * 跳去选择页面，判断演示还是查看制作流程
     */
    @OnClick(R.id.continue_iv)
    public void continueClick() {
        BigJudgeWorkActivity.startActivity(myActivity, cocktailID, cocktailName, picID, serverPerson, 1, Integer.valueOf(categoryBean.getTotlestep()));
    }

    /**
     * add person
     */
    @OnClick(R.id.down_person_iv)
    public void reducePersonClick() {
        setData(-2);
    }


    public static RecipeDetailFragment newInstance(String cocktailID, String cocktailName, String picID) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString("cocktailID", cocktailID);
        args.putString("cocktailName", cocktailName);
        args.putString("picID", picID);
        fragment.setArguments(args);
        return fragment;
    }
}
