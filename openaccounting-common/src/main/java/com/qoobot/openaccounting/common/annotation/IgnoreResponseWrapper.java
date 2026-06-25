package com.qoobot.openaccounting.common.annotation;

import java.lang.annotation.*;

/**
 * 忽略响应包装注解
 * 用于需要直接返回响应（如文件下载、重定向等）的接口
 *
 * @author openaccounting
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreResponseWrapper {
}
