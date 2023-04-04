/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.tools.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.tools.domain.EmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author jack胡
 */
@Repository
@Mapper
public interface EmailConfigMapper extends CoreMapper<EmailConfig> {

}
