package com.qoobot.openaccounting.system.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus配置
 *
 * @author openaccounting
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatis Plus拦截器（分页）
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 自动填充处理器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                log.debug("开始插入填充...");
                this.strictInsertFill(metaObject, "createTime", java.time.LocalDateTime.class, java.time.LocalDateTime.now());
                this.strictInsertFill(metaObject, "updateTime", java.time.LocalDateTime.class, java.time.LocalDateTime.now());
                // TODO: 从SecurityContext获取当前用户ID
                this.strictInsertFill(metaObject, "createdBy", Long.class, getCurrentUserId());
                this.strictInsertFill(metaObject, "updatedBy", Long.class, getCurrentUserId());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                log.debug("开始更新填充...");
                this.strictUpdateFill(metaObject, "updateTime", java.time.LocalDateTime.class, java.time.LocalDateTime.now());
                // TODO: 从SecurityContext获取当前用户ID
                this.strictUpdateFill(metaObject, "updatedBy", Long.class, getCurrentUserId());
            }

            /**
             * 获取当前用户ID（临时实现，后续从SecurityContext获取）
             */
            private Long getCurrentUserId() {
                // TODO: 实现从SecurityContext获取当前用户ID
                return 1L;
            }
        };
    }
}
