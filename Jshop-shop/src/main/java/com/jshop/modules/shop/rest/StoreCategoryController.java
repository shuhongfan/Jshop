/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import cn.hutool.core.util.StrUtil;
import com.jshop.constant.ShopConstants;
import com.jshop.exception.BadRequestException;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.StoreCategory;
import com.jshop.modules.shop.service.StoreCategoryService;
import com.jshop.modules.shop.service.dto.StoreCategoryDto;
import com.jshop.modules.shop.service.dto.StoreCategoryQueryCriteria;
import com.jshop.utils.OrderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
/**
 * @author jack胡
 */
@Api(tags = "商城:商品分类管理")
@RestController
@RequestMapping("api")
public class StoreCategoryController {


    private final StoreCategoryService storeCategoryService;


    public StoreCategoryController(StoreCategoryService storeCategoryService) {
        this.storeCategoryService = storeCategoryService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/StoreCategory/download")
    @PreAuthorize("@el.check('admin','cate:list')")
    public void download(HttpServletResponse response, StoreCategoryQueryCriteria criteria) throws IOException {
        storeCategoryService.download(storeCategoryService.queryAll(criteria), response);
    }


    @Log("查询商品分类")
    @ApiOperation(value = "查询商品分类")
    @GetMapping(value = "/StoreCategory")
    @PreAuthorize("@el.check('admin','STORECATEGORY_ALL','STORECATEGORY_SELECT')")
    public ResponseEntity getStoreCategorys(StoreCategoryQueryCriteria criteria, Pageable pageable){

        List<StoreCategoryDto> categoryDTOList = storeCategoryService.queryAll(criteria);
        return new ResponseEntity(storeCategoryService.buildTree(categoryDTOList),HttpStatus.OK);
    }

    @Log("新增商品分类")
    @ApiOperation(value = "新增商品分类")
    @PostMapping(value = "/StoreCategory")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("@el.check('admin','STORECATEGORY_ALL','STORECATEGORY_CREATE')")
    public ResponseEntity create(@Validated @RequestBody StoreCategory resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        if(resources.getPid() > 0 && StrUtil.isBlank(resources.getPic())) {
            throw new BadRequestException("子分类图片必传");
        }


        boolean checkResult = storeCategoryService.checkCategory(resources.getPid());

        if(!checkResult) throw new BadRequestException("分类最多能添加2级哦");


        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(storeCategoryService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改商品分类")
    @ApiOperation(value = "修改商品分类")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PutMapping(value = "/StoreCategory")
    @PreAuthorize("@el.check('admin','STORECATEGORY_ALL','STORECATEGORY_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreCategory resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        if(resources.getPid() > 0 && StrUtil.isBlank(resources.getPic())) {
            throw new BadRequestException("子分类图片必传");
        }

        if(resources.getId().equals(resources.getPid())){
            throw new BadRequestException("自己不能选择自己哦");
        }

        boolean checkResult = storeCategoryService.checkCategory(resources.getPid());

        if(!checkResult) throw new BadRequestException("分类最多能添加2级哦");
        
        storeCategoryService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除商品分类")
    @ApiOperation(value = "删除商品分类")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/StoreCategory/{id}")
    @PreAuthorize("@el.check('admin','STORECATEGORY_ALL','STORECATEGORY_DELETE')")
    public ResponseEntity delete(@PathVariable String id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        String[] ids = id.split(",");
        for (String newId: ids) {
            storeCategoryService.removeById(Integer.valueOf(newId));
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
