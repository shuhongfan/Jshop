/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.tools.service.impl;

import com.jshop.dozer.service.IGenerator;
import com.jshop.exception.BadRequestException;
import com.jshop.tools.domain.QiniuConfig;
import com.jshop.tools.domain.QiniuContent;
import com.jshop.tools.service.QiNiuService;
import com.jshop.tools.service.QiniuConfigService;
import com.jshop.tools.service.QiniuContentService;
import com.jshop.tools.service.dto.QiniuQueryCriteria;
import com.jshop.tools.utils.QiNiuUtil;
import com.jshop.utils.FileUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jack胡
 */
@Service
//@CacheConfig(cacheNames = "qiNiu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QiNiuServiceImpl implements QiNiuService {

    private final QiniuConfigService qiniuConfigService;

    private final QiniuContentService qiniuContentService;

    private final IGenerator generator;

    @Value("${qiniu.max-size}")
    private Long maxSize;

    public QiNiuServiceImpl(QiniuConfigService qiniuConfigService, QiniuContentService qiniuContentService, IGenerator generator) {
        this.qiniuConfigService = qiniuConfigService;
        this.qiniuContentService = qiniuContentService;
        this.generator = generator;
    }

    @Override
//    @Cacheable
    public Object queryAll(QiniuQueryCriteria criteria, Pageable pageable){
        return qiniuContentService.queryAll(criteria,pageable);
    }

    @Override
    public List<QiniuContent> queryAll(QiniuQueryCriteria criteria) {
        return qiniuContentService.queryAll(criteria);
    }

    @Override
//    @Cacheable(key = "'1'")
    public QiniuConfig find() {
        QiniuConfig qiniuConfig = qiniuConfigService.getById(1L);
        return qiniuConfig;
    }

    @Override
//    @CachePut(cacheNames = "qiNiuConfig", key = "'1'")
    @Transactional(rollbackFor = Exception.class)
    public QiniuConfig update(QiniuConfig qiniuConfig) {
        String http = "http://", https = "https://";
        if (!(qiniuConfig.getHost().toLowerCase().startsWith(http)||qiniuConfig.getHost().toLowerCase().startsWith(https))) {
            throw new BadRequestException("外链域名必须以http://或者https://开头");
        }
        qiniuConfig.setId(1L);
        qiniuConfigService.saveOrUpdate(qiniuConfig);
        return qiniuConfig;
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public QiniuContent upload(MultipartFile file, QiniuConfig qiniuConfig) {
        FileUtil.checkSize(maxSize, file.getSize());
        if(qiniuConfig.getId() == null){
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(qiniuConfig.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        try {
            String key = file.getOriginalFilename();
            if(qiniuContentService.getOne(new QueryWrapper<QiniuContent>().eq("name",key)) != null) {
                key = QiNiuUtil.getKey(key);
            }
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析上传成功的结果

            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

            QiniuContent content = qiniuContentService.getOne(new QueryWrapper<QiniuContent>().eq("name",FileUtil.getFileNameNoEx(putRet.key)));
            if (content == null) {
                //存入数据库
                QiniuContent qiniuContent = new QiniuContent();
                qiniuContent.setSuffix(FileUtil.getExtensionName(putRet.key));
                qiniuContent.setBucket(qiniuConfig.getBucket());
                qiniuContent.setType(qiniuConfig.getType());
                qiniuContent.setName(FileUtil.getFileNameNoEx(putRet.key));
                qiniuContent.setUrl(qiniuConfig.getHost()+"/"+putRet.key);
                qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(file.getSize()+"")));
                qiniuContentService.save(qiniuContent);
                return qiniuContent;
            }
            return content;
        } catch (Exception e) {
           throw new BadRequestException(e.getMessage());
        }
    }

    @Override
//    @Cacheable
    public QiniuContent findByContentId(Long id) {
        QiniuContent qiniuContent = qiniuContentService.getById(id);
        return qiniuContent;
    }

    @Override
//    @Cacheable
    public String download(QiniuContent content,QiniuConfig config){
        String finalUrl;
        String type = "公开";
        if(type.equals(content.getType())){
            finalUrl  = content.getUrl();
        } else {
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            // 1小时，可以自定义链接过期时间
            long expireInSeconds = 3600;
            finalUrl = auth.privateDownloadUrl(content.getUrl(), expireInSeconds);
        }
        return finalUrl;
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(QiniuContent content, QiniuConfig config) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(content.getBucket(), content.getName() + "." + content.getSuffix());
            qiniuContentService.removeById(content.getId());
        } catch (QiniuException ex) {
            qiniuConfigService.removeById(content.getId());
        }
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void synchronize(QiniuConfig config) {
        if(config.getId() == null){
            throw new BadRequestException("请先添加相应配置，再操作");
        }
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(config.getBucket(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            QiniuContent qiniuContent;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if(qiniuContentService.getOne(new QueryWrapper<QiniuContent>().eq("name",FileUtil.getFileNameNoEx(item.key)))
                        == null){
                    qiniuContent = new QiniuContent();
                    qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(item.fsize+"")));
                    qiniuContent.setSuffix(FileUtil.getExtensionName(item.key));
                    qiniuContent.setName(FileUtil.getFileNameNoEx(item.key));
                    qiniuContent.setType(config.getType());
                    qiniuContent.setBucket(config.getBucket());
                    qiniuContent.setUrl(config.getHost()+"/"+item.key);
                    qiniuContentService.save(qiniuContent);
                }
            }
        }
    }

    @Override
//    @CacheEvict(allEntries = true)
    public void deleteAll(Long[] ids, QiniuConfig config) {
        for (Long id : ids) {
            delete(findByContentId(id), config);
        }
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(String type) {
        qiniuConfigService.update(type);
    }

    @Override
    public void downloadList(List<QiniuContent> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QiniuContent content : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文件名", content.getName());
            map.put("文件类型", content.getSuffix());
            map.put("空间名称", content.getBucket());
            map.put("文件大小", content.getSize());
            map.put("空间类型", content.getType());
            map.put("创建日期", content.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
