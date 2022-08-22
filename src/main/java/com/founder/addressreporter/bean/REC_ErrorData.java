package com.founder.addressreporter.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 姜涛
 * @create 2021-11-05 13:19
 */
@Data
public class REC_ErrorData implements Serializable {
    private String dzdm;
    private String dzmc;
    private BigDecimal x;
    private BigDecimal y;
    private String errorFlag;
    private String errorFlagStr;
    private String source;
    private String sourceStr;
    private Date rksj;
}
