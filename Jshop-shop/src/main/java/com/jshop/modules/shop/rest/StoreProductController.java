/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jshop.constant.ShopConstants;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.StoreProduct;
import com.jshop.modules.shop.service.StoreProductService;
import com.jshop.modules.shop.service.dto.StoreProductQueryCriteria;
import com.jshop.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

import java.math.BigDecimal;

/**
 * @author jack胡
 */
@Api(tags = "商城:商品管理")
@RestController
@RequestMapping("api")
public class StoreProductController {

    private final StoreProductService storeProductService;

    public StoreProductController(StoreProductService storeProductService) {
        this.storeProductService = storeProductService;
    }

    @Log("查询商品")
    @ApiOperation(value = "查询商品")
    @GetMapping(value = "/StoreProduct")
    @PreAuthorize("@el.check('admin','STOREPRODUCT_ALL','STOREPRODUCT_SELECT')")
    public ResponseEntity getStoreProducts(StoreProductQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(storeProductService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增商品")
    @ApiOperation(value = "新增商品")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/StoreProduct")
    @PreAuthorize("@el.check('admin','STOREPRODUCT_ALL','STOREPRODUCT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody StoreProduct resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        if(ObjectUtil.isEmpty(resources.getGiveIntegral())) resources.setGiveIntegral(BigDecimal.ZERO);
        if(ObjectUtil.isEmpty(resources.getCost_price())) resources.setCost_price(BigDecimal.ZERO);
        return new ResponseEntity(storeProductService.saveProduct(resources),HttpStatus.CREATED);
    }

    @Log("修改商品")
    @ApiOperation(value = "修改商品")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PutMapping(value = "/StoreProduct")
    @PreAuthorize("@el.check('admin','STOREPRODUCT_ALL','STOREPRODUCT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreProduct resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        storeProductService.updateProduct(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除商品")
    @ApiOperation(value = "删除商品")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/StoreProduct/{id}")
    @PreAuthorize("@el.check('admin','STOREPRODUCT_ALL','STOREPRODUCT_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        storeProductService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "恢复数据")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/StoreProduct/recovery/{id}")
    @PreAuthorize("@el.check('admin','STOREPRODUCT_ALL','STOREPRODUCT_DELETE')")
    public ResponseEntity recovery(@PathVariable Integer id){
        storeProductService.recovery(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "商品上架/下架")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/StoreProduct/onsale/{id}")
    public ResponseEntity onSale(@PathVariable Integer id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = Integer.valueOf(jsonObject.get("status").toString());
        storeProductService.onSale(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "生成属性")
    @PostMapping(value = "/StoreProduct/isFormatAttr/{id}")
    public ResponseEntity isFormatAttr(@PathVariable Integer id,@RequestBody String jsonStr){
        return new ResponseEntity(storeProductService.isFormatAttr(id,jsonStr),HttpStatus.OK);
    }

    @ApiOperation(value = "设置保存属性")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/StoreProduct/setAttr/{id}")
    public ResponseEntity setAttr(@PathVariable Integer id,@RequestBody String jsonStr){
        storeProductService.createProductAttr(id,jsonStr);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "清除属性")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/StoreProduct/clearAttr/{id}")
    public ResponseEntity clearAttr(@PathVariable Integer id){
        storeProductService.clearProductAttr(id,true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "获取属性")
    @GetMapping(value = "/StoreProduct/attr/{id}")
    public ResponseEntity attr(@PathVariable Integer id){
        String str = storeProductService.getStoreProductAttrResult(id);
        if(StrUtil.isEmpty(str)){
            return new ResponseEntity(HttpStatus.OK);
        }
        JSONObject jsonObject = JSON.parseObject(str);

        return new ResponseEntity(jsonObject,HttpStatus.OK);
    }



}
