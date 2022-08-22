package com.founder.addressreporter.config;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author 姜涛
 * @create 2021-10-08 17:34
 */
/*多线程执行定时任务 10个线程*/
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        taskRegistrar.setScheduler(scheduledExecutorService);
    }
}
