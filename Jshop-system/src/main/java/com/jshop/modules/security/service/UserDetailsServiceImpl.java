/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.security.service;

import com.jshop.exception.BadRequestException;
import com.jshop.modules.security.security.vo.JwtUser;
import com.jshop.modules.system.service.RoleService;
import com.jshop.modules.system.service.UserService;
import com.jshop.modules.system.service.dto.DeptSmallDto;
import com.jshop.modules.system.service.dto.JobSmallDto;
import com.jshop.modules.system.service.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author jack胡
 */
@Service("userDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final RoleService roleService;

    public UserDetailsServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        UserDto user = userService.findByName(username);
        if (user == null) {
            throw new BadRequestException("账号不存在");
        } else {
            if (!user.getEnabled()) {
                throw new BadRequestException("账号未激活");
            }
            return createJwtUser(user);
        }
    }

    private UserDetails createJwtUser(UserDto user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getSex(),
                user.getPassword(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                Optional.ofNullable(user.getDept()).map(DeptSmallDto::getName).orElse(null),
                Optional.ofNullable(user.getJob()).map(JobSmallDto::getName).orElse(null),
                roleService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateTime(),
                user.getLastPasswordResetTime()
        );
    }
}
