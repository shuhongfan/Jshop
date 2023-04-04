/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.StoreOrderStatus;
import com.jshop.modules.shop.service.StoreOrderStatusService;
import com.jshop.modules.shop.service.dto.StoreOrderStatusDto;
import com.jshop.modules.shop.service.dto.StoreOrderStatusQueryCriteria;
import com.jshop.modules.shop.service.mapper.StoreOrderStatusMapper;
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
//@CacheConfig(cacheNames = "storeOrderStatus")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreOrderStatusServiceImpl extends BaseServiceImpl<StoreOrderStatusMapper, StoreOrderStatus> implements StoreOrderStatusService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreOrderStatusQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreOrderStatus> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StoreOrderStatusDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreOrderStatus> queryAll(StoreOrderStatusQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreOrderStatus.class, criteria));
    }


    @Override
    public void download(List<StoreOrderStatusDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreOrderStatusDto storeOrderStatusDto : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单id", storeOrderStatusDto.getOid());
            map.put("操作类型", storeOrderStatusDto.getChangeType());
            map.put("操作备注", storeOrderStatusDto.getChangeMessage());
            map.put("操作时间", storeOrderStatusDto.getChangeTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
