/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.MaterialGroup;
import com.jshop.modules.shop.service.MaterialGroupService;
import com.jshop.modules.shop.service.dto.MaterialGroupDto;
import com.jshop.modules.shop.service.dto.MaterialGroupQueryCriteria;
import com.jshop.modules.shop.service.mapper.MaterialGroupMapper;
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
//@CacheConfig(cacheNames = "materialGroup")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MaterialGroupServiceImpl extends BaseServiceImpl<MaterialGroupMapper, MaterialGroup> implements MaterialGroupService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(MaterialGroupQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<MaterialGroup> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), MaterialGroupDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<MaterialGroup> queryAll(MaterialGroupQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(MaterialGroup.class, criteria));
    }


    @Override
    public void download(List<MaterialGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MaterialGroupDto materialGroup : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("逻辑删除标记（0：显示；1：隐藏）", materialGroup.getDelFlag());
            map.put("创建时间", materialGroup.getCreateTime());
            map.put("创建者ID", materialGroup.getCreateId());
            map.put("分组名", materialGroup.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
