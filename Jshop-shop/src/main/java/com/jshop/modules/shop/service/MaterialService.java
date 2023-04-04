/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service;

import com.jshop.common.service.BaseService;
import com.jshop.modules.shop.domain.Material;
import com.jshop.modules.shop.service.dto.MaterialDto;
import com.jshop.modules.shop.service.dto.MaterialQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author jack胡
 */
public interface MaterialService extends BaseService<Material>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(MaterialQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<Material>
    */
    List<Material> queryAll(MaterialQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<MaterialDto> all, HttpServletResponse response) throws IOException;
}
