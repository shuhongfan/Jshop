/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author hupeng
 * @date 2018-12-03
 */
@Data
public class PermissionDto implements Serializable{

	private Long id;

	private String name;

	private Long pid;

	private String alias;

	private Timestamp createTime;

	private List<PermissionDto>  children;

	@Override
	public String toString() {
		return "Permission{" +
				"id=" + id +
				", name='" + name + '\'' +
				", pid=" + pid +
				", alias='" + alias + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
