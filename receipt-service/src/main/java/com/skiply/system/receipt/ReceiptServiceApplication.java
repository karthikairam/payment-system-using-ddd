package com.skiply.system.receipt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.skiply.system.receipt", "com.skiply.system.common"})
public class ReceiptServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReceiptServiceApplication.class, args);
    }

}
