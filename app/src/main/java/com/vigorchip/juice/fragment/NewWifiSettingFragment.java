package com.vigorchip.juice.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.kongqw.wifilibrary.WiFiManager;
import com.kongqw.wifilibrary.listener.OnWifiConnectListener;
import com.kongqw.wifilibrary.listener.OnWifiEnabledListener;
import com.kongqw.wifilibrary.listener.OnWifiScanResultsListener;
import com.vigorchip.juice.R;
import com.vigorchip.juice.activity.SetWiFiPswdActivity;
import com.vigorchip.juice.adapter.WifiListAdapter;
import com.vigorchip.juice.bean.WiFiBean;
import com.vigorchip.juice.reciever.WifiReciever;
import com.vigorchip.juice.util.event.NetStatuEvent;
import com.vigorchip.juice.util.event.WifiPswdErrorEvent;
import com.vigorchip.juice.util.event.WifiStatuChangeEvent;
import com.vigorchip.puliblib.base.BaseFragment;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/6.
 */

public class NewWifiSettingFragment extends BaseFragment implements OnWifiEnabledListener, OnWifiScanResultsListener, OnWifiConnectListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @InjectView(R.id.wifi_iv)
    ImageView wifi_iv;

    @InjectView(R.id.refresh_iv)
    ImageView refresh_iv;

    @InjectView(R.id.noWifi_iv)
    ImageView noWifi_iv;

    @InjectView(R.id.listview)
    ListView listview;

    @InjectView(R.id.switch_wifi)
    SwitchCompat switch_wifi;

    private WiFiManager mWiFiManager;
    private WifiListAdapter mWifiListAdapter;

    private WifiReciever wifiReciever;

    public static NewWifiSettingFragment newInstance() {
        return new NewWifiSettingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_wifi;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        wifiReciever = new WifiReciever();
        registerReceiver();
        mWiFiManager = WiFiManager.getInstance(myActivity.getApplicationContext());
        mWifiListAdapter = new WifiListAdapter(myActivity.getApplicationContext());
        listview.setAdapter(mWifiListAdapter);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);

        switch_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Logutil.e("-----------------------");
                if (isChecked) {
                    mWiFiManager.openWiFi();

                    refresh_iv.setImageResource(R.drawable.btn_refresh_1);
                    noWifi_iv.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                } else {
                    mWiFiManager.closeWiFi();

                    refresh_iv.setImageResource(R.drawable.btn_refresh_2);
                    noWifi_iv.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        // 添加监听
        mWiFiManager.setOnWifiEnabledListener(this);
        mWiFiManager.setOnWifiScanResultsListener(this);
        mWiFiManager.setOnWifiConnectListener(this);
        // 更新WIFI开关状态
        switch_wifi.setChecked(mWiFiManager.isWifiEnabled());
    }

    @Override
    public void onPause() {
        super.onPause();
        // 移除监听
        mWiFiManager.removeOnWifiEnabledListener();
        mWiFiManager.removeOnWifiScanResultsListener();
        mWiFiManager.removeOnWifiConnectListener();
    }


    private String wifiPassword = null;
    private int requestCode = 10;

    /**
     * WIFI开关状态的回调
     *
     * @param enabled true 可用 false 不可用
     */
    @Override
    public void onWifiEnabled(boolean enabled) {
        Logutil.e("onWifiEnabled=" + enabled);
        switch_wifi.setChecked(enabled);
    }

    /**
     * WIFI列表刷新后的回调
     *
     * @param scanResults 扫描结果
     */
    @Override
    public void onScanResults(List<ScanResult> scanResults) {
        WifiManager wifiManager = (WifiManager) myActivity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = mWifiListAdapter.refreshData(scanResults, wifiInfo);//顺便把wifi强弱给返回
        showWifiStrongStatu(level);
    }

    /**
     * WIFI连接的Log得回调
     *
     * @param log log
     */
    @Override
    public void onWiFiConnectLog(String log) {
        Logutil.e("onWiFiConnectLog=" + log);
    }

    /**
     * WIFI连接成功的回调
     *
     * @param SSID 热点名
     */
    @Override
    public void onWiFiConnectSuccess(String SSID) {
        Logutil.e("onWiFiConnectSuccess成功=" + SSID);
//        showProgressBar(false);
        int rssi = mWifiListAdapter.setConnectedSSID(SSID);
        showWifiStrongStatu(rssi);
    }

    public void showWifiStrongStatu(int rssi) {
        if (rssi > -60) {
            wifi_iv.setImageResource(R.drawable.btn_wifi_5);
        } else if (rssi <= -60 && rssi > -70) {
            wifi_iv.setImageResource(R.drawable.btn_wifi_4);
        } else if (rssi <= -70 && rssi > -80) {
            wifi_iv.setImageResource(R.drawable.btn_wifi_3);
        } else if (rssi <= -80 && rssi > -100) {
            wifi_iv.setImageResource(R.drawable.btn_wifi_2);
        } else {
            if (mWiFiManager.isWifiEnabled()) {
                wifi_iv.setImageResource(R.drawable.btn_wifi_5);
            } else {
                wifi_iv.setImageResource(R.drawable.btn_wifi_1);
            }
        }
    }

    /**
     * WIFI连接失败的回调
     *
     * @param SSID 热点名
     */
    @Override
    public void onWiFiConnectFailure(String SSID) {
        Logutil.e("onWiFiConnectFailure:  [ " + SSID + " ] 连接失败");
        if (isProgressbarShow() && mWiFiManager.isWifiEnabled()) {
            showToast("connect fail");
        }
        showProgressBar(false);
    }


    private ScanResult connScanResult;

    /**
     * WIFI列表单击
     *
     * @param parent   parent
     * @param view     view
     * @param position position
     * @param id       id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WiFiBean wifiBean = (WiFiBean) mWifiListAdapter.getItem(position);
        if (wifiBean.getConnectStatus().equals(WiFiBean.CONNECT_ED) || wifiBean.getConnectStatus().equals(WiFiBean.CONNECT_ING)) {
            return;
        }
        int networkId = mWiFiManager.IsConfiguration(wifiBean.getResult().SSID);
        if (networkId == -1) {
            connScanResult = wifiBean.getResult();
            switch (mWiFiManager.getSecurityMode(connScanResult)) {
                case WPA:
                case WPA2:
                case WEP:
                    SetWiFiPswdActivity.startActivity(myActivity, connScanResult.SSID, requestCode);
                    break;
                case OPEN: // 开放网络
//                    showProgressBar(true);
                    mWiFiManager.connectOpenNetwork(connScanResult.SSID);
                    break;
            }
        } else {
            if (mWiFiManager.ConnectWifi(wifiBean.getResult().SSID, networkId)) {//连接指定WIFI
//                showProgressBar(true);
                mWifiListAdapter.setConnectingSSID(wifiBean.getResult().SSID);
            } else {
                connScanResult = wifiBean.getResult();
                switch (mWiFiManager.getSecurityMode(connScanResult)) {
                    case WPA:
                    case WPA2:
                    case WEP:
                        SetWiFiPswdActivity.startActivity(myActivity, connScanResult.SSID, requestCode);
                        break;
                    case OPEN: // 开放网络
//                        showProgressBar(true);
                        mWiFiManager.connectOpenNetwork(connScanResult.SSID);
                        break;
                }
            }
        }
    }

    /**
     * WIFI列表长按
     *
     * @param parent   parent
     * @param view     view
     * @param position position
     * @param id       id
     * @return 是否拦截长按事件
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        if (1 == 1) {
            return true;
        }
        WiFiBean wifiBean = (WiFiBean) mWifiListAdapter.getItem(position);
        ScanResult scanResult = wifiBean.getResult();
        final String ssid = scanResult.SSID;
        new AlertDialog.Builder(myActivity)
                .setTitle(ssid)
                .setItems(new String[]{"断开连接", "删除网络配置"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 断开连接
                                WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
                                Logutil.e("onClick: connectionInfo :" + connectionInfo.getSSID());
                                if (mWiFiManager.addDoubleQuotation(ssid).equals(connectionInfo.getSSID())) {
                                    mWiFiManager.disconnectWifi(connectionInfo.getNetworkId());
                                } else {
                                    showToast("当前没有连接 [ " + ssid + " ]");
                                }
                                break;
                            case 1: // 删除网络配置
                                WifiConfiguration wifiConfiguration = mWiFiManager.getConfigFromConfiguredNetworksBySsid(ssid);
                                if (null != wifiConfiguration) {
                                    boolean isDelete = mWiFiManager.deleteConfig(wifiConfiguration.networkId);
                                    showToast(isDelete ? "删除成功！" : "其他应用配置的网络没有ROOT权限不能删除！");
                                } else {
                                    showToast("没有保存该网络！");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            wifiPassword = data.getStringExtra("password");
            if (!TextUtils.isEmpty(wifiPassword)) {
//                showProgressBar(true);
                switch (mWiFiManager.getSecurityMode(connScanResult)) {
                    case WPA:
                    case WPA2:
                        Logutil.e("WPA2连接ing=" + connScanResult.SSID + "           pswd=" + wifiPassword);
                        boolean result0 = mWiFiManager.connectWPA2Network(connScanResult.SSID, wifiPassword);
                        Logutil.e("连接结果=" + result0);
                        break;
                    case WEP:
                        Logutil.e("WEP连接ing=" + connScanResult.SSID + "           pswd=" + wifiPassword);
                        boolean result1 = mWiFiManager.connectWEPNetwork(connScanResult.SSID, wifiPassword);
                        Logutil.e("连接结果=" + result1);
                        break;
                    case OPEN: // 开放网络
                        mWiFiManager.connectOpenNetwork(connScanResult.SSID);
                        break;
                }
            }
        }
    }

    @OnClick(R.id.wifi_iv)
    public void open_close_wifi_Click() {
        if (mWiFiManager.isWifiEnabled()) {
            wifi_iv.setImageResource(R.drawable.btn_wifi_1);//伪需求 原为1
            refresh_iv.setImageResource(R.drawable.btn_refresh_2);
            mWifiListAdapter.refreshData(null, null);
            mWifiListAdapter.notifyDataSetChanged();
            switch_wifi.setChecked(false);
        } else {
            wifi_iv.setImageResource(R.drawable.btn_wifi_5);
            switch_wifi.setChecked(true);
        }

    }


    @OnClick(R.id.refresh_iv)
    public void updataClick() {
        if (mWiFiManager.isWifiEnabled()) {
            mWifiListAdapter.refreshData(null, null);
            mWifiListAdapter.notifyDataSetChanged();
            wifi_iv.setImageResource(R.drawable.btn_wifi_5);//伪需求 原为1
            mWiFiManager.startScan();
        }
    }

    public void onEventMainThread(WifiStatuChangeEvent event) {
        int wifistate = event.getStatuCode();
        refresh_iv.setImageResource(R.drawable.btn_refresh_2);
        if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
            Logutil.e("wifi 不能使用 其值为1");
            wifi_iv.setImageResource(R.drawable.btn_wifi_1);
            noWifi_iv.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
            Logutil.e("wifi 可以使用 其值为3");
            listview.setVisibility(View.VISIBLE);
            noWifi_iv.setVisibility(View.GONE);
            wifi_iv.setImageResource(R.drawable.btn_wifi_5);
            refresh_iv.setImageResource(R.drawable.btn_refresh_1);
            mWiFiManager.startScan();
        } else if (wifistate == WifiManager.WIFI_STATE_DISABLING) {
            noWifi_iv.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
            wifi_iv.setImageResource(R.drawable.btn_wifi_1);
            Logutil.e("wifi 不能使用 正在关闭中 其值为0");
        } else if (wifistate == WifiManager.WIFI_STATE_ENABLING) {
            Logutil.e("wifi 不能使用 正在开启中 其值为2");
            noWifi_iv.setVisibility(View.INVISIBLE);
            listview.setVisibility(View.GONE);
            wifi_iv.setImageResource(R.drawable.btn_wifi_5);
        } else if (wifistate == WifiManager.WIFI_STATE_UNKNOWN) {
            Logutil.e("wifi 不能使用 未知错误 其值为4");
            noWifi_iv.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }
    }

    public synchronized void onEventMainThread(NetStatuEvent event) {
        int netStatu = event.getNetStatu();
        Logutil.e("NetStatuEvent=" + netStatu);
    }

    public synchronized void onEventMainThread(WifiPswdErrorEvent event) {
        Logutil.e("W收到IFI密码出错");
//        showProgressBar(false);
        showToast("Password is wrong ");
        if (connScanResult != null) {
            WifiConfiguration wifiConfiguration = mWiFiManager.getConfigFromConfiguredNetworksBySsid(connScanResult.SSID);
            if (null != wifiConfiguration) {
                boolean isDelete = mWiFiManager.deleteConfig(wifiConfiguration.networkId);
                Logutil.e("密码错误,删除配置" + isDelete);
            }
        }
    }


    public void registerReceiver() {
        IntentFilter ins = new IntentFilter();
        ins.addAction(WifiManager.RSSI_CHANGED_ACTION);//信号强度变化
        ins.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//网络状态变化
        ins.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//wifi状态,是否连上,密码
        ins.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);//是否正在获取ip地址
        ins.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        ins.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//连上与否
        myActivity.registerReceiver(wifiReciever, ins);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        myActivity.unregisterReceiver(wifiReciever);
    }
}
