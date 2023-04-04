/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.shop.domain.StoreCart;
import com.jshop.modules.shop.service.dto.CountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jack胡
 */
@Repository
@Mapper
public interface StoreCartMapper extends CoreMapper<StoreCart> {
    @Select("SELECT t.cate_name as catename from store_cart c  " +
            "LEFT JOIN store_product p on c.product_id = p.id  " +
            "LEFT JOIN store_category t on p.cate_id = t.id " +
            "WHERE c.is_pay = 1")
    List<CountDto> findCateName();
}
