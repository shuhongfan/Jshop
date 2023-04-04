/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.domain;

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
@TableName("store_order_status")
public class StoreOrderStatus implements Serializable {

    @TableId
    private Integer id;


    /** 订单id */
    private Integer oid;


    /** 操作类型 */
    private String changeType;


    /** 操作备注 */
    private String changeMessage;


    /** 操作时间 */
    private Integer changeTime;


    public void copy(StoreOrderStatus source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
