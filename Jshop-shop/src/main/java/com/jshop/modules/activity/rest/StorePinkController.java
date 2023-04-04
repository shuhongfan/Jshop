/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.rest;

import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.service.StorePinkService;
import com.jshop.modules.activity.service.dto.StorePinkQueryCriteria;
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
@Api(tags = "商城:拼团记录管理")
@RestController
@RequestMapping("api")
public class StorePinkController {

    private final StorePinkService storePinkService;

    public StorePinkController(StorePinkService storePinkService) {
        this.storePinkService = storePinkService;
    }

    @Log("查询记录")
    @ApiOperation(value = "查询记录")
    @GetMapping(value = "/StorePink")
    @PreAuthorize("@el.check('admin','STOREPINK_ALL','STOREPINK_SELECT')")
    public ResponseEntity getStorePinks(StorePinkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(storePinkService.queryAll(criteria,pageable),HttpStatus.OK);
    }


}
