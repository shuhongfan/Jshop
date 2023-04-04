/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author jack胡
*/
@Data
public class UserExtractDto implements Serializable {


    private Integer id;

    private Integer uid;

    // 名称
    private String realName;

    // bank = 银行卡 alipay = 支付宝wx=微信
    private String extractType;

    // 银行卡
    private String bankCode;

    // 开户地址
    private String bankAddress;

    // 支付宝账号
    private String alipayCode;

    // 提现金额
    private BigDecimal extractPrice;

    private String mark;

    private BigDecimal balance;

    // 无效原因
    private String failMsg;

    private Integer failTime;

    // 添加时间
    private Integer addTime;

    // -1 未通过 0 审核中 1 已提现
    private Integer status;

    // 微信号
    private String wechat;
}
