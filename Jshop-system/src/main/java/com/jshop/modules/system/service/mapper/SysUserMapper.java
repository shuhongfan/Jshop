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
import com.jshop.modules.system.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
* @author hupeng
* @date 2020-05-14
*/
@Repository
@Mapper
public interface SysUserMapper extends CoreMapper<User> {

    /**
     * 修改密码
     * @param username 用户名
     * @param password 密码
     * @param lastPasswordResetTime /
     */
    @Update("update `user` set password = #{password} , last_password_reset_time = #{lastPasswordResetTime} where username = #{username}")
    void updatePass( @Param("password") String password,@Param("lastPasswordResetTime") String lastPasswordResetTime, @Param("username") String username);

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    @Update("update `user` set email = #{email} where username = #{username}")
    void updateEmail(@Param("email") String email, @Param("username") String username);

    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     */
    @Select("SELECT u.id,u.nick_name,u.sex,u.dept_id,u.enabled,u.create_time,u.phone,u.email,u.job_id ,u.`password` ,u.username,ua.real_name avatar FROM `user` " +
            " u LEFT JOIN user_avatar ua ON u.avatar_id = ua.id  WHERE u.username = #{username}")
    User findByName(String userName);

}
