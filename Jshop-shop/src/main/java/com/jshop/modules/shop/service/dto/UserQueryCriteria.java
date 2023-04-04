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
public class UserQueryCriteria {

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String nickname;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String phone;

    @Query
    private Integer isPromoter;

    @Query
    private String userType;
}
