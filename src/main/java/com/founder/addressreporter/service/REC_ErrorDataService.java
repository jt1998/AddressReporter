package com.founder.addressreporter.service;

import com.founder.addressreporter.bean.REC_ErrorData;

/**
 * @author 姜涛
 * @create 2021-11-07 20:50
 */
public interface REC_ErrorDataService {
    int insertDzRepeatErrorDataByBeginIdAndEndIdOnBzdz(int beginId, int maxId);

    int insertDzErrorDataByBeginIdAndEndIdOnBzdz(int beginId, int maxId);

    int insertzbNotInXzqhDataByBeginIdAndEndIdOnBzdz(int beginId, int maxId);

    int insertErrorData(REC_ErrorData errorData);

    int  insertDzRepeatErrorDataByBeginIdAndEndIdOnFBzdz(int beginId, int maxId);

    int insertDzErrorDataByBeginIdAndEndIdOnFBzdz(int beginId, int maxId);

    int insertzbNotInXzqhDataByBeginIdAndEndIdOnFBzdz(int beginId, int maxId);
}
