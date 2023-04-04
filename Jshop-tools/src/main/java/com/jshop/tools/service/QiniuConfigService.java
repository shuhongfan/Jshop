/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.tools.service;

import com.jshop.common.service.BaseService;
import com.jshop.tools.domain.QiniuConfig;
import com.jshop.tools.service.dto.QiniuConfigDto;
import com.jshop.tools.service.dto.QiniuQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author jack胡
 */
public interface QiniuConfigService  extends BaseService<QiniuConfig> {

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(QiniuQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<QiniuConfigDto>
    */
    List<QiniuConfig> queryAll(QiniuQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<QiniuConfigDto> all, HttpServletResponse response) throws IOException;


    void update(String type);
}
