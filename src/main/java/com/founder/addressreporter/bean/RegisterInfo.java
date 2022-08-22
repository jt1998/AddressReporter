package com.founder.addressreporter.bean;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author 姜涛
 * @create 2021-11-03 9:53
 */
/*用户注册信息*/
public class RegisterInfo implements Serializable {
    @ApiModelProperty(value = "认证用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "地址服务接口url地址")
    private String addrserverurl;
    @ApiModelProperty(value = "备注")
    private String remark;

    public RegisterInfo(String username, String password, String addrserverurl, String remark) {
        this.username = username;
        this.password = password;
        this.addrserverurl = addrserverurl;
        this.remark = remark;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddrserverurl() {
        return addrserverurl;
    }

    public void setAddrserverurl(String addrserverurl) {
        this.addrserverurl = addrserverurl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
    }
}
