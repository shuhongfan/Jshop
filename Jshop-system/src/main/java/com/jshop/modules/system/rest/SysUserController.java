/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.system.rest;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.jshop.config.DataScope;
import com.jshop.dozer.service.IGenerator;
import com.jshop.exception.BadRequestException;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.system.domain.User;
import com.jshop.modules.system.domain.vo.UserPassVo;
import com.jshop.modules.system.service.DeptService;
import com.jshop.modules.system.service.RoleService;
import com.jshop.modules.system.service.UserService;
import com.jshop.modules.system.service.dto.RoleSmallDto;
import com.jshop.modules.system.service.dto.UserDto;
import com.jshop.modules.system.service.dto.UserQueryCriteria;
import com.jshop.tools.domain.VerificationCode;
import com.jshop.tools.service.VerificationCodeService;
import com.jshop.utils.PageUtil;
import com.jshop.utils.SecurityUtils;
import com.jshop.utils.JshopConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jack胡
 */
@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/api/users")
public class SysUserController {

    @Value("${rsa.private_key}")
    private String privateKey;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final DataScope dataScope;
    private final DeptService deptService;
    private final RoleService roleService;
    private final VerificationCodeService verificationCodeService;
    private final IGenerator generator;

    public SysUserController(PasswordEncoder passwordEncoder, UserService userService, DataScope dataScope, DeptService deptService, RoleService roleService, VerificationCodeService verificationCodeService, IGenerator generator) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.dataScope = dataScope;
        this.deptService = deptService;
        this.roleService = roleService;
        this.verificationCodeService = verificationCodeService;
        this.generator = generator;
    }

    @Log("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','user:list')")
    public void download(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.download(generator.convert(userService.queryAll(criteria), UserDto.class), response);
    }

    @Log("查询用户")
    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('admin','user:list')")
    public ResponseEntity<Object> getUsers(UserQueryCriteria criteria, Pageable pageable){
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }
        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        // 查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)){
            // 取交集
            result.addAll(deptSet);
            result.retainAll(deptIds);
            // 若无交集，则代表无数据权限
            criteria.setDeptIds(result);
            if(result.size() == 0){
                return new ResponseEntity<>(PageUtil.toPage(null,0),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(userService.queryAll(criteria,pageable),HttpStatus.OK);
            }
        // 否则取并集
        } else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return new ResponseEntity<>(userService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @Log("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('admin','user:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody User resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        return new ResponseEntity<>(userService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改用户")
    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('admin','user:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody User resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        checkLevel(resources);
        userService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("修改用户：个人中心")
    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ResponseEntity<Object> center(@Validated @RequestBody User resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        UserDto userDto = userService.findByName(SecurityUtils.getUsername());
        if(!resources.getId().equals(userDto.getId())){
            throw new BadRequestException("不能修改他人资料");
        }
        userService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除用户")
    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('admin','user:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        for (Long id : ids) {
            Integer currentLevel =  Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel =  Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + userService.findByName(SecurityUtils.getUsername()).getUsername());
            }
        }
        userService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity<Object> updatePass(@RequestBody UserPassVo passVo){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String oldPass = new String(rsa.decrypt(passVo.getOldPass(), KeyType.PrivateKey));
        String newPass = new String(rsa.decrypt(passVo.getNewPass(), KeyType.PrivateKey));
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        if(!passwordEncoder.matches(oldPass, user.getPassword())){
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if(passwordEncoder.matches(newPass, user.getPassword())){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(user.getUsername(),passwordEncoder.encode(newPass));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/updateAvatar")
    public ResponseEntity<Object> updateAvatar(@RequestParam MultipartFile file){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        userService.updateAvatar(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("修改邮箱")
    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity<Object> updateEmail(@PathVariable String code,@RequestBody User user){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(user.getPassword(), KeyType.PrivateKey));
        UserDto userDto = userService.findByName(SecurityUtils.getUsername());
        if(!passwordEncoder.matches(password, userDto.getPassword())){
            throw new BadRequestException("密码错误");
        }
        VerificationCode verificationCode = new VerificationCode(code, JshopConstant.RESET_MAIL,"email",user.getEmail());
        verificationCodeService.validated(verificationCode);
        userService.updateEmail(userDto.getUsername(),user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     * @param resources /
     */
    private void checkLevel(User resources) {
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        Integer currentLevel =  Collections.min(roleService.findByUsersId(user.getId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }
}
