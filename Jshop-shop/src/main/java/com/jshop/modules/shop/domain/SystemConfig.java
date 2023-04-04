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

import java.io.Serializable;

/**
 * @author jack胡
 */
@Data
@TableName("system_config")
public class SystemConfig implements Serializable {

    /** 配置id */
    @TableId
    private Integer id;


    /** 字段名称 */
    private String menuName;


    /** 默认值 */
    private String value;


    /** 排序 */
    private Integer sort;


    /** 是否隐藏 */
    private Integer status;


    public void copy(SystemConfig source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
