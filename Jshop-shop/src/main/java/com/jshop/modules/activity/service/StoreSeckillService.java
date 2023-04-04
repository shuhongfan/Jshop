/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.modules.activity.service;

import com.jshop.common.service.BaseService;
import com.jshop.modules.activity.domain.StoreSeckill;
import com.jshop.modules.activity.service.dto.StoreSeckillDto;
import com.jshop.modules.activity.service.dto.StoreSeckillQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author jack胡
 */
public interface StoreSeckillService extends BaseService<StoreSeckill> {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(StoreSeckillQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<StoreSeckillDto>
     */
    List<StoreSeckill> queryAll(StoreSeckillQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<StoreSeckillDto> all, HttpServletResponse response) throws IOException;

    /**
     * redis中存储方式一
     * hset(key,value,goods);
     * 把秒杀商品对象添加到Redis,使用hash结构进行存储
     * redis入库
     *
     * @param resource
     * @return
     */
    Boolean setRedisOne(StoreSeckill resource);


    /**
     * redis中存储方式二
     * hset(key,value,goods);
     * lpush(key,itemId)//使用队列来存储商品id
     * redis入库
     *
     * @param resource
     * @return
     */
    Boolean setRedisTwo(StoreSeckill resource);
}
