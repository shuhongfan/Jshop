/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.kaikeba.co
* 注意：
* 本软件为www.kaikeba.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.jshop.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author jack胡
*/
@Data
@TableName("menu")
public class Menu implements Serializable {

    /** ID */
    @TableId
    private Long id;


    /** 是否外链 */
    private Boolean iFrame;


    /** 菜单名称 */
    @NotBlank(message = "请填写菜单名称")
    private String name;


    /** 组件 */
    private String component;


    /** 上级菜单ID */
    @NotNull(message = "上级菜单ID不能为空")
    private Long pid;


    /** 排序 */
    @NotNull(message = "排序不能为空")
    private Long sort;


    /** 图标 */
    private String icon;


    /** 链接地址 */
    private String path;


    /** 缓存 */
    private Boolean cache;


    /** 是否隐藏 */
    private Boolean hidden;


    /** 组件名称 */
    private String componentName;


    /** 创建日期 */
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 权限 */
    private String permission;


    /** 类型，目录、菜单、按钮 */
    private Integer type;


    public void copy(Menu source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
