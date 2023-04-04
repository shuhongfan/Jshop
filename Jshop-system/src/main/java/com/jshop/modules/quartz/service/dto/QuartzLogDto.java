/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.quartz.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author jack胡
*/
@Data
public class QuartzLogDto implements Serializable {

    /** 定时任务名称 */
    private String baenName;

    /** Bean名称  */
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
}
