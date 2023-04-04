/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.mp.controller;


import com.jshop.exception.BadRequestException;
import com.jshop.mp.config.WxMpConfiguration;
import com.jshop.mp.domain.WechatMenu;
import com.jshop.mp.service.WechatMenuService;
import com.jshop.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author jack胡
*/
@Api(tags = "商城:微信菜單")
@RestController
@RequestMapping("api")
@SuppressWarnings("unchecked")
public class WechatMenuController {

    private final WechatMenuService WechatMenuService;

    public WechatMenuController(WechatMenuService WechatMenuService) {
        this.WechatMenuService = WechatMenuService;
    }

    @ApiOperation(value = "查询菜单")
    @GetMapping(value = "/WechatMenu")
    @PreAuthorize("@el.check('admin','WechatMenu_ALL','WechatMenu_SELECT')")
    public ResponseEntity getWechatMenus(){
        return new ResponseEntity(WechatMenuService.getOne(new QueryWrapper<WechatMenu>().eq("`key`","wechat_menus")),HttpStatus.OK);
    }


    @ApiOperation(value = "创建菜单")
    @PostMapping(value = "/WechatMenu")
    @PreAuthorize("@el.check('admin','WechatMenu_ALL','WechatMenu_CREATE')")
    public ResponseEntity create( @RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String jsonButton = jsonObject.get("buttons").toString();
        WechatMenu WechatMenu = new WechatMenu();
        Boolean isExist = WechatMenuService.isExist("wechat_menus");
        WxMenu menu = JSONObject.parseObject(jsonStr,WxMenu.class);

        WxMpService wxService = WxMpConfiguration.getWxMpService();
        if(isExist){
            WechatMenu.setKey("wechat_menus");
            WechatMenu.setResult(jsonButton);
            WechatMenuService.saveOrUpdate(WechatMenu);
        }else {
            WechatMenu.setKey("wechat_menus");
            WechatMenu.setResult(jsonButton);
            WechatMenu.setAddTime(OrderUtil.getSecondTimestampTwo());
            WechatMenuService.save(WechatMenu);
        }


        //创建菜单
        try {
            wxService.getMenuService().menuDelete();
            wxService.getMenuService().menuCreate(menu);
        } catch (WxErrorException e) {
            throw new BadRequestException(e.getMessage());
           // e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK);
    }




}
