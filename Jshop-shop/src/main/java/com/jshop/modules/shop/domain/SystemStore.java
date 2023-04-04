/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jack胡
 */

@Data
@TableName("system_store")
public class SystemStore implements Serializable {

    @TableId
    private Integer id;


    /** 门店名称 */
    @NotBlank(message = "请填写门店名称")
    private String name;


    /** 简介 */
    @NotBlank(message = "请填写门店简介")
    private String introduction;


    /** 手机号码 */
    @NotBlank(message = "请填手机号码")
    private String phone;


    /** 省市区 */
    @NotBlank(message = "请填地址")
    private String address;


    /** 详细地址 */
    private String detailedAddress;


    /** 门店logo */
    @NotBlank(message = "请上传门店logo")
    private String image;


    /** 纬度 */
    @NotBlank(message = "请输入纬度")
    private String latitude;


    /** 经度 */
    @NotBlank(message = "请输入经度")
    private String longitude;


    /** 核销有效日期 */
    @NotBlank(message = "请输入核销时效")
    private String validTime;


    /** 每日营业开关时间 */
    @NotBlank(message = "请输入营业时间")
    private String dayTime;


    /** 添加时间 */
    @TableField(fill= FieldFill.INSERT)
    private Integer addTime;


    /** 是否显示 */
    private Integer isShow;


    /** 是否删除 */
    private Integer isDel;

    private Date validTimeEnd;

    private Date validTimeStart;

    private Date dayTimeStart;

    private Date dayTimeEnd;


    public void copy(SystemStore source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
