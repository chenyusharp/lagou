package com.lagou.redis.aof;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDynamicThreadPool
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
