/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jack胡
 */
@Data
public class TbUser implements Serializable {

    /** 用户id */
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;


    /** 用户账户(跟accout一样) */
    private String username;


    /** 用户账号 */
    private String account;


    /** 用户密码（跟pwd） */
    private String password;


    /** 用户密码 */
    private String pwd;


    /** 真实姓名 */
    private String realName;


    /** 生日 */
    private Integer birthday;


    /** 身份证号码 */
    private String cardId;


    /** 用户备注 */
    private String mark;


    /** 合伙人id */
    private Integer partnerId;


    /** 用户分组id */
    private Integer groupId;


    /** 用户昵称 */
    private String nickname;


    /** 用户头像 */
    private String avatar;


    /** 手机号码 */
    private String phone;


    /** 添加时间 */
    @TableField(fill= FieldFill.INSERT)
    private Integer addTime;


    /** 添加ip */
    private String addIp;


    /** 最后一次登录时间 */
    private Integer lastTime;


    /** 最后一次登录ip */
    private String lastIp;


    /** 用户余额 */
    private BigDecimal nowMoney;


    /** 佣金金额 */
    private BigDecimal brokeragePrice;


    /** 用户剩余积分 */
    private BigDecimal integral;


    /** 连续签到天数 */
    private Integer signNum;


    /** 1为正常，0为禁止 */
    private Integer status;


    /** 等级 */
    private Integer level;


    /** 推广元id */
    private Integer spreadUid;


    /** 推广员关联时间 */
    private Integer spreadTime;


    /** 用户类型 */
    private String userType;


    /** 是否为推广员 */
    private Integer isPromoter;


    /** 用户购买次数 */
    private Integer payCount;


    /** 下级人数 */
    private Integer spreadCount;


    /** 清理会员时间 */
    private Integer cleanTime;


    /** 详细地址 */
    private String addres;


    /** 管理员编号  */
    private Integer adminid;


    /** 用户登陆类型，h5,wechat,routine */
    private String loginType;


    public void copy(TbUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
