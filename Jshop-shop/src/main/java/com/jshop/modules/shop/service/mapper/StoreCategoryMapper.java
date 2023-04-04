/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.shop.domain.StoreCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author jack胡
 */
@Repository
@Mapper
public interface StoreCategoryMapper extends CoreMapper<StoreCategory> {

}
