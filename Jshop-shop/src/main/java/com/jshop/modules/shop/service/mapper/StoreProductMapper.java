/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.shop.domain.StoreProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author jack胡
 */
@Repository
@Mapper
public interface StoreProductMapper extends CoreMapper<StoreProduct> {


    @Update("update store_product set is_del = #{status} where id = #{id}")
    void updateDel(@Param("status")int status,@Param("id") Integer id);
    @Update("update store_product set is_show = #{status} where id = #{id}")
    void updateOnsale(@Param("status")int status, @Param("id")Integer id);
}
