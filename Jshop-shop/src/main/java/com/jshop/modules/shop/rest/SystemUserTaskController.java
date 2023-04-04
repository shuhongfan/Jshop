/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.SystemUserTask;
import com.jshop.modules.shop.service.SystemUserTaskService;
import com.jshop.modules.shop.service.dto.SystemUserTaskQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@Api(tags = "商城:用户任务管理")
@RestController
@RequestMapping("api")
public class SystemUserTaskController {

    private final SystemUserTaskService systemUserTaskService;

    public SystemUserTaskController(SystemUserTaskService systemUserTaskService) {
        this.systemUserTaskService = systemUserTaskService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/SystemUserTask")
    @PreAuthorize("@el.check('admin','SYSTEMUSERTASK_ALL','SYSTEMUSERTASK_SELECT')")
    public ResponseEntity getSystemUserTasks(SystemUserTaskQueryCriteria criteria,
                                               Pageable pageable){
        Sort sort = new Sort(Sort.Direction.ASC, "level_id");
        Pageable pageableT = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);
        return new ResponseEntity(systemUserTaskService.queryAll(criteria,pageableT),
                HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/SystemUserTask")
    @PreAuthorize("@el.check('admin','SYSTEMUSERTASK_ALL','SYSTEMUSERTASK_CREATE')")
    public ResponseEntity create(@Validated @RequestBody SystemUserTask resources){
        return new ResponseEntity(systemUserTaskService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/SystemUserTask")
    @PreAuthorize("@el.check('admin','SYSTEMUSERTASK_ALL','SYSTEMUSERTASK_EDIT')")
    public ResponseEntity update(@Validated @RequestBody SystemUserTask resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        systemUserTaskService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/SystemUserTask/{id}")
    @PreAuthorize("@el.check('admin','SYSTEMUSERTASK_ALL','SYSTEMUSERTASK_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        systemUserTaskService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
