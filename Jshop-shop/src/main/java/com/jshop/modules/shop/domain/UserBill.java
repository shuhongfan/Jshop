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

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jack胡
 */

@Data
@TableName("user_bill")
public class UserBill implements Serializable {

    /** 用户账单id */
    @TableId
    private Integer id;


    /** 用户uid */
    private Integer uid;


    /** 关联id */
    private String linkId;


    /** 0 = 支出 1 = 获得 */
    private Integer pm;


    /** 账单标题 */
    private String title;


    /** 明细种类 */
    private String category;


    /** 明细类型 */
    private String type;


    /** 明细数字 */
    private BigDecimal number;


    /** 剩余 */
    private BigDecimal balance;


    /** 备注 */
    private String mark;


    /** 添加时间 */
    @TableField(fill= FieldFill.INSERT)
    private Integer addTime;


    /** 0 = 带确定 1 = 有效 -1 = 无效 */
    private Integer status;


    public void copy(UserBill source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
