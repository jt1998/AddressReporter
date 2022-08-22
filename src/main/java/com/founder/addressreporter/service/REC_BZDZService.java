package com.founder.addressreporter.service;

import com.founder.addressreporter.bean.New_REC_BZDZ;
import com.founder.addressreporter.bean.REC_BZDZ;
import com.founder.addressreporter.bean.SendData;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 姜涛
 * @create 2021-11-03 10:09
 */
public interface REC_BZDZService {
    int getTotal(String begintime, String endtime);
    Map<String, Object> sendData(SendData sendData);
    List<New_REC_BZDZ> findListById(int beginId, int endId);
    Integer getMaxIdByTime(String beginTime,String endTime);

    int getMaxIdByHandlerId(int checkOutId);

    int deleteDzRepeatByBeginIdAndEndIdOnBzdz(int beginId, int maxId);

    int deleteDzErrorByBeginIdAndEndIdOnBzdz(int beginId, int maxId);

    int deletezbNotInXzqhByBeginIdAndEndIdOnBzdz(int beginId, int maxId);

    int getAll();

}
