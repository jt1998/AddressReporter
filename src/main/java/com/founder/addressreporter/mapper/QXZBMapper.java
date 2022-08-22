package com.founder.addressreporter.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author 姜涛
 * @create 2021-11-09 14:07
 */
public interface QXZBMapper {

    Long checkZB(@Param("x") BigDecimal x, @Param("y") BigDecimal y);
}
