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
@TableName("store_product_attr_result")
public class StoreProductAttrResult implements Serializable {

    @TableId
    private Integer id;


    /** 商品ID */
    private Integer productId;


    /** 商品属性参数 */
    private String result;


    /** 上次修改时间 */
    private Integer changeTime;


    public void copy(StoreProductAttrResult source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
