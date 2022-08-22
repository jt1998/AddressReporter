package com.founder.addressreporter.service;

import com.founder.addressreporter.bean.New_REC_FBZDZ;
import com.founder.addressreporter.bean.REC_BZDZ;
import com.founder.addressreporter.bean.REC_FBZDZ;
import com.founder.addressreporter.bean.SendData;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 姜涛
 * @create 2021-11-03 10:09
 */
public interface REC_FBZDZService {
    int getTotal(String begintime, String endtime);
    Map<String, Object> sendData(SendData sendData);
    List<New_REC_FBZDZ> findListById(int beginId, int endId);
    Integer getMaxIdByTime(String beginTime, String endTime);

    int deleteDzRepeatByBeginIdAndEndIdOnFBzdz(int beginId, int maxId);

    int deleteDzErrorByBeginIdAndEndIdOnFBzdz(int beginId, int maxId);

    int deletezbNotInXzqhByBeginIdAndEndIdOnFBzdz(int beginId, int maxId);

    int getMaxIdByHandlerId(int checkOutId);


    int getAll();

}
