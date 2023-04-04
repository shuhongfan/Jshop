package com.jshop.modules.activity.service.impl;

import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.activity.domain.TbPayLog;
import com.jshop.modules.activity.service.UserPayLogService;
import com.jshop.modules.activity.service.mapper.UserPayLogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserPayLogServiceImpl extends BaseServiceImpl<UserPayLogMapper, TbPayLog> implements UserPayLogService {

    @Autowired
    private UserPayLogMapper payLogMapper;

    private final IGenerator generator;


    @Override
    public Map<String, Object> queryAll(Pageable pageable) {
        getPage(pageable);
        PageInfo<TbPayLog> page = new PageInfo<>(payLogMapper.selectList(null));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), TbPayLog.class));
        map.put("totalElements", page.getTotal());
        return map;
    }

    protected void getPage(Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber()+1, pageable.getPageSize());
    }
}
