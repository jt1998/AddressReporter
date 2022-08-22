package com.founder.addressreporter.service.impl;

import com.alibaba.fastjson.JSON;
import com.founder.addressreporter.bean.New_REC_BZDZ;
import com.founder.addressreporter.bean.REC_BZDZ;
import com.founder.addressreporter.bean.REC_ErrorData;
import com.founder.addressreporter.bean.SendData;
import com.founder.addressreporter.mapper.REC_BZDZMapper;
import com.founder.addressreporter.service.QXZBService;
import com.founder.addressreporter.service.REC_BZDZService;
import com.founder.addressreporter.service.REC_ErrorDataService;
import com.founder.addressreporter.utils.DataTransUtil;
import com.founder.addressreporter.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 姜涛
 * @create 2021-11-03 10:09
 */
@Service
public class REC_BZDZServiceImpl implements REC_BZDZService {
    @Autowired
    private REC_BZDZMapper bzdzMapper;
    @Autowired
    private REC_ErrorDataService errorDataService;
    @Autowired
    private QXZBService qxzbService;

    @Override
    public int getTotal(String begintime, String endtime) {
        int count  = bzdzMapper.getTotal(begintime,endtime);
        return count;
    }
    @Override
    public Map<String, Object> sendData(SendData sendData) {
        Map<String, Object> resultMap  = new HashMap<>();
        /*失败个数*/
        int failnum = 0;
        /*存放dzmc 重复map*/
        Map<String, String> repeatMap = new HashMap<>();
        /*存dzmc map*/
        Set<String> repeatSet = new HashSet<>();
       try {
           String datalist = sendData.getDatalist();
           List<REC_BZDZ> bzdzList = JSON.parseArray(datalist, REC_BZDZ.class);
           Iterator<REC_BZDZ> iterator = bzdzList.iterator();
           while (iterator.hasNext()) {
               REC_BZDZ bzdz = iterator.next();
               if (repeatMap.containsKey(bzdz.getDzmc())) {
                   repeatSet.add(bzdz.getDzmc());
               } else {
                   repeatMap.put(bzdz.getDzmc(), "1");
               }
           }
           if (repeatSet.size() != 0) {
               for (String dzmc : repeatSet) {
                   List<REC_BZDZ> newList = bzdzList.stream().filter(bzdz -> dzmc.equals(bzdz.getDzmc())).collect(Collectors.toList());
                   /*得到重复数据的个数*/
                   failnum = failnum + newList.size();
                   for (REC_BZDZ bzdz : newList) {
                       REC_ErrorData errorData = new REC_ErrorData();
                       errorData.setDzdm(bzdz.getDzbm());
                       errorData.setDzmc(bzdz.getDzmc());
                       errorData.setErrorFlag("03");
                       errorData.setErrorFlagStr("地址名称重复");
                       errorData.setRksj(DateUtil.strToDate(DateUtil.dateToStr(new Date())));
                       errorData.setX(bzdz.getZxdhzb());
                       errorData.setY(bzdz.getZxdzzb());
                       errorData.setSource("01");
                       errorData.setSourceStr("标准地址");
                       int num = errorDataService.insertErrorData(errorData);
                   }
                   bzdzList.removeAll(newList);
               }
           }
               /*判断重复地址数据之后，继续判断 地址格式不正确的地址数据*/
               if (bzdzList.size() == 0) {
                   System.out.println("传输来的数据中都是重复数据...不能在标准地址数据表中增添新数据...");
                   /*置零*/
                   repeatMap.clear();
                   repeatSet.clear();
                   resultMap.put("failnum", failnum);
                   resultMap.put("successnum", 0);
                   resultMap.put("message", "OK");
                   resultMap.put("result", "0");
                   return resultMap;
               } else {
                   Iterator<REC_BZDZ> iterator1 = bzdzList.iterator();
                   while (iterator1.hasNext()) {
                       REC_BZDZ bzdz =  iterator1.next();
                       BigDecimal x = bzdz.getZxdhzb();
                       int xInt = x.setScale(0, BigDecimal.ROUND_DOWN).intValue();
                       BigDecimal y = bzdz.getZxdzzb();
                       int yInt = y.setScale(0, BigDecimal.ROUND_DOWN).intValue();
                       /*验证x,y坐标的正确格式,比较x,y的整数位的值*/
                       if (xInt <= 0 || xInt >= 180 || yInt <= 0 || yInt >= 90) {
                           REC_ErrorData errorData = new REC_ErrorData();
                           errorData.setDzdm(bzdz.getDzbm());
                           errorData.setDzmc(bzdz.getDzmc());
                           errorData.setErrorFlag("01");
                           errorData.setErrorFlagStr("经纬度格式不合格");
                           errorData.setRksj(DateUtil.strToDate(DateUtil.dateToStr(new Date())));
                           errorData.setX(bzdz.getZxdhzb());
                           errorData.setY(bzdz.getZxdzzb());
                           errorData.setSource("01");
                           errorData.setSourceStr("标准地址");
                           int num = errorDataService.insertErrorData(errorData);
                           failnum++;
                           iterator1.remove();
                       } else {
                           /*验证*经纬度是否在行政区划范围内*/
                           Long check = qxzbService.checkZB(x, y);
                           if (check == null){
                               REC_ErrorData errorData = new REC_ErrorData();
                               errorData.setDzdm(bzdz.getDzbm());
                               errorData.setDzmc(bzdz.getDzmc());
                               errorData.setErrorFlag("02");
                               errorData.setErrorFlagStr("经纬度不在行政区划范围内");
                               errorData.setRksj(DateUtil.strToDate(DateUtil.dateToStr(new Date())));
                               errorData.setX(bzdz.getZxdhzb());
                               errorData.setY(bzdz.getZxdzzb());
                               errorData.setSource("01");
                               errorData.setSourceStr("标准地址");
                               int num = errorDataService.insertErrorData(errorData);
                               failnum++;
                               iterator1.remove();
                           }

                       }
                   }
               }
               if (bzdzList.size() != 0) {
                   for (REC_BZDZ bzdz : bzdzList) {
                       bzdz.setDzmc(DataTransUtil.addressTranslate(bzdz.getDzmc()));
                   }
                   int row = bzdzMapper.insert(bzdzList);
               }
               repeatMap.clear();
               repeatSet.clear();
               resultMap.put("failnum", failnum);
               resultMap.put("successnum", bzdzList.size());
               resultMap.put("message", "OK");
               resultMap.put("result", "0");
               return resultMap;
       } catch (Exception e){
           e.printStackTrace();
       }
        resultMap.put("failnum", 0);
        resultMap.put("successnum", 0);
        resultMap.put("message", "出现异常");
        resultMap.put("result", "1");
        return resultMap;
    }

    @Override
    public List<New_REC_BZDZ> findListById(int beginId, int endId) {
        List<New_REC_BZDZ> list = bzdzMapper.findListById(beginId, endId);
        return list;
    }



    @Override
    public Integer getMaxIdByTime(String beginTime, String endTime) {
        Integer id = bzdzMapper.getMaxIdByTime(beginTime, endTime);
        return id;
    }


    @Override
    public int getMaxIdByHandlerId(int checkOutId) {
        int id = bzdzMapper.getMaxIdByHandlerId(checkOutId);
        return id;
    }

    @Override
    public int deleteDzRepeatByBeginIdAndEndIdOnBzdz(int beginId, int maxId) {
       int row =  bzdzMapper.deleteDzRepeatByBeginIdAndEndIdOnBzdz(beginId,maxId);
       return row;
    }

    @Override
    public int deleteDzErrorByBeginIdAndEndIdOnBzdz(int beginId, int maxId) {
        int row = bzdzMapper.deleteDzErrorByBeginIdAndEndIdOnBzdz(beginId,maxId);
        return row;
    }

    @Override
    public int deletezbNotInXzqhByBeginIdAndEndIdOnBzdz(int beginId, int maxId) {
        int row = bzdzMapper.deletezbNotInXzqhByBeginIdAndEndIdOnBzdz(beginId,maxId);
        return row;
    }

    @Override
    public int getAll() {
        int all = bzdzMapper.getAll();
        return all;
    }



}
