/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.quartz.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.quartz.domain.QuartzLog;
import com.jshop.modules.quartz.service.QuartzLogService;
import com.jshop.modules.quartz.service.dto.QuartzLogDto;
import com.jshop.modules.quartz.service.dto.QuartzLogQueryCriteria;
import com.jshop.modules.quartz.service.mapper.QuartzLogMapper;
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
//@CacheConfig(cacheNames = "quartzLog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QuartzLogServiceImpl extends BaseServiceImpl<QuartzLogMapper, QuartzLog> implements QuartzLogService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(QuartzLogQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<QuartzLog> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), QuartzLogDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<QuartzLog> queryAll(QuartzLogQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(QuartzLog.class, criteria));
    }

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    @Override
    public void download(List<QuartzLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QuartzLogDto quartzLog : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" baenName",  quartzLog.getBaenName());
            map.put(" createTime",  quartzLog.getCreateTime());
            map.put(" cronExpression",  quartzLog.getCronExpression());
            map.put(" exceptionDetail",  quartzLog.getExceptionDetail());
            map.put(" isSuccess",  quartzLog.getIsSuccess());
            map.put(" jobName",  quartzLog.getJobName());
            map.put(" methodName",  quartzLog.getMethodName());
            map.put(" params",  quartzLog.getParams());
            map.put(" time",  quartzLog.getTime());
            map.put("任务名称", quartzLog.getBaenName());
            map.put("Bean名称 ", quartzLog.getCreateTime());
            map.put("cron表达式", quartzLog.getCronExpression());
            map.put("异常详细 ", quartzLog.getExceptionDetail());
            map.put("状态", quartzLog.getIsSuccess());
            map.put("任务名称", quartzLog.getJobName());
            map.put("方法名称", quartzLog.getMethodName());
            map.put("参数", quartzLog.getParams());
            map.put("耗时（毫秒）", quartzLog.getTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
