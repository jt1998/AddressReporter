package com.founder.addressreporter.bean;

import java.io.Serializable;

/**
 * @author 姜涛
 * @create 2021-11-03 9:55
 */
public class CancelInfo implements Serializable {
    private String token;
    private String message;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CancelInfo(String token, String message) {
        this.token = token;
        this.message = message;
    }
}
