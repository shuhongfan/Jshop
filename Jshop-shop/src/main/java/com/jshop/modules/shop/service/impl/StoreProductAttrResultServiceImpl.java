/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.modules.shop.domain.StoreProductAttrResult;
import com.jshop.modules.shop.service.StoreProductAttrResultService;
import com.jshop.modules.shop.service.mapper.StoreProductAttrResultMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;


/**
 * @author jack胡
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "storeProductAttrResult")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreProductAttrResultServiceImpl extends BaseServiceImpl<StoreProductAttrResultMapper, StoreProductAttrResult> implements StoreProductAttrResultService {



}
