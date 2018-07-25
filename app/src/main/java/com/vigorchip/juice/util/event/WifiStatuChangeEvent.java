package com.vigorchip.juice.util.event;

/**
 * Created by Administrator on 2017/2/6.
 */

public class WifiStatuChangeEvent {
    private int statuCode;

    public WifiStatuChangeEvent(int statuCode) {
        this.statuCode = statuCode;
    }

    public int getStatuCode() {
        return statuCode;
    }

    public void setStatuCode(int statuCode) {
        this.statuCode = statuCode;
    }
}
