package com.founder.addressreporter.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 姜涛
 * @create 2022-08-22 9:26
 */
public class AddressUploadValue  {

    /*标准地址数据上传标识 0代表未启动质检流程 1代表完成 2代表质检中*/
    public static volatile int bzdzProcessValue = 0;

    /*非标准地址上传清理结果 0代表未启动质检流程 1代表完成 2代表质检中*/
    public static volatile int fbzdzProcessValue = 0;

    /*标准地址数据上传结果*/
    public static volatile Map<String,Object> bzdzProcessResult= new HashMap<>();

    /*非标准地址数据上传结果*/
    public static volatile Map<String,Object> fbzdzProcessResult= new HashMap<>();


}
