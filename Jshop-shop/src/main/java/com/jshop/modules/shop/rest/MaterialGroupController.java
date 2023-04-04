/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.MaterialGroup;
import com.jshop.modules.shop.service.MaterialGroupService;
import com.jshop.modules.shop.service.dto.MaterialGroupQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Api(tags = "商城:素材分组管理")
@RestController
@RequestMapping("/api/materialgroup")
public class MaterialGroupController {

    private final MaterialGroupService materialGroupService;

    public MaterialGroupController(MaterialGroupService materialGroupService) {
        this.materialGroupService = materialGroupService;
    }



    @GetMapping(value = "/page")
    @Log("查询素材分组")
    @ApiOperation("查询素材分组")
    public ResponseEntity<Object> getMaterialGroups(MaterialGroupQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(materialGroupService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增素材分组")
    @ApiOperation("新增素材分组")
    public ResponseEntity<Object> create(@Validated @RequestBody MaterialGroup resources){
        return new ResponseEntity<>(materialGroupService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改素材分组")
    @ApiOperation("修改素材分组")
    public ResponseEntity<Object> update(@Validated @RequestBody MaterialGroup resources){
        materialGroupService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除素材分组")
    @ApiOperation("删除素材分组")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAll(@PathVariable String id) {
        materialGroupService.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
