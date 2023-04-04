/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import com.jshop.dozer.service.IGenerator;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.SystemStore;
import com.jshop.modules.shop.domain.SystemStoreStaff;
import com.jshop.modules.shop.service.SystemStoreService;
import com.jshop.modules.shop.service.SystemStoreStaffService;
import com.jshop.modules.shop.service.dto.SystemStoreStaffDto;
import com.jshop.modules.shop.service.dto.SystemStoreStaffQueryCriteria;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.springframework.web.bind.annotation.PutMapping;
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
@Api(tags = "门店店员管理")
@RestController
@RequestMapping("/api/SystemStoreStaff")
public class SystemStoreStaffController {

    private final SystemStoreStaffService systemStoreStaffService;
    private final SystemStoreService systemStoreService;

    private final IGenerator generator;

    public SystemStoreStaffController(SystemStoreService systemStoreService, SystemStoreStaffService systemStoreStaffService, IGenerator generator) {
        this.systemStoreService = systemStoreService;
        this.systemStoreStaffService = systemStoreStaffService;
        this.generator = generator;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('SystemStoreStaff:list')")
    public void download(HttpServletResponse response, SystemStoreStaffQueryCriteria criteria) throws IOException {
        systemStoreStaffService.download(generator.convert(systemStoreStaffService.queryAll(criteria), SystemStoreStaffDto.class), response);
    }

    @GetMapping
    @Log("查询门店店员")
    @ApiOperation("查询门店店员")
    @PreAuthorize("@el.check('SystemStoreStaff:list')")
    public ResponseEntity<Object> getSystemStoreStaffs(SystemStoreStaffQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(systemStoreStaffService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增门店店员")
    @ApiOperation("新增门店店员")
    @PreAuthorize("@el.check('SystemStoreStaff:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SystemStoreStaff resources){
        SystemStore systemStore = systemStoreService.getOne(Wrappers.<SystemStore>lambdaQuery()
                .eq(SystemStore::getId,resources.getStoreId()));
        resources.setStoreName(systemStore.getName());
        return new ResponseEntity<>(systemStoreStaffService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改门店店员")
    @ApiOperation("修改门店店员")
    @PreAuthorize("@el.check('SystemStoreStaff:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SystemStoreStaff resources){
        SystemStore systemStore = systemStoreService.getOne(Wrappers.<SystemStore>lambdaQuery()
                .eq(SystemStore::getId,resources.getStoreId()));
        resources.setStoreName(systemStore.getName());
        systemStoreStaffService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除门店店员")
    @ApiOperation("删除门店店员")
    @PreAuthorize("@el.check('SystemStoreStaff:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        systemStoreStaffService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
