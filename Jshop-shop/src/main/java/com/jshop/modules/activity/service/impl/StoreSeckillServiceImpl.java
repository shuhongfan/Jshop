/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StoreSeckill;
import com.jshop.modules.activity.service.StoreSeckillService;
import com.jshop.modules.activity.service.dto.StoreSeckillDto;
import com.jshop.modules.activity.service.dto.StoreSeckillQueryCriteria;
import com.jshop.modules.activity.service.mapper.StoreSeckillMapper;
import com.jshop.utils.FileUtil;
import com.jshop.utils.OrderUtil;
import com.github.pagehelper.PageInfo;
import com.jshop.utils.RedisUtil;
import com.jshop.utils.RedisUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
* @author jack胡
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "storeSeckill")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreSeckillServiceImpl extends BaseServiceImpl<StoreSeckillMapper, StoreSeckill> implements StoreSeckillService {

    private final IGenerator generator;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private StoreSeckillMapper storeSeckillMapper;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreSeckillQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreSeckill> page = new PageInfo<>(queryAll(criteria));
        List<StoreSeckillDto> storeSeckillDTOS = generator.convert(page.getList(), StoreSeckillDto.class);
        for (StoreSeckillDto storeSeckillDTO : storeSeckillDTOS){
            String statusStr = OrderUtil.checkActivityStatus(storeSeckillDTO.getStartTime(),
                    storeSeckillDTO.getStopTime(), storeSeckillDTO.getStatus());
            storeSeckillDTO.setStatusStr(statusStr);
        }
        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",storeSeckillDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreSeckill> queryAll(StoreSeckillQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreSeckill.class, criteria));
    }


    @Override
    public void download(List<StoreSeckillDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreSeckillDto storeSeckill : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商品id", storeSeckill.getProductId());
            map.put("推荐图", storeSeckill.getImage());
            map.put("轮播图", storeSeckill.getImages());
            map.put("活动标题", storeSeckill.getTitle());
            map.put("简介", storeSeckill.getInfo());
            map.put("价格", storeSeckill.getPrice());
            map.put("成本", storeSeckill.getCost());
            map.put("原价", storeSeckill.getOtPrice());
            map.put("返多少积分", storeSeckill.getGiveIntegral());
            map.put("排序", storeSeckill.getSort());
            map.put("库存", storeSeckill.getStock());
            map.put("销量", storeSeckill.getSales());
            map.put("单位名", storeSeckill.getUnitName());
            map.put("邮费", storeSeckill.getPostage());
            map.put("内容", storeSeckill.getDescription());
            map.put("开始时间", storeSeckill.getStartTime());
            map.put("结束时间", storeSeckill.getStopTime());
            map.put("添加时间", storeSeckill.getAddTime());
            map.put("产品状态", storeSeckill.getStatus());
            map.put("是否包邮", storeSeckill.getIsPostage());
            map.put("热门推荐", storeSeckill.getIsHot());
            map.put("删除 0未删除1已删除", storeSeckill.getIsDel());
            map.put("最多秒杀几个", storeSeckill.getNum());
            map.put("显示", storeSeckill.getIsShow());
            map.put(" endTimeDate",  storeSeckill.getEndTimeDate());
            map.put(" startTimeDate",  storeSeckill.getStartTimeDate());
            map.put("时间段id", storeSeckill.getTimeId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Boolean setRedisOne(StoreSeckill resource) {
        boolean result = redisUtils.hset("GOODS_STOCK_NUM", resource.getProductId() + "", resource);
        return result;
    }

    @Override
    public Boolean setRedisTwo(StoreSeckill resource) {
        boolean result = redisUtils.hset("GOODS_STOCK_NUM", resource.getProductId() + "", resource);
        for (Integer i = 0; i < resource.getStock(); i++) {
            redisUtils.lSet("GOODS_STOCK_NUM",resource,resource.getProductId());
        }
        return result;
    }
}
