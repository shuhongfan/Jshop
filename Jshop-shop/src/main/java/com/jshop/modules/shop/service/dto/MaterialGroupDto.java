/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author jack胡
 */
@Data
public class MaterialGroupDto implements Serializable {
    /** PK */
    private String id;

    /** 逻辑删除标记（0：显示；1：隐藏） */
    private String delFlag;

    /** 创建时间 */
    private Timestamp createTime;

    /** 创建者ID */
    private String createId;

    /** 分组名 */
    private String name;
}
