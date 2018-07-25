package com.vigorchip.juice.bean;

import android.net.wifi.ScanResult;

/**
 * Created by Administrator on 2017/2/6.
 */

public class WiFiBean {
    private ScanResult result;
    private String connectStatus;
    private int rssi;//wifi强度

    public static String CONNECT_UN = "0";
    public static String CONNECT_ING = "1";
    public static String CONNECT_ED = "2";

    public WiFiBean(ScanResult result, String connectStatus, int rssi) {
        this.result = result;
        this.connectStatus = connectStatus;
        this.rssi = rssi;
    }


    public String getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus = connectStatus;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }


    public ScanResult getResult() {
        return result;
    }

    public void setResult(ScanResult result) {
        this.result = result;
    }
}
