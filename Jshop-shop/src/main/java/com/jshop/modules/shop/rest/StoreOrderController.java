/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.rest;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jshop.annotation.AnonymousAccess;
import com.jshop.constant.ShopConstants;
import com.jshop.dozer.service.IGenerator;
import com.jshop.enums.OrderInfoEnum;
import com.jshop.exception.BadRequestException;
import com.jshop.logging.aop.log.Log;
import com.jshop.modules.activity.service.StorePinkService;
import com.jshop.modules.shop.domain.StoreOrder;
import com.jshop.modules.shop.domain.StoreOrderStatus;
import com.jshop.modules.shop.domain.WechatUser;


import com.jshop.modules.shop.service.StoreOrderService;
import com.jshop.modules.shop.service.StoreOrderStatusService;
import com.jshop.modules.shop.service.WechatUserService;
import com.jshop.modules.shop.service.dto.OrderCountDto;
import com.jshop.modules.shop.service.dto.ExpressDto;
import com.jshop.modules.shop.service.dto.StoreOrderDto;
import com.jshop.modules.shop.service.dto.StoreOrderQueryCriteria;
import com.jshop.modules.shop.service.dto.WechatUserDto;
import com.jshop.modules.shop.service.param.ExpressParam;
import com.jshop.modules.shop.service.KExpressService;
import com.jshop.mp.service.TemplateService;
import com.jshop.tools.express.ExpressService;

import com.jshop.tools.express.dao.ExpressInfo;
import com.jshop.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author jack胡
 */
@Api(tags = "商城:订单管理")
@RestController
@RequestMapping("api")
@Slf4j
public class StoreOrderController {

    @Value("${jshop.apiUrl}")
    private String apiUrl;

    private final IGenerator generator;
    private final StoreOrderService storeOrderService;
    private final StoreOrderStatusService storeOrderStatusService;
    private final com.jshop.modules.shop.service.KExpressService KExpressService;
    private final WechatUserService wechatUserService;
    private final RedisTemplate<String, String> redisTemplate;
    private final TemplateService templateService;
    private final ExpressService expressService;

    public StoreOrderController(IGenerator generator, StoreOrderService storeOrderService, StoreOrderStatusService storeOrderStatusService,
                                KExpressService KExpressService, WechatUserService wechatUserService,
                                RedisTemplate<String, String> redisTemplate,
                                TemplateService templateService, StorePinkService storePinkService,
                                ExpressService expressService) {
        this.generator = generator;
        this.storeOrderService = storeOrderService;
        this.storeOrderStatusService = storeOrderStatusService;
        this.KExpressService = KExpressService;
        this.wechatUserService = wechatUserService;
        this.redisTemplate = redisTemplate;
        this.templateService = templateService;
        this.expressService = expressService;

    }

    /**@Valid
     * 根据商品分类统计订单占比
     */
    @GetMapping("/StoreOrder/orderCount")
    @ApiOperation(value = "根据商品分类统计订单占比",notes = "根据商品分类统计订单占比",response = ExpressParam.class)
    public ResponseEntity orderCount(){
        OrderCountDto orderCountDto  = storeOrderService.getOrderCount();
        return new ResponseEntity(orderCountDto, HttpStatus.OK);
    }

    @GetMapping(value = "/data/count")
    @AnonymousAccess
    public ResponseEntity getCount() {
        return new ResponseEntity(storeOrderService.getOrderTimeData(), HttpStatus.OK);
    }

    @GetMapping(value = "/data/chart")
    @AnonymousAccess
    public ResponseEntity getChart() {
        return new ResponseEntity(storeOrderService.chartCount(), HttpStatus.OK);
    }


    @ApiOperation(value = "查询订单")
    @GetMapping(value = "/StoreOrder")
    @PreAuthorize("@el.check('admin','STOREORDER_ALL','STOREORDER_SELECT')")
    public ResponseEntity getStoreOrders(StoreOrderQueryCriteria criteria,
                                         Pageable pageable,
                                         @RequestParam(name = "orderStatus") String orderStatus,
                                         @RequestParam(name = "orderType") String orderType) {


        criteria.setShippingType(1);//默认查询所有快递订单
        //订单状态查询
        if (StrUtil.isNotEmpty(orderStatus)) {
            switch (orderStatus) {
                case "0":
                    criteria.setIsDel(0);
                    criteria.setPaid(0);
                    criteria.setStatus(0);
                    criteria.setRefundStatus(0);
                    break;
                case "1":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setStatus(0);
                    criteria.setRefundStatus(0);
                    break;
                case "2":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setStatus(1);
                    criteria.setRefundStatus(0);
                    break;
                case "3":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setStatus(2);
                    criteria.setRefundStatus(0);
                    break;
                case "4":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setStatus(3);
                    criteria.setRefundStatus(0);
                    break;
                case "-1":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setRefundStatus(1);
                    break;
                case "-2":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setRefundStatus(2);
                    break;
                case "-4":
                    criteria.setIsDel(1);
                    break;
            }
        }
        //订单类型查询
        if (StrUtil.isNotEmpty(orderType)) {
            switch (orderType) {
                case "1":
                    criteria.setBargainId(0);
                    criteria.setCombinationId(0);
                    criteria.setSeckillId(0);
                    break;
                case "2":
                    criteria.setNewCombinationId(0);
                    break;
                case "3":
                    criteria.setNewSeckillId(0);
                    break;
                case "4":
                    criteria.setNewBargainId(0);
                    break;
                case "5":
                    criteria.setShippingType(2);
                    break;
            }
        }


        return new ResponseEntity(storeOrderService.queryAll(criteria, pageable), HttpStatus.OK);
    }


    @ApiOperation(value = "发货")
    @PutMapping(value = "/StoreOrder")
    @PreAuthorize("@el.check('admin','STOREORDER_ALL','STOREORDER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody StoreOrder resources) {
        if (StrUtil.isBlank(resources.getDeliveryName())) throw new BadRequestException("请选择快递公司");
        if (StrUtil.isBlank(resources.getDeliveryId())) throw new BadRequestException("快递单号不能为空");
        ExpressDto expressDTO = generator.convert(KExpressService.getById(Integer.valueOf(resources
                .getDeliveryName())), ExpressDto.class);
        if (ObjectUtil.isNull(expressDTO)) {
            throw new BadRequestException("请先添加快递公司");
        }
        resources.setStatus(1);
        resources.setDeliveryType("express");
        resources.setDeliveryName(expressDTO.getName());
        resources.setDeliverySn(expressDTO.getCode());

        storeOrderService.update(resources);

        StoreOrderStatus storeOrderStatus = new StoreOrderStatus();
        storeOrderStatus.setOid(resources.getId());
        storeOrderStatus.setChangeType("delivery_goods");
        storeOrderStatus.setChangeMessage("已发货 快递公司：" + resources.getDeliveryName()
                + " 快递单号：" + resources.getDeliveryId());
        storeOrderStatus.setChangeTime(OrderUtil.getSecondTimestampTwo());

        storeOrderStatusService.save(storeOrderStatus);

        //模板消息通知
        try {
            WechatUserDto wechatUser = generator.convert(wechatUserService.getOne(new QueryWrapper<WechatUser>().eq("uid",resources.getUid())), WechatUserDto.class);
            if (ObjectUtil.isNotNull(wechatUser)) {
                //公众号与小程序打通统一公众号模板通知
                if (StrUtil.isNotBlank(wechatUser.getOpenid())) {
                    templateService.deliverySuccessNotice(resources.getOrderId(),
                            expressDTO.getName(),resources.getDeliveryId(),wechatUser.getOpenid());
                }
            }
        } catch (Exception e) {
            log.info("当前用户不是微信用户不能发送模板消息哦!");
        }

        //加入redis，7天后自动确认收货
        String redisKey = String.valueOf(StrUtil.format("{}{}",
                ShopConstants.REDIS_ORDER_OUTTIME_UNCONFIRM,resources.getId()));
        redisTemplate.opsForValue().set(redisKey, resources.getOrderId(),
                ShopConstants.ORDER_OUTTIME_UNCONFIRM, TimeUnit.DAYS);


        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "订单核销")
    @PutMapping(value = "/StoreOrder/check")
    @PreAuthorize("@el.check('admin','STOREORDER_ALL','STOREORDER_EDIT')")
    public ResponseEntity check(@Validated @RequestBody StoreOrder resources) {
        if (StrUtil.isBlank(resources.getVerifyCode())) throw new BadRequestException("核销码不能为空");
        StoreOrderDto storeOrderDTO = generator.convert(storeOrderService.getById(resources.getId()), StoreOrderDto.class);
        if(!resources.getVerifyCode().equals(storeOrderDTO.getVerifyCode())){
            throw new BadRequestException("核销码不对");
        }
        if(OrderInfoEnum.PAY_STATUS_0.getValue().equals(storeOrderDTO.getPaid())){
            throw new BadRequestException("订单未支付");
        }

        /**
        if(storeOrderDTO.getStatus() > 0) throw new BadRequestException("订单已核销");

        if(storeOrderDTO.getCombinationId() > 0 && storeOrderDTO.getPinkId() > 0){
            StorePinkDTO storePinkDTO = storePinkService.findById(storeOrderDTO.getPinkId());
            if(!OrderInfoEnum.PINK_STATUS_2.getValue().equals(storePinkDTO.getStatus())){
                throw new BadRequestException("拼团订单暂未成功无法核销");
            }
        }
         **/

        //远程调用API
        RestTemplate rest = new RestTemplate();
        String url = StrUtil.format(apiUrl+"/order/admin/order_verific/{}", resources.getVerifyCode());
        String text = rest.getForObject(url, String.class);


        JSONObject jsonObject = JSON.parseObject(text);

        Integer status = jsonObject.getInteger("status");
        String msg = jsonObject.getString("msg");

        if(status != 200) throw new BadRequestException(msg);


        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ApiOperation(value = "退款")
    @PostMapping(value = "/StoreOrder/refund")
    @PreAuthorize("@el.check('admin','STOREORDER_ALL','STOREORDER_EDIT')")
    public ResponseEntity refund(@Validated @RequestBody StoreOrder resources) {
        storeOrderService.refund(resources);

        //模板消息通知
        try {
            WechatUserDto wechatUser = generator.convert(wechatUserService.getOne(new QueryWrapper<WechatUser>().eq("uid",resources.getUid())), WechatUserDto.class);
            if (ObjectUtil.isNotNull(wechatUser)) {
                //公众号与小程序打通统一公众号模板通知
                if (StrUtil.isNotBlank(wechatUser.getOpenid())) {
                    templateService.refundSuccessNotice(resources.getOrderId(),
                            resources.getPayPrice().toString(),wechatUser.getOpenid(),
                            OrderUtil.stampToDate(resources.getAddTime().toString()));
                }
            }
        } catch (Exception e) {
            log.info("当前用户不是微信用户不能发送模板消息哦!");
        }


        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/StoreOrder/{id}")
    @PreAuthorize("@el.check('admin','STOREORDER_ALL','STOREORDER_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id) {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        storeOrderService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @Log("修改订单")
    @ApiOperation(value = "修改订单")
    @PostMapping(value = "/StoreOrder/edit")
    @PreAuthorize("hasAnyRole('admin','STOREORDER_ALL','STOREORDER_EDIT')")
    public ResponseEntity editOrder(@RequestBody StoreOrder resources) {
        if (ObjectUtil.isNull(resources.getPayPrice())) throw new BadRequestException("请输入支付金额");
        if (resources.getPayPrice().doubleValue() < 0) throw new BadRequestException("金额不能低于0");

        StoreOrderDto storeOrder = generator.convert(storeOrderService.getById(resources.getId()), StoreOrderDto.class);
        //判断金额是否有变动,生成一个额外订单号去支付

        int res = NumberUtil.compare(storeOrder.getPayPrice().doubleValue(), resources.getPayPrice().doubleValue());
        if (res != 0) {
            String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();
            resources.setExtendOrderId(orderSn);
        }


        storeOrderService.saveOrUpdate(resources);

        StoreOrderStatus storeOrderStatus = new StoreOrderStatus();
        storeOrderStatus.setOid(resources.getId());
        storeOrderStatus.setChangeType("order_edit");
        storeOrderStatus.setChangeMessage("修改订单价格为：" + resources.getPayPrice());
        storeOrderStatus.setChangeTime(OrderUtil.getSecondTimestampTwo());

        storeOrderStatusService.save(storeOrderStatus);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改订单备注")
    @ApiOperation(value = "修改订单备注")
    @PostMapping(value = "/StoreOrder/remark")
    @PreAuthorize("hasAnyRole('admin','STOREORDER_ALL','STOREORDER_EDIT')")
    public ResponseEntity editOrderRemark(@RequestBody StoreOrder resources) {
        if (StrUtil.isBlank(resources.getRemark())) throw new BadRequestException("请输入备注");
        storeOrderService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.OK);
    }


    /**@Valid
     * 获取物流信息,根据传的订单编号 ShipperCode快递公司编号 和物流单号,
     *
     */
    @PostMapping("/StoreOrder/express")
    @ApiOperation(value = "获取物流信息",notes = "获取物流信息",response = ExpressParam.class)
    public ResponseEntity express( @RequestBody ExpressParam expressInfoDo){
        ExpressInfo expressInfo = expressService.getExpressInfo(expressInfoDo.getOrderCode(),
                expressInfoDo.getShipperCode(), expressInfoDo.getLogisticCode());
        if(!expressInfo.isSuccess()) throw new BadRequestException(expressInfo.getReason());
        return new ResponseEntity(expressInfo, HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/StoreOrder/download")
    @PreAuthorize("@el.check('admin','cate:list')")
    public void download(HttpServletResponse response,
                         StoreOrderQueryCriteria criteria,
                         Pageable pageable,
                         @RequestParam(name = "orderStatus") String orderStatus,
                         @RequestParam(name = "orderType") String orderType,
                         @RequestParam(name = "listContent") String listContent) throws IOException, ParseException {
        List<StoreOrderDto> list;
        if(StringUtils.isEmpty(listContent)){
            list =  (List)getStoreList(criteria, pageable, orderStatus, orderType).get("content");
        }else {
            List<String> idList = JSONArray.parseArray(listContent).toJavaList(String.class);
            list = (List) storeOrderService.queryAll(idList).get("content");
        }
        storeOrderService.download(list, response);
    }

    public Map<String,Object> getStoreList(StoreOrderQueryCriteria criteria,
                                             Pageable pageable,
                                             String orderStatus,
                                             String orderType){
        criteria.setShippingType(1);//默认查询所有快递订单
        //订单状态查询
        if (StrUtil.isNotEmpty(orderStatus)) {
            switch (orderStatus) {
                case "0":
                    criteria.setIsDel(0);
                    criteria.setPaid(0);
                    criteria.setStatus(0);
                    criteria.setRefundStatus(0);
                    break;
                case "1":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setStatus(0);
                    criteria.setRefundStatus(0);
                    break;
                case "2":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setStatus(1);
                    criteria.setRefundStatus(0);
                    break;
                case "3":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setStatus(2);
                    criteria.setRefundStatus(0);
                    break;
                case "4":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setStatus(3);
                    criteria.setRefundStatus(0);
                    break;
                case "-1":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setRefundStatus(1);
                    break;
                case "-2":
                    criteria.setIsDel(0);
                    criteria.setPaid(1);
                    criteria.setRefundStatus(2);
                    break;
                case "-4":
                    criteria.setIsDel(1);
                    break;
            }
        }
        //订单类型查询
        if (StrUtil.isNotEmpty(orderType)) {
            switch (orderType) {
                case "1":
                    criteria.setBargainId(0);
                    criteria.setCombinationId(0);
                    criteria.setSeckillId(0);
                    break;
                case "2":
                    criteria.setNewCombinationId(0);
                    break;
                case "3":
                    criteria.setNewSeckillId(0);
                    break;
                case "4":
                    criteria.setNewBargainId(0);
                    break;
                case "5":
                    criteria.setShippingType(2);
                    break;
            }
        }
        return storeOrderService.queryAll(criteria, pageable);
    }


}
