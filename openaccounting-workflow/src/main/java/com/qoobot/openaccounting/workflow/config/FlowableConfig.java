package com.qoobot.openaccounting.workflow.config;

import org.flowable.spring.SpringAsyncExecutor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * Flowable配置
 *
 * @author openaccounting
 */
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        // 配置异步执行器
        engineConfiguration.setAsyncExecutorEnabled(true);
        engineConfiguration.setAsyncExecutorActivate(true);

        // 配置数据库更新策略
        engineConfiguration.setDatabaseSchemaUpdate("true");

        // 配置历史级别
        engineConfiguration.setHistoryLevel(org.flowable.engine.impl.history.HistoryLevel.AUDIT);

        // 启用活动ID缓存
        engineConfiguration.setEnableProcessDefinitionInfoCache(true);

        // 配置部署资源模式
        engineConfiguration.setDeploymentMode("single-resource");
    }
}
