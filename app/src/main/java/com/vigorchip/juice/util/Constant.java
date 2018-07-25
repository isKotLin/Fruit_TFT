package com.vigorchip.juice.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.db.entity.FoodBean;
import com.vigorchip.db.entity.FoodHelpBean;
import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/5.
 */

public class Constant {

    public static int HANDLER_SUCCESS = 100;
    public static int HANDLER_FAIL = 101;

    public static int NET_STATU_OK = 1;
    public static int NET_STATU_NO = 0;

    public static boolean ISLONGCLICKRUNNING;
    /**
     * 工程版本
     */
    public static String PROJECT_ID = "1.0.1-20171121175400";

    public static ArrayList<CategoryBean> categoryBeans;

    public static void setCategoryBeans(final String type) {
        categoryBeans = null;
        categoryBeans = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans(type);
    }

    public static ArrayList<CategoryBean> categoryBeens_A;
    public static ArrayList<CategoryBean> categoryBeens_B;
    public static ArrayList<CategoryBean> categoryBeens_C;
    public static ArrayList<CategoryBean> categoryBeens_D;
    public static ArrayList<CategoryBean> categoryBeens_E;
    public static ArrayList<CategoryBean> categoryBeens_F;
    public static ArrayList<CategoryBean> categoryBeens_G;

    public static void setCategoryData() {
        if (categoryBeens_A == null) {
            categoryBeens_A = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans("A");
            categoryBeens_B = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans("B");
            categoryBeens_C = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans("C");
            categoryBeens_D = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans("D");
            categoryBeens_E = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans("E");
            categoryBeens_F = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans("F");
            categoryBeens_G = (ArrayList<CategoryBean>) MyApplication.dataHelper.queryBuilderCategoryBeans("G");

        }
    }


    public static List<FoodBean> detailP2Foods = new ArrayList<>();
    public static List<FoodBean> detailP4Foods = new ArrayList<>();
    public static List<FoodBean> detailP6Foods = new ArrayList<>();
    public static List<FoodHelpBean> detailFoodHelpBeans = new ArrayList<>();
    public static CategoryBean detailCategoryBean;

    public static void hintRecipeDetailInfo(final String cocktailID) {
        if (!TextUtils.isEmpty(cocktailID)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    detailP2Foods.clear();
                    detailP4Foods.clear();
                    detailP6Foods.clear();
                    detailP2Foods.addAll(MyApplication.dataHelper.queryBuilderFoods(cocktailID, "p2"));
                    detailP4Foods.addAll(MyApplication.dataHelper.queryBuilderFoods(cocktailID, "p4"));
                    detailP6Foods.addAll(MyApplication.dataHelper.queryBuilderFoods(cocktailID, "p6"));
                    detailFoodHelpBeans.addAll(MyApplication.dataHelper.queryBuilderFoodHelpBeansByFid(cocktailID));
                    List<CategoryBean> categoryBeens = MyApplication.dataHelper.queryBuilderCategoryByFid(cocktailID);
                    if (categoryBeens != null && categoryBeens.size() > 0) {
                        detailCategoryBean = categoryBeens.get(0);
                    }
                }
            }).start();
        }

    }


    public void showUpdataDialog(Activity myActivity) {
        LayoutInflater inflater = LayoutInflater.from(myActivity);
        View layout = inflater.inflate(R.layout.dialog_updata_wifi, null);
        final Dialog showdialog = new Dialog(myActivity, R.style.DefaultDialog);
        showdialog.setContentView(layout);
        final ImageView close_iv = (ImageView) layout
                .findViewById(R.id.close_iv);
        final TextView updata_text01_tv = (TextView) layout.findViewById(R.id.updata_text01_tv);
        final TextView updata_text02_tv = (TextView) layout.findViewById(R.id.updata_text02_tv);
        final TextView updata_text03_tv = (TextView) layout.findViewById(R.id.updata_text03_tv);
        final TextView updata_text04_tv = (TextView) layout.findViewById(R.id.updata_text04_tv);
        final TextView no_updata_tv = (TextView) layout.findViewById(R.id.no_updata_tv);
        final TextView yes_updata_tv = (TextView) layout.findViewById(R.id.yes_updata_tv);
        TextFontUtil.changeRobotoBoldItalic(myActivity, updata_text01_tv);
        TextFontUtil.changeRobotoBoldItalic(myActivity, updata_text02_tv);
        TextFontUtil.changeRobotoBoldItalic
                (myActivity, updata_text03_tv);
        TextFontUtil.changeRobotoLight(myActivity, updata_text04_tv);
        TextFontUtil.changeRobotoRegular(myActivity, no_updata_tv);
        TextFontUtil.changeRobotoRegular(myActivity, yes_updata_tv);
        close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog.dismiss();
            }
        });
        no_updata_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog.dismiss();
            }
        });
        yes_updata_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog.dismiss();
            }
        });
        WindowManager windowManager = (WindowManager) myActivity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = showdialog.getWindow().getAttributes();
        showdialog.getWindow().setAttributes(lp);
        showdialog.setCanceledOnTouchOutside(false);
        showdialog.setCancelable(false);
        showdialog.show();
    }

    public static boolean isWifiOk;
}
