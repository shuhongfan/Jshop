/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.UserBill;
import com.jshop.modules.shop.service.UserBillService;
import com.jshop.modules.shop.service.dto.JUserBillDto;
import com.jshop.modules.shop.service.dto.UserBillQueryCriteria;
import com.jshop.modules.shop.service.mapper.UserBillMapper;
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
//@CacheConfig(cacheNames = "userBill")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserBillServiceImpl extends BaseServiceImpl<UserBillMapper, UserBill> implements UserBillService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(UserBillQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<JUserBillDto> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<JUserBillDto> queryAll(UserBillQueryCriteria criteria){

        return baseMapper.findAllByQueryCriteria(criteria.getCategory(),criteria.getType(),criteria.getNickname());
    }


    @Override
    public void download(List<JUserBillDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JUserBillDto userBill : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户uid", userBill.getUid());
            map.put("关联id", userBill.getLinkId());
            map.put("0 = 支出 1 = 获得", userBill.getPm());
            map.put("账单标题", userBill.getTitle());
            map.put("明细种类", userBill.getCategory());
            map.put("明细类型", userBill.getType());
            map.put("明细数字", userBill.getNumber());
            map.put("剩余", userBill.getBalance());
            map.put("备注", userBill.getMark());
            map.put("添加时间", userBill.getAddTime());
            map.put("0 = 带确定 1 = 有效 -1 = 无效", userBill.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
