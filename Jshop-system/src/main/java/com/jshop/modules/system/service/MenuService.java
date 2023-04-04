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
import com.jshop.modules.system.domain.Menu;
import com.jshop.modules.system.domain.vo.MenuVo;
import com.jshop.modules.system.service.dto.MenuDto;
import com.jshop.modules.system.service.dto.MenuQueryCriteria;
import com.jshop.modules.system.service.dto.RoleSmallDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author hupeng
* @date 2020-05-14
*/
public interface MenuService  extends BaseService<Menu>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(MenuQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<MenuDto>
    */
    List<Menu> queryAll(MenuQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<MenuDto> all, HttpServletResponse response) throws IOException;

    /**
     * 构建菜单树
     * @param menuDtos 原始数据
     * @return /
     */
    Map<String,Object> buildTree(List<MenuDto> menuDtos);

    /**
     * 构建菜单树
     * @param menuDtos /
     * @return /
     */
    List<MenuVo> buildMenus(List<MenuDto> menuDtos);

    /**
     * 获取菜单树
     * @param menus /
     * @return /
     */
    Object getMenuTree(List<Menu> menus);


    /**
     * 获取待删除的菜单
     * @param menuList /
     * @param menuSet /
     * @return /
     */
    Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet);

    /**
     * 根据pid查询
     * @param pid /
     * @return /
     */
    List<Menu> findByPid(long pid);

    /**
     * 根据角色查询
     * @param roles /
     * @return /
     */
    List<MenuDto> findByRoles(List<RoleSmallDto> roles);

    /**
     * 删除
     * @param menuSet /
     */
    void delete(Set<Menu> menuSet);

    /**
     * 编辑
     * @param resources /
     */
    void update(Menu resources);

    Object create(Menu resources);
}
