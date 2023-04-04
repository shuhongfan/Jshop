/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import com.jshop.dozer.service.IGenerator;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.UserRecharge;
import com.jshop.modules.shop.service.UserRechargeService;
import com.jshop.modules.shop.service.dto.UserRechargeDto;
import com.jshop.modules.shop.service.dto.UserRechargeQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author jack胡
 */
@Api(tags = "充值管理管理")
@RestController
@RequestMapping("/api/UserRecharge")
public class UserRechargeController {

    private final UserRechargeService userRechargeService;
    private final IGenerator generator;
    public UserRechargeController(UserRechargeService userRechargeService, IGenerator generator) {
        this.userRechargeService = userRechargeService;
        this.generator = generator;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('UserRecharge:list')")
    public void download(HttpServletResponse response, UserRechargeQueryCriteria criteria) throws IOException {
        userRechargeService.download(generator.convert(userRechargeService.queryAll(criteria), UserRechargeDto.class), response);
    }

    @GetMapping
    @Log("查询充值管理")
    @ApiOperation("查询充值管理")
    @PreAuthorize("@el.check('UserRecharge:list')")
    public ResponseEntity<Object> getUserRecharges(UserRechargeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(userRechargeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增充值管理")
    @ApiOperation("新增充值管理")
    @PreAuthorize("@el.check('UserRecharge:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody UserRecharge resources){
        return new ResponseEntity<>(userRechargeService.save(resources),HttpStatus.CREATED);
    }



    @Log("删除充值管理")
    @ApiOperation("删除充值管理")
    @PreAuthorize("@el.check('UserRecharge:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        userRechargeService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
