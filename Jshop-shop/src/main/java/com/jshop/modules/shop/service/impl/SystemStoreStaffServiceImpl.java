/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.SystemStoreStaff;
import com.jshop.modules.shop.service.SystemStoreStaffService;
import com.jshop.modules.shop.service.dto.SystemStoreStaffDto;
import com.jshop.modules.shop.service.dto.SystemStoreStaffQueryCriteria;
import com.jshop.modules.shop.service.mapper.SystemStoreStaffMapper;
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
//@CacheConfig(cacheNames = "systemStoreStaff")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SystemStoreStaffServiceImpl extends BaseServiceImpl<SystemStoreStaffMapper, SystemStoreStaff> implements SystemStoreStaffService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(SystemStoreStaffQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SystemStoreStaff> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), SystemStoreStaffDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<SystemStoreStaff> queryAll(SystemStoreStaffQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SystemStoreStaff.class, criteria));
    }


    @Override
    public void download(List<SystemStoreStaffDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemStoreStaffDto systemStoreStaff : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("微信用户id", systemStoreStaff.getUid());
            map.put("店员头像", systemStoreStaff.getAvatar());
            map.put("门店id", systemStoreStaff.getStoreId());
            map.put("店员名称", systemStoreStaff.getStaffName());
            map.put("手机号码", systemStoreStaff.getPhone());
            map.put("核销开关", systemStoreStaff.getVerifyStatus());
            map.put("状态", systemStoreStaff.getStatus());
            map.put("添加时间", systemStoreStaff.getAddTime());
            map.put("微信昵称", systemStoreStaff.getNickname());
            map.put("所属门店", systemStoreStaff.getStoreName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
