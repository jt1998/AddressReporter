package com.founder.addressreporter.schedule;

import com.founder.addressreporter.bean.AddressCheckRecord;
import com.founder.addressreporter.service.REC_ErrorDataService;
import com.founder.addressreporter.service.REC_FBZDZService;
import com.founder.addressreporter.utils.DateUtil;
import com.founder.addressreporter.utils.ProcessValue;
import com.founder.addressreporter.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author 姜涛
 * @create 2021-11-15 16:44
 */

@Slf4j
public class Rec_FBZDZTask implements Runnable {

    private REC_FBZDZService  fbzdzService;

    private RedisTemplate<String,Object> redisTemplate;

    private REC_ErrorDataService errorDataService;

    private String username;

    private String beginTime;

    private String endTime;

    /*时间*/
    private String time = DateUtil.dateToStr(new Date());

    public Rec_FBZDZTask(String username,String beginTime,String endTime) {
        this.fbzdzService = SpringContextUtil.getBean(REC_FBZDZService.class);
        this.redisTemplate = SpringContextUtil.getBean("StringObject",RedisTemplate.class);
        this.username = username;
        this.errorDataService = SpringContextUtil.getBean(REC_ErrorDataService.class);
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    /*非标准地址数据治理*/
    @Override
    public void  run() {

        /*非标准地址数据处理结果 代表处理中*/
        ProcessValue.fbzdzProcessValue = 2;
        /*记录地址质检信息*/
        List<AddressCheckRecord> addressCheckRecords = (List<AddressCheckRecord>) redisTemplate.opsForHash().get(username, "addressCheckRecordsByFBzdz");


        System.out.println("非标准地址数据处理启动...");
        /*标准地址 质检 地址重复的数量*/
        int dzmcRepeatNum = (int) redisTemplate.opsForHash().get(username, "dzmcRepeatNumByFBzdz");
        /*标准地址 质检 坐标不正确的数量*/
        int zbErrorNum = (int) redisTemplate.opsForHash().get(username, "zbErrorNumByFBzdz");
        /*标准地址 质检 坐标不在行政区划内的数量*/
        int zbNotInXzqhNum = (int) redisTemplate.opsForHash().get(username, "zbNotInXzqhNumByFBzdz");

        /*得到每次开始的时间和id*/
        /*id*/
        int beginId = (int) redisTemplate.opsForHash().get(username, "idHandlerByFBzdz");
        /*查询给定一段时间内的最大maxid*/
        Integer id = fbzdzService.getMaxIdByTime(beginTime, endTime);
        if (id == null) {
            System.out.println("在" + beginTime + "和" + endTime + "中没有数据,请您核对时间段...");

            /*非标准地址数据处理结果*/
            ProcessValue.fbzdzProcessValue = 1;
            ProcessValue.fbzdzProcessResult.put("result", "SUCCESS");
            ProcessValue.fbzdzProcessResult.put("msg", "在" + beginTime + "和" + endTime + "中没有数据,请您核对时间段...");

            /*记录地址质检信息list*/
            recordInfo(addressCheckRecords,0,0,0);
            return;

        }
        int maxId = id.intValue();
        if (maxId - beginId <= 0) {
            System.out.println("这段时间的数据已经处理校验过了...");
            /*非标准地址数据处理结果*/
            ProcessValue.fbzdzProcessValue = 1;
            ProcessValue.fbzdzProcessResult.put("result", "SUCCESS");
            ProcessValue.fbzdzProcessResult.put("msg", "在" + beginTime + "和" + endTime + "这段时间的数据已经处理校验过了...");

            /*记录地址质检信息list*/
            recordInfo(addressCheckRecords,0,0,0);
            return;
        }
        /*记录截止id*/
        redisTemplate.opsForHash().put(username, "idHandlerByFBzdz", maxId);

        /*直接在为分批之前 处理地址重复的数据*/
        /*开始进行数据的插入容错表*/
        errorDataService.insertDzRepeatErrorDataByBeginIdAndEndIdOnFBzdz(beginId, maxId);
        /*根据keyId删除数据*/
        int deleRow1 = fbzdzService.deleteDzRepeatByBeginIdAndEndIdOnFBzdz(beginId, maxId);
        /*地址名称重复数量进行累加*/
        dzmcRepeatNum += deleRow1;
        System.out.println("地址名称重复数据处理结束....");

        /*地址坐标不正确*/
        errorDataService.insertDzErrorDataByBeginIdAndEndIdOnFBzdz(beginId, maxId);
        int deleRow2 = fbzdzService.deleteDzErrorByBeginIdAndEndIdOnFBzdz(beginId, maxId);
        zbErrorNum += deleRow2;
        System.out.println("地址坐标不正确处理结束....");

        /*地址坐标不在行政区划范围内*/
        errorDataService.insertzbNotInXzqhDataByBeginIdAndEndIdOnFBzdz(beginId, maxId);
        int deleRow3 = fbzdzService.deletezbNotInXzqhByBeginIdAndEndIdOnFBzdz(beginId, maxId);
        zbNotInXzqhNum += deleRow3;
        System.out.println("地址坐标不在行政区划范围内的数据处理结束...");

        /*将数据进行再次填充*/
        /*标准地址 质检 地址重复的数量*/
        redisTemplate.opsForHash().put(username, "dzmcRepeatNumByFBzdz", dzmcRepeatNum);
        /*标准地址 质检 坐标不正确的数量*/
        redisTemplate.opsForHash().put(username, "zbErrorNumByFBzdz", zbErrorNum);
        /*标准地址 质检 坐标不在行政区划内的数量*/
        redisTemplate.opsForHash().put(username, "zbNotInXzqhNumByFBzdz", zbNotInXzqhNum);
        /*标准地址 质检 正常的数量*/
        int normal = fbzdzService.getAll();
        redisTemplate.opsForHash().put(username, "normalNumByFBzdz", normal);
        System.out.println("非标准地址数据处理结束....");

        /*非标准地址数据处理结果*/
        ProcessValue.fbzdzProcessValue = 1;
        ProcessValue.fbzdzProcessResult.put("result", "SUCCESS");
        ProcessValue.fbzdzProcessResult.put("msg", "在" + beginTime + "和" + endTime + "这段时间的数据处理完成...");

        /*记录地址质检信息list*/
        recordInfo(addressCheckRecords,dzmcRepeatNum,zbErrorNum,zbNotInXzqhNum);
        return;
    }



    /*记录地址质检信息list*/
    public void recordInfo(List<AddressCheckRecord> addressCheckRecords,Integer dzmcRepeatNum,Integer zbErrorNum,Integer zbNotInXzqhNum){
        if (addressCheckRecords.size() == 0 || addressCheckRecords == null){
            System.out.println("标准地址数据第一次质检.....");
            AddressCheckRecord record = new AddressCheckRecord();
            record.setBatch(1);
            record.setTime(time);
            record.setDzmcRepeatNum(dzmcRepeatNum);
            record.setZbErrorNum(zbErrorNum);
            record.setZbNotInXzqhNum(zbNotInXzqhNum);
            addressCheckRecords.add(record);
        }else {
            Integer batch = (addressCheckRecords.get(addressCheckRecords.size() - 1).getBatch()) + 1;
            AddressCheckRecord record = new AddressCheckRecord();
            record.setBatch(batch);
            record.setTime(time);
            record.setDzmcRepeatNum(dzmcRepeatNum);
            record.setZbErrorNum(zbErrorNum);
            record.setZbNotInXzqhNum(zbNotInXzqhNum);
            addressCheckRecords.add(record);
            System.out.println("标准地址数据第" + batch +  "次质检.....");
        }
        redisTemplate.opsForHash().put(username,"addressCheckRecordsByFBzdz",addressCheckRecords);
    }
}
