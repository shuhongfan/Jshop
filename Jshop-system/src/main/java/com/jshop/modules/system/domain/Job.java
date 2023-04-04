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
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author jack胡
*/
@Data
@TableName("job")
public class Job implements Serializable {

    /** 岗位ID */
    @TableId
    private Long id;

    /** 岗位名称 */
    @NotBlank(message = "岗位名称不能为空")
    private String name;


    /** 岗位状态 */
    private Boolean enabled;

    @TableField(exist = false)
    private Dept dept;

    /** 岗位排序 */
    private Long sort;


    /** 部门ID */
    private Long deptId;


    /** 创建日期 */
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    public void copy(Job source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
