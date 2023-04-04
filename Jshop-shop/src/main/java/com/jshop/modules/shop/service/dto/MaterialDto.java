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
public class MaterialDto implements Serializable {

    /** PK */
    private String id;

    /** 所属租户 */
    private String userId;

    /** 逻辑删除标记（0：显示；1：隐藏） */

    /** 创建时间 */
    private Timestamp createTime;

    /** 最后更新时间 */
    private Timestamp updateTime;

    /** 创建者ID */
    private String createId;

    /** 类型1、图片；2、视频 */
    private String type;

    /** 分组ID */
    private String groupId;

    /** 素材名 */
    private String name;

    /** 素材链接 */
    private String url;
}
