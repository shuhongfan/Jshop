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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author jack胡
*/
@Data
@TableName("store_coupon_issue")
public class StoreCouponIssue implements Serializable {

    @TableId
    private Integer id;


    private String cname;


    /** 优惠券ID */
    private Integer cid;


    /** 优惠券领取开启时间 */
    private Integer startTime;


    /** 优惠券领取结束时间 */
    private Integer endTime;


    /** 优惠券领取数量 */
    private Integer totalCount;


    /** 优惠券剩余领取数量 */
    private Integer remainCount;


    /** 是否无限张数 */
    private Integer isPermanent;


    /** 1 正常 0 未开启 -1 已无效 */
    private Integer status;


    private Integer isDel;


    /** 优惠券添加时间 */
    private Integer addTime;


    @NotNull(message = "请选择结束时间")
    private Timestamp endTimeDate;

    @NotNull(message = "请选择开始时间")
    private Timestamp startTimeDate;


    public void copy(StoreCouponIssue source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
