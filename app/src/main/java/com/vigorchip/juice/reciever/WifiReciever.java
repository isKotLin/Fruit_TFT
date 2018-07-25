package com.vigorchip.juice.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.vigorchip.juice.util.event.NetStatuEvent;
import com.vigorchip.juice.util.event.WifiPswdErrorEvent;
import com.vigorchip.juice.util.event.WifiStatuChangeEvent;
import com.vigorchip.puliblib.utils.Logutil;

import de.greenrobot.event.EventBus;

import static com.vigorchip.juice.util.Constant.NET_STATU_NO;
import static com.vigorchip.juice.util.Constant.NET_STATU_OK;
import static com.vigorchip.juice.util.Constant.isWifiOk;

/**
 * Created by Administrator on 2017/2/6.
 */


public class WifiReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//wifi连接上与否
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                Logutil.e("WifiReciever NETWORK_STATE_CHANGED_ACTION wifi网络连接断开");
                isWifiOk = false;
                EventBus.getDefault().post(new NetStatuEvent(NET_STATU_NO));
            } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //获取当前wifi名称
                Logutil.e("WifiReciever NETWORK_STATE_CHANGED_ACTION 连接到网络 NetworkInfo.State.CONNECTED= " + wifiInfo.getSSID());
                if(wifiInfo.getSSID().equals("<unknown ssid>")){
                    isWifiOk = false;
                    EventBus.getDefault().post(new NetStatuEvent(NET_STATU_NO));
                }else{
                    isWifiOk = true;
                    EventBus.getDefault().post(new NetStatuEvent(NET_STATU_OK));
                }
            }

        } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi打开与否
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            Logutil.e("WifiReciever  WIFI_STATE_CHANGED_ACTION  " + wifistate);
            EventBus.getDefault().post(new WifiStatuChangeEvent(wifistate));
        }
        if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            Logutil.e("WifiReciever   SUPPLICANT_STATE_CHANGED_ACTION");
            int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
            if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                Logutil.e("密码错误");
                EventBus.getDefault().post(new WifiPswdErrorEvent());
            }
        }
    }
}
