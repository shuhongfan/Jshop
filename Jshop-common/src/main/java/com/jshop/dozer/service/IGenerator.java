/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.dozer.service;

import com.jshop.common.web.vo.Paging;

import java.util.List;
import java.util.Set;

/**
 * @author ：jack胡
 * @description：doczer转换接口
 */
public interface IGenerator {

    /**
     * 转换
     *
     * @param s   数据对象
     * @param clz 复制目标类型
     * @return {@link T}
     * @Description: 单个对象的深度复制及类型转换，vo/domain , po
     * @author jack胡
     */
    <T, S> T convert(S s, Class<T> clz);

    /**
     * @Description: 深度复制结果集(ResultSet为自定义的分页结果集)
     * @param s 数据对象
     * @param clz 复制目标类型
     * @return
     * @author jack胡
     */
    //<T, S> Result<T> convert(Result<S> s, Class<T> clz);

    /**
     * 转换
     * @param s   数据对象
     * @param clz 复制目标类型
     * @return {@link List<T>}
     * @Description: list深度复制
     * @author jack胡
     */
    <T, S> List<T> convert(List<S> s, Class<T> clz);

    /**
     *
     * @param s
     * @param clz
     * @param <T>
     * @param <S>
     * @return
     */
    <T, S> Paging<T> convertPaging(Paging<S> s, Class<T> clz);
    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @return
     * @Description: set深度复制
     * @author jack胡
     */
    <T, S> Set<T> convert(Set<S> s, Class<T> clz);

    /**
     * @param s   数据对象
     * @param clz 复制目标类型
     * @return
     * @Description: 数组深度复制
     * @author jack胡
     */
    <T, S> T[] convert(S[] s, Class<T> clz);
}
