/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.StoreCart;
import com.jshop.modules.shop.service.StoreCartService;
import com.jshop.modules.shop.service.dto.CountDto;
import com.jshop.modules.shop.service.dto.StoreCartDto;
import com.jshop.modules.shop.service.dto.StoreCartQueryCriteria;
import com.jshop.modules.shop.service.mapper.StoreCartMapper;
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
//@CacheConfig(cacheNames = "StoreCart")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreCartServiceImpl extends BaseServiceImpl<StoreCartMapper, StoreCart> implements StoreCartService {

    private final IGenerator generator;

    private final StoreCartMapper storeCartMapper;
    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreCartQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreCart> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StoreCartDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreCart> queryAll(StoreCartQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreCart.class, criteria));
    }


    @Override
    public void download(List<StoreCartDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreCartDto StoreCart : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", StoreCart.getUid());
            map.put("类型", StoreCart.getType());
            map.put("商品ID", StoreCart.getProductId());
            map.put("商品属性", StoreCart.getProductAttrUnique());
            map.put("商品数量", StoreCart.getCartNum());
            map.put("添加时间", StoreCart.getAddTime());
            map.put("0 = 未购买 1 = 已购买", StoreCart.getIsPay());
            map.put("是否删除", StoreCart.getIsDel());
            map.put("是否为立即购买", StoreCart.getIsNew());
            map.put("拼团id", StoreCart.getCombinationId());
            map.put("秒杀产品ID", StoreCart.getSeckillId());
            map.put("砍价id", StoreCart.getBargainId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<CountDto> findCateName() {
        return storeCartMapper.findCateName();
    }
}
