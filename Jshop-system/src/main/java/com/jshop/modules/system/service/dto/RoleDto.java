/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.kaikeba.co
* 注意：
* 本软件为www.kaikeba.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.jshop.modules.system.service.dto;

import com.jshop.modules.system.domain.Menu;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
* @author hupeng
* @date 2020-05-14
*/
@Data
public class RoleDto implements Serializable {

    /** ID */
    private Long id;

    /** 名称 */
    private String name;

    /** 备注 */
    private String remark;

    /** 数据权限 */
    private String dataScope;

    /** 角色级别 */
    private Integer level;

    private Set<Menu> menus;

    /** 创建日期 */
    private Timestamp createTime;

    /** 功能权限 */
    private String permission;
}
