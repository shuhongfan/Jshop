/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.SystemStore;
import com.jshop.modules.shop.service.SystemStoreService;
import com.jshop.modules.shop.service.dto.SystemStoreDto;
import com.jshop.modules.shop.service.dto.SystemStoreQueryCriteria;
import com.jshop.modules.shop.service.mapper.SystemStoreMapper;
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
//@CacheConfig(cacheNames = "systemStore")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SystemStoreServiceImpl extends BaseServiceImpl<SystemStoreMapper, SystemStore> implements SystemStoreService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(SystemStoreQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SystemStore> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), SystemStoreDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<SystemStore> queryAll(SystemStoreQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SystemStore.class, criteria));
    }


    @Override
    public void download(List<SystemStoreDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemStoreDto systemStore : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("门店名称", systemStore.getName());
            map.put("简介", systemStore.getIntroduction());
            map.put("手机号码", systemStore.getPhone());
            map.put("省市区", systemStore.getAddress());
            map.put("详细地址", systemStore.getDetailedAddress());
            map.put("门店logo", systemStore.getImage());
            map.put("纬度", systemStore.getLatitude());
            map.put("经度", systemStore.getLongitude());
            map.put("核销有效日期", systemStore.getValidTime());
            map.put("每日营业开关时间", systemStore.getDayTime());
            map.put("添加时间", systemStore.getAddTime());
            map.put("是否显示", systemStore.getIsShow());
            map.put("是否删除", systemStore.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
