package com.vigorchip.juice.util.event;

/**
 * Created by Administrator on 2017/4/2.
 */

public class MainChangeEvent {
    private int level;//显示级别，一级为菜系分类列表，二级为食谱分类列表，三级为食谱详情
    private int position1;//显示位置 仅二级使用
    private int position2;//显示位置 仅三级使用

    public MainChangeEvent(int level, int position1, int position2) {
        this.level = level;
        this.position1 = position1;
        this.position2 = position2;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPosition1() {
        return position1;
    }

    public void setPosition1(int position1) {
        this.position1 = position1;
    }

    public int getPosition2() {
        return position2;
    }

    public void setPosition2(int position2) {
        this.position2 = position2;
    }
}
