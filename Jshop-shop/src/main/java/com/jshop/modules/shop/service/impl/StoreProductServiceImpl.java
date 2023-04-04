/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.shop.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jshop.common.service.impl.BaseServiceImpl;
import com.jshop.common.utils.QueryHelpPlus;
import com.jshop.dozer.service.IGenerator;
import com.jshop.exception.BadRequestException;
import com.jshop.modules.shop.domain.StoreProduct;
import com.jshop.modules.shop.domain.StoreProductAttr;
import com.jshop.modules.shop.domain.StoreProductAttrResult;
import com.jshop.modules.shop.domain.StoreProductAttrValue;
import com.jshop.modules.shop.service.StoreCategoryService;
import com.jshop.modules.shop.service.StoreProductAttrResultService;
import com.jshop.modules.shop.service.StoreProductAttrService;
import com.jshop.modules.shop.service.StoreProductAttrValueService;
import com.jshop.modules.shop.service.StoreProductService;
import com.jshop.modules.shop.service.dto.DetailDto;
import com.jshop.modules.shop.service.dto.FromatDetailDto;
import com.jshop.modules.shop.service.dto.ProductFormatDto;
import com.jshop.modules.shop.service.dto.StoreProductDto;
import com.jshop.modules.shop.service.dto.StoreProductQueryCriteria;
import com.jshop.modules.shop.service.mapper.StoreProductMapper;
import com.jshop.utils.FileUtil;
import com.jshop.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author jack胡
 */
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "storeProduct")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreProductServiceImpl extends BaseServiceImpl<StoreProductMapper, StoreProduct> implements StoreProductService {

    private final IGenerator generator;

    private final StoreProductMapper storeProductMapper;

    private final StoreCategoryService storeCategoryService;

    private final StoreProductAttrService storeProductAttrService;

    private final StoreProductAttrValueService storeProductAttrValueService;

    private final StoreProductAttrResultService storeProductAttrResultService;
    @Override
    //@Cacheable
    public Map<String, Object> queryAll(StoreProductQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreProduct> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), StoreProductDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreProduct> queryAll(StoreProductQueryCriteria criteria){
        List<StoreProduct> storeProductList = baseMapper.selectList(QueryHelpPlus.getPredicate(StoreProduct.class, criteria));
        storeProductList.forEach(storeProduct ->{
            storeProduct.setStoreCategory(storeCategoryService.getById(storeProduct.getCateId()));
        });
        return storeProductList;
    }


    @Override
    public void download(List<StoreProductDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreProductDto storeProduct : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)", storeProduct.getMerId());
            map.put("商品图片", storeProduct.getImage());
            map.put("轮播图", storeProduct.getSliderImage());
            map.put("商品名称", storeProduct.getStoreName());
            map.put("商品简介", storeProduct.getStoreInfo());
            map.put("关键字", storeProduct.getKeyword());
            map.put("产品条码（一维码）", storeProduct.getBarCode());
            map.put("分类id", storeProduct.getCateId());
            map.put("商品价格", storeProduct.getPrice());
            map.put("会员价格", storeProduct.getVipPrice());
            map.put("市场价", storeProduct.getOtPrice());
            map.put("邮费", storeProduct.getPostage());
            map.put("单位名", storeProduct.getUnitName());
            map.put("排序", storeProduct.getSort());
            map.put("销量", storeProduct.getSales());
            map.put("库存", storeProduct.getStock());
            map.put("状态（0：未上架，1：上架）", storeProduct.getIsShow());
            map.put("是否热卖", storeProduct.getIsHot());
            map.put("是否优惠", storeProduct.getIsBenefit());
            map.put("是否精品", storeProduct.getIsBest());
            map.put("是否新品", storeProduct.getIsNew());
            map.put("产品描述", storeProduct.getDescription());
            map.put("添加时间", storeProduct.getAddTime());
            map.put("是否包邮", storeProduct.getIsPostage());
            map.put("是否删除", storeProduct.getIsDel());
            map.put("商户是否代理 0不可代理1可代理", storeProduct.getMerUse());
            map.put("获得积分", storeProduct.getGiveIntegral());
            map.put("成本价", storeProduct.getCost());
            map.put("秒杀状态 0 未开启 1已开启", storeProduct.getIsSeckill());
            map.put("砍价状态 0未开启 1开启", storeProduct.getIsBargain());
            map.put("是否优品推荐", storeProduct.getIsGood());
            map.put("虚拟销量", storeProduct.getFicti());
            map.put("浏览量", storeProduct.getBrowse());
            map.put("产品二维码地址(用户小程序海报)", storeProduct.getCodePath());
            map.put("淘宝京东1688类型", storeProduct.getSoureLink());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public StoreProduct saveProduct(StoreProduct storeProduct) {
        if (storeProduct.getStoreCategory().getId() == null) {
            throw new BadRequestException("分类名称不能为空");
        }
        boolean check = storeCategoryService
                .checkProductCategory(storeProduct.getStoreCategory().getId());
        if(!check) throw new BadRequestException("商品分类必选选择二级");
        storeProduct.setCateId(storeProduct.getStoreCategory().getId().toString());
        this.save(storeProduct);
        return storeProduct;
    }

    @Override
    public void recovery(Integer id) {
        storeProductMapper.updateDel(0,id);
        storeProductMapper.updateOnsale(0,id);
    }

    @Override
    public void onSale(Integer id, int status) {
        if(status == 1){
            status = 0;
        }else{
            status = 1;
        }
        storeProductMapper.updateOnsale(status,id);
    }

    @Override
    public List<ProductFormatDto> isFormatAttr(Integer id, String jsonStr) {
        if(ObjectUtil.isNull(id)) throw new BadRequestException("产品不存在");

        StoreProductDto storeProductDTO = generator.convert(this.getById(id), StoreProductDto.class);
        DetailDto detailDTO = attrFormat(jsonStr);
        List<ProductFormatDto> newList = new ArrayList<>();
        for (Map<String, Map<String,String>> map : detailDTO.getRes()) {
            ProductFormatDto productFormatDTO = new ProductFormatDto();
            productFormatDTO.setDetail(map.get("detail"));
            productFormatDTO.setCost(storeProductDTO.getCost().doubleValue());
            productFormatDTO.setPrice(storeProductDTO.getPrice().doubleValue());
            productFormatDTO.setSales(storeProductDTO.getSales());
            productFormatDTO.setPic(storeProductDTO.getImage());
            productFormatDTO.setCheck(false);
            newList.add(productFormatDTO);
        }
        return newList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProductAttr(Integer id, String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        //System.out.println(jsonObject);
        List<FromatDetailDto> attrList = JSON.parseArray(
                jsonObject.get("items").toString(),
                FromatDetailDto.class);
        List<ProductFormatDto> valueList = JSON.parseArray(
                jsonObject.get("attrs").toString(),
                ProductFormatDto.class);


        List<StoreProductAttr> attrGroup = new ArrayList<>();
        for (FromatDetailDto fromatDetailDTO : attrList) {
            StoreProductAttr storeProductAttr = new StoreProductAttr();
            storeProductAttr.setProductId(id);
            storeProductAttr.setAttrName(fromatDetailDTO.getValue());
            storeProductAttr.setAttrValues(StrUtil.
                    join(",",fromatDetailDTO.getDetail()));
            attrGroup.add(storeProductAttr);
        }


        List<StoreProductAttrValue> valueGroup = new ArrayList<>();
        for (ProductFormatDto productFormatDTO : valueList) {
            StoreProductAttrValue storeProductAttrValue = new StoreProductAttrValue();
            storeProductAttrValue.setProductId(id);
            //productFormatDTO.getDetail().values().stream().collect(Collectors.toList());
            List<String> stringList = productFormatDTO.getDetail().values()
                    .stream().collect(Collectors.toList());
            Collections.sort(stringList);
            storeProductAttrValue.setSuk(StrUtil.
                    join(",",stringList));
            storeProductAttrValue.setPrice(BigDecimal.valueOf(productFormatDTO.getPrice()));
            storeProductAttrValue.setCost(BigDecimal.valueOf(productFormatDTO.getCost()));
            storeProductAttrValue.setStock(productFormatDTO.getSales());
            storeProductAttrValue.setUnique(IdUtil.simpleUUID());
            storeProductAttrValue.setImage(productFormatDTO.getPic());

            valueGroup.add(storeProductAttrValue);
        }

        if(attrGroup.isEmpty() || valueGroup.isEmpty()){
            throw new BadRequestException("请设置至少一个属性!");
        }

        //如果设置sku 处理价格与库存

        ////取最小价格
        BigDecimal minPrice = valueGroup
                .stream()
                .map(StoreProductAttrValue::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        //计算库存
        Integer stock = valueGroup
                .stream()
                .map(StoreProductAttrValue::getStock)
                .reduce(Integer::sum)
                .orElse(0);

        StoreProduct storeProduct = StoreProduct.builder()
                .stock(stock)
                .price(minPrice)
                .id(id)
                .build();
        this.updateById(storeProduct);

        //插入之前清空
        clearProductAttr(id,false);


        //保存属性
        storeProductAttrService.saveOrUpdateBatch(attrGroup);

        //保存值
        storeProductAttrValueService.saveOrUpdateBatch(valueGroup);

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("attr",jsonObject.get("items"));
        map.put("value",jsonObject.get("attrs"));

        //保存结果
        setResult(map,id);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setResult(Map<String, Object> map,Integer id) {
        StoreProductAttrResult storeProductAttrResult = new StoreProductAttrResult();
        storeProductAttrResult.setProductId(id);
        storeProductAttrResult.setResult(JSON.toJSONString(map));
        storeProductAttrResult.setChangeTime(OrderUtil.getSecondTimestampTwo());

        storeProductAttrResultService.remove(new QueryWrapper<StoreProductAttrResult>().eq("product_id",id));

        storeProductAttrResultService.saveOrUpdate(storeProductAttrResult);
    }

    @Override
    public String getStoreProductAttrResult(Integer id) {
        StoreProductAttrResult storeProductAttrResult = storeProductAttrResultService
                .getOne(new QueryWrapper<StoreProductAttrResult>().eq("product_id",id));
        if(ObjectUtil.isNull(storeProductAttrResult)) return "";
        return  storeProductAttrResult.getResult();
    }

    @Override
    public void updateProduct(StoreProduct resources) {
        if(resources.getStoreCategory() == null || resources.getStoreCategory().getId() == null) throw new BadRequestException("请选择分类");
        boolean check = storeCategoryService
                .checkProductCategory(resources.getStoreCategory().getId());
        if(!check) throw new BadRequestException("商品分类必选选择二级");
        resources.setCateId(resources.getStoreCategory().getId().toString());
        this.saveOrUpdate(resources);
    }

    @Override
    public void delete(Integer id) {
        storeProductMapper.updateDel(1,id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearProductAttr(Integer id,boolean isActice) {
        if(ObjectUtil.isNull(id)) throw new BadRequestException("产品不存在");

        storeProductAttrService.remove(new QueryWrapper<StoreProductAttr>().eq("product_id",id));
        storeProductAttrValueService.remove(new QueryWrapper<StoreProductAttrValue>().eq("product_id",id));

        if(isActice){
            storeProductAttrResultService.remove(new QueryWrapper<StoreProductAttrResult>().eq("product_id",id));
        }
    }
    /**
     * 组合规则属性算法
     * @param jsonStr
     * @return
     */
    public DetailDto attrFormat(String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        List<FromatDetailDto> fromatDetailDTOList = JSON.parseArray(jsonObject.get("items").toString(),
                FromatDetailDto.class);
        List<String> data = new ArrayList<>();
        List<Map<String,Map<String,String>>> res =new ArrayList<>();
        if(fromatDetailDTOList.size() > 1){
            for (int i=0; i < fromatDetailDTOList.size() - 1;i++){
                if(i == 0) data = fromatDetailDTOList.get(i).getDetail();
                List<String> tmp = new LinkedList<>();
                for (String v : data) {
                    for (String g : fromatDetailDTOList.get(i+1).getDetail()) {
                        String rep2 = "";
                        if(i == 0){
                            rep2 = fromatDetailDTOList.get(i).getValue() + "_" + v + "-"
                                    + fromatDetailDTOList.get(i+1).getValue() + "_" + g;
                        }else{
                            rep2 = v + "-"
                                    + fromatDetailDTOList.get(i+1).getValue() + "_" + g;
                        }
                        tmp.add(rep2);
                        if(i == fromatDetailDTOList.size() - 2){
                            Map<String,Map<String,String>> rep4 = new LinkedHashMap<>();
                            Map<String,String> reptemp = new LinkedHashMap<>();
                            for (String h : Arrays.asList(rep2.split("-"))) {
                                List<String> rep3 = Arrays.asList(h.split("_"));

                                if(rep3.size() > 1){
                                    reptemp.put(rep3.get(0),rep3.get(1));
                                }else{
                                    reptemp.put(rep3.get(0),"");
                                }
                            }
                            rep4.put("detail",reptemp);
                            res.add(rep4);
                        }
                    }
                }
                //System.out.println("tmp:"+tmp);
                if(!tmp.isEmpty()){
                    data = tmp;
                }
            }
        }else{
            List<String> dataArr = new ArrayList<>();

            for (FromatDetailDto fromatDetailDTO : fromatDetailDTOList) {

                for (String str : fromatDetailDTO.getDetail()) {
                    Map<String,Map<String,String>> map2 = new LinkedHashMap<>();
                    //List<Map<String,String>> list1 = new ArrayList<>();
                    dataArr.add(fromatDetailDTO.getValue()+"_"+str);
                    Map<String,String> map1 = new LinkedHashMap<>();
                    map1.put(fromatDetailDTO.getValue(),str);
                    //list1.add(map1);
                    map2.put("detail",map1);
                    res.add(map2);
                }
            }
            String s = StrUtil.join("-",dataArr);
            data.add(s);
        }
        DetailDto detailDTO = new DetailDto();
        detailDTO.setData(data);
        detailDTO.setRes(res);
        return detailDTO;
    }
}
