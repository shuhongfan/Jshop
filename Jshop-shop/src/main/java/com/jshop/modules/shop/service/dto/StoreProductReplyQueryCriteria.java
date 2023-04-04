/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import com.jshop.annotation.Query;
import lombok.Data;

/**
 * @author jack胡
 */
@Data
public class StoreProductReplyQueryCriteria {
    @Query
    private Integer isDel;
}
