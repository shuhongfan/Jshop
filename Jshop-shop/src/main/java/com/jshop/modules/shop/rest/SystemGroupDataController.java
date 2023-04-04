/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jshop.constant.ShopConstants;
import com.jshop.exception.BadRequestException;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.SystemGroupData;
import com.jshop.modules.shop.service.SystemGroupDataService;
import com.jshop.modules.shop.service.dto.SystemGroupDataQueryCriteria;
import com.jshop.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(tags = "商城:数据配置管理")
@RestController
@RequestMapping("api")
public class SystemGroupDataController {

    private final SystemGroupDataService systemGroupDataService;

    public SystemGroupDataController(SystemGroupDataService systemGroupDataService) {
        this.systemGroupDataService = systemGroupDataService;
    }

    @Log("查询数据配置")
    @ApiOperation(value = "查询数据配置")
    @GetMapping(value = "/SystemGroupData")
    @PreAuthorize("@el.check('admin','SYSTEMGROUPDATA_ALL','SYSTEMGROUPDATA_SELECT')")
    public ResponseEntity getSystemGroupDatas(SystemGroupDataQueryCriteria criteria,
                                                Pageable pageable){
        Sort sort = new Sort(Sort.Direction.DESC, "sort");
        Pageable pageableT = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);
        return new ResponseEntity(systemGroupDataService.queryAll(criteria,pageableT),HttpStatus.OK);
    }

    @Log("新增数据配置")
    @ApiOperation(value = "新增数据配置")
    @PostMapping(value = "/SystemGroupData")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("@el.check('admin','SYSTEMGROUPDATA_ALL','SYSTEMGROUPDATA_CREATE')")
    public ResponseEntity create(@RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        if(ObjectUtil.isNotNull(jsonObject.get("name"))){
            if(StrUtil.isEmpty(jsonObject.get("name").toString())){
                throw new BadRequestException("名称必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("title"))){
            if(StrUtil.isEmpty(jsonObject.get("title").toString())){
                throw new BadRequestException("标题必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("info"))){
            if(StrUtil.isEmpty(jsonObject.get("info").toString())){
                throw new BadRequestException("简介必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("pic"))){
            if(StrUtil.isEmpty(jsonObject.get("pic").toString())){
                throw new BadRequestException("图片必须上传");
            }
        }


        SystemGroupData systemGroupData = new SystemGroupData();
        systemGroupData.setGroupName(jsonObject.get("groupName").toString());
        jsonObject.remove("groupName");
        systemGroupData.setValue(jsonObject.toJSONString());
        systemGroupData.setStatus(jsonObject.getInteger("status"));
        systemGroupData.setSort(jsonObject.getInteger("sort"));
        systemGroupData.setAddTime(OrderUtil.getSecondTimestampTwo());

        return new ResponseEntity(systemGroupDataService.save(systemGroupData),HttpStatus.CREATED);
    }

    @Log("修改数据配置")
    @ApiOperation(value = "修改数据配置")
    @PutMapping(value = "/SystemGroupData")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("@el.check('admin','SYSTEMGROUPDATA_ALL','SYSTEMGROUPDATA_EDIT')")
    public ResponseEntity update(@RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if(ObjectUtil.isNotNull(jsonObject.get("name"))){
            if(StrUtil.isEmpty(jsonObject.get("name").toString())){
                throw new BadRequestException("名称必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("title"))){
            if(StrUtil.isEmpty(jsonObject.get("title").toString())){
                throw new BadRequestException("标题必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("pic"))){
            if(StrUtil.isEmpty(jsonObject.get("pic").toString())){
                throw new BadRequestException("图片必须上传");
            }
        }

        SystemGroupData systemGroupData = new SystemGroupData();

        systemGroupData.setGroupName(jsonObject.get("groupName").toString());
        jsonObject.remove("groupName");
        systemGroupData.setValue(jsonObject.toJSONString());
        if(jsonObject.getInteger("status") == null){
            systemGroupData.setStatus(1);
        }else{
            systemGroupData.setStatus(jsonObject.getInteger("status"));
        }

        if(jsonObject.getInteger("sort") == null){
            systemGroupData.setSort(0);
        }else{
            systemGroupData.setSort(jsonObject.getInteger("sort"));
        }


        systemGroupData.setId(Integer.valueOf(jsonObject.get("id").toString()));
        systemGroupDataService.saveOrUpdate(systemGroupData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除数据配置")
    @ApiOperation(value = "删除数据配置")
    @DeleteMapping(value = "/SystemGroupData/{id}")
    @PreAuthorize("@el.check('admin','SYSTEMGROUPDATA_ALL','SYSTEMGROUPDATA_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        systemGroupDataService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
