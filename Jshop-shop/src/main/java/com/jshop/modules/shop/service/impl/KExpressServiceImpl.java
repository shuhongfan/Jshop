/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.Express;
import com.jshop.modules.shop.service.KExpressService;
import com.jshop.modules.shop.service.dto.ExpressDto;
import com.jshop.modules.shop.service.dto.ExpressQueryCriteria;
import com.jshop.modules.shop.service.mapper.ExpressMapper;
import com.jshop.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author jack胡
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "express")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KExpressServiceImpl extends BaseServiceImpl<ExpressMapper, Express> implements KExpressService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(ExpressQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Express> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), ExpressDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<Express> queryAll(ExpressQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(Express.class, criteria));
    }


    @Override
    public void download(List<ExpressDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ExpressDto express : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("快递公司简称", express.getCode());
            map.put("快递公司全称", express.getName());
            map.put("排序", express.getSort());
            map.put("是否显示", express.getIsShow());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
