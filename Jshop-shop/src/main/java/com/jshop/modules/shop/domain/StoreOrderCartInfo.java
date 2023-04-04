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

/**
 * @author jack胡
 */

@Data
@TableName("store_order_cart_info")
public class StoreOrderCartInfo implements Serializable {

    @TableId
    private Integer id;


    /** 订单id */
    private Integer oid;


    /** 购物车id */
    private Integer cartId;


    /** 商品ID */
    private Integer productId;


    /** 购买东西的详细信息 */
    private String cartInfo;


    /** 唯一id */
    @TableField(value = "`unique`")
    private String unique;


    public void copy(StoreOrderCartInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
