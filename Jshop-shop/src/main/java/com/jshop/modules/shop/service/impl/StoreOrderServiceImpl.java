/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.enums.OrderInfoEnum;
import com.jshop.exception.BadRequestException;
import com.jshop.exception.EntityExistException;
import com.jshop.modules.activity.domain.StorePink;
import com.jshop.modules.activity.service.StorePinkService;
import com.jshop.modules.shop.domain.StoreOrder;
import com.jshop.modules.shop.domain.StoreOrderCartInfo;
import com.jshop.modules.shop.domain.StoreOrderStatus;
import com.jshop.modules.shop.domain.StoreProduct;
import com.jshop.modules.shop.domain.TbUser;
import com.jshop.modules.shop.domain.UserBill;
import com.jshop.modules.shop.service.StoreCartService;
import com.jshop.modules.shop.service.StoreOrderCartInfoService;
import com.jshop.modules.shop.service.StoreOrderService;
import com.jshop.modules.shop.service.StoreOrderStatusService;
import com.jshop.modules.shop.service.SystemStoreService;
import com.jshop.modules.shop.service.UserBillService;
import com.jshop.modules.shop.service.UserService;
import com.jshop.modules.shop.service.dto.*;
import com.jshop.modules.shop.service.mapper.StoreOrderMapper;
import com.jshop.modules.shop.service.mapper.StoreProductMapper;
import com.jshop.modules.shop.service.mapper.UserMapper;
import com.jshop.mp.service.MiniPayService;
import com.jshop.mp.service.PayService;
import com.jshop.utils.FileUtil;
import com.jshop.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
@Slf4j
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "storeOrder")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreOrderServiceImpl extends BaseServiceImpl<StoreOrderMapper, StoreOrder> implements StoreOrderService {

    private final IGenerator generator;
    private UserService userService;
    private UserMapper userMapper;
    private StorePinkService storePinkService;
    private StoreOrderCartInfoService storeOrderCartInfoService;
    private final UserBillService userBillService;
    private final StoreOrderStatusService storeOrderStatusService;
    private final PayService payService;
    private final MiniPayService miniPayService;
    private final SystemStoreService systemStoreService;
    private final StoreCartService storeCartService;
    private final StoreOrderMapper storeOrderMapper;
    private final StoreProductMapper storeProductMapper;

    @Override
    public OrderCountDto getOrderCount() {
        //获取所有订单转态为已支付的
        List<CountDto> nameList =  storeCartService.findCateName();
        System.out.println("nameList:"+nameList);
        Map<String,Integer> childrenMap = new HashMap<>();
        nameList.forEach(i ->{
            if(i != null) {
                if(childrenMap.containsKey(i.getCatename())) {
                    childrenMap.put(i.getCatename(), childrenMap.get(i.getCatename())+1);
                }else {
                    childrenMap.put(i.getCatename(), 1);
                }
            }

        });
        List<OrderCountDto.OrderCountData> list = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        childrenMap.forEach((k,v) ->{
            OrderCountDto.OrderCountData orderCountData = new OrderCountDto.OrderCountData();
            orderCountData.setName(k);
            orderCountData.setValue(v);
            columns.add(k);
            list.add(orderCountData);
        });
        OrderCountDto orderCountDto = new OrderCountDto();
        orderCountDto.setColumn(columns);
        orderCountDto.setOrderCountDatas(list);
        return orderCountDto;
    }

    @Override
    public OrderTimeDataDto getOrderTimeData() {
        int today = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(new Date()));
        int yesterday = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(DateUtil.
                yesterday()));
        int lastWeek = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(DateUtil.lastWeek()));
        int nowMonth = OrderUtil.dateToTimestampT(DateUtil
                .beginOfMonth(new Date()));
        OrderTimeDataDto orderTimeDataDTO = new OrderTimeDataDto();

        orderTimeDataDTO.setTodayCount(storeOrderMapper.countByPayTimeGreaterThanEqual(today));

        orderTimeDataDTO.setProCount(storeOrderMapper
                .countByPayTimeLessThanAndPayTimeGreaterThanEqual(today,yesterday));
        //orderTimeDataDTO.setProPrice(storeOrderMapper.sumTPrice(today,yesterday));

        orderTimeDataDTO.setLastWeekCount(storeOrderMapper.countByPayTimeGreaterThanEqual(lastWeek));
        //orderTimeDataDTO.setLastWeekPrice(storeOrderMapper.sumPrice(lastWeek));

        orderTimeDataDTO.setMonthCount(storeOrderMapper.countByPayTimeGreaterThanEqual(nowMonth));
        //orderTimeDataDTO.setMonthPrice(storeOrderMapper.sumPrice(nowMonth));

        orderTimeDataDTO.setUserCount(userMapper.selectCount(new QueryWrapper<TbUser>()));
        orderTimeDataDTO.setOrderCount(storeOrderMapper.selectCount(new QueryWrapper<StoreOrder>()));
        orderTimeDataDTO.setPriceCount(storeOrderMapper.sumTotalPrice());
        orderTimeDataDTO.setGoodsCount(storeProductMapper.selectCount(new QueryWrapper<StoreProduct>()));

        return orderTimeDataDTO;
    }

    @Override
    public Map<String, Object> chartCount() {
        Map<String, Object> map = new LinkedHashMap<>();
        int nowMonth = OrderUtil.dateToTimestampT(DateUtil
                .beginOfMonth(new Date()));

        map.put("chart",storeOrderMapper.chartList(nowMonth));
        map.put("chartT",storeOrderMapper.chartListT(nowMonth));

        return map;
    }
    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreOrderQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreOrder> page = new PageInfo<>(queryAll(criteria));
        List<StoreOrderDto> storeOrderDTOS = new ArrayList<>();
        for (StoreOrder storeOrder : page.getList()) {
            orderList(storeOrderDTOS, storeOrder);

        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", storeOrderDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }

    /**
     * 代码提取
     * @param storeOrderDTOS
     * @param storeOrder
     */
    private void orderList(List<StoreOrderDto> storeOrderDTOS, StoreOrder storeOrder) {
        StoreOrderDto storeOrderDto = generator.convert(storeOrder, StoreOrderDto.class);
        Integer _status = OrderUtil.orderStatus(storeOrder.getPaid(), storeOrder.getStatus(),
                storeOrder.getRefundStatus());

        if(storeOrder.getStoreId() > 0) {
            String storeName = systemStoreService.getById(storeOrder.getStoreId()).getName();
            storeOrderDto.setStoreName(storeName);
        }

        //订单状态
        String orderStatusStr = OrderUtil.orderStatusStr(storeOrder.getPaid()
                , storeOrder.getStatus(), storeOrder.getShippingType()
                , storeOrder.getRefundStatus());

        if(_status == 3){
            String refundTime = OrderUtil.stampToDate(String.valueOf(storeOrder
                    .getRefundReasonTime()));
            String str = "<b style='color:#f124c7'>申请退款</b><br/>"+
                    "<span>退款原因："+ storeOrder.getRefundReasonWap()+"</span><br/>" +
                    "<span>备注说明："+ storeOrder.getRefundReasonWapExplain()+"</span><br/>" +
                    "<span>退款时间："+refundTime+"</span><br/>";
            orderStatusStr = str;
        }
        storeOrderDto.setStatusName(orderStatusStr);

        storeOrderDto.set_status(_status);

        String payTypeName = OrderUtil.payTypeName(storeOrder.getPayType()
                , storeOrder.getPaid());
        storeOrderDto.setPayTypeName(payTypeName);

        storeOrderDto.setPinkName(orderType(storeOrder.getId()
                , storeOrder.getPinkId(), storeOrder.getCombinationId()
                , storeOrder.getSeckillId(), storeOrder.getBargainId(),
                storeOrder.getShippingType()));

        List<StoreOrderCartInfo> cartInfos = storeOrderCartInfoService.list(
                new QueryWrapper<StoreOrderCartInfo>().eq("oid", storeOrder.getId()));
        List<StoreOrderCartInfoDto> cartInfoDTOS = new ArrayList<>();
        for (StoreOrderCartInfo cartInfo : cartInfos) {
            StoreOrderCartInfoDto cartInfoDTO = new StoreOrderCartInfoDto();
            cartInfoDTO.setCartInfoMap(JSON.parseObject(cartInfo.getCartInfo()));

            cartInfoDTOS.add(cartInfoDTO);
        }
        storeOrderDto.setCartInfoList(cartInfoDTOS);
        storeOrderDto.setUserDTO(generator.convert(userService.getById(storeOrder.getUid()), UserDto.class));
        if(storeOrderDto.getUserDTO()==null){
            storeOrderDto.setUserDTO(new UserDto());
        }
        storeOrderDTOS.add(storeOrderDto);
    }


    @Override
    //@Cacheable
    public List<StoreOrder> queryAll(StoreOrderQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreOrder.class, criteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StoreOrderDto create(StoreOrder resources) {
        if(this.getOne(new QueryWrapper<StoreOrder>().eq("`unique`",resources.getUnique())) != null){
            throw new EntityExistException(StoreOrder.class,"unique",resources.getUnique());
        }
        this.save(resources);
        return generator.convert(resources, StoreOrderDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StoreOrder resources) {
        StoreOrder storeOrder = this.getById(resources.getId());
        StoreOrder storeOrder1 = this.getOne(new QueryWrapper<StoreOrder>().eq("`unique`",resources.getUnique()));
        if(storeOrder1 != null && !storeOrder1.getId().equals(storeOrder.getId())){
            throw new EntityExistException(StoreOrder.class,"unique",resources.getUnique());
        }
        storeOrder.copy(resources);
        this.saveOrUpdate(storeOrder);
    }


    @Override
    public void download(List<StoreOrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreOrderDto storeOrder : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单号", storeOrder.getOrderId());
            map.put("用户id", storeOrder.getUid());
            map.put("用户姓名", storeOrder.getRealName());
            map.put("用户电话", storeOrder.getUserPhone());
            map.put("详细地址", storeOrder.getUserAddress());
            map.put("购物车id", storeOrder.getCartId());
            map.put("运费金额", storeOrder.getFreightPrice());
            map.put("订单商品总数", storeOrder.getTotalNum());
            map.put("订单总价", storeOrder.getTotalPrice());
            map.put("邮费", storeOrder.getTotalPostage());
            map.put("实际支付金额", storeOrder.getPayPrice());
            map.put("支付邮费", storeOrder.getPayPostage());
            map.put("抵扣金额", storeOrder.getDeductionPrice());
            map.put("优惠券id", storeOrder.getCouponId());
            map.put("优惠券金额", storeOrder.getCouponPrice());
            map.put("支付状态", storeOrder.getPaid());
            map.put("支付时间", storeOrder.getPayTime());
            map.put("支付方式", storeOrder.getPayType());
            map.put("创建时间", storeOrder.getAddTime());
            map.put("订单状态（-1 : 申请退款 -2 : 退货成功 0：待发货；1：待收货；2：已收货；3：待评价；-1：已退款）", storeOrder.getStatus());
            map.put("0 未退款 1 申请中 2 已退款", storeOrder.getRefundStatus());
            map.put("退款图片", storeOrder.getRefundReasonWapImg());
            map.put("退款用户说明", storeOrder.getRefundReasonWapExplain());
            map.put("退款时间", storeOrder.getRefundReasonTime());
            map.put("前台退款原因", storeOrder.getRefundReasonWap());
            map.put("不退款的理由", storeOrder.getRefundReason());
            map.put("退款金额", storeOrder.getRefundPrice());
            map.put("快递公司编号", storeOrder.getDeliverySn());
            map.put("快递名称/送货人姓名", storeOrder.getDeliveryName());
            map.put("发货类型", storeOrder.getDeliveryType());
            map.put("快递单号/手机号", storeOrder.getDeliveryId());
            map.put("消费赚取积分", storeOrder.getGainIntegral());
            map.put("使用积分", storeOrder.getUseIntegral());
            map.put("给用户退了多少积分", storeOrder.getBackIntegral());
            map.put("备注", storeOrder.getMark());
            map.put("是否删除", storeOrder.getIsDel());
            map.put("唯一id(md5加密)类似id", storeOrder.getUnique());
            map.put("管理员备注", storeOrder.getRemark());
            map.put("商户ID", storeOrder.getMerId());
            map.put(" isMerCheck",  storeOrder.getIsMerCheck());
            map.put("拼团产品id0一般产品", storeOrder.getCombinationId());
            map.put("拼团id 0没有拼团", storeOrder.getPinkId());
            map.put("成本价", storeOrder.getCost());
            map.put("秒杀产品ID", storeOrder.getSeckillId());
            map.put("砍价id", storeOrder.getBargainId());
            map.put("核销码", storeOrder.getVerifyCode());
            map.put("门店id", storeOrder.getStoreId());
            map.put("配送方式 1=快递 ，2=门店自提", storeOrder.getShippingType());
            map.put("支付渠道(0微信公众号1微信小程序)", storeOrder.getIsChannel());
            map.put(" isRemind",  storeOrder.getIsRemind());
            map.put(" isSystemDel",  storeOrder.getIsSystemDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Map<String, Object> queryAll(List<String> ids) {
        List<StoreOrder> storeOrders = this.list(new QueryWrapper<StoreOrder>().in("order_id",ids));
        List<StoreOrderDto> storeOrderDTOS = new ArrayList<>();
        for (StoreOrder storeOrder : storeOrders) {
            StoreOrderDto storeOrderDto = generator.convert(storeOrder, StoreOrderDto.class);

            Integer _status = OrderUtil.orderStatus(storeOrder.getPaid(), storeOrder.getStatus(),
                    storeOrder.getRefundStatus());

            if(storeOrder.getStoreId() > 0) {
                String storeName = systemStoreService.getById(storeOrder.getStoreId()).getName();
                storeOrderDto.setStoreName(storeName);
            }

            //订单状态
            String orderStatusStr = OrderUtil.orderStatusStr(storeOrder.getPaid()
                    , storeOrder.getStatus(), storeOrder.getShippingType()
                    , storeOrder.getRefundStatus());

            if(_status == 3){
                String refundTime = OrderUtil.stampToDate(String.valueOf(storeOrder
                        .getRefundReasonTime()));
                String str = "<b style='color:#f124c7'>申请退款</b><br/>"+
                        "<span>退款原因："+ storeOrder.getRefundReasonWap()+"</span><br/>" +
                        "<span>备注说明："+ storeOrder.getRefundReasonWapExplain()+"</span><br/>" +
                        "<span>退款时间："+refundTime+"</span><br/>";
                orderStatusStr = str;
            }
            storeOrderDto.setStatusName(orderStatusStr);

            storeOrderDto.set_status(_status);

            String payTypeName = OrderUtil.payTypeName(storeOrder.getPayType()
                    , storeOrder.getPaid());
            storeOrderDto.setPayTypeName(payTypeName);

            storeOrderDto.setPinkName(orderType(storeOrder.getId()
                    , storeOrder.getPinkId(), storeOrder.getCombinationId()
                    , storeOrder.getSeckillId(), storeOrder.getBargainId(),
                    storeOrder.getShippingType()));

            List<StoreOrderCartInfo> cartInfos = storeOrderCartInfoService.list(new QueryWrapper<StoreOrderCartInfo>().eq("oid", storeOrder.getId()));
            List<StoreOrderCartInfoDto> cartInfoDTOS = new ArrayList<>();
            for (StoreOrderCartInfo cartInfo : cartInfos) {
                StoreOrderCartInfoDto cartInfoDTO = new StoreOrderCartInfoDto();
                cartInfoDTO.setCartInfoMap(JSON.parseObject(cartInfo.getCartInfo()));

                cartInfoDTOS.add(cartInfoDTO);
            }
            storeOrderDto.setCartInfoList(cartInfoDTOS);
            storeOrderDto.setUserDTO(generator.convert(userService.getOne(new QueryWrapper<TbUser>().eq("uid", storeOrder.getUid())), UserDto.class));

            storeOrderDTOS.add(storeOrderDto);

        }

        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",storeOrderDTOS);

        return map;
    }


    @Override
    public String orderType(int id,int pinkId, int combinationId,int seckillId,
                            int bargainId,int shippingType) {
        String str = "[普通订单]";
        if(pinkId > 0 || combinationId > 0){
            StorePink storePink = storePinkService.getOne(new QueryWrapper<StorePink>().
                    eq("order_id_key",id));
            if(ObjectUtil.isNull(storePink)) {
                str = "[拼团订单]";
            }else{
                switch (storePink.getStatus()){
                    case 1:
                        str = "[拼团订单]正在进行中";
                        break;
                    case 2:
                        str = "[拼团订单]已完成";
                        break;
                    case 3:
                        str = "[拼团订单]未完成";
                        break;
                    default:
                        str = "[拼团订单]历史订单";
                        break;
                }
            }

        }else if(seckillId > 0){
            str = "[秒杀订单]";
        }else if(bargainId > 0){
            str = "[砍价订单]";
        }
        if(shippingType == 2) str = "[核销订单]";
        return str;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(StoreOrder resources) {
        if(resources.getPayPrice().doubleValue() < 0){
            throw new BadRequestException("请输入退款金额");
        }

        if(resources.getPayType().equals("yue")){
            //修改状态
            resources.setRefundStatus(2);
            resources.setRefundPrice(resources.getPayPrice());
            this.updateById(resources);

            //退款到余额
            UserDto userDTO = generator.convert(userService.getOne(new QueryWrapper<TbUser>().eq("uid",resources.getUid())), UserDto.class);
            userMapper.updateMoney(resources.getPayPrice().doubleValue(),
                    resources.getUid());

            UserBill userBill = new UserBill();
            userBill.setUid(resources.getUid());

            userBill.setLinkId(resources.getId().toString());
            userBill.setPm(1);
            userBill.setTitle("商品退款");
            userBill.setCategory("now_money");
            userBill.setType("pay_product_refund");
            userBill.setNumber(resources.getPayPrice());
            userBill.setBalance(NumberUtil.add(resources.getPayPrice(),userDTO.getNowMoney()));
            userBill.setMark("订单退款到余额");
            userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
            userBill.setStatus(1);
            userBillService.save(userBill);


            StoreOrderStatus storeOrderStatus = new StoreOrderStatus();
            storeOrderStatus.setOid(resources.getId());
            storeOrderStatus.setChangeType("refund_price");
            storeOrderStatus.setChangeMessage("退款给用户："+resources.getPayPrice() +"元");
            storeOrderStatus.setChangeTime(OrderUtil.getSecondTimestampTwo());

            storeOrderStatusService.save(storeOrderStatus);
        }else{
            BigDecimal bigDecimal = new BigDecimal("100");
            try {
                if(OrderInfoEnum.PAY_CHANNEL_1.getValue().equals(resources.getIsChannel())){
                    miniPayService.refundOrder(resources.getOrderId(),
                            bigDecimal.multiply(resources.getPayPrice()).intValue());
                }else{
                    payService.refundOrder(resources.getOrderId(),
                            bigDecimal.multiply(resources.getPayPrice()).intValue());
                }

            } catch (WxPayException e) {
                log.info("refund-error:{}",e.getMessage());
            }

        }
    }

}
