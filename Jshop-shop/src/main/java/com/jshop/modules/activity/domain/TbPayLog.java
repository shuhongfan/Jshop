package com.jshop.modules.activity.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
@Data
public class TbPayLog implements Serializable {

    private String outTradeNo;

    private Date createTime;

    private Date payTime;

    private BigInteger totalFee;

    private String userId;

    private String transactionId;

    private String tradeState;

    private String orderList;

    private String payType;

    public void copy(UserExtract source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
