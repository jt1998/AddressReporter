package com.founder.addressreporter.bean;

import java.io.Serializable;

/**
 * @author 姜涛
 * @create 2022-08-22 9:07
 */
public class AddressCheckRecord implements Serializable {
    private  static final long serialVersionUID = 100L;
    private Integer batch;
    private Integer dzmcRepeatNum;
    private Integer zbErrorNum;
    private Integer zbNotInXzqhNum;
    private String time;

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public Integer getDzmcRepeatNum() {
        return dzmcRepeatNum;
    }

    public void setDzmcRepeatNum(Integer dzmcRepeatNum) {
        this.dzmcRepeatNum = dzmcRepeatNum;
    }

    public Integer getZbErrorNum() {
        return zbErrorNum;
    }

    public void setZbErrorNum(Integer zbErrorNum) {
        this.zbErrorNum = zbErrorNum;
    }

    public Integer getZbNotInXzqhNum() {
        return zbNotInXzqhNum;
    }

    public void setZbNotInXzqhNum(Integer zbNotInXzqhNum) {
        this.zbNotInXzqhNum = zbNotInXzqhNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "AddressCheckRecord{" +
                "batch=" + batch +
                ", dzmcRepeatNum=" + dzmcRepeatNum +
                ", zbErrorNum=" + zbErrorNum +
                ", zbNotInXzqhNum=" + zbNotInXzqhNum +
                ", time='" + time + '\'' +
                '}';
    }
}
