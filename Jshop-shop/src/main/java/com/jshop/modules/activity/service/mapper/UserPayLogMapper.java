package com.jshop.modules.activity.service.mapper;

import com.jshop.modules.activity.domain.TbPayLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;




@Repository
@Mapper
public interface UserPayLogMapper extends BaseMapper<TbPayLog> {
}
