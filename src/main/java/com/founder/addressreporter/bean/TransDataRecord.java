package com.founder.addressreporter.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 姜涛
 * @create 2021-11-15 9:21
 */
public class TransDataRecord{

    private  static final long serialVersionUID = 101L;
    /*每次上传的数量*/
    private int num;
    /*上传的次数*/
    private int batch;
    /*上传的时间*/
    private String time;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TransDataRecord{" +
                "num=" + num +
                ", batch=" + batch +
                ", time='" + time + '\'' +
                '}';
    }
}
