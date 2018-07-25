package com.vigorchip.juice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongqw.wifilibrary.WiFiManager;
import com.vigorchip.juice.R;
import com.vigorchip.juice.bean.WiFiBean;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by kqw on 2016/8/2.
 * Wifi列表的数据适配器
 */
public class WifiListAdapter extends BaseAdapter {

    private List<WiFiBean> beans;
    private Context mContext;
    private LayoutInflater inflater;

    public WifiListAdapter(Context context) {
        mContext = context.getApplicationContext();
        inflater = LayoutInflater.from(context);
        this.beans = new ArrayList<>();
    }

    public int refreshData(List<ScanResult> scanResults, WifiInfo wifiInfo) {
        int level = -200;
        if (scanResults == null) {
            this.beans.clear();
        } else {
            scanResults = WiFiManager.excludeRepetition(scanResults);
            this.beans.clear();
            for (int i = 0; i < scanResults.size(); i++) {
                ScanResult scanResult = scanResults.get(i);
                if (Constant.isWifiOk && (("\"" + scanResult.SSID + "\"")).equals(wifiInfo.getSSID())) {
                    level = scanResult.level;
                    WiFiBean wiFiBean = new WiFiBean(scanResult, WiFiBean.CONNECT_ED, scanResult.level);
                    beans.add(0, wiFiBean);
                } else {
                    WiFiBean wiFiBean = new WiFiBean(scanResult, WiFiBean.CONNECT_UN, scanResult.level);
                    beans.add(wiFiBean);
                }
            }
        }
        notifyDataSetChanged();
        return level;
    }

    /**
     * 设置正在连接标识
     *
     * @param ConnectedSSID
     */
    public int setConnectedSSID(String ConnectedSSID) {
        Logutil.e("setConnectedSSID=" + ConnectedSSID);
        for (int i = 0; i < beans.size(); i++) {
            WiFiBean bean = beans.get(i);
            bean.setConnectStatus(WiFiBean.CONNECT_UN);
            beans.set(i, bean);
        }
        int level = -200;

        for (int i = 0; i < beans.size(); i++) {
            WiFiBean bean = beans.get(i);
            if ((("\"" + bean.getResult().SSID + "\"")).equals(ConnectedSSID)) {
                bean.setConnectStatus(WiFiBean.CONNECT_ED);
                Collections.swap(beans, i, 0);
                level = bean.getRssi();
            }
        }
        this.notifyDataSetChanged();
        return level;
    }

    public int setConnectingSSID(String ConnectingSSID) {
        for (int i = 0; i < beans.size(); i++) {
            WiFiBean bean = beans.get(i);
            bean.setConnectStatus(WiFiBean.CONNECT_UN);
            beans.set(i, bean);
        }

        for (int i = 0; i < beans.size(); i++) {
            WiFiBean bean = beans.get(i);
            if (("\"" + bean.getResult().SSID + "\"").equals(ConnectingSSID)) {
                bean.setConnectStatus(WiFiBean.CONNECT_ED);
                beans.set(i, bean);
            }
        }
        this.notifyDataSetChanged();
        return -200;
    }


    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WiFiBean bean = beans.get(position);
        convertView = inflater.inflate(R.layout.item_setting_wifi, null);
        LinearLayout bg_ll = (LinearLayout) convertView.findViewById(R.id.bg_ll);
        TextView wifiname_tv = (TextView) convertView.findViewById(R.id.wifiname_tv);
        TextView ischoose_tv = (TextView) convertView.findViewById(R.id.ischoose_tv);
        wifiname_tv.setText("" + bean.getResult().SSID);
        if (bean.getConnectStatus().equals(WiFiBean.CONNECT_ED)) {
            Logutil.e("position=" + position + "<Name>" + bean.getResult().SSID + "《》《信号强度》" + bean.getRssi());
            ischoose_tv.setText("CONNECTED");
            bg_ll.setBackgroundColor(Color.parseColor("#FEDA00"));
        } else if (bean.getConnectStatus().equals(WiFiBean.CONNECT_ING)) {
            bg_ll.setBackgroundColor(0);
            ischoose_tv.setText("CONNECTING");
        } else {
            bg_ll.setBackgroundColor(0);
            ischoose_tv.setText("CONNECT");
        }
        return convertView;
    }
}
