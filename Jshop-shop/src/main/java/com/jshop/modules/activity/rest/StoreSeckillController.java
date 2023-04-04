/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.rest;

import cn.hutool.core.util.ObjectUtil;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.domain.StoreSeckill;
import com.jshop.modules.activity.service.StoreSeckillService;
import com.jshop.modules.activity.service.dto.StoreSeckillQueryCriteria;
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
@Api(tags = "商城:秒杀管理")
@RestController
@RequestMapping("api")
public class StoreSeckillController {

    private final StoreSeckillService storeSeckillService;

    public StoreSeckillController(StoreSeckillService storeSeckillService) {
        this.storeSeckillService = storeSeckillService;
    }

    @Log("列表")
    @ApiOperation(value = "列表")
    @GetMapping(value = "/StoreSeckill")
    @PreAuthorize("@el.check('admin','STORESECKILL_ALL','STORESECKILL_SELECT')")
    public ResponseEntity getStoreSeckills(StoreSeckillQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(storeSeckillService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("发布")
    @ApiOperation(value = "发布")
    @PutMapping(value = "/StoreSeckill")
    @PreAuthorize("@el.check('admin','STORESECKILL_ALL','STORESECKILL_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreSeckill resources){
        if(ObjectUtil.isNotNull(resources.getStartTimeDate())){
            resources.setStartTime(OrderUtil.
                    dateToTimestamp(resources.getStartTimeDate()));
        }
        if(ObjectUtil.isNotNull(resources.getEndTimeDate())){
            resources.setStopTime(OrderUtil.
                    dateToTimestamp(resources.getEndTimeDate()));
        }
        if(ObjectUtil.isNull(resources.getId())){
            resources.setAddTime(String.valueOf(OrderUtil.getSecondTimestampTwo()));
            //redis入库
            Boolean aBoolean = storeSeckillService.setRedisOne(resources);
            if (aBoolean) {
                resources.setStockCount(resources.getStock());
                return new ResponseEntity(storeSeckillService.save(resources), HttpStatus.CREATED);
            }
            return null;
        }else{
            storeSeckillService.saveOrUpdate(resources);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/StoreSeckill/{id}")
    @PreAuthorize("@el.check('admin','STORESECKILL_ALL','STORESECKILL_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        storeSeckillService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
