package com.founder.addressreporter.bean;

import com.alibaba.fastjson.JSON;
import com.founder.addressreporter.utils.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 姜涛
 * @create 2021-11-07 19:55
 */
@Data
public class SendData implements Serializable {
    private String rec_type;
    private String datalist;

}
