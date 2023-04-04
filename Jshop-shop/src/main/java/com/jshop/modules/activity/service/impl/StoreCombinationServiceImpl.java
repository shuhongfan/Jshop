/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.StoreCombination;
import com.jshop.modules.activity.domain.StorePink;
import com.jshop.modules.activity.domain.StoreVisit;
import com.jshop.modules.activity.service.StoreCombinationService;
import com.jshop.modules.activity.service.dto.StoreCombinationDto;
import com.jshop.modules.activity.service.dto.StoreCombinationQueryCriteria;
import com.jshop.modules.activity.service.mapper.StoreCombinationMapper;
import com.jshop.modules.activity.service.mapper.StorePinkMapper;
import com.jshop.modules.activity.service.mapper.StoreVisitMapper;
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
//@CacheConfig(cacheNames = "storeCombination")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreCombinationServiceImpl extends BaseServiceImpl<StoreCombinationMapper, StoreCombination> implements StoreCombinationService {

    private final IGenerator generator;
    private final StorePinkMapper storePinkMapper;
    private final StoreVisitMapper storeVisitMapper;
    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreCombinationQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreCombination> page = new PageInfo<>(queryAll(criteria));

        List<StoreCombinationDto> combinationDTOS = generator.convert(page.getList(), StoreCombinationDto.class);
        for (StoreCombinationDto combinationDTO : combinationDTOS) {
            //参与人数
            combinationDTO.setCountPeopleAll(storePinkMapper.selectCount(new QueryWrapper<StorePink>().eq("cid",combinationDTO.getId())));

            //成团人数
            combinationDTO.setCountPeoplePink(storePinkMapper.selectCount(new QueryWrapper<StorePink>().eq("cid",combinationDTO.getId()).eq("k_id",0)));
            //获取查看拼团产品人数
            combinationDTO.setCountPeopleBrowse(storeVisitMapper.selectCount(new QueryWrapper<StoreVisit>().eq("product_id",combinationDTO.getId())
                    .eq("product_type","combination")));
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content",combinationDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreCombination> queryAll(StoreCombinationQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreCombination.class, criteria));
    }


    @Override
    public void download(List<StoreCombinationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreCombinationDto storeCombination : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商品id", storeCombination.getProductId());
            map.put("商户id", storeCombination.getMerId());
            map.put("推荐图", storeCombination.getImage());
            map.put("轮播图", storeCombination.getImages());
            map.put("活动标题", storeCombination.getTitle());
            map.put("活动属性", storeCombination.getAttr());
            map.put("参团人数", storeCombination.getPeople());
            map.put("简介", storeCombination.getInfo());
            map.put("价格", storeCombination.getPrice());
            map.put("排序", storeCombination.getSort());
            map.put("销量", storeCombination.getSales());
            map.put("库存", storeCombination.getStock());
            map.put("添加时间", storeCombination.getAddTime());
            map.put("推荐", storeCombination.getIsHost());
            map.put("产品状态", storeCombination.getIsShow());
            map.put(" isDel",  storeCombination.getIsDel());
            map.put(" combination",  storeCombination.getCombination());
            map.put("商户是否可用1可用0不可用", storeCombination.getMerUse());
            map.put("是否包邮1是0否", storeCombination.getIsPostage());
            map.put("邮费", storeCombination.getPostage());
            map.put("拼团内容", storeCombination.getDescription());
            map.put("拼团开始时间", storeCombination.getStartTime());
            map.put("拼团结束时间", storeCombination.getStopTime());
            map.put("拼团订单有效时间", storeCombination.getEffectiveTime());
            map.put("拼图产品成本", storeCombination.getCost());
            map.put("浏览量", storeCombination.getBrowse());
            map.put("单位名", storeCombination.getUnitName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void onSale(Integer id, int status) {
        if(status == 1){
            status = 0;
        }else{
            status = 1;
        }
        StoreCombination storeCombination = new StoreCombination();
        storeCombination.setIsShow(status);
        storeCombination.setId(id);
        this.saveOrUpdate(storeCombination);
    }
}
