/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jack胡
 */
@Data
public class SystemConfigDto implements Serializable {

    /** 配置id */
    private Integer id;

    /** 字段名称 */
    private String menuName;

    /** 默认值 */
    private String value;

    /** 排序 */
    private Integer sort;

    /** 是否隐藏 */
    private Integer status;
}
