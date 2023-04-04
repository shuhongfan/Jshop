/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @author jack胡
 */

@Data
public class FromatDetailDto {
    private  boolean attrHidden;

    private  String detailValue;

    private List<String> detail;

    private String value;

}
