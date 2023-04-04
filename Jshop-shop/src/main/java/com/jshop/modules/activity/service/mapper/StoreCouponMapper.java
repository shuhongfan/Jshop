/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.activity.domain.StoreCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author jack胡
*/
@Repository
@Mapper
public interface StoreCouponMapper extends CoreMapper<StoreCoupon> {

}
