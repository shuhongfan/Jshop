/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.mp.service;

import java.util.Map;

public interface WxMpTemplateMessageService {

    String sendWxMpTemplateMessage(String openId, String templateId, String url, Map<String, String> map);
}
