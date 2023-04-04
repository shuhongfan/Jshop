/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service;

import com.jshop.common.service.BaseService;
import com.jshop.modules.shop.domain.StoreProduct;
import com.jshop.modules.shop.service.dto.ProductFormatDto;
import com.jshop.modules.shop.service.dto.StoreProductDto;
import com.jshop.modules.shop.service.dto.StoreProductQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author jack胡
 */
public interface StoreProductService extends BaseService<StoreProduct>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(StoreProductQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<StoreProduct>
    */
    List<StoreProduct> queryAll(StoreProductQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<StoreProductDto> all, HttpServletResponse response) throws IOException;

    StoreProduct saveProduct(StoreProduct storeProduct);

    void recovery(Integer id);

    void onSale(Integer id, int status);

    List<ProductFormatDto> isFormatAttr(Integer id, String jsonStr);

    void createProductAttr(Integer id, String jsonStr);

    void clearProductAttr(Integer id,boolean isActice);

    void setResult(Map<String, Object> map,Integer id);

    String getStoreProductAttrResult(Integer id);

    void updateProduct(StoreProduct resources);

    void delete(Integer id);
}
