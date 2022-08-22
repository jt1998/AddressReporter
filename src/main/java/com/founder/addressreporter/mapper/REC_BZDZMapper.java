package com.founder.addressreporter.mapper;

import com.founder.addressreporter.bean.New_REC_BZDZ;
import com.founder.addressreporter.bean.REC_BZDZ;
import com.founder.addressreporter.bean.REC_FBZDZ;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 姜涛
 * @create 2021-11-03 10:03
 */
public interface REC_BZDZMapper {
    int getTotal(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<New_REC_BZDZ> findListById(@Param("beginId") int beginId, @Param("endId") int endId);

    Integer getMaxIdByTime(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    int insert(@Param("list") List<REC_BZDZ> bzdzList);

    Integer getMaxIdByHandlerId(@Param("id") int checkOutId);

    int deleteDzRepeatByBeginIdAndEndIdOnBzdz(@Param("beginId") int beginId, @Param("endId") int maxId);

    int deleteDzErrorByBeginIdAndEndIdOnBzdz(@Param("beginId") int beginId, @Param("endId") int maxId);

    int deletezbNotInXzqhByBeginIdAndEndIdOnBzdz(@Param("beginId") int beginId, @Param("endId") int maxId);

    int getAll();

}