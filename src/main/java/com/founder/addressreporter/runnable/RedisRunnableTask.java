package com.founder.addressreporter.runnable;

import com.alibaba.fastjson.JSON;
import com.founder.addressreporter.bean.AddressCheckRecord;
import com.founder.addressreporter.bean.NotifyInfo;
import com.founder.addressreporter.bean.RegisterInfo;
import com.founder.addressreporter.bean.TransDataRecord;
import com.founder.addressreporter.utils.DateUtil;
import com.founder.addressreporter.utils.HttpUtil;
import com.founder.addressreporter.utils.SpringContextUtil;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 姜涛
 * @create 2021-11-11 19:31
 */

public class RedisRunnableTask implements Runnable{

    private String username;

    public RedisRunnableTask(String username) {
        this.username = username;
    }


    @Override
    public void run() {

        try {
            /*注入redisTemplate*/
            RedisTemplate<String, Object> redisTemplate = SpringContextUtil.getBean("StringObject", RedisTemplate.class);
            /*标准地址redis存储*/
            if (!redisTemplate.opsForHash().hasKey(username,"idHandlerByBzdz")) {
                /*标准地址id*/
                redisTemplate.opsForHash().put(username, "idByBzdz", 0);
                /*标准地址 上传数量*/
                redisTemplate.opsForHash().put(username, "uploadNumByBzdz", 0);
                /*标准地址 质检 地址重复的数量*/
                redisTemplate.opsForHash().put(username, "dzmcRepeatNumByBzdz", 0);
                /*标准地址 质检 坐标不正确的数量*/
                redisTemplate.opsForHash().put(username, "zbErrorNumByBzdz", 0);
                /*标准地址 质检 坐标不在行政区划内的数量*/
                redisTemplate.opsForHash().put(username, "zbNotInXzqhNumByBzdz", 0);
                /*标准地址 质检 正常的数量*/
                redisTemplate.opsForHash().put(username, "normalNumByBzdz", 0);
                /*标准地址处理id*/
                redisTemplate.opsForHash().put(username, "idHandlerByBzdz", 0);
                /*标准地址lasttime*/
                redisTemplate.opsForHash().put(username,"lasttimeByBzdz","1970-01-01 00:00:00");
                /*每次推送数据的记录信息*/
                List<TransDataRecord> transDataRecords= new ArrayList<>();
                redisTemplate.opsForHash().put(username,"transDataRecordsByBzdz",transDataRecords);
                /*每次地址质检的记录信息*/
                List<AddressCheckRecord> addressCheckRecords = new ArrayList<>();
                redisTemplate.opsForHash().put(username,"addressCheckRecordsByBzdz",addressCheckRecords);

            }

            /*非标准地址redis存储*/
            if (!redisTemplate.opsForHash().hasKey(username,"idByFBzdz")) {
                /*非标准地址id*/
                redisTemplate.opsForHash().put(username, "idByFBzdz", 0);
                /*标准地址 上传数量*/
                redisTemplate.opsForHash().put(username, "uploadNumByFBzdz", 0);
                /*标准地址 质检 地址重复的数量*/
                redisTemplate.opsForHash().put(username, "dzmcRepeatNumByFBzdz", 0);
                /*标准地址 质检 坐标不正确的数量*/
                redisTemplate.opsForHash().put(username, "zbErrorNumByFBzdz", 0);
                /*标准地址 质检 坐标不在行政区划内的数量*/
                redisTemplate.opsForHash().put(username, "zbNotInXzqhNumByFBzdz", 0);
                /*标准地址 质检 正常的数量*/
                redisTemplate.opsForHash().put(username, "normalNumByFBzdz", 0);
                /*非标准地址处理id*/
                redisTemplate.opsForHash().put(username, "idHandlerByFBzdz", 0);
                /*非标准地址lasttime*/
                redisTemplate.opsForHash().put(username,"lasttimeByFBzdz","1970-01-01 00:00:00");
                /*每次推送数据的记录信息*/
                List<TransDataRecord> transDataRecords= new ArrayList<>();
                redisTemplate.opsForHash().put(username,"transDataRecordsByFBzdz",transDataRecords);
                /*每次地址质检的记录信息*/
                List<AddressCheckRecord> addressCheckRecords1 = new ArrayList<>();
                redisTemplate.opsForHash().put(username,"addressCheckRecordsByFBzdz",addressCheckRecords1);
            }
            System.out.println("redis初始化完成.....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
