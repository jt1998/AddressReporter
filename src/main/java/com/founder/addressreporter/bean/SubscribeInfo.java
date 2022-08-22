package com.founder.addressreporter.bean;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 姜涛
 * @create 2021-11-03 10:01
 */
public class SubscribeInfo implements Serializable {
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
    @ApiModelProperty("上级平台资源服务websokcet地址")
    private String source_url;

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

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
}
