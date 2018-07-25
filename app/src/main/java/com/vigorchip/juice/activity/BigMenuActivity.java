package com.vigorchip.juice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;

import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.juice.R;
import com.vigorchip.juice.fragment.BigMainMenuFragment;
import com.vigorchip.juice.fragment.RecipeCategoryFragment;
import com.vigorchip.juice.fragment.RecipeDetailFragment;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.DataContant;
import com.vigorchip.juice.util.event.ChangeJarCupEvent;
import com.vigorchip.juice.util.event.MainChangeEvent;
import com.vigorchip.puliblib.base.BaseActivity;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/4/2.
 */

public class BigMenuActivity extends BaseActivity {

    private String[] categoryNames = DataContant.mainModeNames;
    BigMainMenuFragment bigMainMenuFragment;
    RecipeCategoryFragment recipeCategoryFragmentA;
    RecipeCategoryFragment recipeCategoryFragmentB;
    RecipeCategoryFragment recipeCategoryFragmentC;
    RecipeCategoryFragment recipeCategoryFragmentD;
    RecipeCategoryFragment recipeCategoryFragmentE;
    RecipeCategoryFragment recipeCategoryFragmentF;
    RecipeCategoryFragment recipeCategoryFragmentG;
    private ArrayList<RecipeDetailFragment> recipeDetailFragmentsA = new ArrayList<>();
    private ArrayList<RecipeDetailFragment> recipeDetailFragmentsB = new ArrayList<>();
    private ArrayList<RecipeDetailFragment> recipeDetailFragmentsC = new ArrayList<>();
    private ArrayList<RecipeDetailFragment> recipeDetailFragmentsD = new ArrayList<>();
    private ArrayList<RecipeDetailFragment> recipeDetailFragmentsE = new ArrayList<>();
    private ArrayList<RecipeDetailFragment> recipeDetailFragmentsF = new ArrayList<>();
    private ArrayList<RecipeDetailFragment> recipeDetailFragmentsG = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigmenu;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData(Bundle bundle) {
        Constant.setCategoryData();
        hintCagegoryFragment();
    }



    /**
     * 初始化一级和二级页面
     */
    public void hintCagegoryFragment() {
        bigMainMenuFragment = BigMainMenuFragment.newInstance();
        recipeCategoryFragmentA = RecipeCategoryFragment.newInstance("A", categoryNames[1]);
        recipeCategoryFragmentB = RecipeCategoryFragment.newInstance("B", categoryNames[2]);
        recipeCategoryFragmentC = RecipeCategoryFragment.newInstance("C", categoryNames[3]);
        recipeCategoryFragmentD = RecipeCategoryFragment.newInstance("D", categoryNames[4]);
        recipeCategoryFragmentE = RecipeCategoryFragment.newInstance("E", categoryNames[5]);
        recipeCategoryFragmentF = RecipeCategoryFragment.newInstance("F", categoryNames[6]);
        recipeCategoryFragmentG = RecipeCategoryFragment.newInstance("G", categoryNames[7]);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, recipeCategoryFragmentA);
        transaction.add(R.id.content, recipeCategoryFragmentB);
        transaction.add(R.id.content, recipeCategoryFragmentC);
        transaction.add(R.id.content, recipeCategoryFragmentD);
        transaction.add(R.id.content, recipeCategoryFragmentE);
        transaction.add(R.id.content, recipeCategoryFragmentF);
        transaction.add(R.id.content, recipeCategoryFragmentG);

//        transaction.show(recipeCategoryFragmentA);
//        transaction.show(recipeCategoryFragmentB);
//        transaction.show(recipeCategoryFragmentC);
//        transaction.show(recipeCategoryFragmentD);
//        transaction.show(recipeCategoryFragmentE);
//        transaction.show(recipeCategoryFragmentF);
//        transaction.show(recipeCategoryFragmentG);
//
//        transaction.hide(recipeCategoryFragmentA);
//        transaction.hide(recipeCategoryFragmentB);
//        transaction.hide(recipeCategoryFragmentC);
//        transaction.hide(recipeCategoryFragmentD);
//        transaction.hide(recipeCategoryFragmentE);
//        transaction.hide(recipeCategoryFragmentF);
//        transaction.hide(recipeCategoryFragmentG);

        transaction.add(R.id.content, bigMainMenuFragment);
        transaction.show(bigMainMenuFragment);
        newDRecipeDetailFragment(transaction);
        bigMainMenuFragment.startBeginAnim();
        transaction.commitAllowingStateLoss();
    }

    /**
     * 初始化第三级页面
     */
    public void newDRecipeDetailFragment(FragmentTransaction transaction) {
        ArrayList<CategoryBean> categoryBeens = Constant.categoryBeens_A;
        for (int i = 0; i < categoryBeens.size(); i++) {
            CategoryBean bean = categoryBeens.get(i);
            RecipeDetailFragment recipedetailFragment = RecipeDetailFragment.newInstance(bean.getFid(), bean.getName(), bean.getBg_pic());
            recipeDetailFragmentsA.add(recipedetailFragment);
            transaction.add(R.id.content, recipedetailFragment);
            transaction.hide(recipedetailFragment);
        }

        categoryBeens = Constant.categoryBeens_B;
        for (int i = 0; i < categoryBeens.size(); i++) {
            CategoryBean bean = categoryBeens.get(i);
            RecipeDetailFragment recipedetailFragment = RecipeDetailFragment.newInstance(bean.getFid(), bean.getName(), bean.getBg_pic());
            recipeDetailFragmentsB.add(recipedetailFragment);
            transaction.add(R.id.content, recipedetailFragment);
            transaction.hide(recipedetailFragment);
        }
        categoryBeens = Constant.categoryBeens_C;

        for (int i = 0; i < categoryBeens.size(); i++) {
            CategoryBean bean = categoryBeens.get(i);
            RecipeDetailFragment recipedetailFragment = RecipeDetailFragment.newInstance(bean.getFid(), bean.getName(), bean.getBg_pic());
            recipeDetailFragmentsC.add(recipedetailFragment);
            transaction.add(R.id.content, recipedetailFragment);
            transaction.hide(recipedetailFragment);
        }

        categoryBeens = Constant.categoryBeens_D;
        for (int i = 0; i < categoryBeens.size(); i++) {
            CategoryBean bean = categoryBeens.get(i);
            RecipeDetailFragment recipedetailFragment = RecipeDetailFragment.newInstance(bean.getFid(), bean.getName(), bean.getBg_pic());
            recipeDetailFragmentsD.add(recipedetailFragment);
            transaction.add(R.id.content, recipedetailFragment);
            transaction.hide(recipedetailFragment);
        }

        categoryBeens = Constant.categoryBeens_E;
        for (int i = 0; i < categoryBeens.size(); i++) {
            CategoryBean bean = categoryBeens.get(i);
            RecipeDetailFragment recipedetailFragment = RecipeDetailFragment.newInstance(bean.getFid(), bean.getName(), bean.getBg_pic());
            recipeDetailFragmentsE.add(recipedetailFragment);
            transaction.add(R.id.content, recipedetailFragment);
            transaction.hide(recipedetailFragment);
        }

        categoryBeens = Constant.categoryBeens_F;
        for (int i = 0; i < categoryBeens.size(); i++) {
            CategoryBean bean = categoryBeens.get(i);
            RecipeDetailFragment recipedetailFragment = RecipeDetailFragment.newInstance(bean.getFid(), bean.getName(), bean.getBg_pic());
            recipeDetailFragmentsF.add(recipedetailFragment);
            transaction.add(R.id.content, recipedetailFragment);
            transaction.hide(recipedetailFragment);
        }

        categoryBeens = Constant.categoryBeens_G;
        for (int i = 0; i < categoryBeens.size(); i++) {
            CategoryBean bean = categoryBeens.get(i);
            RecipeDetailFragment recipedetailFragment = RecipeDetailFragment.newInstance(bean.getFid(), bean.getName(), bean.getBg_pic());
            recipeDetailFragmentsG.add(recipedetailFragment);
            transaction.add(R.id.content, recipedetailFragment);
            transaction.hide(recipedetailFragment);
        }

    }

    public void onEventMainThread(ChangeJarCupEvent event) {
        Logutil.e("BigMenuActivity___ChangeJarCupEvent=Mode=" + event.getMode());
        if (event.getMode() == 1) {
            SmallJarNoticeActivity.startActivity(this);
            EventBus.getDefault().post(new MainChangeEvent(1, 0, 0));
        }
    }

    /**
     * 收到更换页面的通知
     *
     * @param event
     */
    public void onEventMainThread(MainChangeEvent event) {
        setShowFragment(event.getLevel(), event.getPosition1(), event.getPosition2());
    }

    public void setShowFragment(int level, int position1, int position2) {
        Logutil.e("现在级别为=" + level + "=位置1=" + position1 + "=位置2=" + position2);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dismissFragment(transaction);
        if (level == 1) {
            if (!bigMainMenuFragment.isAdded()) {
                transaction.add(R.id.content, bigMainMenuFragment);
            }
            transaction.show(bigMainMenuFragment);
            bigMainMenuFragment.startBeginAnim();
        } else if (level == 2) {
            if (position1 == 0) {
                if (!recipeCategoryFragmentA.isAdded()) {
                    transaction.add(R.id.content, recipeCategoryFragmentA);
                }
                transaction.show(recipeCategoryFragmentA);
                recipeCategoryFragmentA.startBeginAnim();
            } else if (position1 == 1) {
                if (!recipeCategoryFragmentB.isAdded()) {
                    transaction.add(R.id.content, recipeCategoryFragmentB);
                }
                transaction.show(recipeCategoryFragmentB);
                recipeCategoryFragmentB.startBeginAnim();
            } else if (position1 == 2) {
                if (!recipeCategoryFragmentC.isAdded()) {
                    transaction.add(R.id.content, recipeCategoryFragmentC);
                }
                transaction.show(recipeCategoryFragmentC);
                recipeCategoryFragmentC.startBeginAnim();
            } else if (position1 == 3) {
                if (!recipeCategoryFragmentD.isAdded()) {
                    transaction.add(R.id.content, recipeCategoryFragmentD);
                }
                transaction.show(recipeCategoryFragmentD);
                recipeCategoryFragmentD.startBeginAnim();
            } else if (position1 == 4) {
                if (!recipeCategoryFragmentE.isAdded()) {
                    transaction.add(R.id.content, recipeCategoryFragmentE);
                }
                transaction.show(recipeCategoryFragmentE);
                recipeCategoryFragmentE.startBeginAnim();
            } else if (position1 == 5) {
                if (!recipeCategoryFragmentF.isAdded()) {
                    transaction.add(R.id.content, recipeCategoryFragmentF);
                }
                transaction.show(recipeCategoryFragmentF);
                recipeCategoryFragmentF.startBeginAnim();
            } else if (position1 == 6) {
                if (!recipeCategoryFragmentG.isAdded()) {
                    transaction.add(R.id.content, recipeCategoryFragmentG);
                }
                transaction.show(recipeCategoryFragmentG);
                recipeCategoryFragmentG.startBeginAnim();
            }
        } else if (level == 3) {
            RecipeDetailFragment recipeDetailFragment = null;
            if (position1 == 0) {
//                transaction.hide(recipeCategoryFragmentA);
                recipeDetailFragment = recipeDetailFragmentsA.get(position2);
            } else if (position1 == 1) {
                transaction.hide(recipeCategoryFragmentB);
                recipeDetailFragment = recipeDetailFragmentsB.get(position2);
            } else if (position1 == 2) {
                transaction.hide(recipeCategoryFragmentC);
                recipeDetailFragment = recipeDetailFragmentsC.get(position2);
            } else if (position1 == 3) {
                transaction.hide(recipeCategoryFragmentD);
                recipeDetailFragment = recipeDetailFragmentsD.get(position2);
            } else if (position1 == 4) {
                transaction.hide(recipeCategoryFragmentE);
                recipeDetailFragment = recipeDetailFragmentsE.get(position2);
            } else if (position1 == 5) {
                transaction.hide(recipeCategoryFragmentF);
                recipeDetailFragment = recipeDetailFragmentsF.get(position2);
            } else if (position1 == 6) {
                transaction.hide(recipeCategoryFragmentG);
                recipeDetailFragment = recipeDetailFragmentsG.get(position2);
            }
            if (recipeDetailFragment == null) {
                return;
            }
            transaction.show(recipeDetailFragment);
        } else if (level == 4) {
            if (position1 == 0) {
                transaction.hide(recipeDetailFragmentsA.get(position2));
                transaction.show(recipeCategoryFragmentA);
                recipeCategoryFragmentA.startBeginAnim();
            } else if (position1 == 1) {
                transaction.hide(recipeDetailFragmentsB.get(position2));
                transaction.show(recipeCategoryFragmentB);
                recipeCategoryFragmentB.startBeginAnim();
            } else if (position1 == 2) {
                transaction.hide(recipeDetailFragmentsC.get(position2));
                transaction.show(recipeCategoryFragmentC);
                recipeCategoryFragmentC.startBeginAnim();
            } else if (position1 == 3) {
                transaction.hide(recipeDetailFragmentsD.get(position2));
                transaction.show(recipeCategoryFragmentD);
                recipeCategoryFragmentD.startBeginAnim();
            } else if (position1 == 4) {
                transaction.hide(recipeDetailFragmentsE.get(position2));
                transaction.show(recipeCategoryFragmentE);
                recipeCategoryFragmentE.startBeginAnim();
            } else if (position1 == 5) {
                transaction.hide(recipeDetailFragmentsF.get(position2));
                transaction.show(recipeCategoryFragmentF);
                recipeCategoryFragmentF.startBeginAnim();
            } else if (position1 == 6) {
                transaction.hide(recipeDetailFragmentsG.get(position2));
                transaction.show(recipeCategoryFragmentG);
                recipeCategoryFragmentG.startBeginAnim();
            }
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 关闭页面
     *
     * @param transaction
     */
    public void dismissFragment(FragmentTransaction transaction) {
        transaction.hide(bigMainMenuFragment);
        transaction.hide(recipeCategoryFragmentA);
        transaction.hide(recipeCategoryFragmentB);
        transaction.hide(recipeCategoryFragmentC);
        transaction.hide(recipeCategoryFragmentD);
        transaction.hide(recipeCategoryFragmentE);
        transaction.hide(recipeCategoryFragmentF);
        transaction.hide(recipeCategoryFragmentG);

        for (int i = 0; i < recipeDetailFragmentsA.size(); i++) {
            RecipeDetailFragment bean = recipeDetailFragmentsA.get(i);
            transaction.hide(bean);
        }
        for (int i = 0; i < recipeDetailFragmentsB.size(); i++) {
            RecipeDetailFragment bean = recipeDetailFragmentsB.get(i);
            transaction.hide(bean);
        }
        for (int i = 0; i < recipeDetailFragmentsC.size(); i++) {
            RecipeDetailFragment bean = recipeDetailFragmentsC.get(i);
            transaction.hide(bean);
        }
        for (int i = 0; i < recipeDetailFragmentsD.size(); i++) {
            RecipeDetailFragment bean = recipeDetailFragmentsD.get(i);
            transaction.hide(bean);
        }
        for (int i = 0; i < recipeDetailFragmentsE.size(); i++) {
            RecipeDetailFragment bean = recipeDetailFragmentsE.get(i);
            transaction.hide(bean);
        }
        for (int i = 0; i < recipeDetailFragmentsF.size(); i++) {
            RecipeDetailFragment bean = recipeDetailFragmentsF.get(i);
            transaction.hide(bean);
        }
        for (int i = 0; i < recipeDetailFragmentsG.size(); i++) {
            RecipeDetailFragment bean = recipeDetailFragmentsG.get(i);
            transaction.hide(bean);
        }
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, BigMenuActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bigMainMenuFragment.isVisible()) {
            bigMainMenuFragment.startBeginAnim();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
