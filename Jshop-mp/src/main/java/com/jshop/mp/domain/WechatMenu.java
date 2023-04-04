/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.mp.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
* @author jack胡
*/
@Data
@TableName("wechat_menu")
public class WechatMenu implements Serializable {

    @TableId(value = "`key`")
    private String key;


    /** 缓存数据 */
    private String result;


    /** 缓存时间 */
    @TableField(fill= FieldFill.INSERT)
    private Integer addTime;


    public void copy(WechatMenu source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
