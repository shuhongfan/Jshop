/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @author jack胡
*/
@Data
public class StoreCouponIssueUserDto implements Serializable {

    private Integer id;

    /** 领取优惠券用户ID */
    private Integer uid;

    /** 优惠券前台领取ID */
    private Integer issueCouponId;

    /** 领取时间 */
    private Integer addTime;
}
