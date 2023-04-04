/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service;

import com.jshop.common.service.BaseService;
import com.jshop.modules.shop.domain.StoreOrder;
import com.jshop.modules.shop.service.dto.OrderCountDto;
import com.jshop.modules.shop.service.dto.OrderTimeDataDto;
import com.jshop.modules.shop.service.dto.StoreOrderDto;
import com.jshop.modules.shop.service.dto.StoreOrderQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author jack胡
 */
public interface StoreOrderService extends BaseService<StoreOrder>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(StoreOrderQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<StoreOrder>
    */
    List<StoreOrder> queryAll(StoreOrderQueryCriteria criteria);


    StoreOrderDto create(StoreOrder resources);

    void update(StoreOrder resources);
    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<StoreOrderDto> all, HttpServletResponse response) throws IOException;


    Map<String,Object> queryAll(List<String> ids);


    String orderType(int id,int pinkId, int combinationId,int seckillId,
                     int bargainId,int shippingType);

    void refund(StoreOrder resources);

    OrderCountDto getOrderCount();

    OrderTimeDataDto getOrderTimeData();

    Map<String,Object> chartCount();
}
