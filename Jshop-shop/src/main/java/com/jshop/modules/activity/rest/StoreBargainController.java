/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.modules.activity.rest;

import cn.hutool.core.util.ObjectUtil;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.domain.StoreBargain;
import com.jshop.modules.activity.service.StoreBargainService;
import com.jshop.modules.activity.service.dto.StoreBargainQueryCriteria;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author jack胡
*/
@Api(tags = "商城:砍价管理")
@RestController
@RequestMapping("api")
public class StoreBargainController {

    private final StoreBargainService storeBargainService;

    public StoreBargainController(StoreBargainService storeBargainService) {
        this.storeBargainService = storeBargainService;
    }

    @Log("查询砍价")
    @ApiOperation(value = "查询砍价")
    @GetMapping(value = "/StoreBargain")
    @PreAuthorize("@el.check('admin','STOREBARGAIN_ALL','STOREBARGAIN_SELECT')")
    public ResponseEntity getStoreBargains(StoreBargainQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(storeBargainService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改砍价")
    @ApiOperation(value = "修改砍价")
    @PutMapping(value = "/StoreBargain")
    @PreAuthorize("@el.check('admin','STOREBARGAIN_ALL','STOREBARGAIN_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreBargain resources){

        if(ObjectUtil.isNotNull(resources.getStartTimeDate())){
            resources.setStartTime(OrderUtil.
                    dateToTimestamp(resources.getStartTimeDate()));
        }
        if(ObjectUtil.isNotNull(resources.getEndTimeDate())){
            resources.setStopTime(OrderUtil.
                    dateToTimestamp(resources.getEndTimeDate()));
        }
        if(ObjectUtil.isNull(resources.getId())){
            resources.setAddTime(OrderUtil.getSecondTimestampTwo());
            return new ResponseEntity(storeBargainService.save(resources),HttpStatus.CREATED);
        }else{
            storeBargainService.saveOrUpdate(resources);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @Log("删除砍价")
    @ApiOperation(value = "删除砍价")
    @DeleteMapping(value = "/StoreBargain/{id}")
    @PreAuthorize("@el.check('admin','STOREBARGAIN_ALL','STOREBARGAIN_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        storeBargainService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
