package com.founder.addressreporter.service.impl;

import com.founder.addressreporter.mapper.QXZBMapper;
import com.founder.addressreporter.service.QXZBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author 姜涛
 * @create 2021-11-09 14:08
 */
@Service
public class QXZBServiceImpl implements QXZBService {
    @Autowired
    private QXZBMapper qxzbMapper;
    @Override
    public Long checkZB(BigDecimal x, BigDecimal y) {
        Long l = qxzbMapper.checkZB(x,y);
        return l;
    }

}
