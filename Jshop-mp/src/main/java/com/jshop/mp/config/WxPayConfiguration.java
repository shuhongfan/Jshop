/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.mp.config;

import com.jshop.mp.handler.RedisHandler;
import com.jshop.utils.RedisUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 支付配置
 * @author jack胡
 */
@Slf4j
@Configuration
public class WxPayConfiguration {

	private static Map<String, WxPayService> payServices = Maps.newHashMap();

	private static RedisHandler redisHandler;

	@Autowired
	public WxPayConfiguration(RedisHandler redisHandler) {
		this.redisHandler = redisHandler;
	}

	/**
	 *  获取WxPayService
	 * @return
	 */
	public static WxPayService getPayService() {
		WxPayService wxPayService = payServices.get(ShopKeyUtils.getJshopWeiXinPayService());
		if(wxPayService == null || RedisUtil.get(ShopKeyUtils.getJshopWeiXinPayService()) == null) {
			WxPayConfig payConfig = new WxPayConfig();
			payConfig.setAppId(RedisUtil.get(ShopKeyUtils.getWechatAppId()));
			payConfig.setMchId(RedisUtil.get(ShopKeyUtils.getWxPayMchId()));
			payConfig.setMchKey(RedisUtil.get(ShopKeyUtils.getWxPayMchKey()));
			payConfig.setKeyPath(RedisUtil.get(ShopKeyUtils.getWxPayKeyPath()));
			// 可以指定是否使用沙箱环境
			payConfig.setUseSandboxEnv(false);
			wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(payConfig);
			payServices.put(ShopKeyUtils.getJshopWeiXinPayService(), wxPayService);

			//增加标识
			RedisUtil.set(ShopKeyUtils.getJshopWeiXinPayService(),"jshop");
		}
		return wxPayService;
    }

	/**
	 *  获取小程序WxAppPayService
	 * @return
	 */
	public static WxPayService getWxAppPayService() {
		WxPayService wxPayService = payServices.get(ShopKeyUtils.getJshopWeiXinMiniPayService());
		if(wxPayService == null || RedisUtil.get(ShopKeyUtils.getJshopWeiXinPayService()) == null) {
			WxPayConfig payConfig = new WxPayConfig();
			payConfig.setAppId(RedisUtil.get(ShopKeyUtils.getWxAppAppId()));
			payConfig.setMchId(RedisUtil.get(ShopKeyUtils.getWxPayMchId()));
			payConfig.setMchKey(RedisUtil.get(ShopKeyUtils.getWxPayMchKey()));
			payConfig.setKeyPath(RedisUtil.get(ShopKeyUtils.getWxPayKeyPath()));
			// 可以指定是否使用沙箱环境
			payConfig.setUseSandboxEnv(false);
			wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(payConfig);
			payServices.put(ShopKeyUtils.getJshopWeiXinMiniPayService(), wxPayService);

			//增加标识
			RedisUtil.set(ShopKeyUtils.getJshopWeiXinPayService(),"jshop");
		}
		return wxPayService;
	}

	/**
	 *  获取APPPayService
	 * @return
	 */
	public static WxPayService getAppPayService() {
		WxPayService wxPayService = payServices.get(ShopKeyUtils.getJshopWeiXinAppPayService());
		if(wxPayService == null || RedisUtil.get(ShopKeyUtils.getJshopWeiXinPayService()) == null) {
			WxPayConfig payConfig = new WxPayConfig();
			payConfig.setAppId(RedisUtil.get(ShopKeyUtils.getWxNativeAppAppId()));
			payConfig.setMchId(RedisUtil.get(ShopKeyUtils.getWxPayMchId()));
			payConfig.setMchKey(RedisUtil.get(ShopKeyUtils.getWxPayMchKey()));
			payConfig.setKeyPath(RedisUtil.get(ShopKeyUtils.getWxPayKeyPath()));
			// 可以指定是否使用沙箱环境
			payConfig.setUseSandboxEnv(false);
			wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(payConfig);
			payServices.put(ShopKeyUtils.getJshopWeiXinAppPayService(), wxPayService);

			//增加标识
			RedisUtil.set(ShopKeyUtils.getJshopWeiXinPayService(),"jshop");
		}
		return wxPayService;
	}

	/**
	 * 移除WxPayService
	 */
	public static void removeWxPayService(){
		RedisUtil.del(ShopKeyUtils.getJshopWeiXinPayService());
		payServices.remove(ShopKeyUtils.getJshopWeiXinPayService());
		payServices.remove(ShopKeyUtils.getJshopWeiXinMiniPayService());
		payServices.remove(ShopKeyUtils.getJshopWeiXinAppPayService());
	}

}
