package com.founder.addressreporter.service;

import org.java_websocket.client.WebSocketClient;

/**
 * @author 姜涛
 * @create 2021-11-03 9:47
 */
public interface MessageCallback {
    void open(WebSocketClient webSocketClient);

    void onMessage(String s);

    void onClose();

    void onError();
}
