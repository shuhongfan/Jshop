/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.quartz.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.quartz.domain.QuartzLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author jack胡
*/
@Repository
@Mapper
public interface QuartzLogMapper extends CoreMapper<QuartzLog> {

}
