package com.founder.addressreporter.schedule;

import com.founder.addressreporter.bean.AddressCheckRecord;
import com.founder.addressreporter.service.REC_BZDZService;
import com.founder.addressreporter.service.REC_ErrorDataService;
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
 * @create 2021-11-15 16:29
 */

@Slf4j
public class Rec_BZDZTask implements Runnable {

    private REC_BZDZService bzdzService;

    private RedisTemplate<String,Object> redisTemplate;

    private REC_ErrorDataService errorDataService;

    private String username;

    private String beginTime;

    private String endTime;

    /*时间信息*/
    private String time = DateUtil.dateToStr(new Date());



    public Rec_BZDZTask(String username,String beginTime,String endTime) {
        this.bzdzService = SpringContextUtil.getBean(REC_BZDZService.class);
        this.redisTemplate = SpringContextUtil.getBean("StringObject",RedisTemplate.class);
        this.username = username;
        this.errorDataService = SpringContextUtil.getBean(REC_ErrorDataService.class);
        this.beginTime = beginTime;
        this.endTime = endTime;
    }


    @Override
    public void run() {

        /*标准地址数据处理中*/
        ProcessValue.bzdzProcessValue = 2;
        /*记录地址质检信息*/
        List<AddressCheckRecord> addressCheckRecords = (List<AddressCheckRecord>) redisTemplate.opsForHash().get(username, "addressCheckRecordsByBzdz");

            System.out.println("标准地址数据处理启动...");
            /*标准地址 质检 地址重复的数量*/
            int dzmcRepeatNum = (int) redisTemplate.opsForHash().get(username, "dzmcRepeatNumByBzdz");
            /*标准地址 质检 坐标不正确的数量*/
            int zbErrorNum = (int) redisTemplate.opsForHash().get(username, "zbErrorNumByBzdz");
            /*标准地址 质检 坐标不在行政区划内的数量*/
            int zbNotInXzqhNum = (int) redisTemplate.opsForHash().get(username, "zbNotInXzqhNumByBzdz");

            /*得到每次的数据处理的开始时间和id*/
            /*开始id*/
            int beginId = (int) redisTemplate.opsForHash().get(username, "idHandlerByBzdz");

            /*查询给定时间内的最大maxid*/
            Integer id = bzdzService.getMaxIdByTime(beginTime, endTime);


            if (id == null) {
                System.out.println("在" + beginTime + "和" + endTime + "中没有数据,请您核对时间段...");

                /*标准地址数据处理结果*/
                ProcessValue.bzdzProcessValue = 1;
                ProcessValue.bzdzProcessResult.put("result", "SUCCESS");
                ProcessValue.bzdzProcessResult.put("msg", "在" + beginTime + "和" + endTime + "中没有数据,请您核对时间段...");

                /*记录地址质检信息list*/
                recordInfo(addressCheckRecords,0,0,0);
                return;
            }
            /*截止id*/
            int maxId = id.intValue();
            if (maxId - beginId <= 0) {
                System.out.println("这段时间的数据已经处理校验过了...");

                /*标准地址数据处理结果*/
                ProcessValue.bzdzProcessValue = 1;
                ProcessValue.bzdzProcessResult.put("result", "SUCCESS");
                ProcessValue.bzdzProcessResult.put("msg", "在" + beginTime + "和" + endTime + "这段时间的数据已经处理校验过了...");

                /*记录地址质检信息list*/
                recordInfo(addressCheckRecords,0,0,0);
                return;

            }

            /*记录截止id*/
            redisTemplate.opsForHash().put(username, "idHandlerByBzdz", maxId);

            /*直接在为分批之前 处理地址重复的数据*/
            /*开始进行数据的插入容错表*/
            errorDataService.insertDzRepeatErrorDataByBeginIdAndEndIdOnBzdz(beginId, maxId);
            /*根据keyId删除数据*/
            int deleRow1 = bzdzService.deleteDzRepeatByBeginIdAndEndIdOnBzdz(beginId, maxId);
            /*地址名称重复数量进行累加*/
            dzmcRepeatNum += deleRow1;
            System.out.println("地址名称重复数据处理结束....");

            /*地址坐标不正确*/
            errorDataService.insertDzErrorDataByBeginIdAndEndIdOnBzdz(beginId, maxId);
            int deleRow2 = bzdzService.deleteDzErrorByBeginIdAndEndIdOnBzdz(beginId, maxId);
            zbErrorNum += deleRow2;
            System.out.println("地址坐标不正确处理结束....");

            /*地址坐标不在行政区划范围内*/
            errorDataService.insertzbNotInXzqhDataByBeginIdAndEndIdOnBzdz(beginId, maxId);
            int deleRow3 = bzdzService.deletezbNotInXzqhByBeginIdAndEndIdOnBzdz(beginId, maxId);
            zbNotInXzqhNum += deleRow3;
            System.out.println("地址坐标不在行政区划范围内的数据处理结束...");

            /*将数据进行再次填充*/
            /*标准地址 质检 地址重复的数量*/
            redisTemplate.opsForHash().put(username, "dzmcRepeatNumByBzdz", dzmcRepeatNum);
            /*标准地址 质检 坐标不正确的数量*/
            redisTemplate.opsForHash().put(username, "zbErrorNumByBzdz", zbErrorNum);
            /*标准地址 质检 坐标不在行政区划内的数量*/
            redisTemplate.opsForHash().put(username, "zbNotInXzqhNumByBzdz", zbNotInXzqhNum);
            /*标准地址 质检 正常的数量*/
            int normal = bzdzService.getAll();
            redisTemplate.opsForHash().put(username, "normalNumByBzdz", normal);

            System.out.println("标准地址数据处理结束....");

            /*标准地址数据处理结果*/
            ProcessValue.bzdzProcessValue = 1;
            ProcessValue.bzdzProcessResult.put("result", "SUCCESS");
            ProcessValue.bzdzProcessResult.put("msg", "在" + beginTime + "和" + endTime + "这段时间的数据处理完成...");

           /*记录地址质检信息list*/
             recordInfo(addressCheckRecords,dzmcRepeatNum,zbErrorNum,zbNotInXzqhNum);

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
        redisTemplate.opsForHash().put(username,"addressCheckRecordsByBzdz",addressCheckRecords);

    }
}
