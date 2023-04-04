/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service;

import com.jshop.common.service.BaseService;
import com.jshop.modules.shop.domain.StoreCategory;
import com.jshop.modules.shop.service.dto.StoreCategoryDto;
import com.jshop.modules.shop.service.dto.StoreCategoryQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author jack胡
 */
public interface StoreCategoryService extends BaseService<StoreCategory>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(StoreCategoryQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<StoreCategoryDto>
    */
    List<StoreCategoryDto> queryAll(StoreCategoryQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<StoreCategoryDto> all, HttpServletResponse response) throws IOException;

    Object buildTree(List<StoreCategoryDto> categoryDTOList);


    boolean checkCategory(int pid);

    boolean checkProductCategory(int id);
}
