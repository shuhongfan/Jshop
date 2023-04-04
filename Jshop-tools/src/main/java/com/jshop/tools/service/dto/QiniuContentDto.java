/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.tools.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author jack胡
 */
@Data
public class QiniuContentDto implements Serializable {

    /** ID */
    private Long id;

    /** Bucket 识别符 */
    private String bucket;

    /** 文件名称 */
    private String name;

    /** 文件大小 */
    private String size;

    /** 文件类型：私有或公开 */
    private String type;

    /** 上传或同步的时间 */
    private Timestamp updateTime;

    /** 文件url */
    private String url;

    private String suffix;

    public String getKey(){
        return this.name;
    }
}
