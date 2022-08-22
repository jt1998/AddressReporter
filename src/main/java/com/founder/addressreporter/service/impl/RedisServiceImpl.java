package com.founder.addressreporter.service.impl;

import com.founder.addressreporter.bean.AddressCheckRecord;
import com.founder.addressreporter.bean.TransDataRecord;
import com.founder.addressreporter.service.RedisService;
import com.founder.addressreporter.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 姜涛
 * @create 2021-11-04 11:27
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Value(value = "${user.usernamepy}")
    private String username;
    @Resource(name = "StringObject")
    private RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> getRedisInfo() {
        /*返回结果map*/
        Map<String, Object> resultMap = new HashMap<>();
        /*redis结果map*/
        Map<String, Map<String, Object>> dataMap = new HashMap<>();
        /*标准地址map*/
        Map<String, Object> bzdzMap = new HashMap<>();
        /*非标准地址map*/
        Map<String, Object> fbzdzMap = new HashMap<>();
        try {
            /*标准地址*/
            if (redisTemplate.opsForHash().hasKey(username,"idByBzdz")){
                /*推送开始id*/
                int id = (int) redisTemplate.opsForHash().get(username, "idByBzdz");
                bzdzMap.put("idByBzdz", id);
                /*校验开始id*/
                int idHandlerByBzdz = (int) redisTemplate.opsForHash().get(username, "idHandlerByBzdz");
                bzdzMap.put("idHandlerByBzdz", idHandlerByBzdz);
                /*lasttime*/
                String lasttime = (String) redisTemplate.opsForHash().get(username,"lasttimeByBzdz");
                bzdzMap.put("lasttimeByBzdz",lasttime);

                /*推送数据记录信息*/
                List<TransDataRecord> transDataRecordsByBzdz = (List<TransDataRecord>) redisTemplate.opsForHash().get(username, "transDataRecordsByBzdz");
                bzdzMap.put("transDataRecordsByBzdz",transDataRecordsByBzdz);
                /*标准地址 质检 地址重复的数量*/
                int dzmcRepeatNum = (int) redisTemplate.opsForHash().get(username, "dzmcRepeatNumByBzdz");
                bzdzMap.put("dzmcRepeatNumByBzdz", dzmcRepeatNum);
                /*标准地址 质检 坐标不正确的数量*/
                int zbErrorNum = (int) redisTemplate.opsForHash().get(username, "zbErrorNumByBzdz");
                bzdzMap.put("zbErrorNumByBzdz", zbErrorNum);
                /*标准地址 质检 坐标不在行政区划内的数量*/
                int zbNotInXzqhNum = (int) redisTemplate.opsForHash().get(username, "zbNotInXzqhNumByBzdz");
                bzdzMap.put("zbNotInXzqhNumByBzdz", zbNotInXzqhNum);
                /*标准地址 质检 正常的数量*/
                int normalNum = (int) redisTemplate.opsForHash().get(username, "normalNumByBzdz");
                bzdzMap.put("normalNumByBzdz", normalNum);
                /*标准地址 上传数量*/
                int uploadNum = (int) redisTemplate.opsForHash().get(username, "uploadNumByBzdz");
                bzdzMap.put("uploadNumByBzdz", uploadNum);
                /*地址质检信息*/
                List<AddressCheckRecord> addressCheckRecords = (List<AddressCheckRecord>) redisTemplate.opsForHash().get(username, "addressCheckRecordsByBzdz");
                bzdzMap.put("addressCheckRecords", addressCheckRecords);
                dataMap.put("bzdz", bzdzMap);
            }


            /*非标准地址*/
            if (redisTemplate.opsForHash().hasKey(username,"idByFBzdz")){
                /*推送开始id*/
                int idByFBzdz = (int) redisTemplate.opsForHash().get(username, "idByFBzdz");
                fbzdzMap.put("idByFBzdz", idByFBzdz);
                /*校验开始id*/
                int idHandlerByFBzdz = (int) redisTemplate.opsForHash().get(username, "idHandlerByFBzdz");
                fbzdzMap.put("idHandlerByFBzdz", idHandlerByFBzdz);
                /*lasttime*/
                String lasttimeByFBzdz = (String) redisTemplate.opsForHash().get(username, "lasttimeByFBzdz");
                fbzdzMap.put("lasttimeByFBzdz",lasttimeByFBzdz);

                /*推送数据记录信息*/
                List<TransDataRecord> transDataRecordsByFBzdz = (List<TransDataRecord>) redisTemplate.opsForHash().get(username, "transDataRecordsByFBzdz");
                fbzdzMap.put("transDataRecordsByFBzdz",transDataRecordsByFBzdz);

                /*非标准地址 质检 地址重复的数量*/
                int dzmcRepeatNum1 = (int) redisTemplate.opsForHash().get(username, "dzmcRepeatNumByFBzdz");
                fbzdzMap.put("dzmcRepeatNumByFBzdz", dzmcRepeatNum1);
                /*非标准地址 质检 坐标不正确的数量*/
                int zbErrorNum1 = (int) redisTemplate.opsForHash().get(username, "zbErrorNumByFBzdz");
                fbzdzMap.put("zbErrorNumByFBzdz", zbErrorNum1);
                /*非标准地址 质检 坐标不在行政区划内的数量*/
                int zbNotInXzqhNum1 = (int) redisTemplate.opsForHash().get(username, "zbNotInXzqhNumByFBzdz");
                fbzdzMap.put("zbNotInXzqhNumByFBzdz", zbNotInXzqhNum1);
                /*非标准地址 质检 正常的数量*/
                int normalNum1 = (int) redisTemplate.opsForHash().get(username, "normalNumByFBzdz");
                fbzdzMap.put("normalNumByFBzdz", normalNum1);
                /*非标准地址 上传数量*/
                int uploadNum1 = (int) redisTemplate.opsForHash().get(username, "uploadNumByFBzdz");
                fbzdzMap.put("uploadNumByFBzdz", uploadNum1);
                /*地址质检信息*/
                List<AddressCheckRecord> addressCheckRecords1 = (List<AddressCheckRecord>) redisTemplate.opsForHash().get(username, "addressCheckRecordsByFBzdz");
                fbzdzMap.put("addressCheckRecords", addressCheckRecords1);
                dataMap.put("fbzdz", fbzdzMap);
            }


            /*token信息存储*/
            if (redisTemplate.opsForHash().hasKey(username,"token")){
                String token = (String) redisTemplate.opsForHash().get(username, "token");
                HashMap<String, Object> map = new HashMap<>();
                map.put("token",token);
                dataMap.put("token",map);
            }
            resultMap.put("result", "0");
            resultMap.put("time", DateUtil.dateToStr(new Date()));
            resultMap.put("data", dataMap);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("result", "1");
        resultMap.put("time", DateUtil.dateToStr(new Date()));
        resultMap.put("data", "");
        return resultMap;
    }

    @Override
    public Map<String, String> deleteRedis() {
        Map<String, String> resultMap = new HashMap<>();
        try {
            /*删除标准地址的redis数据-----赋值默认值*/
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
            /*标准地址lasttime*/
            redisTemplate.opsForHash().put(username,"lasttimeByBzdz","1970-01-01 00:00:00");
            /*每次推送数据的记录信息*/
            List<TransDataRecord> transDataRecords= new ArrayList<>();
            redisTemplate.opsForHash().put(username,"transDataRecordsByBzdz",transDataRecords);
            /*每次地址质检的记录信息*/
            List<AddressCheckRecord> addressCheckRecords = new ArrayList<>();
            redisTemplate.opsForHash().put(username,"addressCheckRecordsByBzdz",addressCheckRecords);


            /*删除非标准地址的redis数据-----赋值默认值*/
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

            /*非标准地址lasttime*/
            redisTemplate.opsForHash().put(username,"lasttimeByFBzdz","1970-01-01 00:00:00");
            /*每次推送数据的记录信息*/
            List<TransDataRecord> transDataRecords1= new ArrayList<>();
            redisTemplate.opsForHash().put(username,"transDataRecordsByFBzdz",transDataRecords1);
            /*每次地址质检的记录信息*/
            List<AddressCheckRecord> addressCheckRecords1 = new ArrayList<>();
            redisTemplate.opsForHash().put(username,"addressCheckRecordsByFBzdz",addressCheckRecords1);

            /*删除token*/
            if (redisTemplate.opsForHash().hasKey(username,"token")){
                redisTemplate.opsForHash().delete(username,"token");
            }
            resultMap.put("result", "0");
            resultMap.put("time", DateUtil.dateToStr(new Date()));
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("result", "1");
        resultMap.put("time", DateUtil.dateToStr(new Date()));
        return resultMap;
    }
}
