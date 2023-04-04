/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author jack胡
 */

@Data
public class ProductFormatDto {

    private Double price;

    private Double cost;

    private int sales;

    private String pic;

   // private Map<String, List<Map<String,String>>> detail;

    //private List<Map<String, String>> detail;
    private Map<String, String> detail;
    private Boolean check;


}
