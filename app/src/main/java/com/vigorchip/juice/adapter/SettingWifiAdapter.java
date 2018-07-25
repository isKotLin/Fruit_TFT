package com.vigorchip.juice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.juice.R;
import com.vigorchip.juice.bean.WiFiBean;
import com.vigorchip.puliblib.base.BaseArrayListAdapter;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/15.
 */

public class SettingWifiAdapter extends BaseArrayListAdapter {
    private Context context;
    private ArrayList<WiFiBean> beans;
    private WifiBack back;

    public SettingWifiAdapter(Context context, ArrayList<WiFiBean> beans, WifiBack back) {
        super(context, beans);
        this.context = context;
        this.beans = beans;
        this.back = back;
    }

    @Override
    public int getContentView() {
        return R.layout.item_setting_wifi;
    }

    @Override
    public void onInitView(View view, int position) {
        if(position>=beans.size()){
            return;
        }
        Logutil.e("beans个数="+beans.size());
        WiFiBean bean = beans.get(position);
        LinearLayout bg_ll = (LinearLayout) get(view, R.id.bg_ll);
        TextView wifiname_tv = (TextView) get(view, R.id.wifiname_tv);
        TextView ischoose_tv = (TextView) get(view, R.id.ischoose_tv);
        wifiname_tv.setText("" + bean.getResult().SSID);
        if (bean.getConnectStatus().equals(WiFiBean.CONNECT_ED)) {
            Logutil.e("position=" + position + "<Name>" + bean.getResult().SSID + "《》《信号强度》" + bean.getRssi());
            ischoose_tv.setText("CONNECTED");
            bg_ll.setBackgroundColor(Color.parseColor("#FEDA00"));
            if (bean.getRssi() > -60) {
                back.WifiBackRssi(1);
            } else if (bean.getRssi() <= -60 && bean.getRssi() > -70) {
                back.WifiBackRssi(2);
            } else if (bean.getRssi() <= -70 && bean.getRssi() > -80) {
                back.WifiBackRssi(3);
            } else if (bean.getRssi() <= -80 && bean.getRssi() > -100) {
                back.WifiBackRssi(4);
            } else {
                back.WifiBackRssi(5);
            }
        } else if(bean.getConnectStatus().equals(WiFiBean.CONNECT_ING)) {
            bg_ll.setBackgroundColor(0);
            ischoose_tv.setText("CONNECTING");
        }else{
            bg_ll.setBackgroundColor(0);
            ischoose_tv.setText("CONNECT");
        }
    }

    public interface WifiBack {
        void WifiBackRssi(int strong);
    }
}
