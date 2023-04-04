/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jack胡
 */

@Data
@TableName("store_product_attr_value")
public class StoreProductAttrValue implements Serializable {

    @TableId
    private Integer id;


    /** 商品ID */
    private Integer productId;


    /** 商品属性索引值 (attr_value|attr_value[|....]) */
    private String suk;


    /** 属性对应的库存 */
    private Integer stock;


    /** 销量 */
    private Integer sales;


    /** 属性金额 */
    private BigDecimal price;


    /** 图片 */
    private String image;


    /** 唯一值 */
     @TableField(value = "`unique`")
    private String unique;


    /** 成本价 */
    private BigDecimal cost;


    public void copy(StoreProductAttrValue source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
