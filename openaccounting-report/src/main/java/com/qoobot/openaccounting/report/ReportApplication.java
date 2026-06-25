package com.qoobot.openaccounting.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 报表模块启动类
 *
 * @author openaccounting
 */
@SpringBootApplication(scanBasePackages = "com.qoobot.openaccounting")
public class ReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }
}
