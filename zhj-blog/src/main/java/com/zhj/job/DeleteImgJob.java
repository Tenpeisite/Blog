package com.zhj.job;

import com.zhj.constants.SystemConstants;
import com.zhj.service.impl.OssUploadService;
import com.zhj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/25 19:46
 */
@Component
//@Slf4j
public class DeleteImgJob {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OssUploadService ossUploadService;

    //@Scheduled(cron = "* 0/10 * * * ? ")
    public void deleteImg() {
        //获取redis中存储的图片路径集
        Set<String> imgs = redisTemplate.opsForSet()
                .difference(SystemConstants.Article_PIC_RESOURCES, SystemConstants.Article_PIC_DB_RESOURCES);
        if (imgs != null) {
            for (String img : imgs) {
                //删除多余的图片
                //ossUploadService.deleteOss(img);
                redisTemplate.opsForSet().remove(SystemConstants.Article_PIC_RESOURCES, img);
                //log.info("自定义任务执行，清理垃圾图片:{}",img);
            }
        }
    }
}
