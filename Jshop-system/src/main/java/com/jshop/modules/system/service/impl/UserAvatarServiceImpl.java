/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.kaikeba.co
* 注意：
* 本软件为www.kaikeba.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.jshop.modules.system.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.system.domain.UserAvatar;
import com.jshop.modules.system.service.UserAvatarService;
import com.jshop.modules.system.service.dto.UserAvatarDto;
import com.jshop.modules.system.service.dto.UserAvatarQueryCriteria;
import com.jshop.modules.system.service.mapper.UserAvatarMapper;
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
* @author hupeng
* @date 2020-05-14
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "userAvatar")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserAvatarServiceImpl extends BaseServiceImpl<UserAvatarMapper, UserAvatar> implements UserAvatarService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(UserAvatarQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<UserAvatar> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), UserAvatarDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<UserAvatar> queryAll(UserAvatarQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(UserAvatar.class, criteria));
    }


    @Override
    public void download(List<UserAvatarDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserAvatarDto userAvatar : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("真实文件名", userAvatar.getRealName());
            map.put("路径", userAvatar.getPath());
            map.put("大小", userAvatar.getSize());
            map.put("创建时间", userAvatar.getCreateTime());
            map.put("真实文件名", userAvatar.getRealName());
            map.put("路径", userAvatar.getPath());
            map.put("大小", userAvatar.getSize());
            map.put("创建时间", userAvatar.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public UserAvatar saveFile(UserAvatar userAvatar) {
        //todo
        return null;
    }
}
