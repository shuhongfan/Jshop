/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.constant;

/**
 * 商城统一常量
 * @author jack胡
 */
public interface ShopConstants {

	/**
	 * 订单自动取消时间（分钟）
	 */
	long ORDER_OUTTIME_UNPAY = 30;
	/**
	 * 订单自动收货时间（天）
	 */
	long ORDER_OUTTIME_UNCONFIRM = 7;
	/**
	 * redis订单未付款key
	 */
	String REDIS_ORDER_OUTTIME_UNPAY = "order:unpay:";
	/**
	 * redis订单收货key
	 */
	String REDIS_ORDER_OUTTIME_UNCONFIRM = "order:unconfirm:";

	/**
	 * redis拼团key
	 */
	String REDIS_PINK_CANCEL_KEY = "pink:cancel:";

	/**
	 * 微信支付service
	 */
	String JSHOP_WEIXIN_PAY_SERVICE = "jshop_weixin_pay_service";

	/**
	 * 微信支付小程序service
	 */
	String JSHOP_WEIXIN_MINI_PAY_SERVICE = "jshop_weixin_mini_pay_service";

	/**
	 * 微信支付app service
	 */
	String JSHOP_WEIXIN_APP_PAY_SERVICE = "jshop_weixin_app_pay_service";

	/**
	 * 微信公众号service
	 */
	String JSHOP_WEIXIN_MP_SERVICE = "jshop_weixin_mp_service";



	/**
	 * 商城默认密码
	 */
	String JSHOP_DEFAULT_PWD = "123456";

	/**
	 * 商城默认注册图片
	 */
	String JSHOP_DEFAULT_AVATAR = "https://image.dayouqiantu.cn/5e79f6cfd33b6.png";

	/**
	 * 腾讯地图地址解析
	 */
	String QQ_MAP_URL = "https://apis.map.qq.com/ws/geocoder/v1/";

	/**
	 * redis首页键
	 */
	String JSHOP_REDIS_INDEX_KEY = "jshop:index_data";

	/**
	 * 充值方案
	 */
	String JSHOP_RECHARGE_PRICE_WAYS = "jshop_recharge_price_ways";
	/**
	 * 首页banner
	 */
	String JSHOP_HOME_BANNER = "jshop_home_banner";
	/**
	 * 首页菜单
	 */
	String JSHOP_HOME_MENUS = "jshop_home_menus";
	/**
	 * 首页滚动新闻
	 */
	String JSHOP_HOME_ROLL_NEWS = "jshop_home_roll_news";
	/**
	 * 热门搜索
	 */
	String JSHOP_HOT_SEARCH = "jshop_hot_search";
	/**
	 * 个人中心菜单
	 */
	String JSHOP_MY_MENUES = "jshop_my_menus";
	/**
	 * 秒杀时间段
	 */
	String JSHOP_SECKILL_TIME = "jshop_seckill_time";
	/**
	 * 签到天数
	 */
	String JSHOP_SIGN_DAY_NUM = "jshop_sign_day_num";

	/**
	 * 打印机配置
	 */
	String JSHOP_ORDER_PRINT_COUNT = "order_print_count";
	/**
	 * 飞蛾用户信息
	 */
	String JSHOP_FEI_E_USER = "fei_e_user";
	/**
	 * 飞蛾用户密钥
	 */
	String JSHOP_FEI_E_UKEY = "fei_e_ukey";

	/**
	 * 打印机配置
	 */
	String JSHOP_ORDER_PRINT_COUNT_DETAIL = "order_print_count_detail";



}
