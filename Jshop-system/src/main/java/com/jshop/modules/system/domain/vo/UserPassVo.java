/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.system.domain.vo;

import lombok.Data;

/**
 * 修改密码的 Vo 类
 * @author jack胡
 */
@Data
public class UserPassVo {

    private String oldPass;

    private String newPass;
}
