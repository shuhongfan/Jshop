/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author jack胡
 * 统一异常处理
 */
@Getter
public class ErrorRequestException extends RuntimeException{

    private Integer status = BAD_REQUEST.value();

    public ErrorRequestException(String msg){
        super(msg);
    }

    public ErrorRequestException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }
}
