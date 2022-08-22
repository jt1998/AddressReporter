package com.founder.addressreporter.bean;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 姜涛
 * @create 2021-11-03 9:57
 */
public class NotifyInfo implements Serializable {
    @ApiModelProperty("交互token")
    private String token;
    @ApiModelProperty("请求时间")
    private String time;
    @ApiModelProperty("资源类型，参照：C.1 资源目录")
    private String rec_type;
    @ApiModelProperty("更新说明")
    private String message;

    @ApiModelProperty("上一次数据更新时间戳")
    private String lasttime;

    public NotifyInfo(String token, String time, String rec_type, String message, String lasttime) {
        this.token = token;
        this.time = time;
        this.rec_type = rec_type;
        this.message = message;
        this.lasttime = lasttime;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }
}
