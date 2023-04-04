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
public class StoreCartDto implements Serializable {

    /** 购物车表ID */
    private Long id;

    /** 用户ID */
    private Integer uid;

    /** 类型 */
    private String type;

    /** 商品ID */
    private Integer productId;

    /** 商品属性 */
    private String productAttrUnique;

    /** 商品数量 */
    private Integer cartNum;

    /** 添加时间 */
    private Integer addTime;

    /** 0 = 未购买 1 = 已购买 */
    private Integer isPay;

    /** 是否删除 */
    private Integer isDel;

    /** 是否为立即购买 */
    private Integer isNew;

    /** 拼团id */
    private Integer combinationId;

    /** 秒杀产品ID */
    private Integer seckillId;

    /** 砍价id */
    private Integer bargainId;
}
