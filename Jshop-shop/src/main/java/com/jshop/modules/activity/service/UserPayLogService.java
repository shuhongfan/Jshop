package com.jshop.modules.activity.service;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserPayLogService {

    Map<String, Object> queryAll(Pageable pageable);
}
