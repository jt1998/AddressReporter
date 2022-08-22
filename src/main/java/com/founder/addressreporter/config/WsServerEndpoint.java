package com.founder.addressreporter.config;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 姜涛
 * @create 2022-08-17 10:47
 */

@ServerEndpoint("/ws")
@Component
public class WsServerEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("链接成功");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("链接关闭");
    }

    @OnMessage
    public void onMessage(Session session, String text) throws IOException, IOException, InterruptedException {
        Map<String,Object> resultMap = new HashMap<>();
        System.out.println("收到消息：" + text);
        Map<String, String> object = JSON.parseObject(text, Map.class);
        if ("AUT".equals(object.get("messtype"))) {
            resultMap.put("messtype","AUT");
            resultMap.put("result","0");
            session.getBasicRemote().sendText(JSON.toJSONString(resultMap));

        }else{
            resultMap.put("messtype","data");
            resultMap.put("result","0");
            session.getBasicRemote().sendText(JSON.toJSONString(resultMap));

        }


    }
}
