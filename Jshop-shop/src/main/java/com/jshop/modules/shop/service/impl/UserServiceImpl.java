/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.modules.shop.domain.TbUser;
import com.jshop.modules.shop.domain.UserBill;
import com.jshop.modules.shop.service.UserBillService;
import com.jshop.modules.shop.service.UserService;
import com.jshop.modules.shop.service.dto.UserMoneyDto;
import com.jshop.modules.shop.service.dto.UserDto;
import com.jshop.modules.shop.service.dto.UserQueryCriteria;
import com.jshop.modules.shop.service.mapper.UserMapper;
import com.jshop.utils.FileUtil;
import com.jshop.utils.OrderUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author jack胡
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "user")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends BaseServiceImpl<UserMapper, TbUser> implements UserService {

    private final IGenerator generator;

    private final UserMapper userMapper;

    private final UserBillService userBillService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(UserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<TbUser> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), UserDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<TbUser> queryAll(UserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(TbUser.class, criteria));
    }


    @Override
    public void download(List<UserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDto user : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户账户(跟accout一样)", user.getUsername());
            map.put("用户账号", user.getAccount());
            map.put("用户密码（跟pwd）", user.getPassword());
            map.put("用户密码", user.getPwd());
            map.put("真实姓名", user.getRealName());
            map.put("生日", user.getBirthday());
            map.put("身份证号码", user.getCardId());
            map.put("用户备注", user.getMark());
            map.put("合伙人id", user.getPartnerId());
            map.put("用户分组id", user.getGroupId());
            map.put("用户昵称", user.getNickname());
            map.put("用户头像", user.getAvatar());
            map.put("手机号码", user.getPhone());
            map.put("添加时间", user.getAddTime());
            map.put("添加ip", user.getAddIp());
            map.put("最后一次登录时间", user.getLastTime());
            map.put("最后一次登录ip", user.getLastIp());
            map.put("用户余额", user.getNowMoney());
            map.put("佣金金额", user.getBrokeragePrice());
            map.put("用户剩余积分", user.getIntegral());
            map.put("连续签到天数", user.getSignNum());
            map.put("1为正常，0为禁止", user.getStatus());
            map.put("等级", user.getLevel());
            map.put("推广元id", user.getSpreadUid());
            map.put("推广员关联时间", user.getSpreadTime());
            map.put("用户类型", user.getUserType());
            map.put("是否为推广员", user.getIsPromoter());
            map.put("用户购买次数", user.getPayCount());
            map.put("下级人数", user.getSpreadCount());
            map.put("清理会员时间", user.getCleanTime());
            map.put("详细地址", user.getAddres());
            map.put("管理员编号 ", user.getAdminid());
            map.put("用户登陆类型，h5,wechat,routine", user.getLoginType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onStatus(Integer uid, int status) {
        if(status == 1){
            status = 0;
        }else{
            status = 1;
        }

        userMapper.updateOnstatus(status,uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMoney(UserMoneyDto param) {
        UserDto userDTO = generator.convert(getById(param.getUid()), UserDto.class);
        Double newMoney = 0d;
        String mark = "";
        String type = "system_add";
        Integer pm = 1;
        String title = "系统增加余额";
        if(param.getPtype() == 1){
            mark = "系统增加了"+param.getMoney()+"余额";
            newMoney = NumberUtil.add(userDTO.getNowMoney(),param.getMoney()).doubleValue();
        }else{
            title = "系统减少余额";
            mark = "系统扣除了"+param.getMoney()+"余额";
            type = "system_sub";
            pm = 0;
            newMoney = NumberUtil.sub(userDTO.getNowMoney(),param.getMoney()).doubleValue();
            if(newMoney < 0) newMoney = 0d;

        }
        TbUser tbUser = new TbUser();
        tbUser.setUid(userDTO.getUid());
        tbUser.setNowMoney(BigDecimal.valueOf(newMoney));
        saveOrUpdate(tbUser);

        UserBill userBill = new UserBill();
        userBill.setUid(userDTO.getUid());
        userBill.setLinkId("0");
        userBill.setPm(pm);
        userBill.setTitle(title);
        userBill.setCategory("now_money");
        userBill.setType(type);
        userBill.setNumber(BigDecimal.valueOf(param.getMoney()));
        userBill.setBalance(BigDecimal.valueOf(newMoney));
        userBill.setMark(mark);
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        userBill.setStatus(1);
        userBillService.save(userBill);
    }

    @Override
    public void incBrokeragePrice(double price, Integer uid) {
        userMapper.incBrokeragePrice(price,uid);
    }
}
