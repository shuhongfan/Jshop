/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.mp.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.mp.domain.WechatMenu;
import com.jshop.mp.service.WechatMenuService;
import com.jshop.mp.service.dto.WechatMenuDto;
import com.jshop.mp.service.dto.WechatMenuQueryCriteria;
import com.jshop.mp.service.mapper.WechatMenuMapper;
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
//@CacheConfig(cacheNames = "wechatMenu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WechatMenuServiceImpl extends BaseServiceImpl<WechatMenuMapper, WechatMenu> implements WechatMenuService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(WechatMenuQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<WechatMenu> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), WechatMenuDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<WechatMenu> queryAll(WechatMenuQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(WechatMenu.class, criteria));
    }


    @Override
    public void download(List<WechatMenuDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WechatMenuDto wechatMenu : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("缓存数据", wechatMenu.getResult());
            map.put("缓存时间", wechatMenu.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Boolean isExist(String wechat_menus) {
        WechatMenu wechatMenu = this.getOne(new QueryWrapper<WechatMenu>().eq("`key`",wechat_menus));
        if(wechatMenu == null){
            return false;
        }
        return true;
    }
}
