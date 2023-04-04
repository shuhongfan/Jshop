/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop;

import com.jshop.annotation.AnonymousAccess;
import com.jshop.utils.SpringContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jack胡
 */
@EnableAsync
@RestController
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages ={ "co.kaikeba.*.mapper", "co.kaikeba.config"})
public class AppRun {

    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
        System.out.println(
                "  _   _        ___   _   _   _    _____   _____       ___           \n" +
                " | | / /      /   | | | | | / /  | ____| |  _  \\     /   |         \n" +
                " | |/ /      / /| | | | | |/ /   | |__   | |_| |    / /| |          \n" +
                " | |\\ \\     / / | | | | | |\\ \\   |  __|  |  _  {   / / | |      \n" +
                " | | \\ \\   / /  | | | | | | \\ \\  | |___  | |_| |  / /  | |      \n "+
                " |_|  \\_\\ /_/   |_| |_| |_|  \\_\\ |_____| |_____/ /_/   |_|      \n "+

                "\n开课吧电商系统启动成功 \n官网：https://www.kaikeba.com 提供技术支持ﾞ  \n");
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        fa.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return fa;
    }

    /**
     * 访问首页提示
     * @return /
     */
    @GetMapping("/")
    @AnonymousAccess
    public String index() {
        return "Backend service started successfully";
    }
}
