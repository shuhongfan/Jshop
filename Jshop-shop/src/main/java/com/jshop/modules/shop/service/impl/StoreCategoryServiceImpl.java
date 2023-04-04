/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.StoreCategory;
import com.jshop.modules.shop.service.StoreCategoryService;
import com.jshop.modules.shop.service.dto.StoreCategoryDto;
import com.jshop.modules.shop.service.dto.StoreCategoryQueryCriteria;
import com.jshop.modules.shop.service.mapper.StoreCategoryMapper;
import com.jshop.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author jack胡
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "StoreCategory")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreCategoryServiceImpl extends BaseServiceImpl<StoreCategoryMapper, StoreCategory> implements StoreCategoryService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreCategoryQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreCategoryDto> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreCategoryDto> queryAll(StoreCategoryQueryCriteria criteria){
        return generator.convert(baseMapper.selectList(QueryHelpPlus.getPredicate(StoreCategory.class, criteria)), StoreCategoryDto.class);
    }


    @Override
    public void download(List<StoreCategoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreCategoryDto StoreCategory : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("父id", StoreCategory.getPid());
            map.put("分类名称", StoreCategory.getCateName());
            map.put("排序", StoreCategory.getSort());
            map.put("图标", StoreCategory.getPic());
            map.put("是否推荐", StoreCategory.getIsShow());
            map.put("添加时间", StoreCategory.getAddTime());
            map.put("删除状态", StoreCategory.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Object buildTree(List<StoreCategoryDto> categoryDTOS) {
        Set<StoreCategoryDto> trees = new LinkedHashSet<>();
        Set<StoreCategoryDto> cates= new LinkedHashSet<>();
        List<String> deptNames = categoryDTOS.stream().map(StoreCategoryDto::getCateName)
                .collect(Collectors.toList());

        StoreCategoryDto categoryDTO = new StoreCategoryDto();
        Boolean isChild;
        List<StoreCategory> categories = this.list();
        for (StoreCategoryDto deptDTO : categoryDTOS) {
            isChild = false;
            if ("0".equals(deptDTO.getPid().toString())) {
                trees.add(deptDTO);
            }
            for (StoreCategoryDto it : categoryDTOS) {
                if (it.getPid().equals(deptDTO.getId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<StoreCategoryDto>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if(isChild)
                cates.add(deptDTO);
            for (StoreCategory category : categories) {
                if(category.getId()==deptDTO.getPid()&&!deptNames.contains(category.getCateName())){
                    cates.add(deptDTO);
                }
            }
        }



        if (CollectionUtils.isEmpty(trees)) {
            trees = cates;
        }



        Integer totalElements = categoryDTOS!=null?categoryDTOS.size():0;

        Map map = new HashMap();
        map.put("totalElements",totalElements);
        map.put("content",CollectionUtils.isEmpty(trees)?categoryDTOS:trees);
        return map;
    }


    /**
     * 检测分类是否操过二级
     * @param pid 父级id
     * @return boolean
     */
    @Override
    public boolean checkCategory(int pid){
        if(pid == 0) return true;
        StoreCategory storeCategory =  this.getOne(Wrappers.<StoreCategory>lambdaQuery()
                        .eq(StoreCategory::getId,pid));
        if(storeCategory.getPid() > 0) return false;

        return true;
    }

    /**
     * 检测商品分类必选选择二级
     * @param id 分类id
     * @return boolean
     */
    public boolean checkProductCategory(int id){
        StoreCategory storeCategory =  this.getOne(Wrappers.<StoreCategory>lambdaQuery()
                .eq(StoreCategory::getId,id));

        if(storeCategory.getPid() == 0) return false;

        return true;
    }

}
