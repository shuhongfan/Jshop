/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.mp.service;

import com.jshop.common.service.BaseService;
import com.jshop.mp.domain.WechatReply;
import com.jshop.mp.service.dto.WechatReplyDto;
import com.jshop.mp.service.dto.WechatReplyQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author jack胡
*/
public interface WechatReplyService extends BaseService<WechatReply> {

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(WechatReplyQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<WechatReply>
    */
    List<WechatReply> queryAll(WechatReplyQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<WechatReplyDto> all, HttpServletResponse response) throws IOException;

    WechatReply isExist(String subscribe);

    void create(WechatReply wechatReply);

    void upDate(WechatReply wechatReply);
}
