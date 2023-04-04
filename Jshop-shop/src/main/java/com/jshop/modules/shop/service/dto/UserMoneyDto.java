/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author jack胡
 */
@Data
public class UserMoneyDto implements Serializable {
   //@NotNull(message = "参数缺失")
    private Integer uid;
   //@NotNull(message = "请选择修改余额方式")
    private Integer ptype;
   //@NotNull(message = "金额必填")
    @Min(message = "最低金额为0",value = 0)
    private Double money;
}
