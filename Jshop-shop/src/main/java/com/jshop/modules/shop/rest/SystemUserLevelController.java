/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.SystemUserLevel;
import com.jshop.modules.shop.service.SystemUserLevelService;
import com.jshop.modules.shop.service.dto.SystemUserLevelQueryCriteria;
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
@Api(tags = "商城:用户等级管理")
@RestController
@RequestMapping("api")
public class SystemUserLevelController {

    private final SystemUserLevelService systemUserLevelService;

    public SystemUserLevelController(SystemUserLevelService systemUserLevelService) {
        this.systemUserLevelService = systemUserLevelService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/SystemUserLevel")
    @PreAuthorize("@el.check('admin','SYSTEMUSERLEVEL_ALL','SYSTEMUSERLEVEL_SELECT')")
    public ResponseEntity getSystemUserLevels(SystemUserLevelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(systemUserLevelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/SystemUserLevel")
    @PreAuthorize("@el.check('admin','SYSTEMUSERLEVEL_ALL','SYSTEMUSERLEVEL_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SystemUserLevel resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity(systemUserLevelService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/SystemUserLevel")
    @PreAuthorize("@el.check('admin','SYSTEMUSERLEVEL_ALL','SYSTEMUSERLEVEL_EDIT')")
    public ResponseEntity update(@Validated @RequestBody SystemUserLevel resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        systemUserLevelService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/SystemUserLevel/{id}")
    @PreAuthorize("@el.check('admin','SYSTEMUSERLEVEL_ALL','SYSTEMUSERLEVEL_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        systemUserLevelService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
