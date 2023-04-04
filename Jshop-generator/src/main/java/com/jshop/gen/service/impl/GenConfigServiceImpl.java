/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.gen.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.gen.domain.GenConfig;
import com.jshop.gen.service.GenConfigService;
import com.jshop.gen.service.mapper.GenConfigMapper;
import com.jshop.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author jack胡
 */
@Service
//@CacheConfig(cacheNames = "genConfig")
public class GenConfigServiceImpl extends BaseServiceImpl<GenConfigMapper, GenConfig> implements GenConfigService {

    @Override
//    @Cacheable(key = "#p0")
    public GenConfig find(String tableName) {
        GenConfig genConfig = this.getOne(new QueryWrapper<GenConfig>().eq("table_name",tableName));
        if(genConfig == null){
            return new GenConfig(tableName);
        }
        return genConfig;
    }

    @Override
//    @CachePut(key = "#p0")
    public GenConfig update(String tableName, GenConfig genConfig) {
        // 如果 api 路径为空，则自动生成路径
        if(StringUtils.isBlank(genConfig.getApiPath())){
            String separator = File.separator;
            String[] paths;
            String symbol = "\\";
            if (symbol.equals(separator)) {
                paths = genConfig.getPath().split("\\\\");
            } else {
                paths = genConfig.getPath().split(File.separator);
            }
            StringBuilder api = new StringBuilder();
            for (String path : paths) {
                api.append(path);
                api.append(separator);
                if ("src".equals(path)) {
                    api.append("api");
                    break;
                }
            }
            genConfig.setApiPath(api.toString());
        }
        this.saveOrUpdate(genConfig);
        return genConfig;
    }
}
