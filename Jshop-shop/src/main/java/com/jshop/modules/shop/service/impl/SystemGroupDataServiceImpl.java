/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.SystemGroupData;
import com.jshop.modules.shop.service.SystemGroupDataService;
import com.jshop.modules.shop.service.dto.SystemGroupDataDto;
import com.jshop.modules.shop.service.dto.SystemGroupDataQueryCriteria;
import com.jshop.modules.shop.service.mapper.SystemGroupDataMapper;
import com.jshop.utils.FileUtil;
import com.alibaba.fastjson.JSON;
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
//@CacheConfig(cacheNames = "systemGroupData")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SystemGroupDataServiceImpl extends BaseServiceImpl<SystemGroupDataMapper, SystemGroupData> implements SystemGroupDataService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(SystemGroupDataQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SystemGroupData> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        List<SystemGroupDataDto> systemGroupDataDTOS = new ArrayList<>();
        for (SystemGroupData systemGroupData : page.getList()) {

            SystemGroupDataDto systemGroupDataDTO = generator.convert(systemGroupData, SystemGroupDataDto.class);
            systemGroupDataDTO.setMap(JSON.parseObject(systemGroupData.getValue()));
            systemGroupDataDTOS.add(systemGroupDataDTO);
        }
        map.put("content",systemGroupDataDTOS);
        map.put("totalElements",page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<SystemGroupData> queryAll(SystemGroupDataQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SystemGroupData.class, criteria));
    }


    @Override
    public void download(List<SystemGroupDataDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemGroupDataDto systemGroupData : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("对应的数据名称", systemGroupData.getGroupName());
            map.put("数据组对应的数据值（json数据）", systemGroupData.getValue());
            map.put("添加数据时间", systemGroupData.getAddTime());
            map.put("数据排序", systemGroupData.getSort());
            map.put("状态（1：开启；2：关闭；）", systemGroupData.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
