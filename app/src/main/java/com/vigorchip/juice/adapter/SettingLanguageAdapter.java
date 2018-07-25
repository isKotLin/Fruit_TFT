package com.vigorchip.juice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.bean.LanguageBean;
import com.vigorchip.puliblib.base.BaseArrayListAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/15.
 */

public class SettingLanguageAdapter extends BaseArrayListAdapter {
    private Context context;
    private ArrayList<LanguageBean> beans;

    public SettingLanguageAdapter(Context context, ArrayList<LanguageBean> beans) {
        super(context, beans);
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getContentView() {
        return R.layout.item_setting_language;
    }

    @Override
    public void onInitView(View view, int position) {
        LanguageBean bean = beans.get(position);
        TextView name_tv = (TextView) get(view, R.id.name_tv);
        TextView ischoose_tv = (TextView) get(view, R.id.ischoose_tv);
        LinearLayout bg_ll = (LinearLayout) get(view, R.id.bg_ll);
        name_tv.setText(bean.getLanguageName());
        if (bean.isChoosed()) {
            ischoose_tv.setVisibility(View.VISIBLE);
        } else {
            ischoose_tv.setVisibility(View.GONE);
        }
        if (bean.isClicked()) {
            if (bean.isChoosed()) {
                bg_ll.setBackgroundColor(Color.parseColor("#00000000"));
            } else {
                bg_ll.setBackgroundColor(Color.parseColor("#B99A01"));
            }
        } else {
            bg_ll.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }
}
