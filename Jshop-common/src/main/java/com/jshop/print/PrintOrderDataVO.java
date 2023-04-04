/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.print;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ：jack胡
 * @description：打印数据VO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrintOrderDataVO implements Serializable{
    @ApiModelProperty(value = "订单号")
    private String orderId;
    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "用户姓名")
    private String realName;
    @ApiModelProperty(value = "详细地址")
    private String userAddress;
    @ApiModelProperty(value = "用户电话")
    private String userPhone;
    @ApiModelProperty(value = "备注")
    private String mark;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
}
