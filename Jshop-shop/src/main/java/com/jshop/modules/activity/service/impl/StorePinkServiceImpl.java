/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StorePink;
import com.jshop.modules.activity.service.StorePinkService;
import com.jshop.modules.activity.service.dto.StorePinkDto;
import com.jshop.modules.activity.service.dto.StorePinkQueryCriteria;
import com.jshop.modules.activity.service.mapper.StorePinkMapper;
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
//@CacheConfig(cacheNames = "storePink")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StorePinkServiceImpl extends BaseServiceImpl<StorePinkMapper, StorePink> implements StorePinkService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StorePinkQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StorePink> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StorePinkDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StorePink> queryAll(StorePinkQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StorePink.class, criteria));
    }


    @Override
    public void download(List<StorePinkDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StorePinkDto storePink : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户id", storePink.getUid());
            map.put("订单id 生成", storePink.getOrderId());
            map.put("订单id  数据库", storePink.getOrderIdKey());
            map.put("购买商品个数", storePink.getTotalNum());
            map.put("购买总金额", storePink.getTotalPrice());
            map.put("拼团产品id", storePink.getCid());
            map.put("产品id", storePink.getPid());
            map.put("拼图总人数", storePink.getPeople());
            map.put("拼团产品单价", storePink.getPrice());
            map.put("开始时间", storePink.getAddTime());
            map.put(" stopTime",  storePink.getStopTime());
            map.put("团长id 0为团长", storePink.getKId());
            map.put("是否发送模板消息0未发送1已发送", storePink.getIsTpl());
            map.put("是否退款 0未退款 1已退款", storePink.getIsRefund());
            map.put("状态1进行中2已完成3未完成", storePink.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
