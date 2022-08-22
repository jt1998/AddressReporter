package com.founder.addressreporter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 姜涛
 * @create 2021-10-08 17:53
 */
/*时间处理工具类*/
public class DateUtil {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /*将日期转换成字符串*/
    public static String dateToStr(Date date) {
        String s = simpleDateFormat.format(date);
        return s;
    }

    /*将字符串转换成日期*/
    public static Date strToDate(String str) throws ParseException {
        Date date = simpleDateFormat.parse(str);
        return date;
    }



}

