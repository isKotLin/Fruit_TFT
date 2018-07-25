package com.vigorchip.juice.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.adapter.SettingLanguageAdapter;
import com.vigorchip.juice.bean.LanguageBean;
import com.vigorchip.puliblib.base.BaseFragment;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/6.
 */

public class LanguageSettingFragment extends BaseFragment {

    @InjectView(R.id.ok_tv)
    TextView ok_tv;

    @InjectView(R.id.listview)
    ListView listview;

    private SettingLanguageAdapter languageAdapter;

    private ArrayList<LanguageBean> beans = new ArrayList<>();

    public static LanguageSettingFragment newInstance() {
        return new LanguageSettingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_language;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        final LanguageBean bean0 = new LanguageBean("English", true, false);
        final LanguageBean bean1 = new LanguageBean("简体中文", false, false);
        final LanguageBean bean2 = new LanguageBean("繁體中文", false, false);
        beans.add(bean0);
        beans.add(bean1);
        beans.add(bean2);
        languageAdapter = new SettingLanguageAdapter(myActivity, beans);
        listview.setAdapter(languageAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                for (int i = 0; i < beans.size(); i++) {
                    LanguageBean bean1 = beans.get(i);
                    bean1.setClicked(false);
                    beans.set(i, bean1);
                }
                LanguageBean bean1 = beans.get(position);
                bean1.setClicked(true);
                beans.set(position, bean1);
                languageAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.ok_tv)
    public void setLanguage() {
        for (int i = 0; i < beans.size(); i++) {
            LanguageBean bean = beans.get(i);
            if (bean.isClicked()) {
                for (int m = 0; m < beans.size(); m++) {
                    LanguageBean bean_m = beans.get(m);
                    bean_m.setChoosed(false);
                    beans.set(m, bean_m);
                    languageAdapter.notifyDataSetChanged();
                }
                bean.setChoosed(true);
                beans.set(i, bean);
                languageAdapter.notifyDataSetChanged();
            }
        }
    }
}
