/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.mp.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
* @author jack胡
*/
@Data
@TableName("wechat_reply")
public class WechatReply implements Serializable {

    /** 微信关键字回复id */
    @TableId
    private Integer id;


    /** 关键字 */
    @TableField(value = "`key`")
    private String key;


    /** 回复类型 */
    private String type;


    /** 回复数据 */
    private String data;


    /** 0=不可用  1 =可用 */
    private Integer status;


    /** 是否隐藏 */
    private Integer hide;


    public void copy(WechatReply source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
