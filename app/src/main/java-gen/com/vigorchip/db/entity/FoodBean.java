package com.vigorchip.db.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * auto greenDao generate javaBean by MonkeyKing
 */
public class FoodBean {

    private Long id;
    private String count;
    private String cur_step;
    private String fid;
    private String name;
    private String person;
    private String totlestep;
    private String unit;

    public FoodBean() {
    }

    public FoodBean(Long id) {
        this.id = id;
    }

    public FoodBean(Long id, String count, String cur_step, String fid, String name, String person, String totlestep, String unit) {
        this.id = id;
        this.count = count;
        this.cur_step = cur_step;
        this.fid = fid;
        this.name = name;
        this.person = person;
        this.totlestep = totlestep;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCur_step() {
        return cur_step;
    }

    public void setCur_step(String cur_step) {
        this.cur_step = cur_step;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTotlestep() {
        return totlestep;
    }

    public void setTotlestep(String totlestep) {
        this.totlestep = totlestep;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
