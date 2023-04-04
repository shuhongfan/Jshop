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
@TableName("express")
public class Express implements Serializable {

    /** 快递公司id */
    @TableId
    private Integer id;


    /** 快递公司简称 */
    @NotBlank(message = "请输入快递公司编号")
    private String code;


    /** 快递公司全称 */
    @NotBlank(message = "请输入快递公司名称")

    private String name;


    /** 排序 */
    private Integer sort;


    /** 是否显示 */
    private Integer isShow;


    public void copy(Express source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
