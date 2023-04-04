/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author jack胡
 */
@Data
@TableName("store_category")
public class StoreCategory implements Serializable {

    /** 商品分类表ID */
    @TableId
    private Integer id;


    /** 父id */
    private Integer pid;


    /** 分类名称 */
    @NotBlank(message = "分类名称必填")
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


    public void copy(StoreCategory source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
