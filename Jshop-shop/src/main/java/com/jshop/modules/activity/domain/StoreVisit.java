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
@TableName("store_visit")
public class StoreVisit implements Serializable {

    @TableId
    private Integer id;


    /** 产品ID */
    private Integer productId;


    /** 产品类型 */
    private String productType;


    /** 产品分类ID */
    private Integer cateId;


    /** 产品类型 */
    private String type;


    /** 用户ID */
    private Integer uid;


    /** 访问次数 */
    private Integer count;


    /** 备注描述 */
    private String content;


    /** 添加时间 */
    private Integer addTime;


    public void copy(StoreVisit source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
