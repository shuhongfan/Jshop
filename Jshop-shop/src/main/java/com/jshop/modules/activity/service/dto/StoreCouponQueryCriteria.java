/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.service.dto;

import com.jshop.annotation.Query;
import lombok.Data;

/**
* @author jack胡
*/
@Data
public class StoreCouponQueryCriteria {

    @Query
    private Integer isDel;
}
