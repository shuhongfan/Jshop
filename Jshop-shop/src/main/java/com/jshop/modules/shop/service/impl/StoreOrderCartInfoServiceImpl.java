/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.StoreOrderCartInfo;
import com.jshop.modules.shop.service.StoreOrderCartInfoService;
import com.jshop.modules.shop.service.dto.JStoreOrderCartInfoDto;
import com.jshop.modules.shop.service.dto.StoreOrderCartInfoQueryCriteria;
import com.jshop.modules.shop.service.mapper.StoreOrderCartInfoMapper;
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
//@CacheConfig(cacheNames = "StoreOrderCartInfo")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreOrderCartInfoServiceImpl extends BaseServiceImpl<StoreOrderCartInfoMapper, StoreOrderCartInfo> implements StoreOrderCartInfoService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreOrderCartInfoQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreOrderCartInfo> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), JStoreOrderCartInfoDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreOrderCartInfo> queryAll(StoreOrderCartInfoQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreOrderCartInfo.class, criteria));
    }


    @Override
    public void download(List<JStoreOrderCartInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JStoreOrderCartInfoDto StoreOrderCartInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单id", StoreOrderCartInfo.getOid());
            map.put("购物车id", StoreOrderCartInfo.getCartId());
            map.put("商品ID", StoreOrderCartInfo.getProductId());
            map.put("购买东西的详细信息", StoreOrderCartInfo.getCartInfo());
            map.put("唯一id", StoreOrderCartInfo.getUnique());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
