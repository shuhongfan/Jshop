/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.mp.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.mp.domain.WechatTemplate;
import com.jshop.mp.service.WechatTemplateService;
import com.jshop.mp.service.dto.WechatTemplateDto;
import com.jshop.mp.service.dto.WechatTemplateQueryCriteria;
import com.jshop.mp.service.mapper.WechatTemplateMapper;
import com.jshop.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
//@CacheConfig(cacheNames = "wechatTemplate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WechatTemplateServiceImpl extends BaseServiceImpl<WechatTemplateMapper, WechatTemplate> implements WechatTemplateService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(WechatTemplateQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<WechatTemplate> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), WechatTemplateDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<WechatTemplate> queryAll(WechatTemplateQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(WechatTemplate.class, criteria));
    }


    @Override
    public void download(List<WechatTemplateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WechatTemplateDto wechatTemplate : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("模板编号", wechatTemplate.getTempkey());
            map.put("模板名", wechatTemplate.getName());
            map.put("回复内容", wechatTemplate.getContent());
            map.put("模板ID", wechatTemplate.getTempid());
            map.put("添加时间", wechatTemplate.getAddTime());
            map.put("状态", wechatTemplate.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public WechatTemplate findByTempkey(String recharge_success_key) {
        return this.getOne(new QueryWrapper<WechatTemplate>().eq("tempkey",recharge_success_key));
    }
}
