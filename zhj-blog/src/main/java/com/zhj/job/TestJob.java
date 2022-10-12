package com.zhj.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/11 23:27
 */
@Component
public class TestJob {

    //@Scheduled(cron = "0/5 * * * * ?")
    public void testJob(){
        //要执行的代码
        System.out.println("定时任务执行了");
    }
}
