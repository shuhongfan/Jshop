/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.Material;
import com.jshop.modules.shop.service.MaterialService;
import com.jshop.modules.shop.service.dto.MaterialDto;
import com.jshop.modules.shop.service.dto.MaterialQueryCriteria;
import com.jshop.modules.shop.service.mapper.MaterialMapper;
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
//@CacheConfig(cacheNames = "material")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MaterialServiceImpl extends BaseServiceImpl<MaterialMapper, Material> implements MaterialService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(MaterialQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Material> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), MaterialDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<Material> queryAll(MaterialQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(Material.class, criteria));
    }


    @Override
    public void download(List<MaterialDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MaterialDto material : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("所属租户", material.getUserId());
            map.put("创建时间", material.getCreateTime());
            map.put("最后更新时间", material.getUpdateTime());
            map.put("创建者ID", material.getCreateId());
            map.put("类型1、图片；2、视频", material.getType());
            map.put("分组ID", material.getGroupId());
            map.put("素材名", material.getName());
            map.put("素材链接", material.getUrl());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
