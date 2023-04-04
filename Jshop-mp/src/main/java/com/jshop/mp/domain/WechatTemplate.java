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
@TableName("wechat_template")
public class WechatTemplate implements Serializable {

    /** 模板id */
    @TableId
    private Integer id;


    /** 模板编号 */
    private String tempkey;


    /** 模板名 */
    private String name;


    /** 回复内容 */
    private String content;


    /** 模板ID */
    private String tempid;


    /** 添加时间 */
    @TableField(fill= FieldFill.INSERT)
    private String addTime;


    /** 状态 */
    private Integer status;


    public void copy(WechatTemplate source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
