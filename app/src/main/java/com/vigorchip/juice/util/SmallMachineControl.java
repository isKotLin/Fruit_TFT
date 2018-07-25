package com.vigorchip.juice.util;

import android.os.Handler;
import android.os.Message;

import com.vigorchip.db.entity.ActionBean;
import com.vigorchip.juice.server.ControlServer;
import com.vigorchip.puliblib.utils.Logutil;

import java.util.List;


/**
 * Created by Administrator on 2017/2/10.
 */

public class SmallMachineControl {
    long allTimes;//总的时间
    long curTimes;//当前记录总时间 未加上当前已运行的时间
    long curRunTimes;//当前运行时间
    long startTime;//保存开始时间和重新开始时间
    long endTime; //结束时间
    int curSpeed;//当前运行档 0 1 2 3 4 5 6 7 8 9 10
    boolean isMachineWorking = false;//当前是否运行
    private ControlListener listener;
    List<ActionBean> allActionBeans;

    boolean isSpeedControl;//速度是否外部控制

    public SmallMachineControl(long allTimes, List<ActionBean> allActionBeans, ControlListener listener) {
        this.allActionBeans = allActionBeans;
        this.allTimes = allTimes;
        this.listener = listener;
        startTime = 0;
        endTime = 0;
        curSpeed = 0;
    }

    /**
     * 设置回调的
     *
     * @param listener
     */
    public void setOnControlListener(ControlListener listener) {
        this.listener = listener;
    }


    public void setActionBeans(List<ActionBean> actionBeans) {
        if (actionBeans == null) {
            return;
        }
        this.allActionBeans = actionBeans;
        if (isMachineWorking) {
            endTime = System.currentTimeMillis();
            curRunTimes = curRunTimes + endTime - startTime;
            curTimes = curTimes + curRunTimes;
            startTime = endTime;
        } else {
            curTimes = curTimes + curRunTimes;
        }

        curRunTimes = 0;
        Logutil.e("setActionBeans运行总时间=" + allTimes);
    }

    /**
     * 设置速度
     *
     * @param speed
     */
    public void setSpeed(int speed, boolean isspeedControl) {
        if (speed < 0) {
            return;
        }
        this.isSpeedControl = isspeedControl;
        if (isMachineWorking) {
            if (isSpeedControl && curSpeed != speed) {//如果外部控制，设置外部速度
                curSpeed = speed;
                if (speed != 0) {
                    ControlServer.setContinueRunSpeed(speed, 0);
                } else {
                    ControlServer.stopRun();
                }
            }
        } else {
            if (speed == 0) {
                curSpeed = speed;
                ControlServer.stopRun();
                listener.curSpeedBack(curSpeed);
            }
            if (isspeedControl) {
                if (curSpeed == 0) {
                    curSpeed = speed;
                    ControlServer.setContinueRunSpeed(speed, 0);
                    ControlServer.startRun();
                } else {
                    if (curSpeed != speed) {
                        curSpeed = speed;
                        ControlServer.setContinueRunSpeed(speed, 0);
                    }
                }
                if (listener != null) {
                    listener.curSpeedBack(speed);
                }
                //    huidiao
            } else {
                if (curSpeed != 0) {
                    curSpeed = 0;
                    ControlServer.stopRun();
                    listener.curSpeedBack(curSpeed);
                }
            }
        }
    }

    public boolean isWorking() {
        return isMachineWorking;
    }

    /**
     * 开始运行
     */
    public void startPlay() {
        if (allTimes <= 0) {
            return;
        }
        if ((curRunTimes + curTimes) >= allTimes) {
            isMachineWorking = false;
            listener.curControlFinish();
            return;
        }
        Logutil.e("开始运行");
        isMachineWorking = true;
        int[] actionMethon = getActionMethon(curRunTimes);

        if (actionMethon[2] == 1) {//设置速度
            ControlServer.setContinueRunSpeed(actionMethon[3], actionMethon[1]);
            curSpeed = actionMethon[3];
        }
        ControlServer.startRun();
        startTime = System.currentTimeMillis();
        mhandler.sendEmptyMessage(PLAY_MESSAGE_CODE);
    }

    /**
     * 暂停运行
     */
    public void stopPlay() {
        Logutil.e("手动暂停");
        ControlServer.stopRun();
        endTime = System.currentTimeMillis();
        curRunTimes = curRunTimes + endTime - startTime;
        isMachineWorking = false;
        notifyCurStatu();
        curSpeed = 0;
        mhandler.removeMessages(PLAY_MESSAGE_CODE);
    }

    private static int PLAY_MESSAGE_CODE = 100;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PLAY_MESSAGE_CODE) {
                notifyCurStatu();
                if (isMachineWorking) {
                    mhandler.sendEmptyMessageDelayed(PLAY_MESSAGE_CODE, 100);
                } else {
                    curSpeed = 0;
                }
            }
        }
    };

    /**
     * 刷新当前状态
     */

    private void notifyCurStatu() {
        if (allTimes <= 0) {
            listener.showInfo("设置总时间");
            isMachineWorking = false;
            mhandler.removeMessages(PLAY_MESSAGE_CODE);
            Logutil.e("总时间为0暂停运行");
            ControlServer.stopRun();
            return;
        }
        if ((curTimes + curRunTimes) >= allTimes) {
            if (isMachineWorking) {
                isMachineWorking = false;
                mhandler.removeMessages(PLAY_MESSAGE_CODE);
                Logutil.e("当前运行时间>总时间暂停");
                ControlServer.stopRun();
                listener.curControlFinish();
            }
            return;
        }
        if (isMachineWorking) {
            endTime = System.currentTimeMillis();
            if (curTimes + curRunTimes + endTime - startTime >= allTimes) {
                curRunTimes = curRunTimes + endTime - startTime;
                curRunTimes = 0;
                isMachineWorking = false;
                Logutil.e("isMachineWorking当前运行时间>总时间暂停");
                ControlServer.stopRun();
                mhandler.removeMessages(PLAY_MESSAGE_CODE);
                listener.curControlFinish();
            } else {
                notifyCurSpeed(curRunTimes + endTime - startTime);
                listener.curMachineStatu(isMachineWorking, allTimes, curTimes, curRunTimes + endTime - startTime, curSpeed);
            }
        } else {
            endTime = 0;
            startTime = 0;
            isMachineWorking = false;
            curSpeed = 0;
            notifyCurSpeed(curRunTimes);
            Logutil.e("isMachineWorking为false暂停");
            ControlServer.stopRun();
            mhandler.removeMessages(PLAY_MESSAGE_CODE);
            listener.curMachineStatu(isMachineWorking, allTimes, curTimes, curRunTimes, curSpeed);
        }
    }

    /**
     * @param curtime
     * @return
     */
    private int[] getActionMethon(long curtime) {
        int[] result = new int[4];//检测的结果，第一位为0表示不软减速1为软加速，第二位为软加速的时间，第三位为是否持续速度,第四位为速度值
        for (int i = 0; i < allActionBeans.size(); i++) {
            ActionBean actionBean = allActionBeans.get(i);
            String totletime = actionBean.getTotletime();
            String stime = actionBean.getStime();
            String etime = actionBean.getEtime();
            long totletime_int = Integer.valueOf(totletime) * 1000;
            long stime_int = Integer.valueOf(stime) * 1000;
            long etime_int = Integer.valueOf(etime) * 1000;
            if (curtime >= totletime_int) {
                return result;
            }
            if (curtime >= stime_int && curtime < etime_int) {
                if (curtime - stime_int < 150) {//只给一次软加速
                    if (actionBean.getMethod().equals("softup")) {
                        result[0] = 1;
                        result[1] = Integer.valueOf(actionBean.getEtime()) - Integer.valueOf(actionBean.getStime());
                    }
                }
                result[2] = 1;

                int startSpeed = Integer.valueOf(actionBean.getStartspeed());
                int endSpeed = Integer.valueOf(actionBean.getEndspeed());
                int speed = startSpeed + (int) ((endSpeed - startSpeed) * (curtime - stime_int) * 1.0 / (etime_int - stime_int));
                result[3] = speed;
                return result;
            }
        }
        return result;
    }

    private void notifyCurSpeed(long curtime) {
        for (int i = 0; i < allActionBeans.size(); i++) {
            ActionBean actionBean = allActionBeans.get(i);
            String totletime = actionBean.getTotletime();
            String stime = actionBean.getStime();
            String etime = actionBean.getEtime();
            long totletime_int = Integer.valueOf(totletime) * 1000;
            long stime_int = Integer.valueOf(stime) * 1000;
            long etime_int = Integer.valueOf(etime) * 1000;
            if (curtime >= totletime_int) {
                return;
            }
            if (curtime > stime_int && curtime <= etime_int) {
                if (!isSpeedControl) {//外部控制，所以不取此速度
                    int startSpeed = Integer.valueOf(actionBean.getStartspeed());
                    int endSpeed = Integer.valueOf(actionBean.getEndspeed());
                    int speed = startSpeed + (int) ((endSpeed - startSpeed) * (curtime - stime_int) * 1.0 / (etime_int - stime_int));
                    if (speed != curSpeed) {
                        curSpeed = speed;
                        ControlServer.setContinueRunSpeed(curSpeed, 0);
                    }
                } else {
                    Logutil.e("外部控制");
                }
                return;
            }
        }
        return;
    }

    /**
     * 控制的回调监听
     */
    public interface ControlListener {
        /**
         * 当前状态
         *
         * @param isWorking
         * @param alltime
         * @param curtime
         * @param curSpeed
         */
        void curMachineStatu(boolean isWorking, long alltime, long curtime, long curRunTime, int curSpeed);

        /**
         * 设备完成
         */

        void curControlFinish();

        void curSpeedBack(int speed);

        /**
         * 弹信息
         *
         * @param str
         */
        void showInfo(String str);

        void handlerExecpter();
    }
}
