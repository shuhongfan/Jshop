/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.StoreProductReply;
import com.jshop.modules.shop.service.StoreProductReplyService;
import com.jshop.modules.shop.service.StoreProductService;
import com.jshop.modules.shop.service.UserService;
import com.jshop.modules.shop.service.dto.StoreProductReplyDto;
import com.jshop.modules.shop.service.dto.StoreProductReplyQueryCriteria;
import com.jshop.modules.shop.service.mapper.StoreProductReplyMapper;
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
//@CacheConfig(cacheNames = "storeProductReply")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreProductReplyServiceImpl extends BaseServiceImpl<StoreProductReplyMapper, StoreProductReply> implements StoreProductReplyService {

    private final IGenerator generator;

    private final UserService userService;

    private final StoreProductService storeProductService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreProductReplyQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreProductReply> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StoreProductReplyDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreProductReply> queryAll(StoreProductReplyQueryCriteria criteria){
        List<StoreProductReply> storeProductReplyList =  baseMapper.selectList(QueryHelpPlus.getPredicate(StoreProductReply.class, criteria));
        storeProductReplyList.forEach(storeProductReply->{
            storeProductReply.setTbUser(userService.getById(storeProductReply.getUid()));;
            storeProductReply.setStoreProduct(storeProductService.getById(storeProductReply.getProductId()));
        });
        return storeProductReplyList;
    }


    @Override
    public void download(List<StoreProductReplyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreProductReplyDto storeProductReplyDto : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", storeProductReplyDto.getUid());
            map.put("订单ID", storeProductReplyDto.getOid());
            map.put("唯一id", storeProductReplyDto.getUnique());
            map.put("产品id", storeProductReplyDto.getProductId());
            map.put("某种商品类型(普通商品、秒杀商品）", storeProductReplyDto.getReplyType());
            map.put("商品分数", storeProductReplyDto.getProductScore());
            map.put("服务分数", storeProductReplyDto.getServiceScore());
            map.put("评论内容", storeProductReplyDto.getComment());
            map.put("评论图片", storeProductReplyDto.getPics());
            map.put("评论时间", storeProductReplyDto.getAddTime());
            map.put("管理员回复内容", storeProductReplyDto.getMerchantReplyContent());
            map.put("管理员回复时间", storeProductReplyDto.getMerchantReplyTime());
            map.put("0未删除1已删除", storeProductReplyDto.getIsDel());
            map.put("0未回复1已回复", storeProductReplyDto.getIsReply());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
