package com.vigorchip.juice.bean;

/**
 * Created by Administrator on 2017/2/6.
 */

public class LanguageBean {
    private String languageName;
    private boolean isChoosed;
    private boolean isClicked;

    public LanguageBean(String languageName, boolean isChoosed, boolean isClicked) {
        this.languageName = languageName;
        this.isChoosed = isChoosed;
        this.isClicked = isClicked;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}
