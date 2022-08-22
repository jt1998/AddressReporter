package com.founder.addressreporter.bean;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 姜涛
 * @create 2021-11-03 9:59
 */
public class GetTotalInfo implements Serializable {
    @ApiModelProperty("交互token")
    private String token;
    @ApiModelProperty("请求时间")
    private String time;
    @ApiModelProperty("资源类型，参照：C.1 资源目录")
    private String rec_type;
    @ApiModelProperty("数据查询范围，起始时间戳")
    private String data_begintime;

    @ApiModelProperty("数据查询范围，结束时间戳")
    private String data_endtime;

    public GetTotalInfo(String token, String time, String rec_type, String data_begintime, String data_endtime) {
        this.token = token;
        this.time = time;
        this.rec_type = rec_type;
        this.data_begintime = data_begintime;
        this.data_endtime = data_endtime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRec_type() {
        return rec_type;
    }

    public void setRec_type(String rec_type) {
        this.rec_type = rec_type;
    }

    public String getData_begintime() {
        return data_begintime;
    }

    public void setData_begintime(String data_begintime) {
        this.data_begintime = data_begintime;
    }

    public String getData_endtime() {
        return data_endtime;
    }

    public void setData_endtime(String data_endtime) {
        this.data_endtime = data_endtime;
    }
}
