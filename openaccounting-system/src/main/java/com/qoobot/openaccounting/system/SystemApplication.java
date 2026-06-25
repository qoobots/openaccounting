package com.qoobot.openaccounting.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * System模块启动类
 *
 * @author openaccounting
 */
@SpringBootApplication(scanBasePackages = {"com.qoobot.openaccounting"})
@MapperScan("com.qoobot.openaccounting.system.mapper")
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
