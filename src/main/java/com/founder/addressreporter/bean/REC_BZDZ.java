package com.founder.addressreporter.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class REC_BZDZ implements Serializable {
 /*主键*/
 private Integer id;
 //地址编码	字符型
 private String dzbm ;
 //	地址名称	字符型
 private String dzmc;
 //	别名简称	字符型
 private String bmjc;
 //	地址元素类型	字符型
 private String dzyslx;
 //	地址元素名称	字符型
 private String dzysmc;
 //	中心点横坐标	数值型
 private BigDecimal zxdhzb;
 //		中心点纵坐标 ; 数值型
 private BigDecimal zxdzzb;
 //结构化地址编码	字符型
 private String jghdzbm	;
 //行政区划代码	字符型
 private String xzqhdm	;
 //行政区划名称	字符型
 private String xzqhmc	;
 //类行政区域代码	字符型
 private String lxzqydm	;
 //类行政区域名称	字符型
 private String lxzqymc	;
 //乡镇（街道）代码	字符型
 private String xzjddm	;
 //乡镇（街道）名称	字符型
 private String xzjdmc	;
 //乡镇（街道）_别名简称	字符型
 private String xzjd_bmjc	;
 //	社区/居（村）委会代码	字符型
 private String sqjcwhdm;
 //	社区/居（村）委会名称	字符型
 private String sqjcwhmc;
 //	社区/居（村）委会_别名简称	字符型
 private String sqjcwh_bmjc;
 //	所属街路巷_地址编码	字符型
 private String ssjlx_dzbm;
 //	所属街路巷名称	字符型
 private String ssjlxmc;
 //	所属街路巷_别名简称	字符型
 private String ssjlx_bmjc;
 //	所属小区_地址编码	字符型
 private String ssxq_dzbm;
 //	所属小区名称	字符型
 private String ssxqmc;
 //	所属小区_别名简称	字符型
 private String ssxq_bmjc;
 //	所属建筑物_地址编码	字符型
 private String ssjzw_dzbm;
 //	所属建筑物名称	字符型
 private String ss_jzwmc;
 //	所属建筑物_别名简称	字符型
 private String ssjzw_bmjc;
 //	所属最低一级_公安机关机构代码	字符型
 private String sszdyj_gajgjgdm;
 //	所属最低一级_公安机关名称	字符型
 private String sszdyj_gajgmc;
 //	地（住）址存在标识	布尔型
 private String dzzczbs;
 //地（住）址在用标识	布尔型
 private String dzzzybs	;
 //	更新时间	日期时间型
 private Date gxsj;
 //	启用日期	日期型
 private Date qyrq;
 //	停用日期	日期型
 private Date tyrq;
 //备注	字符型
 private String  bz	;


}
