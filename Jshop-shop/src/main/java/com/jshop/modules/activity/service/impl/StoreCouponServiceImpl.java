/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StoreCoupon;
import com.jshop.modules.activity.service.StoreCouponService;
import com.jshop.modules.activity.service.dto.StoreCouponDto;
import com.jshop.modules.activity.service.dto.StoreCouponQueryCriteria;
import com.jshop.modules.activity.service.mapper.StoreCouponMapper;
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
//@CacheConfig(cacheNames = "storeCoupon")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreCouponServiceImpl extends BaseServiceImpl<StoreCouponMapper, StoreCoupon> implements StoreCouponService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreCouponQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreCoupon> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StoreCouponDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreCoupon> queryAll(StoreCouponQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreCoupon.class, criteria));
    }


    @Override
    public void download(List<StoreCouponDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreCouponDto storeCoupon : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("优惠券名称", storeCoupon.getTitle());
            map.put("兑换消耗积分值", storeCoupon.getIntegral());
            map.put("兑换的优惠券面值", storeCoupon.getCouponPrice());
            map.put("最低消费多少金额可用优惠券", storeCoupon.getUseMinPrice());
            map.put("优惠券有效期限（单位：天）", storeCoupon.getCouponTime());
            map.put("排序", storeCoupon.getSort());
            map.put("状态（0：关闭，1：开启）", storeCoupon.getStatus());
            map.put("兑换项目添加时间", storeCoupon.getAddTime());
            map.put("是否删除", storeCoupon.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
