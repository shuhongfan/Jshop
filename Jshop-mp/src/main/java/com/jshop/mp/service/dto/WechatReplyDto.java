/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.mp.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @author jack胡
*/
@Data
public class WechatReplyDto implements Serializable {

    /** 微信关键字回复id */
    private Integer id;

    /** 关键字 */
    private String key;

    /** 回复类型 */
    private String type;

    /** 回复数据 */
    private String data;

    /** 0=不可用  1 =可用 */
    private Integer status;

    /** 是否隐藏 */
    private Integer hide;
}
