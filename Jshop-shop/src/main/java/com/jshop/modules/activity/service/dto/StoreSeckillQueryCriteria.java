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
public class StoreSeckillQueryCriteria {


    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String title;
}
