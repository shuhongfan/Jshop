/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.rest;

import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.service.StoreCouponUserService;
import com.jshop.modules.activity.service.dto.StoreCouponUserQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author jack胡
*/
@Api(tags = "商城:优惠券发放记录管理")
@RestController
@RequestMapping("api")
public class StoreCouponUserController {

    private final StoreCouponUserService storeCouponUserService;

    public StoreCouponUserController(StoreCouponUserService storeCouponUserService) {
        this.storeCouponUserService = storeCouponUserService;
    }

    @Log("查询Y")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/StoreCouponUser")
    @PreAuthorize("@el.check('admin','STORECOUPONUSER_ALL','STORECOUPONUSER_SELECT')")
    public ResponseEntity getStoreCouponUsers(StoreCouponUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(storeCouponUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }


}
