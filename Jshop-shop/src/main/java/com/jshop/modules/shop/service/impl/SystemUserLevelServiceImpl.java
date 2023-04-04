/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.SystemUserLevel;
import com.jshop.modules.shop.service.SystemUserLevelService;
import com.jshop.modules.shop.service.dto.SystemUserLevelDto;
import com.jshop.modules.shop.service.dto.SystemUserLevelQueryCriteria;
import com.jshop.modules.shop.service.mapper.SystemUserLevelMapper;
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
//@CacheConfig(cacheNames = "systemUserLevel")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SystemUserLevelServiceImpl extends BaseServiceImpl<SystemUserLevelMapper, SystemUserLevel> implements SystemUserLevelService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(SystemUserLevelQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SystemUserLevel> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), SystemUserLevelDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<SystemUserLevel> queryAll(SystemUserLevelQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SystemUserLevel.class, criteria));
    }


    @Override
    public void download(List<SystemUserLevelDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemUserLevelDto systemUserLevel : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商户id", systemUserLevel.getMerId());
            map.put("会员名称", systemUserLevel.getName());
            map.put("购买金额", systemUserLevel.getMoney());
            map.put("有效时间", systemUserLevel.getValidDate());
            map.put("是否为永久会员", systemUserLevel.getIsForever());
            map.put("是否购买,1=购买,0=不购买", systemUserLevel.getIsPay());
            map.put("是否显示 1=显示,0=隐藏", systemUserLevel.getIsShow());
            map.put("会员等级", systemUserLevel.getGrade());
            map.put("享受折扣", systemUserLevel.getDiscount());
            map.put("会员卡背景", systemUserLevel.getImage());
            map.put("会员图标", systemUserLevel.getIcon());
            map.put("说明", systemUserLevel.getExplain());
            map.put("添加时间", systemUserLevel.getAddTime());
            map.put("是否删除.1=删除,0=未删除", systemUserLevel.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
