/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.activity.rest;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jshop.dozer.service.IGenerator;
import com.jshop.exception.BadRequestException;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.domain.UserExtract;
import com.jshop.modules.activity.service.UserExtractService;
import com.jshop.modules.activity.service.dto.UserExtractQueryCriteria;
import com.jshop.modules.shop.domain.TbUser;
import com.jshop.modules.shop.domain.UserBill;
import com.jshop.modules.shop.domain.WechatUser;
import com.jshop.modules.shop.service.UserBillService;
import com.jshop.modules.shop.service.UserService;
import com.jshop.modules.shop.service.WechatUserService;
import com.jshop.modules.shop.service.dto.UserDto;
import com.jshop.modules.shop.service.dto.WechatUserDto;
import com.jshop.mp.service.PayService;
import com.jshop.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.exception.WxPayException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
* @author jack胡
*/
@Api(tags = "商城:提现管理")
@RestController
@RequestMapping("api")
public class UserExtractController {

    private final UserExtractService userExtractService;
    private final UserService userService;
    private final UserBillService userBillService;
    private final WechatUserService wechatUserService;
    private final PayService payService;
    private final IGenerator generator;

    public UserExtractController(UserExtractService userExtractService, UserService userService,
                                 UserBillService userBillService, WechatUserService wechatUserService,
                                 PayService payService, IGenerator generator) {
        this.userExtractService = userExtractService;
        this.userService = userService;
        this.userBillService = userBillService;
        this.wechatUserService = wechatUserService;
        this.payService = payService;
        this.generator = generator;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/UserExtract")
    @PreAuthorize("@el.check('admin','USEREXTRACT_ALL','USEREXTRACT_SELECT')")
    public ResponseEntity getUserExtracts(UserExtractQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(userExtractService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改")
    @ApiOperation(value = "修改审核")
    @PutMapping(value = "/UserExtract")
    @PreAuthorize("@el.check('admin','USEREXTRACT_ALL','USEREXTRACT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody UserExtract resources){
        if(StrUtil.isEmpty(resources.getStatus().toString())){
            throw new BadRequestException("请选择审核状态");
        }
        if(resources.getStatus() != -1 && resources.getStatus() != 1){
            throw new BadRequestException("请选择审核状态");
        }
        if(resources.getStatus() == -1){
            if(StrUtil.isEmpty(resources.getFailMsg())){
                throw new BadRequestException("请填写失败原因");
            }
            String mark = "提现失败,退回佣金"+resources.getExtractPrice()+"元";
            UserDto userDTO = generator.convert(userService.getOne(new QueryWrapper<TbUser>().eq("uid",resources.getUid())), UserDto.class);

            //增加流水
            UserBill userBill = new UserBill();
            userBill.setTitle("提现失败");
            userBill.setUid(resources.getUid());
            userBill.setCategory("now_money");
            userBill.setType("extract");
            userBill.setNumber(resources.getExtractPrice());
            userBill.setLinkId(resources.getId().toString());
            userBill.setBalance(NumberUtil.add(userDTO.getBrokeragePrice(),resources.getExtractPrice()));
            userBill.setMark(mark);
            userBill.setStatus(1);
            userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
            userBill.setPm(1);
            userBillService.save(userBill);

            //返回提现金额
            userService.incBrokeragePrice(resources.getExtractPrice().doubleValue()
                    ,resources.getUid());

            resources.setFailTime(OrderUtil.getSecondTimestampTwo());

        }
        //todo 此处为企业付款，没经过测试
        boolean isTest = true;
        if(!isTest){
            WechatUserDto wechatUser =  generator.convert(wechatUserService.getOne(new QueryWrapper<WechatUser>().eq("uid",resources.getUid())), WechatUserDto.class);
            if(ObjectUtil.isNotNull(wechatUser)){
                try {
                    payService.entPay(wechatUser.getOpenid(),resources.getId().toString(),
                            resources.getRealName(),
                            resources.getExtractPrice().multiply(new BigDecimal(100)).intValue());
                } catch (WxPayException e) {
                    throw new BadRequestException(e.getMessage());
                }
            }else{
                throw new BadRequestException("不是微信用户无法退款");
            }

        }
        userExtractService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
