package com.founder.addressreporter.mapper;

import com.founder.addressreporter.bean.New_REC_FBZDZ;
import com.founder.addressreporter.bean.REC_FBZDZ;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author 姜涛
 * @create 2021-11-03 10:04
 */
public interface REC_FBZDZMapper {
    int getTotal(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    int insert(@Param("list") List<REC_FBZDZ> fbzdzList);

    Integer getMaxIdByTime(@Param("beginTime")String beginTime, @Param("endTime")String endTime);

    List<New_REC_FBZDZ> findListById(@Param("beginId") int beginId, @Param("endId") int endId);

    int  getMaxIdByHandlerId(@Param("id") int checkOutId);

    int deleteDzRepeatByBeginIdAndEndIdOnFBzdz(@Param("beginId") int beginId, @Param("endId") int endId);

    int deleteDzErrorByBeginIdAndEndIdOnFBzdz(@Param("beginId") int beginId, @Param("endId") int endId);

    int deletezbNotInXzqhByBeginIdAndEndIdOnFBzdz(@Param("beginId") int beginId, @Param("endId") int endId);

    int getAll();

}
