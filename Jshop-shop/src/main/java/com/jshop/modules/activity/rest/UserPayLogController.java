/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.rest;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.service.UserPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
* @author jack胡
*/
@Api(tags = "商城:支付记录管理")
@RestController
@RequestMapping("api")
public class UserPayLogController {

    @Autowired
    private UserPayLogService payLogService;

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/userPayLog")
    public ResponseEntity getUserPayLog(Pageable pageable){
        return new ResponseEntity(payLogService.queryAll(pageable),HttpStatus.OK);
    }


}
