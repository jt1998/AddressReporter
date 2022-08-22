package com.founder.addressreporter.mapper;

import com.founder.addressreporter.bean.REC_ErrorData;
import org.apache.ibatis.annotations.Param;

/**
 * @author 姜涛
 * @create 2021-11-07 20:49
 */
public interface REC_ErrorDataMapper {

    int insertDzRepeatErrorDataByBeginIdAndEndIdOnBzdz(@Param("beginId") int beginId, @Param("endId") int maxId);

    int insertDzErrorDataByBeginIdAndEndIdOnBzdz(@Param("beginId")int beginId, @Param("endId")int maxId);

    int insertzbNotInXzqhDataByBeginIdAndEndIdOnBzdz(@Param("beginId")int beginId, @Param("endId")int maxId);

    int insertErrorData(REC_ErrorData errorData);

    int insertDzRepeatErrorDataByBeginIdAndEndIdOnFBzdzb(@Param("beginId")int beginId, @Param("endId")int maxId);

    int insertDzErrorDataByBeginIdAndEndIdOnFBzdz(@Param("beginId")int beginId, @Param("endId")int maxId);

    int insertzbNotInXzqhDataByBeginIdAndEndIdOnFBzdz(@Param("beginId")int beginId, @Param("endId")int maxId);
}
