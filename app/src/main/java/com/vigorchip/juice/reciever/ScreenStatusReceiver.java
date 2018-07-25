package com.vigorchip.juice.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vigorchip.juice.server.ControlServer;
import com.vigorchip.juice.util.event.ScreenCloseEvent;
import com.vigorchip.puliblib.utils.Logutil;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/24.
 */

public class ScreenStatusReceiver extends BroadcastReceiver {
    String SCREEN_ON = "android.intent.action.SCREEN_ON";
    String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 屏幕唤醒
        if (SCREEN_ON.equals(intent.getAction())) {
            Logutil.e(SCREEN_ON);
        }
        // 屏幕休眠
        else if (SCREEN_OFF.equals(intent.getAction())) {
            ControlServer.screenSleepCode();
            EventBus.getDefault().post(new ScreenCloseEvent());
            Logutil.e(SCREEN_OFF);
        }
    }
}