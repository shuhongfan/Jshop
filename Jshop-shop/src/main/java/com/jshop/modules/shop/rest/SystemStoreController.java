/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.jshop.constant.ShopConstants;
import com.jshop.dozer.service.IGenerator;
import com.jshop.exception.BadRequestException;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.SystemStore;
import com.jshop.modules.shop.service.SystemStoreService;
import com.jshop.modules.shop.service.dto.SystemStoreDto;
import com.jshop.modules.shop.service.dto.SystemStoreQueryCriteria;
import com.jshop.mp.config.ShopKeyUtils;
import com.jshop.utils.OrderUtil;
import com.jshop.utils.RedisUtil;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author jack胡
 */
@Api(tags = "门店管理")
@RestController
@RequestMapping("/api/SystemStore")
public class SystemStoreController {

    private final SystemStoreService systemStoreService;
    private final IGenerator generator;
    public SystemStoreController(SystemStoreService systemStoreService, IGenerator generator) {
        this.systemStoreService = systemStoreService;
        this.generator = generator;
    }


    @Log("所有门店")
    @ApiOperation("所有门店")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('SystemStore:list')")
    public ResponseEntity<Object>  getAll(SystemStoreQueryCriteria criteria) {
        return new ResponseEntity<>(systemStoreService.queryAll(criteria),HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('SystemStore:list')")
    public void download(HttpServletResponse response, SystemStoreQueryCriteria criteria) throws IOException {
        systemStoreService.download(generator.convert(systemStoreService.queryAll(criteria), SystemStoreDto.class), response);
    }

    @GetMapping
    @Log("查询门店")
    @ApiOperation("查询门店")
    @PreAuthorize("@el.check('SystemStore:list')")
    public ResponseEntity<Object> getSystemStores(SystemStoreQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(systemStoreService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/getL")
    @Log("获取经纬度")
    @ApiOperation("获取经纬度")
    @PreAuthorize("@el.check('SystemStore:getl')")
    public ResponseEntity<Object> create(@Validated @RequestBody String jsonStr){
        String key = RedisUtil.get(ShopKeyUtils.getTengXunMapKey());
        if(StrUtil.isBlank(key)) throw  new BadRequestException("请先配置腾讯地图key");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String addr = jsonObject.getString("addr");
        String url = StrUtil.format("?address={}&key={}",addr,key);
        String json = HttpUtil.get(ShopConstants.QQ_MAP_URL+url);
        return new ResponseEntity<>(json,HttpStatus.CREATED);
    }

    @PostMapping
    @Log("新增门店")
    @ApiOperation("新增门店")
    @PreAuthorize("@el.check('SystemStore:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SystemStore resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        return new ResponseEntity<>(systemStoreService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改门店")
    @ApiOperation("修改门店")
    @PreAuthorize("@el.check('SystemStore:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SystemStore resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        systemStoreService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除门店")
    @ApiOperation("删除门店")
    @PreAuthorize("@el.check('SystemStore:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        systemStoreService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
