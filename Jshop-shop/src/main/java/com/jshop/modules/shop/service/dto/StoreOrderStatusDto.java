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
public class StoreOrderStatusDto implements Serializable {

    private Integer id;

    /** 订单id */
    private Integer oid;

    /** 操作类型 */
    private String changeType;

    /** 操作备注 */
    private String changeMessage;

    /** 操作时间 */
    private Integer changeTime;
}
