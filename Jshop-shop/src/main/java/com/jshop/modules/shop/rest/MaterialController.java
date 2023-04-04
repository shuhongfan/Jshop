/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;


import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.Material;
import com.jshop.modules.shop.service.MaterialService;
import com.jshop.modules.shop.service.dto.MaterialQueryCriteria;
import com.jshop.utils.SecurityUtils;
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
@Api(tags = "商城:素材管理管理")
@RestController
@RequestMapping("/api/material")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }



    @GetMapping(value = "/page")
    @Log("查询素材管理")
    @ApiOperation("查询素材管理")
    public ResponseEntity<Object> getMaterials(MaterialQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(materialService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增素材管理")
    @ApiOperation("新增素材管理")
    public ResponseEntity<Object> create(@Validated @RequestBody Material resources){
        resources.setCreateId(SecurityUtils.getUsername());
        return new ResponseEntity<>(materialService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改素材管理")
    @ApiOperation("修改素材管理")
    public ResponseEntity<Object> update(@Validated @RequestBody Material resources){
        materialService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除素材管理")
    @ApiOperation("删除素材管理")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteAll(@PathVariable String id) {
        materialService.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
