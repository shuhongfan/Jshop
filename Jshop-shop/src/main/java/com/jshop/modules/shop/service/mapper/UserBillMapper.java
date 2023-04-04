/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.mapper;

import com.jshop.common.mapper.CoreMapper;
import com.jshop.modules.shop.domain.UserBill;
import com.jshop.modules.shop.service.dto.JUserBillDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jack胡
 */
@Repository
@Mapper
public interface UserBillMapper extends CoreMapper<UserBill> {

    @Select("<script> select b.title,b.pm,b.category,b.type,b.number,b.add_time ,u.nickname " +
            "from user_bill b left join tb_user u on u.uid=b.uid  where 1=1  "  +
            "<if test =\"category !=''\">and b.category=#{category}</if> " +
            "<if test =\"type !=''\">and b.type=#{type}</if> " +
            "<if test =\"nickname !=''\">and u.nickname= LIKE CONCAT('%',#{nickname},'%')</if> </script> ")
    List<JUserBillDto> findAllByQueryCriteria(@Param("category") String category, @Param("type") String type, @Param("nickname") String nickname);
}
