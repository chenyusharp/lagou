package com.lagou.redis.aof;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ResizableCapacityLinkedBlockIngQueue;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Date: 2022/3/27
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
@Configuration
public class ThreadPoolConfig {


    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor dynamicThreadPoolExecutor() {
        String threadPoolId = "message-consumer";
        ThreadPoolExecutor dynamicExecutor = ThreadPoolBuilder.builder()
                .threadFactory(threadPoolId)
                .threadPoolId(threadPoolId)
                .corePoolSize(Runtime.getRuntime().availableProcessors())
                .maxPoolNum(Runtime.getRuntime().availableProcessors() * 2)
                .workQueue(new ResizableCapacityLinkedBlockIngQueue(1024))
                .rejected(new AbortPolicy())
                .keepAliveTime(100, TimeUnit.MILLISECONDS)
                .dynamicPool()
                .build();
        return dynamicExecutor;
    }

}