/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.kaikeba.co
* 注意：
* 本软件为www.kaikeba.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.jshop.modules.system.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.exception.EntityExistException;
import com.jshop.modules.system.domain.Dept;
import com.jshop.modules.system.domain.Menu;
import com.jshop.modules.system.domain.Role;
import com.jshop.modules.system.domain.RolesDepts;
import com.jshop.modules.system.domain.RolesMenus;
import com.jshop.modules.system.service.RoleService;
import com.jshop.modules.system.service.RolesDeptsService;
import com.jshop.modules.system.service.RolesMenusService;
import com.jshop.modules.system.service.dto.RoleDto;
import com.jshop.modules.system.service.dto.RoleQueryCriteria;
import com.jshop.modules.system.service.dto.RoleSmallDto;
import com.jshop.modules.system.service.dto.UserDto;
import com.jshop.modules.system.service.mapper.DeptMapper;
import com.jshop.modules.system.service.mapper.MenuMapper;
import com.jshop.modules.system.service.mapper.RoleMapper;
import com.jshop.utils.FileUtil;
import com.jshop.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
* @author hupeng
* @date 2020-05-14
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    private final IGenerator generator;
    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final DeptMapper deptMapper;
    private final RolesMenusService rolesMenusService;
    private final  RolesDeptsService rolesDeptsService;

    @Override
    public Map<String, Object> queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<Role> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), RoleDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }

    /**
     * 查询数据分页
     *
     * @param pageable 分页参数
     * @return Object
     */
    @Override
    public Object queryAlls(RoleQueryCriteria criteria,Pageable pageable) {
        List<Role> roleList =  baseMapper.selectList(QueryHelpPlus.getPredicate(Role.class, criteria));
        return roleList;
    }


    @Override
    public List<Role> queryAll(RoleQueryCriteria criteria){
        List<Role> roleList =  baseMapper.selectList(QueryHelpPlus.getPredicate(Role.class, criteria));
        for (Role role : roleList) {
            role.setMenus(menuMapper.findMenuByRoleId(role.getId()));
            role.setDepts(deptMapper.findDeptByRoleId(role.getId()));
        }
        return roleList;
    }


    @Override
    public void download(List<RoleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDto role : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", role.getName());
            map.put("备注", role.getRemark());
            map.put("数据权限", role.getDataScope());
            map.put("角色级别", role.getLevel());
            map.put("创建日期", role.getCreateTime());
            map.put("功能权限", role.getPermission());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 根据用户ID查询
     *
     * @param id 用户ID
     * @return /
     */
//    @Cacheable(key = "'findByUsers_Id:' + #p0")
    @Override
    public List<RoleSmallDto> findByUsersId(Long id) {
        List<Role> roles = roleMapper.selectListByUserId(id);
        return generator.convert(roles,RoleSmallDto.class);
    }

    /**
     * 根据角色查询角色级别
     *
     * @param roles /
     * @return /
     */
    @Override
    public Integer findByRoles(Set<Role> roles) {
        Set<RoleDto> roleDtos = new HashSet<>();
        for (Role role : roles) {
            roleDtos.add(findById(role.getId()));
        }
        return Collections.min(roleDtos.stream().map(RoleDto::getLevel).collect(Collectors.toList()));
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    @Override
    public RoleDto findById(long id) {
        Role role = this.getById(id);
        role.setMenus(menuMapper.findMenuByRoleId(role.getId()));
        role.setDepts(deptMapper.findDeptByRoleId(role.getId()));
        return generator.convert(role, RoleDto.class);
    }

    /**
     * 修改绑定的菜单
     *
     * @param resources /
     * @param roleDto   /
     */
    @Override
//    @CacheEvict(allEntries = true)
    public void updateMenu(Role resources, RoleDto roleDto) {
        if(resources.getMenus().size()>0){
            List<RolesMenus> rolesMenusList = resources.getMenus().stream().map(i ->{
                RolesMenus rolesMenus = new RolesMenus();
                rolesMenus.setRoleId(resources.getId());
                rolesMenus.setMenuId(i.getId());
                return rolesMenus;
            }).collect(Collectors.toList());
            rolesMenusService.remove(new LambdaQueryWrapper<RolesMenus>().eq(RolesMenus::getRoleId,resources.getId()));
            rolesMenusService.saveBatch(rolesMenusList);
        }
    }


    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public RoleDto create(Role resources) {
        if(this.getOne(new QueryWrapper<Role>().lambda().eq(Role::getName,resources.getName())) != null){
            throw new EntityExistException(Role.class,"username",resources.getName());
        }

        if(this.getOne(new QueryWrapper<Role>().lambda().eq(Role::getName,resources.getName())) != null){
            throw new EntityExistException(Role.class,"username",resources.getName());
        }
        this.save(resources);
        if(resources.getDepts().size()>0){
            List<RolesDepts> rolesDeptsList = resources.getDepts().stream().map(i ->{
                RolesDepts rolesDepts = new RolesDepts();
                rolesDepts.setRoleId(resources.getId());
                rolesDepts.setDeptId(i.getId());
                return rolesDepts;
            }).collect(Collectors.toList());
            rolesDeptsService.saveBatch(rolesDeptsList);
        }
        return generator.convert(resources,RoleDto.class);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        Role role = this.getById(resources.getId());

        Role role1 = this.getOne(new QueryWrapper<Role>().lambda().eq(Role::getName,resources.getName()));

        if(role1 != null && !role1.getId().equals(role.getId())){
            throw new EntityExistException(Role.class,"username",resources.getName());
        }
        role1 = this.getOne(new QueryWrapper<Role>().lambda().eq(Role::getPermission,resources.getPermission()));
        if(role1 != null && !role1.getId().equals(role.getId())){
            throw new EntityExistException(Role.class,"permission",resources.getPermission());
        }
        role.setName(resources.getName());
        role.setRemark(resources.getRemark());
        role.setDataScope(resources.getDataScope());
        if(resources.getDepts().size()>0){
            List<RolesDepts> rolesDeptsList = resources.getDepts().stream().map(i ->{
                RolesDepts rolesDepts = new RolesDepts();
                rolesDepts.setRoleId(resources.getId());
                rolesDepts.setDeptId(i.getId());
                return rolesDepts;
            }).collect(Collectors.toList());
            rolesDeptsService.remove(new LambdaQueryWrapper<RolesDepts>().eq(RolesDepts::getRoleId,resources.getId()));
            rolesDeptsService.saveBatch(rolesDeptsList);
        }
        role.setLevel(resources.getLevel());
        role.setPermission(resources.getPermission());
        this.saveOrUpdate(role);
    }
    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    @Override
//    @Cacheable(key = "'loadPermissionByUser:' + #p0.username")
    public Collection<GrantedAuthority> mapToGrantedAuthorities(UserDto user) {
        Set<Role> roles = roleMapper.findByUsers_Id(user.getId());
        for (Role role : roles) {
            Set<Menu> menuSet = menuMapper.findMenuByRoleId(role.getId());
            role.setMenus(menuSet);
            Set<Dept> deptSet = deptMapper.findDeptByRoleId(role.getId());
            role.setDepts(deptSet);
        }
        Set<String> permissions = roles.stream().filter(role -> StringUtils.isNotBlank(role.getPermission())).map(Role::getPermission).collect(Collectors.toSet());
        permissions.addAll(
                roles.stream().flatMap(role -> role.getMenus().stream())
                        .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                        .map(Menu::getPermission).collect(Collectors.toSet())
        );
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            rolesMenusService.lambdaUpdate().eq(RolesMenus::getRoleId, id).remove();
            rolesDeptsService.lambdaUpdate().eq(RolesDepts::getRoleId,id).remove();
        }
        this.removeByIds(ids);
    }

}
