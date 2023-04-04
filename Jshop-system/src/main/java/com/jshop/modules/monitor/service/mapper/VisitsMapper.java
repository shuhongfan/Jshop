package com.jshop.modules.monitor.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.monitor.domain.Visits;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface VisitsMapper extends CoreMapper<Visits> {
    @Select("select * FROM visits where create_time between #{time1} and #{time2}")
    List<Visits> findAllVisits(@Param("time1") String time1, @Param("time2")String time2);
}
