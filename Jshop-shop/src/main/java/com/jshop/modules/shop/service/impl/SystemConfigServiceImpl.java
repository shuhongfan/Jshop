/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.SystemConfig;
import com.jshop.modules.shop.service.SystemConfigService;
import com.jshop.modules.shop.service.dto.SystemConfigDto;
import com.jshop.modules.shop.service.dto.SystemConfigQueryCriteria;
import com.jshop.modules.shop.service.mapper.SystemConfigMapper;
import com.jshop.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
//@CacheConfig(cacheNames = "systemConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(SystemConfigQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SystemConfig> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), SystemConfigDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<SystemConfig> queryAll(SystemConfigQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SystemConfig.class, criteria));
    }


    @Override
    public void download(List<SystemConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemConfigDto systemConfig : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("字段名称", systemConfig.getMenuName());
            map.put("默认值", systemConfig.getValue());
            map.put("排序", systemConfig.getSort());
            map.put("是否隐藏", systemConfig.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public SystemConfig findByKey(String key) {
        return this.getOne(new QueryWrapper<SystemConfig>().eq("menu_name",key));
    }
}
