package com.founder.addressreporter.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 姜涛
 * @create 2022-08-19 10:54
 */
public class ProcessValue {

    /*标准地址数据清理标识 0代表未启动质检流程 1代表完成 2代表质检中*/
    public static volatile int bzdzProcessValue = 0;

    /*非标准地址数据清理结果 0代表未启动质检流程 1代表完成 2代表质检中*/
    public static volatile int fbzdzProcessValue = 0;

    /*标准地址数据清理结果*/
    public static volatile  Map<String,Object> bzdzProcessResult= new HashMap<>();

    /*非标准地址数据清理结果*/
    public static volatile Map<String,Object> fbzdzProcessResult= new HashMap<>();

}
