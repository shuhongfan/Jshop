/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.shop.domain.StoreOrder;
import com.jshop.modules.shop.service.dto.ChartDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jack胡
 */
@Repository
@Mapper
public interface StoreOrderMapper extends CoreMapper<StoreOrder> {

    @Select("SELECT COUNT(*) FROM store_order WHERE pay_time >= ${today}")
    Integer countByPayTimeGreaterThanEqual(@Param("today")int today);
    @Select("SELECT COUNT(*) FROM store_order WHERE pay_time < ${today}  and pay_time >= ${yesterday}")
    Integer countByPayTimeLessThanAndPayTimeGreaterThanEqual(@Param("today")int today, @Param("yesterday")int yesterday);
    @Select( "select IFNULL(sum(pay_price),0)  from store_order " +
            "where refund_status=0 and is_del=0 and paid=1")
    Double sumTotalPrice();

    @Select("SELECT IFNULL(sum(pay_price),0) as num," +
            "FROM_UNIXTIME(add_time, '%m-%d') as time " +
            " FROM store_order where refund_status=0 and is_del=0 and paid=1 and pay_time >= ${time}" +
            " GROUP BY FROM_UNIXTIME(add_time,'%Y-%m-%d') " +
            " ORDER BY add_time ASC")
    List<ChartDataDto> chartList(@Param("time") int time);
    @Select("SELECT count(id) as num," +
            "FROM_UNIXTIME(add_time, '%m-%d') as time " +
            " FROM store_order where refund_status=0 and is_del=0 and paid=1 and pay_time >= ${time}" +
            " GROUP BY FROM_UNIXTIME(add_time,'%Y-%m-%d') " +
            " ORDER BY add_time ASC")
    List<ChartDataDto> chartListT(@Param("time")int time);
}
