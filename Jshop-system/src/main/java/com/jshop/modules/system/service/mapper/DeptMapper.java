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
import com.jshop.modules.system.domain.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
* @author hupeng
* @date 2020-05-14
*/
@Repository
@Mapper
public interface DeptMapper extends CoreMapper<Dept> {

    @Select("select m.* from dept m LEFT JOIN roles_depts t on m.id= t.dept_id LEFT JOIN role r on r.id = t.role_id where r.id = ${roleId}")
    Set<Dept> findDeptByRoleId(@Param("roleId") Long roleId);

    @Select("select * from dept m LEFT JOIN roles_depts t on m.id= t.dept_id LEFT JOIN role r on r.id = t.role_id where r.id = #{roleId}")
    Set<Dept> findDeptByRoleIds(@Param("roleIds") Set<Long> roleId);
}
