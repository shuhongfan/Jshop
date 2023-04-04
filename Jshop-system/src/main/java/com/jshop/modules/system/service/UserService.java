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
import com.jshop.modules.system.domain.User;
import com.jshop.modules.system.service.dto.UserDto;
import com.jshop.modules.system.service.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jack胡
 */
public interface UserService  extends BaseService<User>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UserQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UserDto>
    */
    List<User> queryAll(UserQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<UserDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据用户名查询
     * @param userName /
     * @return /
     */
    UserDto findByName(String userName);

    /**
     * 修改密码
     * @param username 用户名
     * @param encryptPassword 密码
     */
    void updatePass(String username, String encryptPassword);

    /**
     * 修改头像
     * @param multipartFile 文件
     */
    void updateAvatar(MultipartFile multipartFile);

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    void updateEmail(String username, String email);

    /**
     * 新增用户
     * @param resources /
     * @return /
     */
    boolean create(User resources);

    /**
     * 编辑用户
     * @param resources /
     */
    void update(User resources);

    void delete(Set<Long> ids);
}
