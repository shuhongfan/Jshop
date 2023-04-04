/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.kaikeba.co
* 注意：
* 本软件为www.kaikeba.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.jshop.modules.system.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.system.domain.RolesMenus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author hupeng
* @date 2020-05-16
*/
@Repository
@Mapper
public interface RolesMenusMapper extends CoreMapper<RolesMenus> {

}
