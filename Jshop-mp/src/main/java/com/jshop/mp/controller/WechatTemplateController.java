/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.mp.controller;

import com.jshop.dozer.service.IGenerator;
import com.jshop.mp.domain.WechatTemplate;
import com.jshop.mp.service.WechatTemplateService;
import com.jshop.mp.service.dto.WechatTemplateDto;
import com.jshop.mp.service.dto.WechatTemplateQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
import java.util.Arrays;

/**
* @author jack胡
*/
@Api(tags = "商城:微信模板管理")
@RestController
@RequestMapping("/api/WechatTemplate")
@AllArgsConstructor
public class WechatTemplateController {


    private final WechatTemplateService wechatTemplateService;
    private final IGenerator generator;


    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','WechatTemplate:list')")
    public void download(HttpServletResponse response, WechatTemplateQueryCriteria criteria) throws IOException {
        wechatTemplateService.download(generator.convert(wechatTemplateService.queryAll(criteria), WechatTemplateDto.class), response);
    }

    @GetMapping
    @ApiOperation("查询微信模板消息")
    @PreAuthorize("@el.check('admin','WechatTemplate:list')")
    public ResponseEntity<Object> getWechatTemplates(WechatTemplateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(wechatTemplateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增微信模板消息")
    @PreAuthorize("@el.check('admin','WechatTemplate:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody WechatTemplate resources){
        return new ResponseEntity<>(wechatTemplateService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改微信模板消息")
    @PreAuthorize("@el.check('admin','WechatTemplate:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody WechatTemplate resources){
        wechatTemplateService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除微信模板消息")
    @PreAuthorize("@el.check('admin','WechatTemplate:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            wechatTemplateService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
