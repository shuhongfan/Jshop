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

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
* @author jack胡
*/
@Data
@TableName("tb_seckill_goods")
public class StoreSeckill implements Serializable {

    /** 商品秒杀产品表id */
    @TableId
    private Integer id;


    /** 商品id */
    private Integer productId;


    /** 推荐图 */
    @NotBlank(message = "请上传商品图片")
    private String image;


    /** 轮播图 */
    @NotBlank(message = "请上传商品轮播")
    private String images;


    /** 活动标题 */
    @NotBlank(message = "请填写秒杀名称")
    private String title;


    /** 简介 */
    private String info;


    /** 价格 */
    @NotNull(message = "请输入秒杀价")
    @DecimalMin(value="0.00", message = "秒杀价不在合法范围内" )
    @DecimalMax(value="99999999.99", message = "秒杀价不在合法范围内")
    private BigDecimal price;


    /** 成本 */
    private BigDecimal costPrice;


    /** 原价 */
    private BigDecimal otPrice;


    /** 返多少积分 */
    private BigDecimal giveIntegral;


    /** 排序 */
    private Integer sort;


    /** 库存 */
    @NotNull(message = "请输入库存")
    @Min(message = "库存不能小于0",value = 1)
    private Integer stock;

    /** 剩余库存 */
    private Integer stockCount;

    /** 销量 */
    private Integer sales;


    /** 单位名 */
    private String unitName;


    /** 邮费 */
    private BigDecimal postage;


    /** 内容 */
    @NotBlank(message = "请填写详情")
    private String description;


    /** 开始时间 */
    private Integer startTime;


    /** 结束时间 */
    private Integer stopTime;


    /** 添加时间 */
    private String addTime;


    /** 产品状态 */
    private Integer status;


    /** 是否包邮 */
    private Integer isPostage;


    /** 热门推荐 */
    private Integer isHot;


    /** 删除 0未删除1已删除 */
    private Integer isDel;


    /** 最多秒杀几个 */
    @NotNull(message = "请输入限购")
    @Min(message = "限购不能小于0",value = 1)
    private Integer num;


    /** 显示 */
    private Integer isShow;

    @NotNull(message = "请选择秒杀结束时间")
    private Timestamp endTimeDate;

    @NotNull(message = "请选择秒杀开始时间")
    private Timestamp startTimeDate;


    /** 时间段id */
    @NotNull(message = "请选择开始时间")
    private Integer timeId;


    public void copy(StoreSeckill source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
