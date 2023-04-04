/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.quartz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author jack胡
*/

@Data
@TableName("quartz_log")
public class QuartzLog implements Serializable {

    /** 任务日志ID */
    @TableId
    private Long id;


    /** 任务名称 */
    private String baenName;


    /** 创建时间  */
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** cron表达式 */
    private String cronExpression;


    /** 异常详细  */
    private String exceptionDetail;


    /** 状态 */
    private Boolean isSuccess;


    /** 任务名称 */
    private String jobName;


    /** 方法名称 */
    private String methodName;


    /** 参数 */
    private String params;


    /** 耗时（毫秒） */
    private Long time;


    public void copy(QuartzLog source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
