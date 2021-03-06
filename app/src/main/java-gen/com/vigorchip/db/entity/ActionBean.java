package com.vigorchip.db.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * auto greenDao generate javaBean by MonkeyKing
 */
public class ActionBean {

    private Long id;
    private String fid;
    private String person;
    private String stime;
    private String etime;
    private String method;
    private String totletime;
    private String startspeed;
    private String endspeed;
    private String cur_step;
    private String totlestep;

    public ActionBean() {
    }

    public ActionBean(Long id) {
        this.id = id;
    }

    public ActionBean(Long id, String fid, String person, String stime, String etime, String method, String totletime, String startspeed, String endspeed, String cur_step, String totlestep) {
        this.id = id;
        this.fid = fid;
        this.person = person;
        this.stime = stime;
        this.etime = etime;
        this.method = method;
        this.totletime = totletime;
        this.startspeed = startspeed;
        this.endspeed = endspeed;
        this.cur_step = cur_step;
        this.totlestep = totlestep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTotletime() {
        return totletime;
    }

    public void setTotletime(String totletime) {
        this.totletime = totletime;
    }

    public String getStartspeed() {
        return startspeed;
    }

    public void setStartspeed(String startspeed) {
        this.startspeed = startspeed;
    }

    public String getEndspeed() {
        return endspeed;
    }

    public void setEndspeed(String endspeed) {
        this.endspeed = endspeed;
    }

    public String getCur_step() {
        return cur_step;
    }

    public void setCur_step(String cur_step) {
        this.cur_step = cur_step;
    }

    public String getTotlestep() {
        return totlestep;
    }

    public void setTotlestep(String totlestep) {
        this.totlestep = totlestep;
    }

}
