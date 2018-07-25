package com.vigorchip.puliblib.utils.event;

/**
 * Created by Administrator on 2017.05.23.
 */

public class ErrorEvent {
    private String tag;//E0表示正常

    public ErrorEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
