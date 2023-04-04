/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.tools.rest;

import com.jshop.dozer.service.IGenerator;
import com.jshop.logging.aop.log.Log;
import com.jshop.tools.service.LocalStorageService;
import com.jshop.tools.service.dto.LocalStorageDto;
import com.jshop.tools.service.dto.LocalStorageQueryCriteria;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author jack胡
 */
@AllArgsConstructor
@Api(tags = "文件管理")
@RestController
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','localStorage:list')")
    public void download(HttpServletResponse response, LocalStorageQueryCriteria criteria) throws IOException {
        localStorageService.download(generator.convert(localStorageService.queryAll(criteria), LocalStorageDto.class), response);
    }

    @GetMapping
    @Log("查询文件")
    @ApiOperation("查询文件")
    @PreAuthorize("@el.check('admin','localStorage:list')")
    public ResponseEntity<Object> getLocalStorages(LocalStorageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(localStorageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增文件")
    @ApiOperation("新增文件")
    @PreAuthorize("@el.check('admin','localStorage:add')")
    public ResponseEntity<Object> create(@RequestParam String name, @RequestParam("file") MultipartFile file){
        return new ResponseEntity<>(localStorageService.create(name,file),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改文件")
    @ApiOperation("修改文件")
    @PreAuthorize("@el.check('admin','localStorage:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody LocalStorageDto resources){
        localStorageService.updateLocalStorage(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除文件")
    @ApiOperation("删除文件")
    @PreAuthorize("@el.check('admin','localStorage:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            localStorageService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
