/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import com.jshop.dozer.service.IGenerator;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.WechatUser;
import com.jshop.modules.shop.service.WechatUserService;
import com.jshop.modules.shop.service.dto.WechatUserDto;
import com.jshop.modules.shop.service.dto.WechatUserQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author jack胡
 */
@AllArgsConstructor
@Api(tags = "微信用户管理")
@RestController
@RequestMapping("/api/WechatUser")
public class WechatUserController {

    private final WechatUserService wechatUserService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','WechatUser:list')")
    public void download(HttpServletResponse response, WechatUserQueryCriteria criteria) throws IOException {
        wechatUserService.download(generator.convert(wechatUserService.queryAll(criteria), WechatUserDto.class), response);
    }

    @GetMapping
    @Log("查询微信用户")
    @ApiOperation("查询微信用户")
    @PreAuthorize("@el.check('admin','WechatUser:list')")
    public ResponseEntity<Object> getWechatUsers(WechatUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(wechatUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增微信用户")
    @ApiOperation("新增微信用户")
    @PreAuthorize("@el.check('admin','WechatUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody WechatUser resources){
        return new ResponseEntity<>(wechatUserService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改微信用户")
    @ApiOperation("修改微信用户")
    @PreAuthorize("@el.check('admin','WechatUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody WechatUser resources){
        wechatUserService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除微信用户")
    @ApiOperation("删除微信用户")
    @PreAuthorize("@el.check('admin','WechatUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            wechatUserService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
