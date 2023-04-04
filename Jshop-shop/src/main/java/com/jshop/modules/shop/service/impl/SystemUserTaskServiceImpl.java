/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.SystemUserLevel;
import com.jshop.modules.shop.domain.SystemUserTask;
import com.jshop.modules.shop.service.SystemUserLevelService;
import com.jshop.modules.shop.service.SystemUserTaskService;
import com.jshop.modules.shop.service.dto.SystemUserTaskDto;
import com.jshop.modules.shop.service.dto.SystemUserTaskQueryCriteria;
import com.jshop.modules.shop.service.mapper.SystemUserTaskMapper;
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
//@CacheConfig(cacheNames = "systemUserTask")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SystemUserTaskServiceImpl extends BaseServiceImpl<SystemUserTaskMapper, SystemUserTask> implements SystemUserTaskService {

    private final IGenerator generator;
    private final SystemUserLevelService systemUserLevelService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(SystemUserTaskQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<SystemUserTask> page = new PageInfo<>(queryAll(criteria));
        List<SystemUserTaskDto> systemUserTaskDTOS = generator.convert(page.getList(), SystemUserTaskDto.class);
        for (SystemUserTaskDto systemUserTaskDTO : systemUserTaskDTOS) {
            SystemUserLevel userLevel=systemUserLevelService.getById(systemUserTaskDTO.getLevelId());
            systemUserTaskDTO.setLevalName(userLevel.getName());
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", systemUserTaskDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<SystemUserTask> queryAll(SystemUserTaskQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(SystemUserTask.class, criteria));
    }


    @Override
    public void download(List<SystemUserTaskDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemUserTaskDto systemUserTask : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("任务名称", systemUserTask.getName());
            map.put("配置原名", systemUserTask.getRealName());
            map.put("任务类型", systemUserTask.getTaskType());
            map.put("限定数", systemUserTask.getNumber());
            map.put("等级id", systemUserTask.getLevelId());
            map.put("排序", systemUserTask.getSort());
            map.put("是否显示", systemUserTask.getIsShow());
            map.put("是否务必达成任务,1务必达成,0=满足其一", systemUserTask.getIsMust());
            map.put("任务说明", systemUserTask.getIllustrate());
            map.put("新增时间", systemUserTask.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
