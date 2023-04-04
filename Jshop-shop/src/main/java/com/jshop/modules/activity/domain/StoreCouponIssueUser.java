/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.domain;

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
@TableName("store_coupon_issue_user")
public class StoreCouponIssueUser implements Serializable {

    @TableId
    private Integer id;


    /** 领取优惠券用户ID */
    private Integer uid;


    /** 优惠券前台领取ID */
    private Integer issueCouponId;


    /** 领取时间 */
    private Integer addTime;


    public void copy(StoreCouponIssueUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
