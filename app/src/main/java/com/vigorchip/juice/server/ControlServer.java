package com.vigorchip.juice.server;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.vigorchip.juice.MyApplication;
import com.vigorchip.juice.reciever.ScreenStatusReceiver;
import com.vigorchip.juice.util.Constant;
import com.vigorchip.juice.util.CupStatusManager;
import com.vigorchip.juice.util.event.ChangeJarCupEvent;
import com.vigorchip.puliblib.utils.Logutil;
import com.vigorchip.puliblib.utils.event.ErrorEvent;

import java.util.ArrayList;

import android_serialport_api.Commands;
import android_serialport_api.SerialPortUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/2/19.
 */

public class ControlServer extends Service {

    public static SerialPortUtil serialPortUtil;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initSerialPort();
        openReceiver();
        return super.onStartCommand(intent, flags, startId);
    }

    //屏幕状态广播
    ScreenStatusReceiver screenStatusReceiver;

    /**
     * 开启监听屏幕睡眠的广播
     */
    public void openReceiver() {
        if (screenStatusReceiver == null) {
            // 屏幕状态广播初始化
            screenStatusReceiver = new ScreenStatusReceiver();
            IntentFilter screenStatusIF = new IntentFilter();
            screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
            screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
            // 注册
            registerReceiver(screenStatusReceiver, screenStatusIF);
        }
    }

    public void initSerialPort() {
        Logutil.e("初始化串口信息");
        if (serialPortUtil == null) {
            serialPortUtil = SerialPortUtil.getInstance();
            serialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
                @Override
                public void onDataReceive(byte[] buffer, int size) {
                    analysisJarCupBack(buffer, size);
                }
            });
            mHandler.sendEmptyMessage(Constant.HANDLER_SUCCESS);
            mHandler.sendEmptyMessage(ERROR_CODE);
        }

    }

    public static long time;

    /**
     * 开始校验杯子
     */
    public static void judgeJarCup() {
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x21);
        code.add((char) 0x0C);
        char[] realCode = Commands.getRealCode(code);
        long time1 = System.currentTimeMillis();

        time = time1;
        serialPortUtil.sendCmds(realCode);
    }

    /**
     * 开始校验杯子
     */
    public static void checkERROR() {
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x21);
        code.add((char) 0x05);
        char[] realCode = Commands.getRealCode(code);
        serialPortUtil.sendCmds(realCode);
    }


    public static int bigCount;
    public static int smallCount;
    public static int noCupCount;

    /**
     * 校验大杯小杯返回码
     *
     * @param buffer
     * @param size
     */
    public static void analysisJarCupBack(byte[] buffer, int size) {
        if (buffer == null || buffer.length == 0) {
            return;
        }
        byte[] newBuffer = new byte[size];
        for (int i = 0; i < size; i++) {
            newBuffer[i] = buffer[i];
        }
        if (Commands.isCheckOk(newBuffer, size)) {
            if (newBuffer.length >= 5) {
                if (newBuffer[1] == 0) {//执行结果成功
                    if (newBuffer[3] == 12) {
                        Logutil.i("杯子校验成功");
                        if (newBuffer[4] == 14 || newBuffer[4] == 13 || newBuffer[4] == 11 || newBuffer[4] == 7) {//大杯
                            Logutil.i("现在大杯" + "bigCount=" + bigCount);
                            smallCount = 0;
                            noCupCount = 0;
                            bigCount++;
                            if (bigCount == 5) {
                                bigCount = 0;
                                CupStatusManager.setBigCupStatus();
                                EventBus.getDefault().post(new ChangeJarCupEvent(2));
                            }
                        } else if (newBuffer[4] == 12 || newBuffer[4] == 10 || newBuffer[4] == 9 || newBuffer[4] == 6 || newBuffer[4] == 5 || newBuffer[4] == 3) {
                            Logutil.i("现在小杯" + "smallCount=" + bigCount);
                            bigCount = 0;
                            noCupCount = 0;
                            smallCount++;
                            if (smallCount == 5) {
                                smallCount = 0;
                                CupStatusManager.setSmallCupStatus();
                                EventBus.getDefault().post(new ChangeJarCupEvent(1));
                            }
                        } else {
                            Logutil.i("大小杯没放好" + "noCount=" + bigCount);
                            bigCount = 0;
                            smallCount = 0;
                            noCupCount++;
                            if (noCupCount == 1) {
                                noCupCount = 0;
                                CupStatusManager.setNoCupStatus();
                                EventBus.getDefault().post(new ChangeJarCupEvent(0));
                            }
                        }
                    } else if (newBuffer[3] == 5) {
                        Logutil.i("异常反馈=" + newBuffer[4]);
                        if (newBuffer[4] == 2) {
                            EventBus.getDefault().post(new ErrorEvent("E2"));
                        } else if (newBuffer[4] == 1) {
                            EventBus.getDefault().post(new ErrorEvent("E3"));
                        } else if (newBuffer[4] == 0) {
                            EventBus.getDefault().post(new ErrorEvent("E0"));
                        }
                    }
                } else {

                }
            }
        } else {
        }
    }

    public static long curRunTime;

    /**
     * 启动
     */
    public static void startRun() {
        if (curRunTime == 0) {
            curRunTime = System.currentTimeMillis();
            Logutil.e("MEEE开始时间=" + curRunTime);
        }
        Logutil.e("开始启动");
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x10);
        code.add((char) 0x01);
        char[] realCode = Commands.getRealCode(code);
        Commands.LogChar("startRun", realCode);
        serialPortUtil.sendCmds(realCode);
    }

    /**
     * 以运行速度
     *
     * @param speed
     */
    public static void setContinueRunSpeed(int speed, double softTime) {
        Logutil.e("串口发送的速度=" + speed);
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x40);
        code.add((char) 0x13);
        if (speed == 1) {
            code.add((char) (1500 % 256));
            code.add((char) (1500 / 256));
        } else if (speed == 2) {
            code.add((char) (4000 % 256));
            code.add((char) (4000 / 256));
        } else if (speed == 3) {
            code.add((char) (5000 % 256));
            code.add((char) (5000 / 256));
        } else if (speed == 4) {
            code.add((char) (7000 % 256));
            code.add((char) (7000 / 256));
        } else if (speed == 5) {
            code.add((char) (10000 % 256));
            code.add((char) (10000 / 256));
        } else if (speed == 6) {
            code.add((char) (11500 % 256));
            code.add((char) (11500 / 256));
        } else if (speed == 7) {
            code.add((char) (14000 % 256));
            code.add((char) (14000 / 256));
        } else if (speed == 8) {
            code.add((char) (16500 % 256));
            code.add((char) (16500 / 256));
        } else if (speed == 9) {
            code.add((char) (18000 % 256));
            code.add((char) (18000 / 256));
        } else if (speed == 10) {
            code.add((char) (20000 % 256));
            code.add((char) (20000 / 256));
        } else {
            code.add((char) 0);
            code.add((char) 0);
        }
        if (softTime > 10) {
            softTime = 10;
        }
        if (softTime < 0) {
            softTime = 0;
        }
        if (softTime == 0) {
            code.add((char) 0);
        } else {
            code.add((char) 10);
        }
        char[] realCode = Commands.getRealCode(code);
        Commands.LogChar("setContinueRunSpeed", realCode);
        serialPortUtil.sendCmds(realCode);
    }
    /**
     * 以运行速度(老速度,仅做备份处理)
     *
     * @param speed
     */
    public static void setContinueRunOldSpeed(int speed, double softTime) {
        Logutil.e("串口发送的速度=" + speed);
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x40);
        code.add((char) 0x13);
        if (speed == 1) {
            code.add((char) (5000 % 256));
            code.add((char) (5000 / 256));
        } else if (speed == 2) {
            code.add((char) (6000 % 256));
            code.add((char) (6000 / 256));
        } else if (speed == 3) {
            code.add((char) (7000 % 256));
            code.add((char) (7000 / 256));
        } else if (speed == 4) {
            code.add((char) (8000 % 256));
            code.add((char) (8000 / 256));
        } else if (speed == 5) {
            code.add((char) (9000 % 256));
            code.add((char) (9000 / 256));
        } else if (speed == 6) {
            code.add((char) (10000 % 256));
            code.add((char) (10000 / 256));
        } else if (speed == 7) {
            code.add((char) (11500 % 256));
            code.add((char) (11500 / 256));
        } else if (speed == 8) {
            code.add((char) (13000 % 256));
            code.add((char) (13000 / 256));
        } else if (speed == 9) {
            code.add((char) (14500 % 256));
            code.add((char) (14500 / 256));
        } else if (speed == 10) {
            code.add((char) (15000 % 256));
            code.add((char) (15000 / 256));
        } else {
            code.add((char) 0);
            code.add((char) 0);
        }
        if (softTime > 10) {
            softTime = 10;
        }
        if (softTime < 0) {
            softTime = 0;
        }
        if (softTime == 0) {
            code.add((char) 0);
        } else {
            code.add((char) 10);
        }
        char[] realCode = Commands.getRealCode(code);
        Commands.LogChar("setContinueRunSpeed", realCode);
        serialPortUtil.sendCmds(realCode);
    }

    /**
     * Manual模式下设置运行速度
     *
     * @param speed
     */
    public static void setManualContinueRunSpeed(int speed, double softTime) {
        Logutil.e("串口发送的速度=" + speed);
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x40);
        code.add((char) 0x13);
        if (speed == 1) {
            code.add((char) (1500 % 256));
            code.add((char) (1500 / 256));
        } else if (speed == 2) {
            code.add((char) (4000 % 256));
            code.add((char) (4000 / 256));
        } else if (speed == 3) {
            code.add((char) (5000 % 256));
            code.add((char) (5000 / 256));
        } else if (speed == 4) {
            code.add((char) (7000 % 256));
            code.add((char) (7000 / 256));
        } else if (speed == 5) {
            code.add((char) (10000 % 256));
            code.add((char) (10000 / 256));
        } else if (speed == 6) {
            code.add((char) (11500 % 256));
            code.add((char) (11500 / 256));
        } else if (speed == 7) {
            code.add((char) (14000 % 256));
            code.add((char) (14000 / 256));
        } else if (speed == 8) {
            code.add((char) (16500 % 256));
            code.add((char) (16500 / 256));
        } else if (speed == 9) {
            code.add((char) (18000 % 256));
            code.add((char) (18000 / 256));
        } else if (speed == 10) {
            code.add((char) (20000 % 256));
            code.add((char) (20000 / 256));
        } else {
            code.add((char) 0);
            code.add((char) 0);
        }
        if (softTime > 10) {
            softTime = 10;
        }
        if (softTime < 0) {
            softTime = 0;
        }
        code.add((char) (((int) softTime * 10)));
        char[] realCode = Commands.getRealCode(code);
        Commands.LogChar("setContinueRunSpeed", realCode);
        serialPortUtil.sendCmds(realCode);
    }
    /**
     * Manual模式下设置运行速度
     *
     * @param speed
     */
    public static void setManualContinueRunOldSpeed(int speed, double softTime) {
        Logutil.e("串口发送的速度=" + speed);
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x40);
        code.add((char) 0x13);
        if (speed == 1) {
            code.add((char) (1500 % 256));
            code.add((char) (1500 / 256));
        } else if (speed == 2) {
            code.add((char) (4000 % 256));
            code.add((char) (4000 / 256));
        } else if (speed == 3) {
            code.add((char) (5000 % 256));
            code.add((char) (5000 / 256));
        } else if (speed == 4) {
            code.add((char) (6000 % 256));
            code.add((char) (6000 / 256));
        } else if (speed == 5) {
            code.add((char) (7000 % 256));
            code.add((char) (7000 / 256));
        } else if (speed == 6) {
            code.add((char) (10000 % 256));
            code.add((char) (10000 / 256));
        } else if (speed == 7) {
            code.add((char) (11500 % 256));
            code.add((char) (11500 / 256));
        } else if (speed == 8) {
            code.add((char) (13000 % 256));
            code.add((char) (13000 / 256));
        } else if (speed == 9) {
            code.add((char) (14500 % 256));
            code.add((char) (14500 / 256));
        } else if (speed == 10) {
            code.add((char) (15000 % 256));
            code.add((char) (15000 / 256));
        } else {
            code.add((char) 0);
            code.add((char) 0);
        }
        if (softTime > 10) {
            softTime = 10;
        }
        if (softTime < 0) {
            softTime = 0;
        }
        code.add((char) (((int) softTime * 10)));
        char[] realCode = Commands.getRealCode(code);
        Commands.LogChar("setContinueRunSpeed", realCode);
        serialPortUtil.sendCmds(realCode);
    }

    /**
     * 设置软启动时间
     * 功能码	参数序号	参数值
     * 0x20	0x00	0x64,0x00
     *
     * @param time
     */
    public static void setSoftUpTime(int time) {
        Logutil.e("缓速时间=" + time);
        if (time > 10) {
            time = 10;
        }
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x20);
        code.add((char) 0x12);
        code.add((char) (time * 100));
        char[] realCode = Commands.getRealCode(code);
        Commands.LogChar("setSoftUpTime", realCode);
        serialPortUtil.sendCmds(realCode);
    }

    /**
     * 停止运行
     */
    public static void stopRun() {
        if (curRunTime != 0) {
            curRunTime = System.currentTimeMillis() - curRunTime;
            Logutil.e("MEEE结束时间=" + System.currentTimeMillis() + "MEEE运行时间=" + curRunTime);
            MyApplication.runAllTime += curRunTime;
            MyApplication.setRunAllTime();
            curRunTime = 0;
        }
        Logutil.e("我被暂停了");
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x10);
        code.add((char) 0x00);
        char[] realCode = Commands.getRealCode(code);
        serialPortUtil.sendCmds(realCode);
    }

    private static int ERROR_CODE = 2;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.HANDLER_SUCCESS) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        judgeJarCup();
                        mHandler.sendEmptyMessage(Constant.HANDLER_SUCCESS);
                    }
                }, 100);
            } else if (msg.what == ERROR_CODE) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkERROR();
                        mHandler.sendEmptyMessage(ERROR_CODE);
                    }
                }, 1100);
            } else {
                mHandler.removeMessages(Constant.HANDLER_SUCCESS);
                mHandler.removeMessages(ERROR_CODE);
            }

        }
    };

    /**
     * 屏幕休眠的广播
     */
    public static void screenSleepCode() {
        Logutil.e("睡眠....");
        ArrayList<Character> code = new ArrayList<>();
        code.add((char) 0x20);
        code.add((char) 0x45);
        code.add((char) 0x00);
        char[] realCode = Commands.getRealCode(code);
        Commands.LogChar("stopRun", realCode);
        serialPortUtil.sendCmds(realCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (screenStatusReceiver != null) {
            unregisterReceiver(screenStatusReceiver);
        }
        mHandler.removeMessages(Constant.HANDLER_SUCCESS);
    }
}
