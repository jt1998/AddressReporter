package com.founder.addressreporter.runnable;

import com.alibaba.fastjson.JSON;
import com.founder.addressreporter.bean.*;
import com.founder.addressreporter.client.WebsocketClient;
import com.founder.addressreporter.service.MessageCallback;
import com.founder.addressreporter.service.REC_FBZDZService;
import com.founder.addressreporter.utils.AddressUploadValue;
import com.founder.addressreporter.utils.DataTransUtil;
import com.founder.addressreporter.utils.DateUtil;
import com.founder.addressreporter.utils.SpringContextUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.*;

/**
 * @author 姜涛
 * @create 2021-11-03 10:36
 */
@Slf4j
public class REC_FBZDZRunnableTask implements Runnable {
    private SubscribeInfo subscribeInfo;
    /*构造方法 主要是为了能够把SubscribeInfo信息传递过来*/
    private WebsocketClient websocketClient;
    /*redis hashkey值*/
    private String username;
    /*推送数量*/
    private int pointSize;
    /*注销接口httpurl*/
    private String httpUrl;


    public REC_FBZDZRunnableTask(SubscribeInfo subscribeInfo, String username, int pointSize,String httpUrl) {
        this.subscribeInfo = subscribeInfo;
        websocketClient = new WebsocketClient();
        this.pointSize = pointSize;
        this.username = username;
        this.httpUrl = httpUrl;
    }

    @Override
    public void run() {
        synchronized ("b"){
            /*非标准地址数据上传结果*/
            AddressUploadValue.fbzdzProcessValue = 2;

            websocketClient.getClient(subscribeInfo.getSource_url(), new MessageCallback() {
                /*注入REC_BZDZMapper*/
                REC_FBZDZService fbzdzService =   SpringContextUtil.getBean(REC_FBZDZService.class);
                /*注入redisTemplate*/
                RedisTemplate<String,Object> redisTemplate = SpringContextUtil.getBean("StringObject",RedisTemplate.class);
                /*建立websocket连接*/
                private WebSocketClient client;
                /*需要发送的批次*/
                private int batch = 0;
                /*总的数据量*/
                private int maxId = 0;
                /*判断是否和服务端建立链接标识*/
                private boolean connectFlag = true;
                /*分页计数器*/
                private int count = 0;
                /*标准地址 上传数量*/
                int uploadNum =  (int)redisTemplate.opsForHash().get(username, "uploadNumByFBzdz");
                /*开始id*/
                private int beginId = 0;
                /*截止id*/
                private int endId = 0;
                /*接收推送数据集合*/
                private List<New_REC_FBZDZ> fbzdzList = new ArrayList<>();
                /*每批上传的数量记录*/
                private int uploadCount = 0;
                /*每次上传的次数记录*/
                private int batchCount = 0;
                /*每次上传的时间记录*/
                private String time = DateUtil.dateToStr(new Date());


                /*得到上次的记录信息*/
                List<TransDataRecord> recordsList = ( List<TransDataRecord>)redisTemplate.opsForHash().get(username,"transDataRecordsByFBzdz");

                private void noData(){
                    System.out.println("您这段时间内的数据已经上传过,不需要重新上传,请您重新选择合适的时间段...");
                    /*进行每次上传记录信息的存储*/
                    TransDataRecord dataRecord = new TransDataRecord();
                    dataRecord.setBatch(batchCount);
                    dataRecord.setNum(uploadCount);
                    dataRecord.setTime(time);
                    /*纳入records中*/
                    recordsList.add(dataRecord);
                    /*将recordsList存放到redis中*/
                    redisTemplate.opsForHash().put(username,"transDataRecordsByFBzdz",recordsList);
                    /*记录lasttime*/
                    redisTemplate.opsForHash().put(username,"lasttimeByFBzdz",time);
                    /*记录截止id*/
                    redisTemplate.opsForHash().put(username, "idByFBzdz", maxId);
                    /*延迟5秒*/
                    try {
                        Thread.sleep(1000L * 5);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    /*非标准地址数据上传结果*/
                    AddressUploadValue.fbzdzProcessValue = 1;
                    AddressUploadValue.fbzdzProcessResult.put("result", "SUCCESS");
                    AddressUploadValue.fbzdzProcessResult.put("msg", "您这段时间内的数据已经上传过,不需要重新上传,请您重新选择合适的时间段...");

                    /*结束推送标识*/
                    Map<String, Object> FinMap = new HashMap<>();
                    FinMap.put("token", subscribeInfo.getToken());
                    FinMap.put("time", DateUtil.dateToStr(new Date()));
                    FinMap.put("messtype", "FIN");
                    String finMapJsonStr = JSON.toJSONString(FinMap);
                    if (connectFlag) {
                        sendMessage(finMapJsonStr);
                        System.out.println("这段时间没有数据,和WebSocket服务端断开链接....");
                    } else {
                        System.out.println("WebSocket服务端断开链接....");
                    }
                }

                private void oneOrLastPage(List<New_REC_FBZDZ> fbzdzList){
                    if (fbzdzList.size() == 0){
                        /*记录上传数据*/
                        uploadNum += fbzdzList.size();
                        /*redis数据重新添加*/
                        redisTemplate.opsForHash().put(username, "uploadNumByFBzdz", uploadNum);
                        /*记录截止id*/
                        redisTemplate.opsForHash().put(username, "idByFBzdz", maxId);

                        /*记录每次上传的数据*/
                        uploadCount += fbzdzList.size();
                        /*进行每次上传记录信息的存储*/
                        TransDataRecord dataRecord = new TransDataRecord();
                        dataRecord.setBatch(batchCount);
                        dataRecord.setNum(uploadCount);
                        dataRecord.setTime(time);
                        /*纳入records中*/
                        recordsList.add(dataRecord);
                        /*将recordsList存放到redis中*/
                        redisTemplate.opsForHash().put(username,"transDataRecordsByFBzdz",recordsList);
                        /*记录lasttime*/
                        redisTemplate.opsForHash().put(username,"lasttimeByFBzdz",time);
                        /*记录截止id*/
                        redisTemplate.opsForHash().put(username, "idByFBzdz", maxId);
                        /*延迟5秒*/
                        try {
                            Thread.sleep(1000L * 5);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        /*非标准地址数据上传结果*/
                        AddressUploadValue.fbzdzProcessValue = 1;
                        AddressUploadValue.fbzdzProcessResult.put("result", "SUCCESS");
                        AddressUploadValue.fbzdzProcessResult.put("msg", "数据上传完成......");

                        /*结束推送标识*/
                        Map<String, Object> FinMap = new HashMap<>();
                        FinMap.put("token", subscribeInfo.getToken());
                        FinMap.put("time", DateUtil.dateToStr(new Date()));
                        FinMap.put("messtype", "FIN");
                        String finMapJsonStr = JSON.toJSONString(FinMap);
                        if (connectFlag) {
                            sendMessage(finMapJsonStr);
                            System.out.println("数据推送完成,和WebSocket服务端断开链接....");
                        } else {
                            System.out.println("WebSocket服务端断开链接....");
                        }
                    }
                    if (fbzdzList.size() > 0){
                        /*对数据进行地址名称特殊符号处理*/
                        for (New_REC_FBZDZ fbzdz : fbzdzList) {
                            fbzdz.setDzmc(DataTransUtil.addressTranslate(fbzdz.getDzmc()));
                        }
                        /*进行上传数据*/
                        Map<String, Object> messageMap = new HashMap<>();
                        messageMap.put("token", subscribeInfo.getToken());
                        messageMap.put("time", DateUtil.dateToStr(new Date()));
                        messageMap.put("rec_type", subscribeInfo.getRec_type());
                        messageMap.put("count", fbzdzList.size() + "");
                        messageMap.put("messtype", "DATA");
                        messageMap.put("datalist", fbzdzList);
                        String jsonStr = JSON.toJSONString(messageMap);
                        if (connectFlag) {
                            sendMessage(jsonStr);
                            /*记录上传数据*/
                            uploadNum += fbzdzList.size();
                            /*redis数据重新添加*/
                            redisTemplate.opsForHash().put(username, "uploadNumByFBzdz", uploadNum);
                            /*记录截止id*/
                            redisTemplate.opsForHash().put(username, "idByFBzdz", maxId);
                            /*记录lasttime*/
                            redisTemplate.opsForHash().put(username,"lasttimeByFBzdz",time);

                            /*记录每次上传的数据*/
                            uploadCount += fbzdzList.size();
                            /*进行每次上传记录信息的存储*/
                            TransDataRecord dataRecord = new TransDataRecord();
                            dataRecord.setBatch(batchCount);
                            dataRecord.setNum(uploadCount);
                            dataRecord.setTime(time);
                            /*纳入records中*/
                            recordsList.add(dataRecord);
                            /*将recordsList存放到redis中*/
                            redisTemplate.opsForHash().put(username,"transDataRecordsByFBzdz",recordsList);

                            /*延迟5秒*/
                            try {
                                Thread.sleep(1000L * 5);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            /*非标准地址数据上传结果*/
                            AddressUploadValue.fbzdzProcessValue = 1;
                            AddressUploadValue.fbzdzProcessResult.put("result", "SUCCESS");
                            AddressUploadValue.fbzdzProcessResult.put("msg", "数据上传完成......");

                            /*结束推送标识*/
                            Map<String, Object> FinMap = new HashMap<>();
                            FinMap.put("token", subscribeInfo.getToken());
                            FinMap.put("time", DateUtil.dateToStr(new Date()));
                            FinMap.put("messtype", "FIN");
                            String finMapJsonStr = JSON.toJSONString(FinMap);
                            if (connectFlag) {
                                sendMessage(finMapJsonStr);
                                System.out.println("数据推送完成,和WebSocket服务端断开链接....");
                            } else {
                                System.out.println("WebSocket服务端断开链接....");
                            }
                        } else {
                            System.out.println("WebSocket服务端断开链接....");
                        }
                    }
                }

                private void normalData(List<New_REC_FBZDZ> fbzdzList){
                    /*对数据进行地址名称特殊符号处理*/
                    for (New_REC_FBZDZ bzdz : fbzdzList) {
                        bzdz.setDzmc(DataTransUtil.addressTranslate(bzdz.getDzmc()));
                    }
                    /*进行上传数据*/
                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("token", subscribeInfo.getToken());
                    messageMap.put("time", DateUtil.dateToStr(new Date()));
                    messageMap.put("rec_type", subscribeInfo.getRec_type());
                    messageMap.put("count", fbzdzList.size() + "");
                    messageMap.put("messtype", "DATA");
                    messageMap.put("datalist", fbzdzList);
                    String jsonStr = JSON.toJSONString(messageMap);
                    if (connectFlag) {
                        sendMessage(jsonStr);
                        /*记录上传数据*/
                        uploadNum += fbzdzList.size();
                        /*每批上传的数量*/
                        uploadCount += fbzdzList.size();
                        /*redis数据重新添加*/
                        redisTemplate.opsForHash().put(username, "uploadNumByFBzdz", uploadNum);
                        /*记录截止id*/
                        redisTemplate.opsForHash().put(username, "idByFBzdz", endId);
                    }else{
                        System.out.println("WebSocket服务端断开链接....");
                    }
                }

                /*刚建立连接时 open方法*/
                @Override
                public void open(WebSocketClient webSocketClient) {
                    this.client = webSocketClient;
                    Map<String, String> messageMap = Maps.newHashMap();
                    messageMap.put("messtype", "AUT");
                    messageMap.put("time", DateUtil.dateToStr(new Date()));
                    messageMap.put("token", subscribeInfo.getToken());
                    String s = JSON.toJSONString(messageMap);
                    if (connectFlag) {
                        /*发送消息*/
                        sendMessage(s);
                    } else {
                        System.out.println("WebSocket服务端断开链接....");
                        return;
                    }
                }
                /*接收服务端发送message onMessage方法*/
                @Override
                public void onMessage(String s) {
                    /*Json解析s*/
                    Map<String, String> resultMap = (Map<String, String>) JSON.parseObject(s, Map.class);
                    System.out.println(resultMap);
                    /*开始 判断其中是否 messtype 这个key值*/
                    if (!resultMap.containsKey("messtype")) {
                        System.out.println("不是正确的消息类型，无法进行数据推送...");

                        /*非标准地址数据上传结果*/
                        AddressUploadValue.fbzdzProcessValue = 1;
                        AddressUploadValue.fbzdzProcessResult.put("result", "SUCCESS");
                        AddressUploadValue.fbzdzProcessResult.put("msg", "messtype不是正确的消息类型，无法进行数据推送...");
                        return;
                    }
                    if ("AUT".equals(resultMap.get("messtype"))) {
                        if ("1".equals(resultMap.get("result"))) {
                            System.out.println("token认证失败，无法进行数据推送...");

                            /*非标准地址数据上传结果*/
                            AddressUploadValue.fbzdzProcessValue = 1;
                            AddressUploadValue.fbzdzProcessResult.put("result", "SUCCESS");
                            AddressUploadValue.fbzdzProcessResult.put("msg", "token认证失败，无法进行数据推送...");
                            client.close();
                            return;
                        } else {
                            /* 为了能进行主键id分页的批次*/
                            /*首先得到处理handler处理的imaxId*/
                            int maxHandleId  = (int) redisTemplate.opsForHash().get(username, "idHandlerByFBzdz");
                            int byTimeId = fbzdzService.getMaxIdByTime(subscribeInfo.getData_begintime(),subscribeInfo.getData_endtime());

                            if (maxHandleId >= byTimeId){
                                maxId = byTimeId;
                            }

                            if (maxHandleId <= byTimeId){
                                maxId = maxHandleId;
                            }
                            System.out.println("这段时间的最大id是:" + maxId);

                            int id = (int) redisTemplate.opsForHash().get(username, "idByFBzdz");
                            System.out.println("初始id"+id);
                            /*进行数据记录的写入*/
                            if (recordsList.size() == 0){
                                System.out.println("第一次进行标准地址数据推送....");
                                /*将batchCount赋值为1*/
                                batchCount = 1;
                            }else{
                                /*拿出recordsList最后一次中的count  batchCount进行赋值*/
                                int size = recordsList.size() - 1 ;
                                int batch = recordsList.get(size).getBatch();
                                batchCount = batch + 1;
                                System.out.println("第" + batchCount +"次推送标准地址数据....");
                            }

                            if (maxId - id <= 0) {
                                noData();
                                return;
                            }


                            /*计算需要发送的批次数量*/
                            batch = (maxId -id) % pointSize == 0 ? (maxId -id) / pointSize : ((maxId -id) / pointSize) + 1;
                            System.out.println("开始id--" + id + "最大id--" + maxId +"一共--" + (maxId - id)+"数据" + "--每批" + pointSize + "条--需要推送" + batch + "批");
                        }
                    }
                    /*开始推送标准地址数据*/
                    sendData();
                }

                @Override
                public void onClose() {
                    connectFlag = false;

                }

                @Override
                public void onError() {
                    connectFlag = false;
                }


                /*发送数据库数据的方法*/
                private void sendData() {
                    try {
                        count++;
                        if (count > batch) {
                            System.out.println("数据已经推送完成...");
                            client.close();
                            return;
                        }
                        /*只有一页数据*/
                        if (batch == 1) {
                            int id = (int) redisTemplate.opsForHash().get(username, "idByFBzdz");
                            /*开始id*/
                            beginId = id + 1;
                            /*截止id*/
                            endId = maxId;
                            System.out.println("第"+count+"批次数据--"+"开始keyid"+beginId+"--截止keyid"+endId);
                            /*查找数据*/
                            fbzdzList = fbzdzService.findListById(beginId, endId);
                            oneOrLastPage(fbzdzList);
                            return;
                        }

                        /*多页数据处理*/
                        if (batch > 1) {
                            /*批次小于batch*/
                            if (count < batch) {
                                int id = (int) redisTemplate.opsForHash().get(username, "idByFBzdz");
                                /*开始id*/
                                beginId = id + 1;
                                /*截止id*/
                                endId = beginId + (pointSize-1);
                                System.out.println("第"+count+"批次数据--"+"开始keyid"+beginId+"--截止keyid"+endId);
                                /*查找数据*/
                                fbzdzList = fbzdzService.findListById(beginId, endId);
                                /*数据为空*/
                                while (fbzdzList.size() == 0){
                                    System.out.println("第"+count+"批次数据--"+"开始keyid"+beginId+"--截止keyid"+endId+"--没有数据");
                                    /*记录之前的截止id*/
                                    redisTemplate.opsForHash().put(username,"idByFBzdz",endId);
                                    /*继续分页*/
                                    count++;
                                    if (count < batch){
                                        int keyId = (int) redisTemplate.opsForHash().get(username, "idByFBzdz");
                                        beginId =  keyId + 1;
                                        endId = beginId + (pointSize-1);
                                        System.out.println("第"+count+"批次数据--"+"开始keyid"+beginId+"--截止keyid"+endId);
                                        /*查找数据*/
                                        fbzdzList = fbzdzService.findListById(beginId, endId);
                                    }
                                    if (count == batch){
                                        int keyId = (int) redisTemplate.opsForHash().get(username, "idByFBzdz");
                                        beginId =  keyId + 1;
                                        endId = maxId;
                                        System.out.println("第"+count+"批次数据--"+"开始keyid"+beginId+"--截止keyid"+endId);
                                        /*查找数据*/
                                        fbzdzList = fbzdzService.findListById(beginId, endId);
                                        oneOrLastPage(fbzdzList);
                                        return;
                                    }
                                }
                                normalData(fbzdzList);
                            }

                            if (count == batch) {
                                int id = (int) redisTemplate.opsForHash().get(username, "idByFBzdz");
                                /*开始id*/
                                int beginId = id + 1;
                                /*截止id*/
                                int endId = maxId;
                                System.out.println("第"+count+"批次数据--"+"开始keyid"+beginId+"--截止keyid"+endId);
                                /*查找数据*/
                                fbzdzList = fbzdzService.findListById(beginId, endId);
                                oneOrLastPage(fbzdzList);
                                return;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                /*发送消息方法*/
                private void sendMessage(String message) {
                    client.send(message);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        log.error("出现异常;{}", e.getMessage());
                    }
                }
            });
        }
    }
}
