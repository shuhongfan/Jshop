/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.shop.domain.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author jack胡
 */
@Repository
@Mapper
public interface UserMapper extends CoreMapper<TbUser> {

    @Update( "update user set status = #{status} where uid = #{id}")
    void updateOnstatus(@Param("status") int status, @Param("id") int id);

    @Update( "update user set now_money = now_money + ${money} where uid = #{id}")
    void updateMoney(@Param("money") double money, @Param("id")int id);

    @Update("update user set brokerage_price = brokerage_price+ ${price} where uid = #{id}")
    void incBrokeragePrice(@Param("price")double price,@Param("id") int id);

}
