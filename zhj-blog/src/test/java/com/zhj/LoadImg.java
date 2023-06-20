package com.zhj;

import com.zhj.domin.entity.Article;
import com.zhj.service.ArticleService;
import com.zhj.service.impl.OssUploadService;
import org.apache.commons.math3.distribution.RealDistribution;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/25 19:50
 */
//@SpringBootTest
public class LoadImg {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OssUploadService ossUploadService;

    @Test
    public void deleteImg(){
        //获取数据库里所有的图片地址
        Set<String> imgDB = articleService.list().stream().map(Article::getThumbnail).collect(Collectors.toSet());

    }

    @Test
    public void getAllOss(){
        //ossUploadService.getAllOss();
    }
}
