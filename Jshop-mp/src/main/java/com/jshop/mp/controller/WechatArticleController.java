/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co
 */
package com.jshop.mp.controller;

import cn.hutool.core.date.DateUtil;
import com.jshop.mp.domain.Article;
import com.jshop.mp.service.ArticleService;
import com.jshop.mp.service.dto.ArticleDto;
import com.jshop.mp.service.dto.ArticleQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
* @author jack胡
*/
@Api(tags = "商城:微信图文管理")
@RestController
@RequestMapping("api")
public class WechatArticleController {

    private final ArticleService articleService;

    public WechatArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/Article")
    @PreAuthorize("@el.check('admin','ARTICLE_ALL','ARTICLE_SELECT')")
    public ResponseEntity getArticles(ArticleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(articleService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/Article")
    @PreAuthorize("@el.check('admin','ARTICLE_ALL','ARTICLE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Article resources){
        resources.setAddTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm"));
        return new ResponseEntity(articleService.save(resources),HttpStatus.CREATED);
    }


    @ApiOperation(value = "修改")
    @PutMapping(value = "/Article")
    @PreAuthorize("@el.check('admin','ARTICLE_ALL','ARTICLE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Article resources){
        articleService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/Article/{id}")
    @PreAuthorize("@el.check('admin','ARTICLE_ALL','ARTICLE_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        articleService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "发布文章")
    @GetMapping(value = "/Article/publish/{id}")
    @PreAuthorize("@el.check('admin','ARTICLE_ALL','ARTICLE_DELETE')")
    public ResponseEntity publish(@PathVariable Integer id)  throws Exception{
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        ArticleDto articleDTO = new ArticleDto();
        Article article = articleService.getById(id);
        BeanUtils.copyProperties(article, articleDTO);
        articleService.uploadNews(articleDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

}
