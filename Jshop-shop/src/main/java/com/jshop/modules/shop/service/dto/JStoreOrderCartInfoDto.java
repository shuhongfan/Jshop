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
public class JStoreOrderCartInfoDto implements Serializable {

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
    private String unique;
}
