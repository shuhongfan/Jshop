/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StoreVisit;
import com.jshop.modules.activity.service.StoreVisitService;
import com.jshop.modules.activity.service.dto.StoreVisitDto;
import com.jshop.modules.activity.service.dto.StoreVisitQueryCriteria;
import com.jshop.modules.activity.service.mapper.StoreVisitMapper;
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
//@CacheConfig(cacheNames = "StoreVisit")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreVisitServiceImpl extends BaseServiceImpl<StoreVisitMapper, StoreVisit> implements StoreVisitService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreVisitQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreVisit> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StoreVisitDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreVisit> queryAll(StoreVisitQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreVisit.class, criteria));
    }


    @Override
    public void download(List<StoreVisitDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreVisitDto StoreVisit : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品ID", StoreVisit.getProductId());
            map.put("产品类型", StoreVisit.getProductType());
            map.put("产品分类ID", StoreVisit.getCateId());
            map.put("产品类型", StoreVisit.getType());
            map.put("用户ID", StoreVisit.getUid());
            map.put("访问次数", StoreVisit.getCount());
            map.put("备注描述", StoreVisit.getContent());
            map.put("添加时间", StoreVisit.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
