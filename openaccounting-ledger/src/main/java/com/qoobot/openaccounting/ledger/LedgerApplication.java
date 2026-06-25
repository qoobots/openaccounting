package com.qoobot.openaccounting.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 总账核算模块启动类
 *
 * @author openaccounting
 */
@SpringBootApplication(scanBasePackages = "com.qoobot.openaccounting")
public class LedgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LedgerApplication.class, args);
    }
}
