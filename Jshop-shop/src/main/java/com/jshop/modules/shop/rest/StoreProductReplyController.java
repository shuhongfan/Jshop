/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.StoreProductReply;
import com.jshop.modules.shop.service.StoreProductReplyService;
import com.jshop.modules.shop.service.dto.StoreProductReplyQueryCriteria;
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
@Api(tags = "商城:评论管理")
@RestController
@RequestMapping("api")
public class StoreProductReplyController {


    private final StoreProductReplyService storeProductReplyService;

    public StoreProductReplyController(StoreProductReplyService storeProductReplyService) {
        this.storeProductReplyService = storeProductReplyService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/StoreProductReply")
    @PreAuthorize("@el.check('admin','STOREPRODUCTREPLY_ALL','STOREPRODUCTREPLY_SELECT')")
    public ResponseEntity getStoreProductReplys(StoreProductReplyQueryCriteria criteria, Pageable pageable){
        criteria.setIsDel(0);
        return new ResponseEntity(storeProductReplyService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/StoreProductReply")
    @PreAuthorize("@el.check('admin','STOREPRODUCTREPLY_ALL','STOREPRODUCTREPLY_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreProductReply resources){
        storeProductReplyService.save(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/StoreProductReply/{id}")
    @PreAuthorize("@el.check('admin','STOREPRODUCTREPLY_ALL','STOREPRODUCTREPLY_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        StoreProductReply reply = new StoreProductReply();
        reply.setIsDel(1);
        reply.setId(id);
        storeProductReplyService.saveOrUpdate(reply);
        return new ResponseEntity(HttpStatus.OK);
    }
}
