/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.UserExtract;
import com.jshop.modules.activity.service.UserExtractService;
import com.jshop.modules.activity.service.dto.UserExtractDto;
import com.jshop.modules.activity.service.dto.UserExtractQueryCriteria;
import com.jshop.modules.activity.service.mapper.UserExtractMapper;
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
//@CacheConfig(cacheNames = "UserExtract")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserExtractServiceImpl extends BaseServiceImpl<UserExtractMapper, UserExtract> implements UserExtractService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(UserExtractQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<UserExtract> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), UserExtractDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<UserExtract> queryAll(UserExtractQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(UserExtract.class, criteria));
    }


    @Override
    public void download(List<UserExtractDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserExtractDto UserExtract : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" uid",  UserExtract.getUid());
            map.put("名称", UserExtract.getRealName());
            map.put("bank = 银行卡 alipay = 支付宝wx=微信", UserExtract.getExtractType());
            map.put("银行卡", UserExtract.getBankCode());
            map.put("开户地址", UserExtract.getBankAddress());
            map.put("支付宝账号", UserExtract.getAlipayCode());
            map.put("提现金额", UserExtract.getExtractPrice());
            map.put(" mark",  UserExtract.getMark());
            map.put(" balance",  UserExtract.getBalance());
            map.put("无效原因", UserExtract.getFailMsg());
            map.put(" failTime",  UserExtract.getFailTime());
            map.put("添加时间", UserExtract.getAddTime());
            map.put("-1 未通过 0 审核中 1 已提现", UserExtract.getStatus());
            map.put("微信号", UserExtract.getWechat());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
