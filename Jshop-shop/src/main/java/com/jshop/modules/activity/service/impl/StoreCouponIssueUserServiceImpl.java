/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StoreCouponIssueUser;
import com.jshop.modules.activity.service.StoreCouponIssueUserService;
import com.jshop.modules.activity.service.dto.StoreCouponIssueUserDto;
import com.jshop.modules.activity.service.dto.StoreCouponIssueUserQueryCriteria;
import com.jshop.modules.activity.service.mapper.StoreCouponIssueUserMapper;
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
//@CacheConfig(cacheNames = "storeCouponIssueUser")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreCouponIssueUserServiceImpl extends BaseServiceImpl<StoreCouponIssueUserMapper, StoreCouponIssueUser> implements StoreCouponIssueUserService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreCouponIssueUserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreCouponIssueUser> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StoreCouponIssueUserDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreCouponIssueUser> queryAll(StoreCouponIssueUserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreCouponIssueUser.class, criteria));
    }


    @Override
    public void download(List<StoreCouponIssueUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreCouponIssueUserDto storeCouponIssueUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("领取优惠券用户ID", storeCouponIssueUser.getUid());
            map.put("优惠券前台领取ID", storeCouponIssueUser.getIssueCouponId());
            map.put("领取时间", storeCouponIssueUser.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
