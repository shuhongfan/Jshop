/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.mp.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.exception.EntityExistException;
import com.jshop.mp.domain.WechatReply;
import com.jshop.mp.service.WechatReplyService;
import com.jshop.mp.service.dto.WechatReplyDto;
import com.jshop.mp.service.dto.WechatReplyQueryCriteria;
import com.jshop.mp.service.mapper.WechatReplyMapper;
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
//@CacheConfig(cacheNames = "wechatReply")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WechatReplyServiceImpl extends BaseServiceImpl<WechatReplyMapper, WechatReply> implements WechatReplyService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(WechatReplyQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<WechatReply> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), WechatReplyDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<WechatReply> queryAll(WechatReplyQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(WechatReply.class, criteria));
    }


    @Override
    public void download(List<WechatReplyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WechatReplyDto wechatReply : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("关键字", wechatReply.getKey());
            map.put("回复类型", wechatReply.getType());
            map.put("回复数据", wechatReply.getData());
            map.put("0=不可用  1 =可用", wechatReply.getStatus());
            map.put("是否隐藏", wechatReply.getHide());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public WechatReply isExist(String key) {
        WechatReply wechatReply = this.getOne(new QueryWrapper<WechatReply>().eq("`key`",key));
        return wechatReply;
    }

    @Override
    public void create(WechatReply wechatReply) {
        if(this.isExist(wechatReply.getKey()) != null){
            throw new EntityExistException(WechatReply.class,"key", wechatReply.getKey());
        }
        this.save(wechatReply);
    }

    @Override
    public void upDate(WechatReply resources) {
        WechatReply wechatReply = this.getById(resources.getId());
        WechatReply wechatReply1 = null;
        wechatReply1 = this.isExist(resources.getKey());
        if(wechatReply1 != null && !wechatReply1.getId().equals(wechatReply.getId())){
            throw new EntityExistException(WechatReply.class,"key",resources.getKey());
        }
        wechatReply.copy(resources);
        this.saveOrUpdate(wechatReply);
    }
}
