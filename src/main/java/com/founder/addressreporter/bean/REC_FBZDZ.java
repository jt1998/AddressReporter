package com.founder.addressreporter.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class REC_FBZDZ implements Serializable {
    /*主键*/
    private Integer id;
    //	地址编码	字符型
    private String dzbm;
    //地址名称	字符型
    private String  dzmc;
    //别名简称	字符型
    private String  bmjc;
    //中心点横坐标	数值型
    private BigDecimal zxdhzb;
    //中心点纵坐标	数值型
    private BigDecimal zxdzzb;
    //地址状态	数值型
    private int dzzt;
    //所属最低一级行政区域_行政区划代码	字符型
    private String  sszdyjxzqy_xzqhdm;
    //所属最低一级行政区域_行政区划名称 字符型
    private String  sszdyjxzqy_xzqhmc;
    //所属_乡镇(街道)代码	字符型
    private String  ss_xzjddm;
    //所属乡镇(街道)名称	字符型
    private String  ssxzjdmc;
    //所属_社区/居(村)委会代码	字符型
    private String  ss_sqjcwhdm	;
    //所属社区/居(村)委会名称	字符型
    private String  sssqjcwhmc;
    //所属街路巷_地址编码	字符型
    private String  ssjlx_dzbm;
    //所属街路巷名称	字符型
    private String ssjlxmc;
    //所属小区_地址编码	字符型
    private String  ssxq_dzbm;
    //所属小区名称	字符型
    private String  ssxqmc;
    //所属建筑物_地址编码	字符型
    private String ssjzw_dzbm;
    //所属建筑物名称	字符型
    private String  ssjzwmc	;
    //所属单元房屋_地址编码	字符型
    private String  ssdyfw_dzbm	;
    //所属单元房屋名称	字符型
    private String  ssdyfwmc;
    //数据生产时间	日期时间型
    private Date sjscsj;
    //更新时间	日期时间型
    private Date  gxsj;
    //地址来源分类	数值型
    private  int  dzlyfl;
    //地址来源_行政区划代码	字符型
    private String  dzly_xzqhdm	;
    //地址来源_单位名称	字符型
    private String  dzly_dwmc;
    //地址来源_系统名称	字符型
    private String  dzly_xtmc;
    //地址来源_唯一标识	字符型
    private String  dzly_wybs;
    //备注	字符型
    private String  bz;



}
