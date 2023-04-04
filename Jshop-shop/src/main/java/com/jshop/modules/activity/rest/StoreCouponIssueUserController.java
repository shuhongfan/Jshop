/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.modules.activity.rest;

import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.domain.StoreCouponIssueUser;
import com.jshop.modules.activity.service.StoreCouponIssueUserService;
import com.jshop.modules.activity.service.dto.StoreCouponIssueUserQueryCriteria;
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
@Api(tags = "商城:优惠券前台用户领取记录管理")
@RestController
@RequestMapping("api")
public class StoreCouponIssueUserController {

    private final StoreCouponIssueUserService storeCouponIssueUserService;

    public StoreCouponIssueUserController(StoreCouponIssueUserService storeCouponIssueUserService) {
        this.storeCouponIssueUserService = storeCouponIssueUserService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/StoreCouponIssueUser")
    @PreAuthorize("@el.check('admin','STORECOUPONISSUEUSER_ALL','STORECOUPONISSUEUSER_SELECT')")
    public ResponseEntity getStoreCouponIssueUsers(StoreCouponIssueUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(storeCouponIssueUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/StoreCouponIssueUser")
    @PreAuthorize("@el.check('admin','STORECOUPONISSUEUSER_ALL','STORECOUPONISSUEUSER_CREATE')")
    public ResponseEntity create(@Validated @RequestBody StoreCouponIssueUser resources){
        return new ResponseEntity(storeCouponIssueUserService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/StoreCouponIssueUser")
    @PreAuthorize("@el.check('admin','STORECOUPONISSUEUSER_ALL','STORECOUPONISSUEUSER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreCouponIssueUser resources){
        storeCouponIssueUserService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/StoreCouponIssueUser/{id}")
    @PreAuthorize("@el.check('admin','STORECOUPONISSUEUSER_ALL','STORECOUPONISSUEUSER_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        storeCouponIssueUserService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
