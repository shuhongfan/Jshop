/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jack胡
 */
@Data
public class StoreCategoryDto implements Serializable {

    /** 商品分类表ID */
    private Integer id;

    /** 父id */
    private Integer pid;

    /** 分类名称 */
    private String cateName;

    /** 排序 */
    private Integer sort;

    /** 图标 */
    private String pic;

    /** 是否推荐 */
    private Integer isShow;

    /** 添加时间 */
    private Integer addTime;

    /** 删除状态 */
    private Integer isDel;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StoreCategoryDto> children;

    public String getLabel() {
        return cateName;
    }
}
