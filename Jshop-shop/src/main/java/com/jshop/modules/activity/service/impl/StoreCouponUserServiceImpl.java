/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StoreCouponUser;
import com.jshop.modules.activity.service.StoreCouponUserService;
import com.jshop.modules.activity.service.dto.StoreCouponUserDto;
import com.jshop.modules.activity.service.dto.StoreCouponUserQueryCriteria;
import com.jshop.modules.activity.service.mapper.StoreCouponUserMapper;
import com.jshop.modules.shop.domain.TbUser;
import com.jshop.modules.shop.service.UserService;
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
//@CacheConfig(cacheNames = "storeCouponUser")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreCouponUserServiceImpl extends BaseServiceImpl<StoreCouponUserMapper, StoreCouponUser> implements StoreCouponUserService {

    private final IGenerator generator;
    private final UserService userService;
    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreCouponUserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreCouponUser> page = new PageInfo<>(queryAll(criteria));
        List<StoreCouponUserDto> storeOrderDTOS = generator.convert(page.getList(), StoreCouponUserDto.class);
        for (StoreCouponUserDto couponUserDTO : storeOrderDTOS) {
            couponUserDTO.setNickname(userService.getOne(new QueryWrapper<TbUser>().eq("uid",couponUserDTO.getUid())).getNickname());
        }
        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",storeOrderDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreCouponUser> queryAll(StoreCouponUserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreCouponUser.class, criteria));
    }
    @Override
    public void download(List<StoreCouponUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreCouponUserDto storeCouponUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("兑换的项目id", storeCouponUser.getCid());
            map.put("优惠券所属用户", storeCouponUser.getUid());
            map.put("优惠券名称", storeCouponUser.getCouponTitle());
            map.put("优惠券的面值", storeCouponUser.getCouponPrice());
            map.put("最低消费多少金额可用优惠券", storeCouponUser.getUseMinPrice());
            map.put("优惠券创建时间", storeCouponUser.getAddTime());
            map.put("优惠券结束时间", storeCouponUser.getEndTime());
            map.put("使用时间", storeCouponUser.getUseTime());
            map.put("获取方式", storeCouponUser.getType());
            map.put("状态（0：未使用，1：已使用, 2:已过期）", storeCouponUser.getStatus());
            map.put("是否有效", storeCouponUser.getIsFail());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
