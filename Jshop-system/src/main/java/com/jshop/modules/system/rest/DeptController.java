/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.modules.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import com.jshop.config.DataScope;
import com.jshop.dozer.service.IGenerator;
import com.jshop.exception.BadRequestException;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.system.domain.Dept;
import com.jshop.modules.system.service.DeptService;
import com.jshop.modules.system.service.dto.DeptDto;
import com.jshop.modules.system.service.dto.DeptQueryCriteria;
import com.jshop.utils.ValidationUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;
import java.util.Set;

/**
* @author jack胡
*/
@RestController
@Api(tags = "系统：部门管理")
@RequestMapping("/api/dept")
public class DeptController {

    private final DeptService deptService;

    private final DataScope dataScope;

    private final IGenerator generator;

    private static final String ENTITY_NAME = "dept";

    public DeptController(DeptService deptService, DataScope dataScope, IGenerator generator) {
        this.deptService = deptService;
        this.dataScope = dataScope;
        this.generator = generator;
    }

    @Log("导出部门数据")
    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','dept:list')")
    public void download(HttpServletResponse response, DeptQueryCriteria criteria) throws IOException {
        deptService.download(generator.convert(deptService.queryAll(criteria), DeptDto.class), response);
    }

    @Log("查询部门")
    @ApiOperation("查询部门")
    @GetMapping
    @PreAuthorize("@el.check('user:list','admin','dept:list')")
    public ResponseEntity<Object> getDepts(DeptQueryCriteria criteria){
        // 数据权限
        criteria.setIds(dataScope.getDeptIds());
        List<DeptDto> deptDtos = generator.convert(deptService.queryAll(criteria),DeptDto.class);
        return new ResponseEntity<>(deptService.buildTree(deptDtos),HttpStatus.OK);
    }

    @Log("新增部门")
    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@el.check('admin','dept:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Dept resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity<>(deptService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改部门")
    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@el.check('admin','dept:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Dept resources){
        if(resources.getId().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        Dept dept = deptService.getOne(new QueryWrapper<Dept>().lambda()
                .eq(Dept::getId,resources.getId()));
        ValidationUtil.isNull( dept.getId(),"Dept","id",resources.getId());
        resources.setId(dept.getId());
        deptService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除部门")
    @ApiOperation("删除部门")
    @DeleteMapping
    @PreAuthorize("@el.check('admin','dept:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        List<Long> deptIds = new ArrayList<>();
        for (Long id : ids) {
            List<Dept> deptList = deptService.findByPid(id);
            Dept dept =  deptService.getOne(new QueryWrapper<Dept>().eq("id",id));
            if(null!=dept){
                deptIds.add(dept.getId());
            }
            if(CollectionUtil.isNotEmpty(deptList)){
                for(Dept d:deptList){
                    deptIds.add(d.getId());
                }
            }
        }
        try {
            deptService.removeByIds(deptIds);
        }catch (Throwable e){
            throw new BadRequestException( "所选部门中存在岗位或者角色关联，请取消关联后再试");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
