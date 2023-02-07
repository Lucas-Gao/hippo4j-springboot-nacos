package com.example.hippo4j.nacos;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

//    @Bean
    @DynamicThreadPool
    public ThreadPoolTaskExecutor messageConsumeDynamicExecutor() {
        String threadPoolId = "message-consume";
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setThreadNamePrefix(threadPoolId);
        threadPoolExecutor.setCorePoolSize(10);
        threadPoolExecutor.setMaxPoolSize(10);
        threadPoolExecutor.setQueueCapacity(100);
        threadPoolExecutor.setRejectedExecutionHandler(new AbortPolicy());
        return threadPoolExecutor;
    }

    @Bean
    @DynamicThreadPool
    public ExecutorService messageProduceDynamicExecutor() {
        String threadPoolId = "message-produce";
        ThreadPoolExecutor messageProduceDynamicExecutor = ThreadPoolBuilder.builder()
            .threadFactory(threadPoolId)
            .threadPoolId(threadPoolId)
            .dynamicPool()
            .build();


        return messageProduceDynamicExecutor;
    }

}

