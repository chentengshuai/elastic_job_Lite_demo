package com.chentengshuai.elastic_job_Lite_demo.config;


import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (C), 2019-2019,
 * FileName: ElasticConfig
 * Author:   陈腾帅
 * Date:     2019 4 24 0024 9:43
 * Description: 添加zookeeper注册中心
 * History:
 * <author>          <time>          <version>          <desc>
 * 陈腾帅          修改时间           V1.0              描述
 */
@Configuration
@ConditionalOnExpression("'${spring.elasticjob.serverList}'.length() > 0") //判断是否配置了zookeeper 地址
public class JobRegistryCenterConfig {
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(@Value("${spring.elasticjob.serverList}") final String serverList, @Value("${spring.elasticjob.namespace}") final String namespace) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
    }
}
