/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import com.jshop.constant.ShopConstants;
import com.jshop.constant.SystemConfigConstants;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.shop.domain.SystemConfig;
import com.jshop.modules.shop.service.SystemConfigService;
import com.jshop.modules.shop.service.dto.SystemConfigQueryCriteria;
import com.jshop.mp.config.WxMpConfiguration;
import com.jshop.mp.config.WxPayConfiguration;
import com.jshop.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
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
@Api(tags = "商城:配置管理")
@RestController
@RequestMapping("api")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/SystemConfig")
    @PreAuthorize("@el.check('admin','SYSTEMCONFIG_ALL','SYSTEMCONFIG_SELECT')")
    public ResponseEntity getSystemConfigs(SystemConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(systemConfigService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增或修改")
    @ApiOperation(value = "新增或修改")
    @PostMapping(value = "/SystemConfig")
    @CacheEvict(cacheNames = ShopConstants.JSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("@el.check('admin','SYSTEMCONFIG_ALL','SYSTEMCONFIG_CREATE')")
    public ResponseEntity create(@RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        jsonObject.forEach(
                (key,value)->{
                    SystemConfig systemConfig = systemConfigService.getOne(new LambdaQueryWrapper<SystemConfig>()
                            .eq(SystemConfig::getMenuName,key));
                    SystemConfig systemConfigModel = new SystemConfig();
                    systemConfigModel.setMenuName(key);
                    systemConfigModel.setValue(value.toString());
                    //重新配置微信相关
                    if(SystemConfigConstants.WECHAT_APPID.equals(key)){
                        WxMpConfiguration.removeWxMpService();
                        WxPayConfiguration.removeWxPayService();
                    }
                    if(SystemConfigConstants.WXPAY_MCHID.equals(key) || SystemConfigConstants.WXAPP_APPID.equals(key)){
                        WxPayConfiguration.removeWxPayService();
                    }
                    RedisUtil.set(key,value.toString(),0);
                    if(ObjectUtil.isNull(systemConfig)){
                        systemConfigService.save(systemConfigModel);
                    }else{
                        systemConfigModel.setId(systemConfig.getId());
                        systemConfigService.saveOrUpdate(systemConfigModel);
                    }
                }
        );

        return new ResponseEntity(HttpStatus.CREATED);
    }



}
