package com.suyh.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务，这些定时任务是在同一个线程中运行的
 */
@Component
public class SchedulerTask {
    private Logger logger = LoggerFactory.getLogger(SchedulerTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private int count = 0;

    // 似乎是每6 秒一次
    @Scheduled(cron = "*/6 * * * * ?")
    private void process() {
        logger.info("this is scheduler task runing  " + (count++));
    }

    // 这个似乎也是每6 秒一次
    @Scheduled(fixedRate = 6000)
    public void reportCurrentTime() {
        logger.info("现在时间：" + dateFormat.format(new Date()));
    }
}
