/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jack胡
 */
@Data
public class ExpressParam implements Serializable {
    ////@NotBlank()
    private String orderCode;
    private String shipperCode;
    private String logisticCode;
}
