package com.zhj.job;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.entity.Article;
import com.zhj.service.ArticleService;
import com.zhj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/12 19:11
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    //每隔十分钟执行一次
    @Scheduled(cron = "* 0/10 * * * ? ")
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> map = redisCache.getCacheMap(SystemConstants.REDIS_VIEWCOUNT_PREFIX);
        //更新到数据库中
        Set<String> set = map.keySet();
        for (String id : set) {
            Integer viewCount = map.get(id);
            articleService.lambdaUpdate()
                    .eq(StringUtils.isNotBlank(id), Article::getId,Long.valueOf(id))
                    .set(Article::getViewCount,Long.valueOf(viewCount)).update();
        }
    }
}
