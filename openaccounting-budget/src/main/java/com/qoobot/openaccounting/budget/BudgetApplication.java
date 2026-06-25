package com.qoobot.openaccounting.budget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 预算管理模块启动类
 *
 * @author openaccounting
 */
@SpringBootApplication(scanBasePackages = "com.qoobot.openaccounting")
public class BudgetApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgetApplication.class, args);
    }
}
