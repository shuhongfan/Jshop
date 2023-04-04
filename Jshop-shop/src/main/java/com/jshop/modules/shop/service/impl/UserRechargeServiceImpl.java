/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.UserRecharge;
import com.jshop.modules.shop.service.UserRechargeService;
import com.jshop.modules.shop.service.dto.UserRechargeDto;
import com.jshop.modules.shop.service.dto.UserRechargeQueryCriteria;
import com.jshop.modules.shop.service.mapper.UserRechargeMapper;
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
//@CacheConfig(cacheNames = "userRecharge")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRechargeServiceImpl extends BaseServiceImpl<UserRechargeMapper, UserRecharge> implements UserRechargeService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(UserRechargeQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<UserRecharge> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), UserRechargeDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<UserRecharge> queryAll(UserRechargeQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(UserRecharge.class, criteria));
    }


    @Override
    public void download(List<UserRechargeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserRechargeDto userRecharge : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("充值用户UID", userRecharge.getUid());
            map.put("订单号", userRecharge.getOrderId());
            map.put("充值金额", userRecharge.getPrice());
            map.put("充值类型", userRecharge.getRechargeType());
            map.put("是否充值", userRecharge.getPaid());
            map.put("充值支付时间", userRecharge.getPayTime());
            map.put("充值时间", userRecharge.getAddTime());
            map.put("退款金额", userRecharge.getRefundPrice());
            map.put("昵称", userRecharge.getNickname());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
