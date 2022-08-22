package com.founder.addressreporter.client;

import com.founder.addressreporter.service.MessageCallback;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class WebsocketClient {

    public WebSocketClient getClient(String uri, MessageCallback messageCallback) {
        try {
            //创建客户端连接对象
            WebSocketClient client = new WebSocketClient(new URI(uri), new Draft_6455()) {

                /*建立连接时调用*/
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                   if (messageCallback != null){
                       messageCallback.open(this);
                   }
                }

                @Override
                public void onMessage(String s){
                    log.info("receive>>" + s);
                    if(messageCallback!=null)
                        messageCallback.onMessage(s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("关闭连接:::" + "i = " + i + ":::s = " + s +":::b = " + b);
                    if(messageCallback!=null)
                        messageCallback.onClose();
                }

                @Override
                public void onError(Exception e) {
                    log.error("报错了:::" + e.getMessage());
                    if(messageCallback!=null)
                        messageCallback.onError();
                }
            };
            //请求与服务端建立连接
            client.connect();
            //判断连接状态，0为请求中  1为已建立  其它值都是建立失败
            while (!client.getReadyState().equals(ReadyState.OPEN)){
                try {
                    Thread.sleep(200);
                }catch (Exception e){
                    log.warn("延迟操作出现问题，但并不影响功能");
                }
                log.info("连接中......");
            }
            return client;
        }catch(URISyntaxException e) {
            log.error(e.getMessage());
        }
        return null;

    }
}
