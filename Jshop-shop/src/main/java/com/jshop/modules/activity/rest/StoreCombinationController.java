/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.modules.activity.rest;

import cn.hutool.core.util.ObjectUtil;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.domain.StoreCombination;
import com.jshop.modules.activity.service.StoreCombinationService;
import com.jshop.modules.activity.service.dto.StoreCombinationQueryCriteria;
import com.jshop.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
@Api(tags = "商城:拼团管理")
@RestController
@RequestMapping("api")
public class StoreCombinationController {

    private final StoreCombinationService storeCombinationService;

    public StoreCombinationController(StoreCombinationService storeCombinationService) {
        this.storeCombinationService = storeCombinationService;
    }

    @Log("查询拼团")
    @ApiOperation(value = "查询拼团")
    @GetMapping(value = "/StoreCombination")
    @PreAuthorize("@el.check('admin','STORECOMBINATION_ALL','STORECOMBINATION_SELECT')")
    public ResponseEntity getStoreCombinations(StoreCombinationQueryCriteria criteria, Pageable pageable){
        criteria.setIsDel(0);
        return new ResponseEntity(storeCombinationService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改拼团")
    @ApiOperation(value = "新增/修改拼团")
    @PutMapping(value = "/StoreCombination")
    @PreAuthorize("@el.check('admin','STORECOMBINATION_ALL','STORECOMBINATION_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreCombination resources){
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
            return new ResponseEntity(storeCombinationService.save(resources),HttpStatus.CREATED);
        }else{
            storeCombinationService.saveOrUpdate(resources);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

    }

    @ApiOperation(value = "开启关闭")
    @PostMapping(value = "/StoreCombination/onsale/{id}")
    public ResponseEntity onSale(@PathVariable Integer id,@RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = Integer.valueOf(jsonObject.get("status").toString());
        //System.out.println(status);
        storeCombinationService.onSale(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("删除拼团")
    @ApiOperation(value = "删除拼团")
    @DeleteMapping(value = "/StoreCombination/{id}")
    @PreAuthorize("@el.check('admin','STORECOMBINATION_ALL','STORECOMBINATION_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        StoreCombination combination = new StoreCombination();
        combination.setIsDel(1);
        combination.setId(id);
        storeCombinationService.saveOrUpdate(combination);
        return new ResponseEntity(HttpStatus.OK);
    }
}
