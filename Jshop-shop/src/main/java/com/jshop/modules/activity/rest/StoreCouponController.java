/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.modules.activity.rest;

import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.domain.StoreCoupon;
import com.jshop.modules.activity.service.StoreCouponService;
import com.jshop.modules.activity.service.dto.StoreCouponQueryCriteria;
import com.jshop.utils.OrderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

/**
* @author jack胡
*/
@Api(tags = "商城:优惠券管理")
@RestController
@RequestMapping("api")
public class StoreCouponController {

    private final StoreCouponService storeCouponService;

    public StoreCouponController(StoreCouponService storeCouponService) {
        this.storeCouponService = storeCouponService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/StoreCoupon")
    @PreAuthorize("@el.check('admin','STORECOUPON_ALL','STORECOUPON_SELECT')")
    public ResponseEntity getStoreCoupons(StoreCouponQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(storeCouponService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/StoreCoupon")
    @PreAuthorize("@el.check('admin','STORECOUPON_ALL','STORECOUPON_CREATE')")
    public ResponseEntity create(@Validated @RequestBody StoreCoupon resources){
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(storeCouponService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/StoreCoupon")
    @PreAuthorize("@el.check('admin','STORECOUPON_ALL','STORECOUPON_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreCoupon resources){
        storeCouponService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/StoreCoupon/{id}")
    @PreAuthorize("@el.check('admin','STORECOUPON_ALL','STORECOUPON_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        StoreCoupon resources = new StoreCoupon();
        resources.setId(id);
        resources.setIsDel(1);
        storeCouponService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.OK);
    }
}
