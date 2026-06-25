package com.qoobot.openaccounting.common.aspect;

import com.qoobot.openaccounting.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 *
 * @author openaccounting
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.qoobot.openaccounting..controller..*.*(..))")
    public void controllerPointcut() {
    }

    @Pointcut("execution(* com.qoobot.openaccounting..service..*.*(..))")
    public void servicePointcut() {
    }

    @Around("controllerPointcut()")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Controller调用: {}.{}, 参数: {}", className, methodName, JsonUtils.toJson(args));

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;

        log.info("Controller返回: {}.{}, 耗时: {}ms", className, methodName, duration);

        return result;
    }

    @Around("servicePointcut()")
    public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;

        if (duration > 1000) {
            log.warn("Service执行较慢: {}.{}, 耗时: {}ms", className, methodName, duration);
        }

        return result;
    }
}
