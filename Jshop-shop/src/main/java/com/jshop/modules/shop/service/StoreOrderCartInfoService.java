/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service;

import com.jshop.common.service.BaseService;
import com.jshop.modules.shop.domain.StoreOrderCartInfo;
import com.jshop.modules.shop.service.dto.JStoreOrderCartInfoDto;
import com.jshop.modules.shop.service.dto.StoreOrderCartInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author jack胡
 */
public interface StoreOrderCartInfoService extends BaseService<StoreOrderCartInfo>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(StoreOrderCartInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<StoreOrderCartInfo>
    */
    List<StoreOrderCartInfo> queryAll(StoreOrderCartInfoQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<JStoreOrderCartInfoDto> all, HttpServletResponse response) throws IOException;
}
