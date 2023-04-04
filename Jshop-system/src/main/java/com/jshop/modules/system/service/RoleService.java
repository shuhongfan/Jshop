/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.kaikeba.co
* 注意：
* 本软件为www.kaikeba.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.jshop.modules.system.service;

import com.jshop.common.service.BaseService;
import com.jshop.modules.system.domain.Role;
import com.jshop.modules.system.service.dto.RoleDto;
import com.jshop.modules.system.service.dto.RoleQueryCriteria;
import com.jshop.modules.system.service.dto.RoleSmallDto;
import com.jshop.modules.system.service.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author hupeng
* @date 2020-05-14
*/
public interface RoleService  extends BaseService<Role>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(RoleQueryCriteria criteria, Pageable pageable);


    /**
     * 查询数据分页
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    Object queryAlls(RoleQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<RoleDto>
    */
    List<Role> queryAll(RoleQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<RoleDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    List<RoleSmallDto> findByUsersId(Long id);

    /**
     * 根据角色查询角色级别
     * @param roles /
     * @return /
     */
    Integer findByRoles(Set<Role> roles);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    RoleDto findById(long id);

    /**
     * 修改绑定的菜单
     * @param resources /
     * @param roleDto /
     */
    void updateMenu(Role resources, RoleDto roleDto);

    /**
     * 创建
     * @param resources /
     * @return /
     */
    RoleDto create(Role resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Role resources);

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    Collection<GrantedAuthority> mapToGrantedAuthorities(UserDto user);

    void delete(Set<Long> ids);
}
