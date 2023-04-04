/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.print;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 打印订单数据
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPrint {
	private String title;
	private String price;
	private String num;
}
