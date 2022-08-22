package com.founder.addressreporter.service.impl;

import com.founder.addressreporter.bean.REC_ErrorData;
import com.founder.addressreporter.mapper.REC_ErrorDataMapper;
import com.founder.addressreporter.service.REC_ErrorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 姜涛
 * @create 2021-11-07 20:50
 */
@Service
public class REC_ErrorDataServiceImpl implements REC_ErrorDataService {
    @Autowired
    private REC_ErrorDataMapper errorDataMapper;

    @Override
    public int insertDzRepeatErrorDataByBeginIdAndEndIdOnBzdz(int beginId, int maxId) {
        int row  = errorDataMapper.insertDzRepeatErrorDataByBeginIdAndEndIdOnBzdz(beginId,maxId);
        return row;
    }

    @Override
    public int insertDzErrorDataByBeginIdAndEndIdOnBzdz(int beginId, int maxId) {
        int row = errorDataMapper.insertDzErrorDataByBeginIdAndEndIdOnBzdz(beginId,maxId);
        return row;
    }

    @Override
    public int insertzbNotInXzqhDataByBeginIdAndEndIdOnBzdz(int beginId, int maxId) {
        int row = errorDataMapper.insertzbNotInXzqhDataByBeginIdAndEndIdOnBzdz(beginId,maxId);
        return row;
    }

    @Override
    public int insertErrorData(REC_ErrorData errorData) {
        int row  = errorDataMapper.insertErrorData(errorData);
        return row;
    }

    @Override
    public int insertDzRepeatErrorDataByBeginIdAndEndIdOnFBzdz(int beginId, int maxId) {
        int row = errorDataMapper.insertDzRepeatErrorDataByBeginIdAndEndIdOnFBzdzb(beginId,maxId);
        return row;
    }

    @Override
    public int insertDzErrorDataByBeginIdAndEndIdOnFBzdz(int beginId, int maxId) {
        int row = errorDataMapper.insertDzErrorDataByBeginIdAndEndIdOnFBzdz(beginId,maxId);
        return 0;
    }

    @Override
    public int insertzbNotInXzqhDataByBeginIdAndEndIdOnFBzdz(int beginId, int maxId) {
        int row = errorDataMapper.insertzbNotInXzqhDataByBeginIdAndEndIdOnFBzdz(beginId,maxId);
        return row;
    }
}
