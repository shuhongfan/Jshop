/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jack胡
 */
@Data
public class UserRechargeDto implements Serializable {

    private Integer id;

    /** 充值用户UID */
    private Integer uid;

    /** 订单号 */
    private String orderId;

    /** 充值金额 */
    private BigDecimal price;

    /** 充值类型 */
    private String rechargeType;

    /** 是否充值 */
    private Integer paid;

    /** 充值支付时间 */
    private Integer payTime;

    /** 充值时间 */
    private Integer addTime;

    /** 退款金额 */
    private BigDecimal refundPrice;

    /** 昵称 */
    private String nickname;
}
