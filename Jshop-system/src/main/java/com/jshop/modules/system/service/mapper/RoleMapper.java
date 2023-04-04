/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.kaikeba.co
* 注意：
* 本软件为www.kaikeba.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.jshop.modules.system.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.system.domain.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
* @author hupeng
* @date 2020-05-14
*/
@Repository
@Mapper
public interface RoleMapper extends CoreMapper<Role> {

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return
     */
    @Select( "SELECT r.id,r.create_time,r.data_scope,r.`level`,r.`name`,r.permission,r.remark " +
            "FROM role r LEFT OUTER JOIN users_roles u1 ON r.id = u1.role_id " +
            "LEFT OUTER JOIN user u2 ON u1.user_id = u2.id "+
            "WHERE u2.id = #{id}")
    Set<Role> findByUsers_Id(@Param("id") Long id);

    /**
     * 解绑角色菜单
     * @param id 菜单ID
     */
    @Delete("delete from roles_menus where menu_id = #{id}")
    void untiedMenu(@Param("id") Long id);

    /**
     * 根据用户ID查询
     *
     * @param id 用户ID
     * @return
     */
    @Select("select m.* from role m LEFT JOIN users_roles t on m.id= t.role_id LEFT JOIN `user` r on r.id = t.user_id where r.id = #{id}")
    List<Role> selectListByUserId(@Param("id") Long id);

}
