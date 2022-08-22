package com.founder.addressreporter.service.impl;

import com.alibaba.fastjson.JSON;
import com.founder.addressreporter.bean.*;
import com.founder.addressreporter.mapper.REC_FBZDZMapper;
import com.founder.addressreporter.service.QXZBService;
import com.founder.addressreporter.service.REC_ErrorDataService;
import com.founder.addressreporter.service.REC_FBZDZService;
import com.founder.addressreporter.utils.DataTransUtil;
import com.founder.addressreporter.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 姜涛
 * @create 2021-11-03 10:10
 */
@Service
public class REC_FBZDZServiceImpl implements REC_FBZDZService {
    @Autowired
    private REC_FBZDZMapper fbzdzMapper;
    @Autowired
    private REC_ErrorDataService errorDataService;
    @Autowired
    private QXZBService qxzbService;
    @Override
    public int getTotal(String begintime, String endtime) {
        int total = fbzdzMapper.getTotal(begintime,endtime);
        return total;
    }

    @Override
    public Map<String, Object> sendData(SendData sendData) {
        /*返回结果Map*/
        Map<String, Object> resultMap = new HashMap<>();
        /*失败个数*/
        int failnum = 0;
        /*判断是否含有重复的地址名称*/
        Map<String, String> repeatMap = new HashMap<>();
        /*存放dzmc 重复map*/
        Set<String> repeatSet = new HashSet<>();

        try {
            String datalist = sendData.getDatalist();
            List<REC_FBZDZ> fbzdzList = JSON.parseArray(datalist, REC_FBZDZ.class);
            Iterator<REC_FBZDZ> iterator = fbzdzList.iterator();
            while (iterator.hasNext()) {
                REC_FBZDZ fbzdz = iterator.next();
                if (repeatMap.containsKey(fbzdz.getDzmc())) {
                    repeatSet.add(fbzdz.getDzmc());
                } else {
                    repeatMap.put(fbzdz.getDzmc(), "1");
                }
            }
            if (repeatSet.size() != 0) {
                for (String dzmc : repeatSet) {
                    List<REC_FBZDZ> newList = fbzdzList.stream().filter(fbzdz -> dzmc.equals(fbzdz.getDzmc())).collect(Collectors.toList());
                    /*得到重复数据的个数*/
                    failnum = failnum + newList.size();
                    for (REC_FBZDZ fbzdz : newList) {
                        REC_ErrorData errorData = new REC_ErrorData();
                        errorData.setDzdm(fbzdz.getDzbm());
                        errorData.setDzmc(fbzdz.getDzmc());
                        errorData.setErrorFlag("03");
                        errorData.setErrorFlagStr("地址名称重复");
                        errorData.setRksj(DateUtil.strToDate(DateUtil.dateToStr(new Date())));
                        errorData.setX(fbzdz.getZxdhzb());
                        errorData.setY(fbzdz.getZxdzzb());
                        errorData.setSource("02");
                        errorData.setSourceStr("非标准地址");
                        int num = errorDataService.insertErrorData(errorData);
                    }
                    fbzdzList.removeAll(newList);
                }
            }
            /*判断重复地址数据之后，继续判断 地址格式不正确的地址数据*/
            if (fbzdzList.size() == 0) {
                System.out.println("传输来的数据中都是重复数据...不能在非标准地址数据表中增添新数据...");
                /*置零*/
                repeatMap.clear();
                repeatSet.clear();
                resultMap.put("failnum", failnum);
                resultMap.put("successnum", 0);
                resultMap.put("message", "OK");
                resultMap.put("result", "0");
                return resultMap;
            } else {
                Iterator<REC_FBZDZ> iterator1 = fbzdzList.iterator();
                while (iterator1.hasNext()) {
                    REC_FBZDZ fbzdz = iterator1.next();
                    BigDecimal x = fbzdz.getZxdhzb();
                    int xInt = x.setScale(0, BigDecimal.ROUND_DOWN).intValue();
                    BigDecimal y = fbzdz.getZxdzzb();
                    int yInt = y.setScale(0, BigDecimal.ROUND_DOWN).intValue();
                    /*验证x,y坐标的正确格式,比较x,y的整数位的长度*/
                    if (xInt <= 0 || xInt >= 180 || yInt <= 0 || yInt >= 90) {
                        REC_ErrorData errorData = new REC_ErrorData();
                        errorData.setDzdm(fbzdz.getDzbm());
                        errorData.setDzmc(fbzdz.getDzmc());
                        errorData.setErrorFlag("01");
                        errorData.setErrorFlagStr("经纬度格式不合格");
                        errorData.setRksj(DateUtil.strToDate(DateUtil.dateToStr(new Date())));
                        errorData.setX(fbzdz.getZxdhzb());
                        errorData.setY(fbzdz.getZxdzzb());
                        errorData.setSource("02");
                        errorData.setSourceStr("非标准地址");
                        int num = errorDataService.insertErrorData(errorData);
                        failnum++;
                        iterator1.remove();
                    } else {
                        /*验证*经纬度是否在行政区划范围内*/
                        Long check = qxzbService.checkZB(x,y);
                        if (check == null){
                            REC_ErrorData errorData = new REC_ErrorData();
                            errorData.setDzdm(fbzdz.getDzbm());
                            errorData.setDzmc(fbzdz.getDzmc());
                            errorData.setErrorFlag("02");
                            errorData.setErrorFlagStr("经纬度不在行政区划范围内");
                            errorData.setRksj(DateUtil.strToDate(DateUtil.dateToStr(new Date())));
                            errorData.setX(fbzdz.getZxdhzb());
                            errorData.setY(fbzdz.getZxdzzb());
                            errorData.setSource("02");
                            errorData.setSourceStr("非标准地址");
                            int num = errorDataService.insertErrorData(errorData);
                            failnum++;
                            iterator1.remove();
                        }
                    }
                }
            }
            if (fbzdzList.size() != 0) {
                for (REC_FBZDZ fbzdz : fbzdzList) {
                    fbzdz.setDzmc(DataTransUtil.addressTranslate(fbzdz.getDzmc()));
                }
                int row = fbzdzMapper.insert(fbzdzList);
            }
            repeatMap.clear();
            repeatSet.clear();
            resultMap.put("failnum", failnum);
            resultMap.put("successnum", fbzdzList.size());
            resultMap.put("message", "OK");
            resultMap.put("result", "0");
            return resultMap;
        } catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("failnum", 0);
        resultMap.put("successnum",0);
        resultMap.put("message", "出现异常");
        resultMap.put("result", "1");
        return resultMap;
    }

    @Override
    public List<New_REC_FBZDZ> findListById(int beginId, int endId) {
        List<New_REC_FBZDZ> list = fbzdzMapper.findListById(beginId, endId);
        return list;
    }

    @Override
    public Integer getMaxIdByTime(String beginTime,String endTime) {
        Integer id = fbzdzMapper.getMaxIdByTime(beginTime, endTime);
        return id;
    }

    @Override
    public int deleteDzRepeatByBeginIdAndEndIdOnFBzdz(int beginId, int maxId) {
        int row = fbzdzMapper.deleteDzRepeatByBeginIdAndEndIdOnFBzdz(beginId,maxId);
        return row;
    }

    @Override
    public int deleteDzErrorByBeginIdAndEndIdOnFBzdz(int beginId, int maxId) {
        int row = fbzdzMapper.deleteDzErrorByBeginIdAndEndIdOnFBzdz(beginId,maxId);
        return row;
    }

    @Override
    public int deletezbNotInXzqhByBeginIdAndEndIdOnFBzdz(int beginId, int maxId) {
        int row = fbzdzMapper.deletezbNotInXzqhByBeginIdAndEndIdOnFBzdz(beginId,maxId);
        return row;
    }

    @Override
    public int getMaxIdByHandlerId(int checkOutId) {
        int maxId = fbzdzMapper.getMaxIdByHandlerId(checkOutId);
        return maxId;
    }

    @Override
    public int getAll() {
        int all = fbzdzMapper.getAll();
        return all;
    }


}
