/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StoreCouponIssue;
import com.jshop.modules.activity.service.StoreCouponIssueService;
import com.jshop.modules.activity.service.dto.StoreCouponIssueDto;
import com.jshop.modules.activity.service.dto.StoreCouponIssueQueryCriteria;
import com.jshop.modules.activity.service.mapper.StoreCouponIssueMapper;
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
//@CacheConfig(cacheNames = "storeCouponIssue")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreCouponIssueServiceImpl extends BaseServiceImpl<StoreCouponIssueMapper, StoreCouponIssue> implements StoreCouponIssueService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreCouponIssueQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreCouponIssue> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StoreCouponIssueDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreCouponIssue> queryAll(StoreCouponIssueQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreCouponIssue.class, criteria));
    }


    @Override
    public void download(List<StoreCouponIssueDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreCouponIssueDto storeCouponIssue : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" cname",  storeCouponIssue.getCname());
            map.put("优惠券ID", storeCouponIssue.getCid());
            map.put("优惠券领取开启时间", storeCouponIssue.getStartTime());
            map.put("优惠券领取结束时间", storeCouponIssue.getEndTime());
            map.put("优惠券领取数量", storeCouponIssue.getTotalCount());
            map.put("优惠券剩余领取数量", storeCouponIssue.getRemainCount());
            map.put("是否无限张数", storeCouponIssue.getIsPermanent());
            map.put("1 正常 0 未开启 -1 已无效", storeCouponIssue.getStatus());
            map.put(" isDel",  storeCouponIssue.getIsDel());
            map.put("优惠券添加时间", storeCouponIssue.getAddTime());
            map.put(" endTimeDate",  storeCouponIssue.getEndTimeDate());
            map.put(" startTimeDate",  storeCouponIssue.getStartTimeDate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
