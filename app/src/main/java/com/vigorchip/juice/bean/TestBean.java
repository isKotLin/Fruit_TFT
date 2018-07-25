package com.vigorchip.juice.bean;

/**
 * Created by Administrator on 2017/6/19.
 */

public class TestBean {
    private String fid;
    private String person;
    private String stime;
    private String etime;
    private String method;
    private String totletime;
    private String startSpeed;
    private String endSpeed;
    private String cur_step;
    private String totlestep;

    public TestBean(String fid, String person, String stime, String etime, String method, String totletime, String startSpeed, String endSpeed, String cur_step, String totlestep) {
        this.fid = fid;
        this.person = person;
        this.stime = stime;
        this.etime = etime;
        this.method = method;
        this.totletime = totletime;
        this.startSpeed = startSpeed;
        this.endSpeed = endSpeed;
        this.cur_step = cur_step;
        this.totlestep = totlestep;
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

    public String getStartSpeed() {
        return startSpeed;
    }

    public void setStartSpeed(String startSpeed) {
        this.startSpeed = startSpeed;
    }

    public String getEndSpeed() {
        return endSpeed;
    }

    public void setEndSpeed(String endSpeed) {
        this.endSpeed = endSpeed;
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