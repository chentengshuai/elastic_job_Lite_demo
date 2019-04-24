package com.chentengshuai.elastic_job_Lite_demo.config;

/**
 * Copyright (C), 2019-2019,
 * FileName: SpringJobScheduler
 * Author:   陈腾帅
 * Date:     2019 4 24 0024 10:05
 * Description: 注册SpringJobScheduler 调度程序
 * History:
 * <author>   <time>   <version>   <desc>
 * 陈腾帅   修改时间    V1.0描述
 */

import com.chentengshuai.elastic_job_Lite_demo.job.StockSimpleJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockJobConfig {
    @Autowired
    private JobRegistryCenterConfig jobRegistryCenterConfig;
    @Autowired
    private ZookeeperRegistryCenter regCenter;

    public StockJobConfig() {
    }

    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(final StockSimpleJob simpleJob,
                                           @Value("${stockJob.cron}") final String cron,
                                           @Value("${stockJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${stockJob.shardingItemParameters}") final String shardingItemParameters) {
        return new SpringJobScheduler(simpleJob, regCenter, simpleJobConfigBuilder(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters));
    }

    /**
     * @Description 任务配置类
     */
    private LiteJobConfiguration simpleJobConfigBuilder(final Class<? extends SimpleJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        return LiteJobConfiguration
                .newBuilder(
                        new SimpleJobConfiguration(
                                JobCoreConfiguration.newBuilder(
                                        "my-jobName", cron, shardingTotalCount)
                                        .shardingItemParameters(shardingItemParameters).jobParameter("job-参数")
                                        .build()
                                , jobClass.getCanonicalName()
                        )
                )
                .overwrite(true)
                .build();
    }

    /**
     * 动态添加
     *
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     */
    public void addSimpleJobScheduler(final Class<? extends SimpleJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(coreConfig, jobClass.getCanonicalName());
        JobScheduler jobScheduler = new JobScheduler(regCenter, LiteJobConfiguration.newBuilder(simpleJobConfig).build());
        jobScheduler.init();
    }
}