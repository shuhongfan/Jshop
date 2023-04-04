/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jack胡
 */
@Data
public class SystemStoreDto implements Serializable {

    private Integer id;

    /** 门店名称 */
    private String name;

    /** 简介 */
    private String introduction;

    /** 手机号码 */
    private String phone;

    /** 省市区 */
    private String address;

    /** 详细地址 */
    private String detailedAddress;

    /** 门店logo */
    private String image;

    /** 纬度 */
    private String latitude;

    /** 经度 */
    private String longitude;

    /** 核销有效日期 */
    private String validTime;

    /** 每日营业开关时间 */
    private String dayTime;

    /** 添加时间 */
    private Integer addTime;

    /** 是否显示 */
    private Integer isShow;

    /** 是否删除 */
    private Integer isDel;

    private Date validTimeEnd;

    private Date validTimeStart;

    private Date dayTimeStart;

    private Date dayTimeEnd;

}
