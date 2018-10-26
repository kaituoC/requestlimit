package com.qknode.bigdata.requestlimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author kaituo
 * @date 2018-10-20
 */
@EnableScheduling
@SpringBootApplication
public class RequestLimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequestLimitApplication.class, args);
    }
}
