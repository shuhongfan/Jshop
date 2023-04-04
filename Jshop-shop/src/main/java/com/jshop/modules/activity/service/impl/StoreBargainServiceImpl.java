/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StoreBargain;
import com.jshop.modules.activity.service.StoreBargainService;
import com.jshop.modules.activity.service.dto.StoreBargainDto;
import com.jshop.modules.activity.service.dto.StoreBargainQueryCriteria;
import com.jshop.modules.activity.service.mapper.StoreBargainMapper;
import com.jshop.utils.FileUtil;
import com.jshop.utils.OrderUtil;
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
//@CacheConfig(cacheNames = "storeBargain")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreBargainServiceImpl extends BaseServiceImpl<StoreBargainMapper, StoreBargain> implements StoreBargainService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreBargainQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreBargain> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        List<StoreBargainDto> storeBargainDtoList = generator.convert(page.getList(), StoreBargainDto.class);
        for (StoreBargainDto storeBargainDto : storeBargainDtoList) {

            String statusStr = OrderUtil.checkActivityStatus(storeBargainDto.getStartTime(),
                    storeBargainDto.getStopTime(), storeBargainDto.getStatus());
            storeBargainDto.setStatusStr(statusStr);
        }
        map.put("content", storeBargainDtoList);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreBargain> queryAll(StoreBargainQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreBargain.class, criteria));
    }


    @Override
    public void download(List<StoreBargainDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreBargainDto storeBargain : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("关联产品ID", storeBargain.getProductId());
            map.put("砍价活动名称", storeBargain.getTitle());
            map.put("砍价活动图片", storeBargain.getImage());
            map.put("单位名称", storeBargain.getUnitName());
            map.put("库存", storeBargain.getStock());
            map.put("销量", storeBargain.getSales());
            map.put("砍价产品轮播图", storeBargain.getImages());
            map.put("砍价开启时间", storeBargain.getStartTime());
            map.put("砍价结束时间", storeBargain.getStopTime());
            map.put("砍价产品名称", storeBargain.getStoreName());
            map.put("砍价金额", storeBargain.getPrice());
            map.put("砍价商品最低价", storeBargain.getMinPrice());
            map.put("每次购买的砍价产品数量", storeBargain.getNum());
            map.put("用户每次砍价的最大金额", storeBargain.getBargainMaxPrice());
            map.put("用户每次砍价的最小金额", storeBargain.getBargainMinPrice());
            map.put("用户每次砍价的次数", storeBargain.getBargainNum());
            map.put("砍价状态 0(到砍价时间不自动开启)  1(到砍价时间自动开启时间)", storeBargain.getStatus());
            map.put("砍价详情", storeBargain.getDescription());
            map.put("反多少积分", storeBargain.getGiveIntegral());
            map.put("砍价活动简介", storeBargain.getInfo());
            map.put("成本价", storeBargain.getCost());
            map.put("排序", storeBargain.getSort());
            map.put("是否推荐0不推荐1推荐", storeBargain.getIsHot());
            map.put("是否删除 0未删除 1删除", storeBargain.getIsDel());
            map.put("添加时间", storeBargain.getAddTime());
            map.put("是否包邮 0不包邮 1包邮", storeBargain.getIsPostage());
            map.put("邮费", storeBargain.getPostage());
            map.put("砍价规则", storeBargain.getRule());
            map.put("砍价产品浏览量", storeBargain.getLook());
            map.put("砍价产品分享量", storeBargain.getShare());
            map.put(" endTimeDate", storeBargain.getEndTimeDate());
            map.put(" startTimeDate", storeBargain.getStartTimeDate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
