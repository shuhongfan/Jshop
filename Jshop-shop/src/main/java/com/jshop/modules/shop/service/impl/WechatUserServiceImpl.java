/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.WechatUser;
import com.jshop.modules.shop.service.WechatUserService;
import com.jshop.modules.shop.service.dto.WechatUserDto;
import com.jshop.modules.shop.service.dto.WechatUserQueryCriteria;
import com.jshop.modules.shop.service.mapper.WechatUserMapper;
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
//@CacheConfig(cacheNames = "wechatUser")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WechatUserServiceImpl extends BaseServiceImpl<WechatUserMapper, WechatUser> implements WechatUserService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(WechatUserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<WechatUser> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), WechatUserDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<WechatUser> queryAll(WechatUserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(WechatUser.class, criteria));
    }


    @Override
    public void download(List<WechatUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WechatUserDto wechatUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段", wechatUser.getUnionid());
            map.put("用户的标识，对当前公众号唯一", wechatUser.getOpenid());
            map.put("小程序唯一身份ID", wechatUser.getRoutineOpenid());
            map.put("用户的昵称", wechatUser.getNickname());
            map.put("用户头像", wechatUser.getHeadimgurl());
            map.put("用户的性别，值为1时是男性，值为2时是女性，值为0时是未知", wechatUser.getSex());
            map.put("用户所在城市", wechatUser.getCity());
            map.put("用户的语言，简体中文为zh_CN", wechatUser.getLanguage());
            map.put("用户所在省份", wechatUser.getProvince());
            map.put("用户所在国家", wechatUser.getCountry());
            map.put("公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注", wechatUser.getRemark());
            map.put("用户所在的分组ID（兼容旧的用户分组接口）", wechatUser.getGroupid());
            map.put("用户被打上的标签ID列表", wechatUser.getTagidList());
            map.put("用户是否订阅该公众号标识", wechatUser.getSubscribe());
            map.put("关注公众号时间", wechatUser.getSubscribeTime());
            map.put("添加时间", wechatUser.getAddTime());
            map.put("一级推荐人", wechatUser.getStair());
            map.put("二级推荐人", wechatUser.getSecond());
            map.put("一级推荐人订单", wechatUser.getOrderStair());
            map.put("二级推荐人订单", wechatUser.getOrderSecond());
            map.put("佣金", wechatUser.getNowMoney());
            map.put("小程序用户会话密匙", wechatUser.getSessionKey());
            map.put("用户类型", wechatUser.getUserType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
