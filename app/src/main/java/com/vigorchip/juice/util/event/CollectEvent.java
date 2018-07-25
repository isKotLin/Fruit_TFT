package com.vigorchip.juice.util.event;

/**
 * Created by Administrator on 2017/7/25.
 */

public class CollectEvent {
    private boolean isCollect;
    private String foodId;

    public CollectEvent(boolean isCollect, String foodId) {
        this.isCollect = isCollect;
        this.foodId = foodId;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
