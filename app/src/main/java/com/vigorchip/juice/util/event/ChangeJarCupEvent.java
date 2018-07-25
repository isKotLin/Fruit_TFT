package com.vigorchip.juice.util.event;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ChangeJarCupEvent {
    public int mode;//0没有杯子 1小杯 2大杯

    public ChangeJarCupEvent(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
