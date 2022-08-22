package com.founder.addressreporter.utils;

import org.springframework.boot.rsocket.context.RSocketPortInfoApplicationContextInitializer;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 姜涛
 * @create 2021-10-22 9:32
 */
/*将数据进行质量标准处理*/
public class DataTransUtil {
    private static final String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
    private static String newDzmc = "";

    /*地址数据处理*/
    public static String addressTranslate (String dzmc) {
        newDzmc = dzmc.replaceAll(regEx, "");
        return newDzmc;
    }

}

